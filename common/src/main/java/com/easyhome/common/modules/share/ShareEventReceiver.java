package com.easyhome.common.modules.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easyhome.common.modules.share.logic.ShareManager;

/**
 * 分享组件广播监听
 *
 * @author zhoulu
 * @date 13-12-11
 */
public class ShareEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ShareManager.getInstance().onHandleNewIntent(context, intent)) {
            return;
        }

        //
    }
}
