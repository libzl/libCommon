package com.easyhome.common.modules.download;

import java.util.List;

/**
 * 默认下载器实现
 *
 * @author zhoulu
 * @date 2014/5/21
 */
public class Downloader implements IDownloader {

	private IDownloadConfig mConfig;
	private RunWorker mWorker;
	private DownloadList mDownloadList;
	private List<DownloadListener> mListeners;

	public Downloader(IDownloadConfig config) {
		init(config);
	}

	@Override
	public void init(IDownloadConfig config) {
		mConfig = config;
	}

	@Override
	public void add(Downloadable item) {

	}

	@Override
	public void addAll(List<Downloadable> list) {

	}

	@Override
	public void remove(Downloadable item) {

	}

	@Override
	public void removeAll(List<Downloadable> list) {

	}

	@Override
	public void clear() {

	}

	@Override
	public void destory() {

	}

	@Override
	public void addDownloadListener(DownloadListener listener) {

	}

	@Override
	public void removeDownloadListener(DownloadListener listener) {

	}

}
