package com.easyhome.common.plugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * <description>
 *
 * @author zhoulu
 * @date 13-12-20
 */
public class PluginManager {

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IPluginService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IPluginService mService;
    private static PluginManager INSTANCE;
    private PluginManager(){}

    public static PluginManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PluginManager();
        }
        return INSTANCE;
    }

    public void bind(Context context) {
        Intent service = new Intent();
        ComponentName component = new ComponentName("com.ting.mp3.android", "com.baidu.music.common.plugin.PluginService");
        service.setComponent(component);
        context.startService(service);
        context.bindService(service, connection, 0);
    }

    public IPluginService getService() {
        return mService;
    }
}
