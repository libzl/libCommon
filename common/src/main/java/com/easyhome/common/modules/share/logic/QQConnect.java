package com.easyhome.common.modules.share.logic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.easyhome.common.R;
import com.easyhome.common.modules.share.ShareConfiguration;
import com.easyhome.common.modules.share.model.IShareObject;
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
	private boolean mIsNeedUpdate;

    protected QQConnect(Context context) {
        super(context);
    }

    protected QQConnect(Context context, IShareObject shareObject) {
        super(context, shareObject);
    }

    @Override
    public String getAppId() {
        return ShareConfiguration.QQCONNECT.APPID;
    }

	@Override
	public String getAppName() {
		return getString(ShareConfiguration.QQCONNECT.APP_NAME_ID);
	}

	@Override
    public boolean isSupportSSO() {
        return isInstalledApp() && !isNeedUpdate();
    }

    @Override
    public boolean isSupportWeb() {
        return false;
    }

	@Override
	public boolean isNeedUpdate() {
		return mIsNeedUpdate;
	}

	@Override
    public boolean isInstalledApp() {
		PackageManager pm = getContext().getPackageManager();
		boolean isInstalled = false;
		try {
			String qqPackageName = "com.tencent.mobileqq";
			PackageInfo pi = pm.getPackageInfo(qqPackageName, PackageManager.GET_ACTIVITIES);
			int versionCode = pi.versionCode;
			int enableFlag = pm.getApplicationEnabledSetting(qqPackageName);
			if (enableFlag == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
					|| enableFlag == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
				isInstalled = true;
			}

			if (versionCode <= 13) {
				mIsNeedUpdate = true;
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return isInstalled;
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
            performShare(true, getString(R.string.share_status_success));
        }
    }

    @Override
    public void onError(UiError uiError) {
        String action = getCurrentAction();
        if (ACTION_LOGIN.equals(action)) {
            performLogin(false, getString(R.string.share_errcode_login_fail));
        } else if (ACTION_SHARE.equals(action)) {
            performShare(false, getString(R.string.share_status_fail));
        }
    }

    @Override
    public void onCancel() {
        String action = getCurrentAction();
        if (ACTION_LOGIN.equals(action)) {
            performLogin(false, getString(R.string.share_errcode_login_cancel));
        } else if (ACTION_SHARE.equals(action)) {
            performShare(false, getString(R.string.share_status_cancel));
        }
    }
}
