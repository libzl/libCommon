package com.easyhome.common.modules.download;

/**
 * 下载器工厂类
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public class DownloaderFactory {

	/**
	 * 创建默认下载器
	 * @param config
	 * @return
	 */
	public IDownloader createDownloader(IDownloadConfig config) {
		if (config == null) {
			return null;
		}
		return new Downloader(config);
	}

	/**
	 * 创建其他下载器
	 * @param config
	 * @return
	 */
	public IDownloader createOtherDownloader(IDownloadConfig config) {
		if (config == null) {
			return null;
		}
		OtherDownloader downloader = new OtherDownloader();
		downloader.init(config);
		return downloader;
	}
}
