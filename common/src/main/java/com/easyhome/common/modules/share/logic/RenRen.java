package com.easyhome.common.modules.share.logic;

import android.content.Context;
import android.content.Intent;

import com.easyhome.common.modules.share.model.IShareObject;


/**
 * 人人网
 *
 * @author zhoulu
 * @date 13-12-13
 */
public class RenRen extends BaseOption{
    public RenRen(Context context) {
        super(context);
    }

    public RenRen(Context context, IShareObject shareObject) {
        super(context, shareObject);
    }

	@Override
	public String getAppName() {
		return null;
	}

	@Override
    public String getAppId() {
        return null;
    }

    @Override
    public boolean isSupportSSO() {
        return false;
    }

    @Override
    public boolean isSupportWeb() {
        return false;
    }

    @Override
    public boolean isInstalledApp() {
        return false;
    }

    @Override
    public int getIcon() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public void onAuth() {

    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public boolean onEvent(Context context, Intent intent) {
        return false;
    }

    @Override
    public void onShare(IShareObject... shareObject) {

    }

    @Override
    public boolean validateCheck(IShareObject... shareObject) {
        return false;
    }

    @Override
    public void onCancelAuth() {

    }
}
