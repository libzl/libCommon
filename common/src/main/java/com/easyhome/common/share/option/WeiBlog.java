package com.easyhome.common.share.option;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.easyhome.common.R;
import com.easyhome.common.share.IShareObject;
import com.easyhome.common.share.ShareConfiguration;
import com.easyhome.common.share.net.AsyncWeiboRunner;
import com.easyhome.common.share.net.RequestListener;
import com.easyhome.common.utils.TextUtil;
import com.easyhome.common.utils.URIUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 新浪微博
 */
public class WeiBlog extends BaseOption implements IWeiboHandler.Response, IWeiboHandler.Request {

    /**
     * 获取 Token 成功或失败的消息
     */
    private static final int MSG_FETCH_TOKEN_SUCCESS = 1;
    private static final int MSG_FETCH_TOKEN_FAILED = 2;

    private static final String TAG = WeiBlog.class.getSimpleName();

    private WeiboAuth mWeiboAuth;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private IWeiboShareAPI mWeiboShareAPI;

    public WeiBlog() {
    }

    public WeiBlog(Context context, IShareObject shareObject) {
        super(context, shareObject);
    }

    /**
     * 获得APPID
     *
     * @return
     */
    @Override
    public String getAppId() {
        return ShareConfiguration.WEIBLOG.APPID;
    }

    /**
     * 是否支持以SSO进行分享
     *
     * @return
     */
    @Override
    public boolean isSupportSSO() {
        int supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI();
        return isInstalledApp() && supportApiLevel >= 0;
    }

    /**
     * 是否支持以Web形式进行分享
     *
     * @return
     */
    @Override
    public boolean isSupportWeb() {
        return true;
    }

    /**
     * 是否安装软件
     *
     * @return
     */
    @Override
    public boolean isInstalledApp() {
        return mWeiboShareAPI != null && mWeiboShareAPI.isWeiboAppInstalled();
    }

    /**
     * 获得ICON资源ID
     *
     * @return
     */
    @Override
    public int getIcon() {
        return R.drawable.bt_share_sinaweibo;
    }

    /**
     * 获得功能名字
     *
     * @return
     */
    @Override
    public String getName() {
        return getString(R.string.action_weiblog);
    }

    /**
     * 检查API-SDK是否有效
     *
     * @return
     */
    @Override
    public boolean onCreate() {
        mAccessToken = AccessTokenKeeper.readAccessToken(getContext());
        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getContext(), ShareConfiguration.WEIBLOG.APPID);

