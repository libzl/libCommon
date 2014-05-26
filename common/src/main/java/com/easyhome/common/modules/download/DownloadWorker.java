package com.easyhome.common.modules.download;

import java.util.List;

/**
 *
 */
class DownloadWorker extends RunWorker {

	private Downloadable mDownloadable;

    DownloadWorker(Downloader downloader, IDownloadConfig config) {
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
		try {
			runPrepare();

		}catch (DownloadException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	void runPrepare() throws DownloadException {
		checkState(RunState.PREPARE);
	}

	void runStart() throws DownloadException {
		checkState(RunState.START);
	}

	void runResume() throws DownloadException {
		checkState(RunState.RESUME);
	}

	void runProgress() throws DownloadException {
		checkState(RunState.PROGRESS);
	}

	void runPause() throws DownloadException {
		checkState(RunState.PAUSE);
	}

	void runEnd() throws DownloadException {
		checkState(RunState.END);
	}

	void runError() throws DownloadException {
		checkState(RunState.ERROR);
	}
}
