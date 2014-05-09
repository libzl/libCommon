package com.easyhome.common.modules.share.logic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.easyhome.common.modules.share.ShareConfiguration;
import com.easyhome.common.modules.share.model.IShareObject;
import com.easyhome.common.utils.EnvironmentUtilities;
import com.easyhome.common.utils.FileUtil;
import com.easyhome.common.utils.TextUtil;
import com.easyhome.common.utils.ToastUtils;
import com.easyhome.common.utils.UiHandler;
import com.easyhome.sample.R;


/**
 * 社交组件抽象基类
 *
 * @author zhoulu
 * @date 13-12-11
 */
public abstract class BaseOption implements IShareOption {

    public static final String ACTION_AUTH = "com.baidu.music.action.SHARE_AUTH";//授权
    public static final String ACTION_LOGIN = "com.baidu.music.action.SHARE_LOGIN";//登录
    public static final String ACTION_LOGOUT = "com.baidu.music.action.SHARE_LOGOUT";//登出
    public static final String ACTION_CANCEL_AUTH = "com.baidu.music.action.SHARE_CANCEL_AUTH";//反授权
    public static final String ACTION_SHARE = "com.baidu.music.action.SHARE_SHARE";//分享
    public static final String ACTION_CREATE = "com.baidu.music.action.SHARE_CREATE";//创建api

    public static final String TYPE_ACTIVITY_RESULT = "activity_result";
    public static final String TYPE_NEW_INTENT = "new_intent";

    public static final String EXTREA_RESULT = "result";
    public static final String EXTREA_MESSAGE = "message";
    public static final String EXTREA_RESULT_CODE = "resultCode";
    public static final String EXTREA_REQUEST_CODE = "requestCode";
    public static final String EXTREA_TYPE = "type";

    private Context mContext;
    private IShareObject mShareObject;
    private IShareListener mShareListener;

    private String mCurrentAction;//当前执行的功能

    public BaseOption(Context context){
        mContext = context;
    }

    public BaseOption(Context context, IShareObject shareObject) {
        mContext = context;
        mShareObject = shareObject;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setShareObject(IShareObject object) {
        mShareObject = object;
    }

	public IShareObject getShareObject() {
		return mShareObject;
	}

    protected Context getContext() {
        return mContext;
    }

    protected String getString(int resId) {
        return mContext.getString(resId);
    }

	@Override
	public boolean isNeedUpdate() {
		return false;
	}

	@Override
    public void setShareListener(IShareListener shareListener) {
        mShareListener = shareListener;
    }

    public String getCurrentAction() {
        return mCurrentAction;
    }

    public void setCurrentAction(String action) {
        mCurrentAction = action;
    }

    @Override
    public int getMaxLength(IShareObject shareObject) {
        return Integer.MAX_VALUE;
    }

	public abstract String getAppName();

    /**
     * 设置授权结果
     * @param success
     */
    protected final void performAuth(boolean success, String message) {
        //发送通知授权结果
        notifyEvent(mContext, createAuthIntent(success, message));
    }

    /**
     * 设置分享结果
     * @param success
     * @param message
     */
    protected final void performShare(boolean success, String message) {
        //发送通知分享结果
        notifyEvent(mContext, createShareIntent(success, message));
    }

    /**
     * 设置创建api结果
     * @param success
     * @param message
     */
    protected final void performCreate(boolean success, String message) {
        //发送通知创建api结果
        notifyEvent(mContext, createCreateIntent(success, message));
    }

    /**
     * 设置取消授权结果
     * @param success
     */
    protected final void performCancelAuth(boolean success) {
        //发送通知创建api结果
        notifyEvent(mContext, createCancelAuthIntent(success, getString(R.string.share_status_cancel_auth)));
    }

    /**
     * 设置登录结果
     * @param success
     * @param message
     */
    protected final void performLogin(boolean success, String message) {
        //发送通知创建api结果
        notifyEvent(mContext, createLoginIntent(success, message));
    }

    /**
     * 设置登出结果
     * @param success
     * @param message
     */
    protected final void performLogout(boolean success, String message) {
        //发送通知创建api结果
        notifyEvent(mContext, createCancelAuthIntent(success, message));
    }

    private Intent createAuthIntent(boolean success, String message) {
        Intent intent = new Intent(ACTION_AUTH);
        intent.putExtra(EXTREA_RESULT, success);
        intent.putExtra(EXTREA_MESSAGE, message);
        return intent;
    }

    private Intent createShareIntent(boolean success, String message) {
        Intent intent = new Intent(ACTION_SHARE);
        intent.putExtra(EXTREA_RESULT, success);
        intent.putExtra(EXTREA_MESSAGE, message);
        return intent;
    }

    private Intent createCreateIntent(boolean success, String message) {
        Intent intent = new Intent(ACTION_CREATE);
        intent.putExtra(EXTREA_RESULT, success);
        intent.putExtra(EXTREA_MESSAGE, message);
        return intent;
    }

    private Intent createCancelAuthIntent(boolean success, String message) {
        Intent intent = new Intent(ACTION_CANCEL_AUTH);
        intent.putExtra(EXTREA_RESULT, success);
        intent.putExtra(EXTREA_MESSAGE, message);
        return intent;
    }

    private Intent createLoginIntent(boolean success, String message) {
        Intent intent = new Intent(ACTION_LOGIN);
        intent.putExtra(EXTREA_RESULT, success);
        intent.putExtra(EXTREA_MESSAGE, message);
        return intent;
    }

    private Intent createLogoutIntent(boolean success, String message) {
        Intent intent = new Intent(ACTION_LOGOUT);
        intent.putExtra(EXTREA_RESULT, success);
        intent.putExtra(EXTREA_MESSAGE, message);
        return intent;
    }

    private void notifyEvent(final Context context, Intent intent) {
        onEvent(context, intent);

		String message = dipatchShareListener(intent);
		notifyEvent(message);
    }

	public String dipatchShareListener(Intent intent) {
		boolean success = intent.getBooleanExtra(EXTREA_RESULT, false);
		String message = intent.getStringExtra(EXTREA_MESSAGE);
		if (mShareListener != null && intent != null ) {
			if (ACTION_SHARE.equals(intent.getAction())) {
				mShareListener.onResponceShare(this, success, message);
			} else if (ACTION_AUTH.equals(intent.getAction())) {
				mShareListener.onResponceAuth(this, success, message);
			}
		}
		return message;
	}

	public void notifyEvent(final String message) {

		if (ShareConfiguration.ENABLE_SHOW_NOTIFY) {
			UiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtil.isEmpty(message)) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
		}

	}

