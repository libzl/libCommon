package com.easyhome.common.modules.download;

import java.util.List;

/**
 * 下载工作流程接口
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public abstract class RunWorker implements Runnable {

	private RunState mState = RunState.PREPARE;
    protected IDownloadConfig mConfig;
    protected Downloader mDownloader;

    RunWorker(Downloader downloader, IDownloadConfig config) {
        mConfig = config;
        mDownloader = downloader;
    }

	/**
	 * 检查当前状态是否正确
	 * 简单一个状态机机制
	 * @param state
	 * @throws com.easyhome.common.modules.download.DownloadException
	 */
	protected final void checkState(RunState state) throws DownloadException {
		boolean stateRight = false;
		switch (mState) {
			case PREPARE:
				if (state == RunState.PREPARE) {
					stateRight = true;
				}
				break;
			case START:
				if (state == RunState.PREPARE) {
					stateRight = true;
				}
				break;
			case RESUME:
                if (state == RunState.PAUSE) {
                    stateRight = true;
                }
				break;
			case PROGRESS:
                if (state == RunState.START
                        || state == RunState.RESUME) {
                    stateRight = true;
                }
				break;
			case PAUSE:
                if (state == RunState.PREPARE
                        || state == RunState.START
                        || state == RunState.RESUME
                        || state == RunState.PROGRESS) {
                    stateRight = true;
                }
				break;
			case END:
                if (state == RunState.PREPARE
                        || state == RunState.PROGRESS) {
                    stateRight = true;
                }
				break;
			case ERROR:
                stateRight = true;
				break;
		}

		if (stateRight) {
			mState = state;
		} else {
			throw new DownloadException(DownloadException.ERROR_RUN_STATE,
					"运行状态出错！当前状态：" + mState.name() + "目标状态：" + state.name());
		}

	}

    /**
     * 执行下载
     * @param item
     */
    public abstract void start(Downloadable item);

    /**
     * 停止下载
     * @param item
     */
    public abstract void stop(Downloadable item);

    /**
     * 批量停止下载
     * @param list
     */
    public abstract void stopBatch(List<Downloadable> list);

    /**
     * 释放资源
     */
    public abstract void release();
}
