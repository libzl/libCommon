package com.easyhome.common.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
