package com.easyhome.common.share.option;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.easyhome.common.R;
import com.easyhome.common.app.UiThreadHandler;
import com.easyhome.common.share.ShareConfiguration;
import com.easyhome.common.share.object.IShareObject;
import com.easyhome.common.utils.TextUtil;
import com.easyhome.common.utils.URIUtil;
import com.tencent.mm.sdk.platformtools.Util;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * QQ微博
 *
 * @author zhoulu
 * @date 13-12-13
 */
public class QQWeibo extends QQConnect {

    public static final int MAX_LENGHT_OF_CONTENT = 140;//最大140个文字，280个字母数字

    private String mLastAddTweetId;//最近发成功的微博ID

    @Override
    public int getIcon() {
        return ShareConfiguration.QQCONNECT.QQWEIBO_ICON_ID;
    }

    @Override
    public String getName() {
        return getString(ShareConfiguration.QQCONNECT.QQWEIBO_NAME_ID);
    }

    @Override
    public void onShare(IShareObject... shareObject) {

        //微博需要先进行登录验证
        if (!mTencent.isSessionValid()) {
            doLogin();
            return;
        }

        IShareObject object = shareObject[0];
        if (mTencent.ready(getContext())) {
            Bundle bundle = new Bundle();
            bundle.putString("format", "json");// 返回的数据格式
            switch (object.getType()) {
                case TYPE_TEXT:
                    bundle.putString("content", object.getMessage());
                    mTencent.requestAsync(Constants.GRAPH_ADD_T, bundle,
                            Constants.HTTP_POST, new TQQApiListener(getContext(), "add_t", false), null);
                    break;
                case TYPE_WEBURL:
                    bundle.putString("content", getWebpageContent(object));
                    mTencent.requestAsync(Constants.GRAPH_ADD_T, bundle,
                            Constants.HTTP_POST, new TQQApiListener(getContext(), "add_t", false), null);
                    break;
                case TYPE_IMAGE:
                    bundle.putString("content", getWebpageContent(object));
                    bundle.putByteArray("pic", Util.bmpToByteArray(object.getThumbnail(), true));
                    mTencent.requestAsync(Constants.GRAPH_ADD_PIC_T, bundle,
                            Constants.HTTP_POST,  new TQQApiListener(getContext(), "add_pic_t", false), null);
                    break;
                case TYPE_MUSIC:
                case TYPE_VIDEO:
                    bundle.putString("content", getMediaContent(object));
                    bundle.putByteArray("pic", Util.bmpToByteArray(object.getThumbnail(), true));
                    mTencent.requestAsync(Constants.GRAPH_ADD_PIC_T, bundle,
                            Constants.HTTP_POST,  new TQQApiListener(getContext(), "add_pic_t", false), null);
                    break;
            }
        }
    }

    private String getMediaContent(IShareObject object) {
        String url = object.getMediaUrl();
        if (TextUtil.isEmpty(url)) {
            url = object.getLowBandMediaUrl();
        }
        return object.getMessage() + object.getTitle() + object.getSecondTitle() + url;
    }

    private String getWebpageContent(IShareObject object) {
        return object.getMessage() + object.getTitle() + object.getSecondTitle() + object.getRedirectUrl();
    }

