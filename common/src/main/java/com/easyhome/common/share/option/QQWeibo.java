package com.easyhome.common.share.option;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;

import com.easyhome.common.R;
import com.easyhome.common.app.UiThreadHandler;
import com.easyhome.common.share.IShareObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.Tencent;

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

    private String mLastAddTweetId;//最近发成功的微博ID

    @Override
    public int getIcon() {
        return R.drawable.bt_share_qqweibo;
    }

    @Override
    public String getName() {
        return getString(R.string.action_qqweibo);
    }

    @Override
    public void onShare(IShareObject... shareObject) {

        //微博需要先进行登录验证
        if (!mTencent.isSessionValid()) {
            doLogin();
            return;
        }

        if (shareObject == null || shareObject.length == 0) {
            return;
        }

        IShareObject object = shareObject[0];
        if (mTencent.ready(getContext())) {
            Bundle bundle = new Bundle();
            bundle.putString("format", "json");// 返回的数据格式
            switch (object.getType()) {
                case TYPE_TEXT:
                    bundle.putString("content", "share a text twitter"); //object.getMessage()
                    mTencent.requestAsync(Constants.GRAPH_ADD_T, bundle,
                            Constants.HTTP_POST, new TQQApiListener(getContext(), "add_t", false), null);
                    break;
                case TYPE_WEBURL:
                    bundle.putString("content", "http://www.baidu.com"); //object.getMediaUrl()
                    mTencent.requestAsync(Constants.GRAPH_ADD_T, bundle,
                            Constants.HTTP_POST, new TQQApiListener(getContext(), "add_t", false), null);
                    break;
                case TYPE_IMAGE:
                case TYPE_MUSIC:
                case TYPE_VIDEO:
                    bundle.putString("content", "share a text and image twitter");
                    Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher);
                    bundle.putByteArray("pic", Util.bmpToByteArray(bitmap, true));//object.getThumbnail()
                    mTencent.requestAsync(Constants.GRAPH_ADD_PIC_T, bundle,
                            Constants.HTTP_POST,  new TQQApiListener(getContext(), "add_pic_t", false), null);
                    break;
            }
        }
    }

    @Override
    public boolean validateCheck(IShareObject... shareObject) {
        return false;
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