        // 如果未安装微博客户端，设置下载微博对应的回调
        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
            mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
                @Override
                public void onCancel() {
                    Toast.makeText(getContext(),
                            R.string.share_cancel_download_weibo,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        return true;
    }

    /**
     * 授权过程
     */
    @Override
    public void onAuth() {

        if (mAccessToken.isSessionValid()) {
            performAuth(true, "");
            return;
        }

        // 创建微博实例
        mWeiboAuth = new WeiboAuth(getContext(), ShareConfiguration.WEIBLOG.APPID,
                ShareConfiguration.WEIBLOG.REDIRECT_URL, ShareConfiguration.WEIBLOG.SCOPE);

        if (isSupportSSO()) {//通过sso
            mSsoHandler = new SsoHandler((Activity) getContext(), mWeiboAuth);
            mSsoHandler.authorize(new WeiboAuthListener() {
                @Override
                public void onComplete(Bundle values) {
                    // 从 Bundle 中解析 Token
                    mAccessToken = Oauth2AccessToken.parseAccessToken(values);
                    if (mAccessToken.isSessionValid()) {
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(getContext(), mAccessToken);
                        performAuth(true, "");
                    } else {
                        String code = values.getString("code");
                        String message = getString(R.string.share_errcode_auth_deny);
                        if (!TextUtil.isEmpty(code)) {
                            message = message + "\nObtained the code: " + code;
                        }
                        performAuth(false, message);
                    }
                }

                @Override
                public void onCancel() {
                    performAuth(false, getString(R.string.share_errcode_auth_cancel));
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    performAuth(false, "Auth exception : " + e.getMessage());
                }
            });

        } else {//通过web
            mWeiboAuth.authorize(new WeiboAuthListener() {
                @Override
                public void onComplete(Bundle bundle) {
                    if (null == bundle) {
                        performAuth(false, getString(R.string.share_errcode_auth_fail_code));
                        return;
                    }

                    String code = bundle.getString("code");
                    if (TextUtil.isEmpty(code)) {
                        performAuth(false, getString(R.string.share_errcode_auth_fail_code));
                        return;
                    }

                    fetchTokenAsync(code, ShareConfiguration.WEIBLOG.WEIBO_DEMO_APP_SECRET);
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    performAuth(false, "Auth exception : " + e.getMessage());
                }

                @Override
                public void onCancel() {
                    performAuth(false, getString(R.string.share_errcode_auth_cancel));
                }
            }, WeiboAuth.OBTAIN_AUTH_CODE);
        }

    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    /**
     * 消息响应监听
     *
     * @param context
     * @param intent
     * @return
     */
    @Override
    public boolean onEvent(Context context, Intent intent) {
        String action = intent.getAction();
        String type = intent.getStringExtra(EXTREA_TYPE);
        int requestCode = intent.getIntExtra(EXTREA_REQUEST_CODE, -1);
        int resultCode = intent.getIntExtra(EXTREA_RESULT_CODE, -1);
        if (ACTION_AUTH.equals(action) && type != null && TYPE_ACTIVITY_RESULT.equals(type)) {
            // SSO 授权回调
            // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
            if (mSsoHandler != null) {
                mSsoHandler.authorizeCallBack(requestCode, resultCode, intent);
            }
            return true;
        } else if (ACTION_SHARE.equals(action)
                ||
                (TYPE_NEW_INTENT.equals(type) && mWeiboShareAPI != null
                        && (mWeiboShareAPI.handleWeiboResponse(intent, this)
                        || mWeiboShareAPI.handleWeiboRequest(intent, this)))) {
            return true;
        }
        return false;
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                performShare(true, getString(R.string.share_errcode_success));
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                performShare(true, getString(R.string.share_errcode_cancel));
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                performShare(true, baseResponse.errMsg);
                break;
        }
    }

    @Override
    public void onRequest(BaseRequest baseRequest) {
        log("onRequest.....");
    }

    /**
     * 开始执行分享过程
     *
     * @param shareObjects
     */
    @Override
    public void onShare(IShareObject... shareObjects) {

        if (shareObjects == null) {
            return;
        }

        if (mWeiboShareAPI.checkEnvironment(true)) {
            mWeiboShareAPI.registerApp();
            int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                sendMultiMessage(shareObjects);
            } else {
                if (shareObjects.length != 0) {
                    sendSingleMessage(shareObjects[0]);
                }
            }
        }
    }


