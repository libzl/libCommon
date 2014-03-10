package com.easyhome.common.modules.share.logic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.easyhome.common.R;
import com.easyhome.common.core.http.AsyncWeiboRunner;
import com.easyhome.common.core.http.RequestListener;
import com.easyhome.common.modules.share.ShareConfiguration;
import com.easyhome.common.modules.share.model.IShareObject;
import com.easyhome.common.modules.share.ui.SinaAuthDialog;
import com.easyhome.common.utils.BitmapUtil;
import com.easyhome.common.utils.TextUtil;
import com.easyhome.common.utils.URIUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboResponse;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 新浪微博
 */
public class WeiBlog extends BaseOption implements IWeiboHandler.Response {

    private static final String REQ_ATION = "com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY";

    public static final int MAX_LENGHT_OF_CONTENT = 140;//最大140个文字，280个字母数字
    private static final String TAG = WeiBlog.class.getSimpleName();

    private IWeiboShareAPI mWeiboShareAPI;
	/** 微博 Web 授权类，提供登陆等功能  */
	private WeiboAuth mWeiboAuth;
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
	private Oauth2AccessToken mAccessToken;

    public WeiBlog(Context context) {
        super(context);
    }

    public WeiBlog(Context context, IShareObject shareObject) {
        super(context, shareObject);
    }

    @Override
    public int getMaxLength(IShareObject shareObject) {
        return MAX_LENGHT_OF_CONTENT;
    }