	/**
	 * 检查是否支持
	 * @return
	 */
	public boolean checkSupport() {
		boolean created = onCreate();
		String message = "";
		if (isSupportSSO() || isSupportWeb()) {
			performCreate(created, message);
			return true;
		} else {
			if (isInstalledApp()) {
				if (isNeedUpdate()) {
					message = mContext.getString(R.string.share_errcode_need_update, getAppName());
				} else {
					message = mContext.getString(R.string.share_errcode_unsupport);
				}
			} else {
				message = mContext.getString(R.string.share_errcode_uninstalled, getAppName());
			}
			performCreate(created, message);
		}
		return false;
	}

    /**
     * 执行分享过程
     */
    public final void doShare() {
        mCurrentAction = ACTION_SHARE;
		//0.检查是否获得api
        if (checkSupport()) {
			//1.检查有效数据
			if (validateCheckSdcard(mShareObject) && validateCheck(mShareObject)){
				//2.发送数据
				onShare(mShareObject);
			}
        }
    }

	/**
	 * 当分享带有图片的内容时候，检查当前sdcard的状态是否满足要求
	 * @param shareObject
	 * @return
	 */
	private boolean validateCheckSdcard(IShareObject shareObject) {
		switch (shareObject.getType()) {
			case TYPE_IMAGE:
			case TYPE_VIDEO:
			case TYPE_MUSIC:

				if (!EnvironmentUtilities.isSdcardMounted()
						|| !EnvironmentUtilities.isSdcardWritable()
						|| !EnvironmentUtilities.isSdcardExist()) {
					ToastUtils.showShortToast(getContext(), R.string.share_errcode_sdcard_badly);
					return false;
				}

				if (!FileUtil.checkSDCardHasEnoughSpace(FileUtil.ONE_MB)) {
					ToastUtils.showShortToast(getContext(), R.string.share_errcode_sdcard_no_space);
					return false;
				}

				break;
		}
		return true;
	}

	/**
     * 执行授权过程
     */
    public final void doAuth() {
        mCurrentAction = ACTION_AUTH;
		//0.检查是否获得api
		if (checkSupport()) {
			//1.授权
			onAuth();
		}
    }

    /**
     * 执行登录
     */
    public final void doLogin() {
        mCurrentAction = ACTION_LOGIN;
		//0.检查是否获得api
		if (checkSupport()) {
			//1.登录
			onLogin();
		}
    }

    /**
     * 执行登出
     */
    public final void doLogout() {
        mCurrentAction = ACTION_LOGOUT;
        //0.检查是否获得api
		if (checkSupport()) {
			//1.登出
			onLogout();
		}
    }

    /**
     * 统一日志输出
     * @param s
     */
    protected void log(String s) {
        Log.i(((Object) this).getClass().getSimpleName(), "[share-common] " + s);
    }

    protected void logE(String s) {
        Log.e(((Object) this).getClass().getSimpleName(), "[share-common] " + s);
    }
}
