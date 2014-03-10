package com.easyhome.common.modules.plugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * <description>
 *
 * @author zhoulu
 * @date 13-12-20
 */
public class PluginService extends Service implements IPluginService {

    private PluginBinder mBinder;
    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new PluginBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder.asBinder();
    }

    @Override
    public int getAPIVer() throws RemoteException {
        return 0;
    }

    @Override
    public void share(long songId, String title, String desc, String url, String imageUrl, String imagePath, int shareType) throws RemoteException {

    }

    @Override
    public void pause() throws RemoteException {

    }

    @Override
    public void nextSong() throws RemoteException {

    }

    @Override
    public void prevSong() throws RemoteException {

    }

    @Override
    public void playSong(PluginTrack track) throws RemoteException {

    }

    @Override
    public int getPlayMode() throws RemoteException {
        return 0;
    }

    @Override
    public boolean isPlaying() throws RemoteException {
        return false;
    }

    @Override
    public void playCurrent() throws RemoteException {

    }

    @Override
    public void playLocalList() throws RemoteException {

    }

    @Override
    public void registerCallback(PluginCallback callback) throws RemoteException {

    }

    @Override
    public void unregisterCallback(PluginCallback callback) throws RemoteException {

    }

    @Override
    public void toast(String str) throws RemoteException {

    }

    @Override
    public void pVLogin(String log) throws RemoteException {

    }

    @Override
    public IBinder asBinder() {
        return mBinder;
    }

    private static class PluginBinder extends IPluginService.Stub {
        @Override
        public int getAPIVer() throws RemoteException {
            return 0;
        }

        @Override
        public void share(long songId, String title, String desc, String url, String imageUrl, String imagePath, int shareType) throws RemoteException {

        }

        @Override
        public void pause() throws RemoteException {

        }

        @Override
        public void nextSong() throws RemoteException {

        }

        @Override
        public void prevSong() throws RemoteException {

        }

        @Override
        public void playSong(PluginTrack track) throws RemoteException {

        }

        @Override
        public int getPlayMode() throws RemoteException {
            return 0;
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return false;
        }

        @Override
        public void playCurrent() throws RemoteException {

        }

        @Override
        public void playLocalList() throws RemoteException {

        }

        @Override
        public void registerCallback(PluginCallback callback) throws RemoteException {

        }

        @Override
        public void unregisterCallback(PluginCallback callback) throws RemoteException {

        }

        @Override
        public void toast(String str) throws RemoteException {

        }

        @Override
        public void pVLogin(String log) throws RemoteException {

        }
    }

}
