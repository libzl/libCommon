package com.easyhome.common.modules.download;

/**
 * 离散式下载流程
 *
 * @author zhoulu
 * @date 2014/5/23
 */
class ScatterDownloadWorker extends RunWorker {

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