    /**
     * 可发送多个内容
     *
     * @param shareObjects
     */
    private void sendMultiMessage(IShareObject[] shareObjects) {
        if (shareObjects == null) {
            return;
        }

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        for (IShareObject shareObject : shareObjects) {

            if (shareObject == null) {
                continue;
            }

            IShareObject.TYPE type = shareObject.getType();

            // 1. 初始化微博的分享消息
            switch (type) {
                case TYPE_TEXT:
                    TextObject mediaObject = new TextObject();
                    mediaObject.text = shareObject.getMessage();
                    weiboMessage.textObject = mediaObject;
                    break;
                case TYPE_WEBURL:
                    WebpageObject webpageObject = new WebpageObject();
                    webpageObject.actionUrl = shareObject.getRedirectUrl();
                    webpageObject.identify = Utility.generateGUID();
                    webpageObject.defaultText = shareObject.getMessage();
                    webpageObject.setThumbImage(shareObject.getThumbnail());
                    webpageObject.title = shareObject.getTitle();
                    webpageObject.description = shareObject.getSecondTitle();
                    weiboMessage.mediaObject = webpageObject;
                    break;
                case TYPE_IMAGE:
                    TextObject messageForImage = new TextObject();
                    messageForImage.text = shareObject.getMessage();
                    weiboMessage.textObject = messageForImage;

                    ImageObject imageObject = new ImageObject();
                    imageObject.setImageObject(shareObject.getThumbnail());
                    weiboMessage.imageObject = imageObject;
                    break;
                case TYPE_MUSIC:
                    TextObject messageForAudio = new TextObject();
                    messageForAudio.text = shareObject.getMessage();
                    weiboMessage.textObject = messageForAudio;

                    MusicObject musicObject = new MusicObject();
                    musicObject.actionUrl = shareObject.getRedirectUrl();
                    musicObject.identify = Utility.generateGUID();
                    musicObject.title = shareObject.getTitle();
                    musicObject.description = shareObject.getSecondTitle();

                    musicObject.duration = shareObject.getContentSize();
                    musicObject.setThumbImage(shareObject.getThumbnail());
                    musicObject.dataHdUrl = shareObject.getMediaUrl();
                    musicObject.dataUrl = shareObject.getLowBandMediaUrl();
                    weiboMessage.mediaObject = musicObject;
                    break;
                case TYPE_VIDEO:
                    TextObject messageForVideo = new TextObject();
                    messageForVideo.text = shareObject.getMessage();
                    weiboMessage.textObject = messageForVideo;

                    VideoObject videoObject = new VideoObject();
                    videoObject.actionUrl = shareObject.getMessage();
                    videoObject.identify = Utility.generateGUID();
                    videoObject.title = shareObject.getTitle();
                    videoObject.description = shareObject.getSecondTitle();

                    videoObject.duration = shareObject.getContentSize();
                    videoObject.setThumbImage(shareObject.getThumbnail());
                    videoObject.dataHdUrl = shareObject.getMediaUrl();
                    videoObject.dataUrl = shareObject.getLowBandMediaUrl();
                    weiboMessage.mediaObject = videoObject;
                    break;
            }

        }

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
    }