	@Override
	public String getAppName() {
		return getString(ShareConfiguration.WEIBLOG.APP_NAME_ID);
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
		PackageManager pm = getContext().getPackageManager();
		boolean isInstalled = mWeiboShareAPI != null && mWeiboShareAPI.isWeiboAppInstalled();
		try {
			String qqPackageName = "com.sina.weibo";
			PackageInfo pi = pm.getPackageInfo(qqPackageName, PackageManager.GET_ACTIVITIES);
			int enableFlag = pm.getApplicationEnabledSetting(qqPackageName);
			if (pi != null && enableFlag == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
					|| enableFlag == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
				isInstalled = true;
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			isInstalled = false;
		}
		return isInstalled;
    }

    /**
     * 获得ICON资源ID
     *
     * @return
     */
    @Override
    public int getIcon() {
        return ShareConfiguration.WEIBLOG.ICON_ID;
    }

    /**
     * 获得功能名字
     *
     * @return
     */
    @Override
    public String getName() {
        return getString(ShareConfiguration.WEIBLOG.NAME_ID);
    }

    /**
     * 检查API-SDK是否有效
     *
     * @return
     */
    @Override
    public boolean onCreate() {
        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getContext(), ShareConfiguration.WEIBLOG.APPID, false);

		mAccessToken = AccessTokenKeeper.readAccessToken(getContext());

		mWeiboAuth = new WeiboAuth(getContext(), ShareConfiguration.WEIBLOG.APPID,
				ShareConfiguration.WEIBLOG.REDIRECT_URL, ShareConfiguration.WEIBLOG.SCOPE);

		// 如果未安装微博客户端，设置下载微博对应的回调
        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
            mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
                @Override
                public void onCancel() {
                    Toast.makeText(getContext(),
							R.string.share_status_cancel_download_weibo,
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
		WeiboParameters requestParams = new WeiboParameters();
		requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_ID,     ShareConfiguration.WEIBLOG.APPID);
		requestParams.add(WBConstants.AUTH_PARAMS_RESPONSE_TYPE, "token");
		requestParams.add(WBConstants.AUTH_PARAMS_SCOPE, "");
		requestParams.add(WBConstants.AUTH_PARAMS_DISPLAY, "mobile");
		requestParams.add(WBConstants.AUTH_PARAMS_REDIRECT_URL,  ShareConfiguration.WEIBLOG.REDIRECT_URL);
		String url = ShareConfiguration.WEIBLOG.OAUTH2_CODE_URL + "?" + Utility.encodeUrl(requestParams);

		SinaAuthDialog dialog = new SinaAuthDialog(getContext(), url, new WeiboAuthListener() {
			@Override
			public void onComplete(Bundle bundle) {
				mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
				if (mAccessToken.isSessionValid()) {
					AccessTokenKeeper.writeAccessToken(getContext(), mAccessToken);
					performAuth(true, getString(R.string.share_status_auth_success));
				}
			}

			@Override
			public void onWeiboException(WeiboException e) {
				performAuth(false, getString(R.string.share_status_auth_deny));
			}

			@Override
			public void onCancel() {
				performAuth(false, getString(R.string.share_status_cancel_auth));
			}
		});
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				performAuth(false, "");
			}
		});
		dialog.show();
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
		boolean success = intent.getBooleanExtra(EXTREA_RESULT, false);
        if (REQ_ATION.equals(action)
                && (mWeiboShareAPI.handleWeiboResponse(intent, this) || handleResponse(intent))) {
            return true;
		} else if(ACTION_SHARE.equals(action) && !success && intent != null) {
			intent.putExtra(EXTREA_RESULT, false);
			intent.putExtra(EXTREA_MESSAGE, "");
			dipatchShareListener(intent);
			return true;
        } else if(ACTION_AUTH.equals(action) && success) {
			doShare();
		}
        return false;
    }

    private boolean handleResponse(Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int error_code = extras.getInt("_weibo_resp_errcode");
                BaseResponse response = new SendMessageToWeiboResponse();
                response.errCode = error_code;
                onResponse(response);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                performShare(true, getString(R.string.share_status_success));
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                performShare(false, getString(R.string.share_status_cancel));
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                performShare(false, baseResponse.errMsg);
                break;
        }
    }

    /**
     * 开始执行分享过程
     *
     * @param shareObjects
     */
    @Override
    public void onShare(IShareObject... shareObjects) {

		if (isSupportSSO()) {
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
		} else if (isSupportWeb()) {
			if (mAccessToken.isSessionValid()) {
				if (shareObjects.length != 0) {
					sendSingleMessageByOpenApi(shareObjects[0]);
				}
			} else {
				doAuth();
			}
		}

	}

	private void sendSingleMessageByOpenApi(IShareObject shareObject) {

		WeiboParameters params = new WeiboParameters();
		params.add("access_token", mAccessToken.getToken());
		int urllength = (int) TextUtil.chineseLength("：" + shareObject.getRedirectUrl());
		String cutString = TextUtil.getChineseStringByMaxLength(shareObject.getMessage(),
				getMaxLength(shareObject) - 1 - urllength);
		params.add("status", cutString);
		byte[] source = null;
		if (shareObject.getThumbnail() != null) {
			source = BitmapUtil.bitmap2Bytes(shareObject.getThumbnail(), 80);
			params.add("pic", "new-pic");
		}

		AsyncWeiboRunner.request4Byte("https://api.weibo.com/2/statuses/upload.json",
				params, source, "POST", new RequestListener() {
			@Override
			public void onComplete(String response) {
				log("onComplete = " + response);
				performShare(true, getString(R.string.share_status_success));
			}

			@Override
			public void onComplete4binary(ByteArrayOutputStream responseOS) {
			}

			@Override
			public void onIOException(IOException e) {
				log("onIOException = " + e.getMessage());
				performShare(false, getString(R.string.share_errcode_unknown));
			}

			@Override
			public void onError(WeiboException e) {
				log("onError = " + e.toString());
				performShare(false, getString(R.string.share_status_fail));
			}
		});

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

					ImageObject imageObjectForAudio = new ImageObject();
					imageObjectForAudio.setImageObject(shareObject.getThumbnail());
					weiboMessage.imageObject = imageObjectForAudio;

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
        try {
            if (weiboMessage.checkArgs()) {
                // 3. 发送请求消息到微博，唤起微博分享界面
                mWeiboShareAPI.sendRequest(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        try {
            if (weiboMessage.checkArgs()) {
                // 3. 发送请求消息到微博，唤起微博分享界面
                mWeiboShareAPI.sendRequest(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 取消授权
     */
    @Override
    public void onCancelAuth() {
		AccessTokenKeeper.clear(getContext());
		mAccessToken = AccessTokenKeeper.readAccessToken(getContext());
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
                    } else if (TextUtil.chineseLength(shareObject.getMessage()) > MAX_LENGHT_OF_CONTENT - 1) {
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
                    } else if(TextUtil.isEmpty(shareObject.getTitle()) ) {
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
                    } else if(TextUtil.isEmpty(shareObject.getTitle()) ) {//title
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
	 * 该类定义了微博授权时所需要的参数。
	 *
	 * @author SINA
	 * @since 2013-10-07
	 */
	public static class AccessTokenKeeper {
		private static final String PREFERENCES_NAME = "com_weibo_sdk_android";

		private static final String KEY_UID           = "uid";
		private static final String KEY_ACCESS_TOKEN  = "access_token";
		private static final String KEY_EXPIRES_IN    = "expires_in";

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
		 *
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
