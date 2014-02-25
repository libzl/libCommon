package com.easyhome.common.share.logic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.easyhome.common.R;
import com.easyhome.common.share.ShareConfiguration;
import com.easyhome.common.share.model.IShareObject;
import com.easyhome.common.utils.TextUtil;
import com.easyhome.common.utils.URIUtil;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

/**
 * 微信
 */
public class WeiChat extends BaseOption implements IWXAPIEventHandler {

    private IWXAPI mApi;

    private boolean mTimeline;//是否分享到朋友圈
    private boolean mRegisterApp;//是否注册到微信中

    public WeiChat(Context context){
        super(context);
    }

    public WeiChat(Context context, IShareObject shareObject) {
        super(context, shareObject);
    }

	@Override
	public String getAppName() {
		return getString(ShareConfiguration.WEICHAT.APP_NAME_ID);
	}

	public WeiChat(Context context, IShareObject shareObject, boolean timeline) {
        super(context, shareObject);
        mTimeline = timeline;
    }

    public void setEnableTimeline(boolean showTimeline) {
        mTimeline = showTimeline;
    }

    /**
     * 获得APPID
     *
     * @return
     */
    @Override
    public String getAppId() {
        return ShareConfiguration.WEICHAT.APPID;
    }

    /**
     * 是否支持SSO调用
     *
     * @return
     */
    @Override
    public boolean isSupportSSO() {
        return mApi != null && mApi.isWXAppInstalled() && mApi.isWXAppSupportAPI();
    }

    /**
     * 是否支持以Web形式进行分享
     *
     * @return
     */
    @Override
    public boolean isSupportWeb() {
        return false;
    }

    /**
     * 是否安装软件
     *
     * @return
     */
    @Override
    public boolean isInstalledApp() {
        return mApi != null && mApi.isWXAppInstalled();
    }

    /**
     * 获得ICON资源ID
     *
     * @return
     */
    @Override
    public int getIcon() {
        return mTimeline ? ShareConfiguration.WEICHAT.WEIXIN_FREINDS_ICON_ID
                : ShareConfiguration.WEICHAT.WEIXIN_ICON_ID;
    }

    /**
     * 获得功能名字
     *
     * @return
     */
    @Override
    public String getName() {
        return mTimeline ?getString(ShareConfiguration.WEICHAT.WEIXIN_FREINDS_NAME_ID)
                : getString(ShareConfiguration.WEICHAT.WEIXIN_NAME_ID);
    }

    /**
     * 检查API-SDK是否有效
     *
     * @return
     */
    @Override
    public boolean onCreate() {
        mApi = WXAPIFactory.createWXAPI(getContext(), getAppId(), false);
        if (mApi != null) {
            mRegisterApp = mApi.registerApp(getAppId());
        }
        return true;
    }