    @Override
    public boolean validateCheck(IShareObject... shareObjects) {
        if (shareObjects == null || shareObjects.length == 0) {
            logE("数据为NULL或者空");
            notifyEvent(getString(R.string.share_invalidate_datas));
            return false;
        }

        for (IShareObject object : shareObjects) {
            if (object == null) {
                notifyEvent(getString(R.string.share_invalidate_datas));
                return false;
            }

            IShareObject.TYPE type = object.getType();

            switch (type) {
                case TYPE_TEXT:
                    if (TextUtil.isEmpty(object.getMessage())) {
                        notifyEvent(getString(R.string.share_text_empty));
                        return false;
                    } else if(TextUtil.length(object.getMessage()) > MAX_LENGHT_OF_CONTENT) {
                        notifyEvent(getString(R.string.share_text_too_long));
                        return false;
                    }
                    break;
                case TYPE_WEBURL:
                    if (TextUtil.isEmpty(getMediaContent(object))) {
                        notifyEvent(getString(R.string.share_text_empty));
                        return false;
                    } else if (TextUtil.isEmpty(object.getRedirectUrl()) || !URIUtil.isValidHttpUri(object.getRedirectUrl())) {
                        notifyEvent(getString(R.string.share_webpage_invalidate_url));
                        return false;
                    } else if(object.getThumbnail() == null) {
                        notifyEvent(getString(R.string.share_image_empty));
                        return false;
                    } else if(TextUtil.length(getWebpageContent(object)) > MAX_LENGHT_OF_CONTENT) {
                        notifyEvent(getString(R.string.share_text_too_long));
                        return false;
                    }
                    break;
                case TYPE_IMAGE:
                    if (TextUtil.isEmpty(getMediaContent(object))) {
                        notifyEvent(getString(R.string.share_text_empty));
                        return false;
                    } else if (TextUtil.isEmpty(object.getRedirectUrl()) || !URIUtil.isValidHttpUri(object.getRedirectUrl())) {
                        notifyEvent(getString(R.string.share_webpage_invalidate_url));
                        return false;
                    } else if(object.getThumbnail() == null) {
                        notifyEvent(getString(R.string.share_image_empty));
                        return false;
                    } else if(TextUtil.length(getMediaContent(object)) > MAX_LENGHT_OF_CONTENT) {
                        notifyEvent(getString(R.string.share_text_too_long));
                        return false;
                    }
                    break;
                case TYPE_MUSIC:
                case TYPE_VIDEO:
                    if (TextUtil.isEmpty(getMediaContent(object))) {
                        notifyEvent(getString(R.string.share_text_empty));
                        return false;
                    } else if ((TextUtil.isEmpty(object.getMediaUrl()) && TextUtil.isEmpty(object.getLowBandMediaUrl()))
                            || (!URIUtil.isValidHttpUri(object.getMediaUrl()) && !URIUtil.isValidHttpUri(object.getLowBandMediaUrl()))) {
                        notifyEvent(getString(R.string.share_music_invalidate_url));
                        return false;
                    } else if (TextUtil.isEmpty(object.getTitle()) || TextUtil.isEmpty(object.getSecondTitle())) {
                        notifyEvent(getString(R.string.share_music_title_empty));
                        return false;
                    } else if (TextUtil.isEmpty(object.getRedirectUrl()) || !URIUtil.isValidHttpUri(object.getRedirectUrl())) {
                        notifyEvent(getString(R.string.share_music_invalidate_redirect_url));
                        return false;
                    } else if(object.getThumbnail() == null) {
                        notifyEvent(getString(R.string.share_image_empty));
                        return false;
                    } else if(TextUtil.length(getMediaContent(object)) > MAX_LENGHT_OF_CONTENT) {
                        notifyEvent(getString(R.string.share_text_too_long));
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean onEvent(Context context, Intent intent) {
        String action = intent.getAction();
        boolean success = intent.getBooleanExtra(EXTREA_RESULT, false);
        //登录成功之后进行分享
        if (ACTION_LOGIN.equals(action) && success) {
            doShare();
        }
        return super.onEvent(context, intent);
    }

    private class TQQApiListener implements IRequestListener {
        private String mScope = "all";
        private Boolean mNeedReAuth = false;
        private Context mContext;

        public TQQApiListener(Context context, String scope, boolean needReAuth) {
            this.mScope = scope;
            this.mNeedReAuth = needReAuth;
            this.mContext = context;
        }

        @Override
        public void onComplete(final JSONObject response, Object state) {
            try {
                int ret = response.getInt("ret");
                if (response.has("data")) {
                    JSONObject data = response.getJSONObject("data");
                    if (data.has("id")) {
                        mLastAddTweetId = data.getString("id");
                    }
                }
                if (ret == 0) {//成功
                    performShare(true, getString(R.string.share_errcode_success));
                } else if (ret == 100030) {
                    if (mNeedReAuth) {
                        Runnable r = new Runnable() {
                            public void run() {
                                mTencent.reAuth((Activity) mContext, mScope, QQWeibo.this);
                            }
                        };
                        UiThreadHandler.post(r);
                    } else {
                        performShare(false, getString(R.string.share_errcode_auth_deny));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onIOException(final IOException e, Object state) {
            performShare(false, getString(R.string.share_errcode_unknown) + e.getMessage());
        }

        @Override
        public void onMalformedURLException(final MalformedURLException e,
                                            Object state) {
            performShare(false, getString(R.string.share_errcode_unknown) + e.getMessage());
        }

        @Override
        public void onJSONException(final JSONException e, Object state) {
            performShare(false, getString(R.string.share_errcode_unknown) + e.getMessage());
        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException e,
                                              Object arg1) {
            performShare(false, getString(R.string.share_errcode_net_timeout) + e.getMessage());
        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException e,
                                             Object arg1) {
            performShare(false, getString(R.string.share_errcode_net_timeout) + e.getMessage());
        }

        @Override
        public void onUnknowException(Exception e, Object arg1) {
            performShare(false, getString(R.string.share_errcode_unknown) + e.getMessage());
        }

        @Override
        public void onHttpStatusException(HttpStatusException e, Object arg1) {
            performShare(false, getString(R.string.share_errcode_unknown) + e.getMessage());
        }

        @Override
        public void onNetworkUnavailableException(
                NetworkUnavailableException e, Object arg1) {
            performShare(false, getString(R.string.share_errcode_net_timeout) + e.getMessage());
        }
    }

}
