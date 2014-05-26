package com.easyhome.common.modules.download;

import java.util.List;

/**
 * 离散式下载流程
 *
 * @author zhoulu
 * @date 2014/5/23
 */
class ScatterDownloadWorker extends RunWorker {

    ScatterDownloadWorker(Downloader downloader,IDownloadConfig config) {
        super(downloader, config);
    }

    @Override
    public void start(Downloadable item) {

    }

    @Override
    public void stop(Downloadable item) {

    }

    @Override
    public void stopBatch(List<Downloadable> list) {

    }

    @Override
    public void release() {

    }

    @Override
	public void run() {

	}

	void runPart(Part part){

	}

	class Part {
		int partNo;
		long start;
		long end;
	}
}