    private void sendSingleMessage(IShareObject shareObject) {
        WeiboMessage weiboMessage = new WeiboMessage();

        IShareObject.TYPE type = shareObject.getType();

        // 1. 初始化微博的分享消息
        switch (type) {
            case TYPE_TEXT:
                TextObject mediaObject = new TextObject();
                mediaObject.text = shareObject.getMessage();
                weiboMessage.mediaObject = mediaObject;
                break;
            case TYPE_WEBURL:
                WebpageObject webpageObject = new WebpageObject();
                webpageObject.actionUrl = shareObject.getRedirectUrl();
                webpageObject.identify = Utility.generateGUID();
                webpageObject.setThumbImage(shareObject.getThumbnail());
                webpageObject.title = shareObject.getTitle();
                webpageObject.description = shareObject.getSecondTitle();
                webpageObject.defaultText = shareObject.getMessage();
                weiboMessage.mediaObject = webpageObject;
                break;
            case TYPE_MUSIC:
                MusicObject musicObject = new MusicObject();
                musicObject.actionUrl = shareObject.getRedirectUrl();
                musicObject.identify = Utility.generateGUID();
                musicObject.title = shareObject.getTitle();
                musicObject.description = shareObject.getSecondTitle();

                musicObject.duration = shareObject.getContentSize();
                musicObject.setThumbImage(shareObject.getThumbnail());
                musicObject.dataHdUrl = shareObject.getMediaUrl();
                musicObject.dataUrl = shareObject.getLowBandMediaUrl();
                weiboMessage.mediaObject = musicObject;

                break;
            case TYPE_IMAGE:
                ImageObject imageObject = new ImageObject();
                imageObject.setImageObject(shareObject.getThumbnail());
                weiboMessage.mediaObject = imageObject;
                break;
            case TYPE_VIDEO:

                VideoObject videoObject = new VideoObject();
                videoObject.actionUrl = shareObject.getRedirectUrl();
                videoObject.identify = Utility.generateGUID();
                videoObject.title = shareObject.getTitle();
                videoObject.description = shareObject.getSecondTitle();

                videoObject.duration = shareObject.getContentSize();
                videoObject.setThumbImage(shareObject.getThumbnail());
                videoObject.dataHdUrl = shareObject.getMediaUrl();
                videoObject.dataUrl = shareObject.getLowBandMediaUrl();
                weiboMessage.mediaObject = videoObject;
                break;
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
    }

    /**
     * 取消授权
     */
    @Override
    public void onCancelAuth() {
        AccessTokenKeeper.clear(getContext());
    }

    @Override
    public boolean validateCheck(IShareObject... shareObjects) {
        if (shareObjects == null || shareObjects.length == 0) {
            logE("数据为NULL或者空");
            notifyEvent(getString(R.string.share_invalidate_datas));
            return false;
        }

        boolean validate = true;
        for (IShareObject shareObject : shareObjects) {
            if (shareObject == null) {
                notifyEvent(getString(R.string.share_invalidate_datas));
                return false;
            }

            IShareObject.TYPE type = shareObject.getType();
            // 1. 初始化微博的分享消息
            switch (type) {
                case TYPE_WEBURL:
                    if(TextUtil.isEmpty(shareObject.getRedirectUrl()) || !URIUtil.isValidHttpUri(shareObject.getRedirectUrl())) {
                        notifyEvent(getString(R.string.share_webpage_invalidate_url));
                        validate = false;
                    } else if (shareObject.getThumbnail() == null) {
                        notifyEvent(getString(R.string.share_image_empty));
                        validate = false;
                    }
                    break;
                case TYPE_TEXT:
                    if (TextUtil.isEmpty(shareObject.getMessage())) {
                        notifyEvent(getString(R.string.share_text_empty));
                        validate = false;
                    } else if (shareObject.getMessage().length() > 140) {
                        notifyEvent(getString(R.string.share_text_too_long));
                        validate = false;
                    }
                    break;
                case TYPE_IMAGE:
                    if (shareObject.getThumbnail() == null) {
                        notifyEvent(getString(R.string.share_image_empty));
                        validate = false;
                    }
                    break;
                case TYPE_MUSIC:

                    if (TextUtil.isEmpty(shareObject.getMediaUrl()) && TextUtil.isEmpty(shareObject.getLowBandMediaUrl())) {
                        notifyEvent(getString(R.string.share_music_invalidate_url));
                        validate = false;
                    } else if(!URIUtil.isValidHttpUri(shareObject.getMediaUrl()) && !URIUtil.isValidHttpUri(shareObject.getLowBandMediaUrl())) {
                        notifyEvent(getString(R.string.share_music_invalidate_url));
                        validate = false;
                    } else if(TextUtil.isEmpty(shareObject.getRedirectUrl()) || !URIUtil.isValidHttpUri(shareObject.getRedirectUrl())) {
                        notifyEvent(getString(R.string.share_music_invalidate_redirect_url));
                        validate = false;
                    } else if(shareObject.getContentSize() <= 0) {
                        notifyEvent(getString(R.string.share_music_invalidate_duration));
                        validate = false;
                    } else if(TextUtil.isEmpty(shareObject.getTitle()) || TextUtil.isEmpty(shareObject.getSecondTitle())) {
                        notifyEvent(getString(R.string.share_music_title_empty));
                        validate = false;
                    }

                    break;
                case TYPE_VIDEO:

                    if (TextUtil.isEmpty(shareObject.getMediaUrl())) {//播放url
                        notifyEvent(getString(R.string.share_video_invalidate_url));
                        validate = false;
                    } else if(!URIUtil.isValidHttpUri(shareObject.getMediaUrl())) {//播放url
                        notifyEvent(getString(R.string.share_video_invalidate_url));
                        validate = false;
                    } else if(TextUtil.isEmpty(shareObject.getRedirectUrl()) || !URIUtil.isValidHttpUri(shareObject.getRedirectUrl())) {//详情url
                        notifyEvent(getString(R.string.share_video_invalidate_redirect_url));
                        validate = false;
                    } else if(shareObject.getContentSize() <= 0) {
                        notifyEvent(getString(R.string.share_video_invalidate_duration));//时长
                        validate = false;
                    } else if(TextUtil.isEmpty(shareObject.getTitle()) || TextUtil.isEmpty(shareObject.getSecondTitle())) {//title
                        notifyEvent(getString(R.string.share_video_title_empty));
                        validate = false;
                    }

                    break;
            }
            return validate;
        }
        return validate;
    }

    /**
     * 该 Handler 配合 {@link com.easyhome.common.share.net.RequestListener} 对应的回调来更新 UI。
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FETCH_TOKEN_SUCCESS:
                    performAuth(true, getString(R.string.share_errcode_auth_success));
                    break;

                case MSG_FETCH_TOKEN_FAILED:
                    performAuth(true, getString(R.string.share_errcode_auth_deny));
                    break;

                default:
                    break;
            }
        }

        ;
    };

    /**
     * 异步获取 Token。
     *
     * @param authCode  授权 Code，该 Code 是一次性的，只能被获取一次 Token
     * @param appSecret 应用程序的 APP_SECRET，请务必妥善保管好自己的 APP_SECRET，
     *                  不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    public void fetchTokenAsync(String authCode, String appSecret) {
        WeiboParameters requestParams = new WeiboParameters();
        requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_ID, ShareConfiguration.WEIBLOG.APPID);
        requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_SECRET, appSecret);
        requestParams.add(WBConstants.AUTH_PARAMS_GRANT_TYPE, "authorization_code");
        requestParams.add(WBConstants.AUTH_PARAMS_CODE, authCode);
        requestParams.add(WBConstants.AUTH_PARAMS_REDIRECT_URL, ShareConfiguration.WEIBLOG.REDIRECT_URL);

        /**
         * 请注意：
         * {@link RequestListener} 对应的回调是运行在后台线程中的，
         * 因此，需要使用 Handler 来配合更新 UI。
         */
        AsyncWeiboRunner.request(ShareConfiguration.WEIBLOG.OAUTH2_ACCESS_TOKEN_URL,
                requestParams, "POST", new RequestListener() {
            @Override
            public void onComplete(String response) {
                LogUtil.d(TAG, "Response: " + response);

                // 获取 Token 成功
                Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(response);
                if (token != null && token.isSessionValid()) {
                    LogUtil.d(TAG, "Success! " + token.toString());

                    mAccessToken = token;
                    mHandler.obtainMessage(MSG_FETCH_TOKEN_SUCCESS).sendToTarget();
                } else {
                    LogUtil.d(TAG, "Failed to receive access token");
                }
            }

            @Override
            public void onComplete4binary(ByteArrayOutputStream responseOS) {
                LogUtil.e(TAG, "onComplete4binary...");
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }

            @Override
            public void onIOException(IOException e) {
                LogUtil.e(TAG, "onIOException： " + e.getMessage());
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }

            @Override
            public void onError(WeiboException e) {
                LogUtil.e(TAG, "WeiboException： " + e.getMessage());
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }
        });
    }

