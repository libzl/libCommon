package com.easyhome.common.modules.download.logic;

import com.easyhome.common.modules.download.DownloaderFactory;
import com.easyhome.common.modules.download.IDownloadConfig;
import com.easyhome.common.modules.download.IDownloader;

/**
 * 下载控制逻辑中心
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public abstract class DownloadController {

    public DownloaderFactory createDownloaderFactory() {
        return null;
    }

    public IDownloadConfig createDownloadConfig() {
        return null;
    }

    public abstract IDownloader obtainDownloader();
}