    @Override
    public void onAuth() {
        if (mApi != null) {
            mRegisterApp = mApi.registerApp(getAppId());
        }
        performAuth(mRegisterApp, "");
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    /**
     * 取消授权
     */
    @Override
    public void onCancelAuth() {
        if (mApi != null) {
            mApi.unregisterApp();
        }
        performCancelAuth(true);
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
        if (mApi != null && mApi.handleIntent(intent, this)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean validateCheck(IShareObject... shareObjects) {

        if (shareObjects == null || shareObjects.length == 0) {
            logE("数据为NULL或者空");
            notifyEvent(getString(R.string.share_invalidate_datas));
            return false;
        }

        IShareObject shareObject = shareObjects[0];

        if (shareObject == null) {
            logE("第一条数据为NULL");
            notifyEvent(getString(R.string.share_invalidate_datas));
            return false;
        }

        if (TextUtil.isEmpty(shareObject.getTitle())) {
            notifyEvent(getString(R.string.share_text_title_empty));
            return false;
        }

        IShareObject.TYPE type = shareObject.getType();
        boolean result = true;
        switch (type) {
            case TYPE_TEXT:
                String content = shareObject.getMessage();
                if (TextUtil.isEmpty(content)) {
                    notifyEvent(getString(R.string.share_text_empty));
                    result = false;
                }
                break;
            case TYPE_MUSIC:
                String musicUrl = shareObject.getMediaUrl();
                String musicLowBandUrl = shareObject.getLowBandMediaUrl();

                if (TextUtil.isEmpty(musicLowBandUrl) && TextUtil.isEmpty(musicUrl)) {
                    notifyEvent(getString(R.string.share_music_invalidate_url));
                    result = false;
                }
                break;
            case TYPE_VIDEO:
                String videoUrl = shareObject.getMediaUrl();
                String videoLowBandUrl = shareObject.getLowBandMediaUrl();

                if (TextUtil.isEmpty(videoLowBandUrl) && TextUtil.isEmpty(videoUrl)) {
                    notifyEvent(getString(R.string.share_video_invalidate_url));
                    result = false;
                }
                break;
            case TYPE_IMAGE:
                Bitmap bitmap = shareObject.getThumbnail();
                String[] urls = shareObject.getThumbnailUrl();
                if (bitmap == null && (urls == null || urls.length == 0)) {
                    notifyEvent(getString(R.string.share_image_empty));
                    result = false;
                } else if(bitmap == null && urls != null) {
                    notifyEvent(getString(R.string.share_image_only_for_local));
                    result = false;
                }
                break;
            case TYPE_WEBURL:
                String url = shareObject.getRedirectUrl();
                if (TextUtil.isEmpty(url) || !URIUtil.isValidHttpUri(url)) {
                    notifyEvent(getString(R.string.share_webpage_invalidate_url));
                    result = false;
                }
                break;
        }
        return result;
    }

    /**
     * 开始执行分享过程
     *
     * @param shareObjects
     */
    @Override
    public void onShare(IShareObject... shareObjects) {

        IShareObject shareObject = shareObjects[0];

        IShareObject.TYPE type = shareObject.getType();

        WXMediaMessage.IMediaObject mediaObject = null;
        switch (type) {
            case TYPE_TEXT:
                mediaObject = new WXTextObject();
                ((WXTextObject) mediaObject).text = shareObject.getMessage();
                break;
            case TYPE_MUSIC:
                mediaObject = new WXMusicObject();
                ((WXMusicObject) mediaObject).musicUrl = shareObject.getRedirectUrl();
				((WXMusicObject) mediaObject).musicLowBandUrl = shareObject.getRedirectUrl();
				((WXMusicObject) mediaObject).musicDataUrl = shareObject.getMediaUrl();
				((WXMusicObject) mediaObject).musicLowBandDataUrl = shareObject.getLowBandMediaUrl();
                break;
            case TYPE_VIDEO:
                mediaObject = new WXVideoObject();
                ((WXVideoObject) mediaObject).videoUrl = shareObject.getMediaUrl();
                ((WXVideoObject) mediaObject).videoLowBandUrl = shareObject.getLowBandMediaUrl();
                break;
            case TYPE_IMAGE:
                mediaObject = new WXImageObject(shareObject.getThumbnail());
                break;
            case TYPE_WEBURL:
                mediaObject = new WXWebpageObject(shareObject.getRedirectUrl());
                break;
        }

        if (mediaObject == null) {
            return;
        }

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = mediaObject;
        msg.title = shareObject.getTitle();
        msg.description = shareObject.getSecondTitle();

        Bitmap thumbnail = shareObject.getThumbnail();
        if (thumbnail != null) {
            msg.thumbData = Util.bmpToByteArray(thumbnail, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(type.getValue());
        req.message = msg;
        req.scene = mTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mApi.sendReq(req);
        performShare(true, "");
    }

    private String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        log("onReq.......");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        boolean success = (baseResp.errCode == BaseResp.ErrCode.ERR_OK);
        String message = baseResp.errStr;

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                message = getString(R.string.share_status_success);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                message = getString(R.string.share_status_cancel);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                message = getString(R.string.share_status_auth_deny);
                break;
            default:
                if (message == null || message.equals("")) {
                    message = getString(R.string.share_errcode_unknown);
                }
                break;
        }

        performShare(success, message);
    }

}