    /**
     * 该类定义了微博授权时所需要的参数。
     *
     * @author SINA
     * @since 2013-10-07
     */
    public static class AccessTokenKeeper {
        private static final String PREFERENCES_NAME = "com_weibo_sdk_android";

        private static final String KEY_UID = "uid";
        private static final String KEY_ACCESS_TOKEN = "access_token";
        private static final String KEY_EXPIRES_IN = "expires_in";

        /**
         * 保存 Token 对象到 SharedPreferences。
         *
         * @param context 应用程序上下文环境
         * @param token   Token 对象
         */
        public static void writeAccessToken(Context context, Oauth2AccessToken token) {
            if (null == context || null == token) {
                return;
            }

            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(KEY_UID, token.getUid());
            editor.putString(KEY_ACCESS_TOKEN, token.getToken());
            editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
            editor.commit();
        }

        /**
         * 从 SharedPreferences 读取 Token 信息。
         *
         * @param context 应用程序上下文环境
         * @return 返回 Token 对象
         */
        public static Oauth2AccessToken readAccessToken(Context context) {
            if (null == context) {
                return null;
            }

            Oauth2AccessToken token = new Oauth2AccessToken();
            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
            token.setUid(pref.getString(KEY_UID, ""));
            token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
            token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
            return token;
        }

        /**
         * 清空 SharedPreferences 中 Token信息。
         *
         * @param context 应用程序上下文环境
         */
        public static void clear(Context context) {
            if (null == context) {
                return;
            }

            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
        }
    }
}
