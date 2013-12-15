package com.easyhome.common.share.option;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.easyhome.common.R;
import com.easyhome.common.share.IShareObject;
import com.easyhome.common.share.ShareConfiguration;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * QQ互联
 *
 * @author zhoulu
 * @date 13-12-13
 */
public abstract class QQConnect extends BaseOption implements IUiListener {

    public Tencent mTencent;

    protected QQConnect() {
    }

    protected QQConnect(Context context, IShareObject shareObject) {
        super(context, shareObject);
    }

    @Override
    public String getAppId() {
        return ShareConfiguration.QQCONNECT.APPID;
    }

    @Override
    public boolean isSupportSSO() {
        return false;
    }

    @Override
    public boolean isSupportWeb() {
        return true;
    }

    @Override
    public boolean isInstalledApp() {
        return false;
    }

    @Override
    public boolean onCreate() {
        mTencent = Tencent.createInstance(ShareConfiguration.QQCONNECT.APPID, getContext());
        return true;
    }

    @Override
    public void onAuth() {

    }

    @Override
    public void onLogin() {
        if (mTencent != null) {
            //无效的时候进行登录
            if (!mTencent.isSessionValid()) {
                mTencent.login((Activity) getContext(), ShareConfiguration.QQCONNECT.SCOPE, this);
            } else {
                performLogin(true, "");
            }
        }
    }

    @Override
    public void onLogout() {
        mTencent.logout(getContext());
        performLogout(true, "");
    }

    @Override
    public boolean onEvent(Context context, Intent intent) {
        return false;
    }

    @Override
    public void onCancelAuth() {

    }

    /*  IUiListener接口回调  */
    @Override
    public void onComplete(JSONObject jsonObject) {
        String action = getCurrentAction();
        if (ACTION_LOGIN.equals(action)) {
            performLogin(true, "");
        } else if (ACTION_SHARE.equals(action)) {
            performShare(true, getString(R.string.share_errcode_success));
        }
    }

    @Override
    public void onError(UiError uiError) {
        String action = getCurrentAction();
        if (ACTION_LOGIN.equals(action)) {
            performLogin(false, getString(R.string.share_errcode_login_fail));
        } else if (ACTION_SHARE.equals(action)) {
            performShare(false, getString(R.string.share_errcode_fail));
        }
    }

    @Override
    public void onCancel() {
        String action = getCurrentAction();
        if (ACTION_LOGIN.equals(action)) {
            performLogin(false, getString(R.string.share_errcode_login_cancel));
        } else if (ACTION_SHARE.equals(action)) {
            performShare(false, getString(R.string.share_errcode_cancel));
        }
    }
}
