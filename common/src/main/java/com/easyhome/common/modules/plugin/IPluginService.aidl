package com.easyhome.common.modules.plugin;

import com.easyhome.common.modules.plugin.PluginCallback;
import com.easyhome.common.modules.plugin.PluginTrack;

interface IPluginService
{
	
	int getAPIVer();

	/**
	 * 分享
	 */
	void share(long songId, String title, String desc, String url, String imageUrl, String imagePath, int shareType);
	
	void pause();//暂停播放
	
	void nextSong();//下一首
	
	void prevSong();//上一首
	
	void playSong(in PluginTrack track);//播放歌曲
	
	
	int getPlayMode();//获取当前播放模式
	
	boolean isPlaying();//是否正在播放
	
	void playCurrent();//播放当前歌曲
	
	
	void playLocalList();//播放本地列表
	
	
	void registerCallback(PluginCallback callback);//注册回调
	
	void unregisterCallback(PluginCallback callback);//注销回调
	

	void toast(String str); //toast
	
	void pVLogin(String log); //log
	
	
}
