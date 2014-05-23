package com.easyhome.common.modules.download;

/**
 * 下载工作流程接口
 *
 * @author zhoulu
 * @date 2014/5/23
 */
public abstract class RunWorker implements Runnable {

	private RunState mState = RunState.PREPARE;

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
				if (state == RunState.PREPARE
						|| state == RunState.END) {
					stateRight = true;
				}
				break;
			case RESUME:
				break;
			case PROGRESS:
				break;
			case PAUSE:
				break;
			case END:
				break;
			case ERROR:
				break;
		}

		if (stateRight) {
			mState = state;
		} else {
			throw new DownloadException(DownloadException.ERROR_RUN_STATE,
					"运行状态出错！当前状态：" + mState.name() + "目标状态：" + state.name());
		}

	}
}
