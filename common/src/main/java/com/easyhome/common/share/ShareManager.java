package com.easyhome.common.share;

import android.content.Context;
import android.content.Intent;

import com.easyhome.common.share.option.BaseOption;
import com.easyhome.common.share.option.QQFriends;
import com.easyhome.common.share.option.QQWeibo;
import com.easyhome.common.share.option.QQZone;
import com.easyhome.common.share.option.WeiBlog;
import com.easyhome.common.share.option.WeiChat;

import java.util.HashMap;
import java.util.Map;

/**
 * 分享管理
 */
public class ShareManager {

    public static final int OPTION_WEIBLOG = 0; /*微博*/
    public static final int OPTION_WEICHAT = 1; /*微信好友*/
    public static final int OPTION_WEICHAT_FRIENDS = 2; /*微信朋友圈*/
    public static final int OPTION_QQZONE = 3; /*QQ空间*/
    public static final int OPTION_QQFIRENTS = 4; /*QQ好友*/
    public static final int OPTION_QQWEIBO = 5; /*QQ微博*/

    private static ShareManager INSTANCE;
    private ShareCreator mShareCreator;

    private Map<Integer, BaseOption> mCreatedOptions = new HashMap<Integer, BaseOption>();

    private ShareManager() {
        mShareCreator = new ShareCreator();
    }

    public static ShareManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShareManager();
        }
        return INSTANCE;
    }

    /**
     * 进行登录
     *
     * @param context
     * @param option
     * @param listener
     */
    public void login(Context context, int option, IShareOption.IShareListener listener) {
        BaseOption shareOption = mShareCreator.createShareOption(context, option, listener);
        if (shareOption != null) {
            shareOption.doLogin();
        }
    }

    /**
     * 进行授权
     *
     * @param context
     * @param option
     * @param listener
     */
    public void auth(Context context, int option, IShareOption.IShareListener listener) {
        BaseOption shareOption = mShareCreator.createShareOption(context, option, listener);
        if (shareOption != null) {
            shareOption.doAuth();
        }
    }

    /**
     * 进行分享
     *
     * @param option      {@link #OPTION_QQZONE#OPTION_WEIBLOG#OPTION_WEICHAT#OPTION_WEICHAT_FRIENDS}
     * @param shareObject
     * @param listener
     */
    public void share(Context context, int option, IShareObject shareObject, IShareOption.IShareListener listener) {
        BaseOption shareOption = mShareCreator.createShareOption(context, option, shareObject, listener);
        if (shareOption != null) {
            shareOption.doShare();
        }
    }


    /**
     * Handle Activity's onActivityResult() method.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onHandleActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        synchronized (mShareCreator) {
            for (BaseOption shareOption : mCreatedOptions.values()) {
                if (data == null) {
                    data = new Intent(shareOption.getCurrentAction());
                }
                data.putExtra(BaseOption.EXTREA_REQUEST_CODE, requestCode);
                data.putExtra(BaseOption.EXTREA_RESULT_CODE, resultCode);
                data.putExtra(BaseOption.EXTREA_TYPE, BaseOption.TYPE_ACTIVITY_RESULT);
                shareOption.onEvent(context, data);
            }
        }
    }

    /**
     * Handle Activity's onNewIntent(intent) method.
     *
     * @param intent
     */
    public boolean onHandleNewIntent(Context context, Intent intent) {
        synchronized (mShareCreator) {
            boolean handled = false;
            for (BaseOption shareOption : mCreatedOptions.values()) {
                if (intent == null) {
                    intent = new Intent(shareOption.getCurrentAction());
                }
                intent.putExtra(BaseOption.EXTREA_TYPE, BaseOption.TYPE_NEW_INTENT);
                if (shareOption.onEvent(context, intent)) {
                    handled = true;
                }
            }
            return handled;
        }
    }

    /**
     * 分享项工厂
     */
    public class ShareCreator {

        public synchronized BaseOption createShareOption(Context context, int option) {
            BaseOption shareOption = mCreatedOptions.get(option);
            switch (option) {
                case OPTION_WEIBLOG:
                    if (shareOption == null) {
                        shareOption = new WeiBlog(context, null);
                        mCreatedOptions.put(option, shareOption);
                    }
                    break;
                case OPTION_WEICHAT:
                    if (shareOption == null) {
                        shareOption = new WeiChat(context, null);
                        mCreatedOptions.put(option, shareOption);
                    }
                    break;
                case OPTION_WEICHAT_FRIENDS:
                    if (shareOption == null) {
                        shareOption = new WeiChat(context, null);
                        mCreatedOptions.put(option, shareOption);
                    }
                    ((WeiChat) shareOption).setEnableTimeline(ShareConfiguration.ENABLE_WEICHAT_SHARE_FIRENDS);
                    break;
                case OPTION_QQZONE:
                    if (shareOption == null) {
                        shareOption = new QQZone();
                        mCreatedOptions.put(option, shareOption);
                    }
                    break;
                case OPTION_QQFIRENTS:
                    if (shareOption == null) {
                        shareOption = new QQFriends();
                        mCreatedOptions.put(option, shareOption);
                    }
                    break;
                case OPTION_QQWEIBO:
                    if (shareOption == null) {
                        shareOption = new QQWeibo();
                        mCreatedOptions.put(option, shareOption);
                    }
                    break;
            }
            return shareOption;
        }

        public synchronized BaseOption createShareOption(Context context, int option, IShareObject shareObject, IShareOption.IShareListener listener) {
            BaseOption shareOption = createShareOption(context, option);
            if (shareOption != null) {
                shareOption.setContext(context);
                shareOption.setShareObject(shareObject);
                shareOption.setShareListener(listener);
            }
            return shareOption;
        }

        public synchronized BaseOption createShareOption(Context context, int option, IShareOption.IShareListener listener) {
            BaseOption shareOption = createShareOption(context, option);
            if (shareOption != null) {
                shareOption.setContext(context);
                shareOption.setShareListener(listener);
            }
            return shareOption;
        }
    }

}
