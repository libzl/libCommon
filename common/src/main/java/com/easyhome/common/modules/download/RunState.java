package com.easyhome.common.modules.download;

/**
 * 下载运行状态
 *
 * @author zhoulu
 * @date 2014/5/21
 */
public enum RunState {
	/**
	 * 准备阶段
	 * 进行文件有效性、网络状态、内存卡状态检查
	 */
	PREPARE,
	/**
	 * 开始阶段
	 * 准备通过之后的状态，此时进行联网请求
	 */
	START,
	/**
	 * 重新开始阶段
	 * 当处于暂停阶段时才会调用此回调，会重新发起联网请求
	 */
	RESUME,
	/**
	 * 读取进度阶段
	 * 联网成功之后，读取下载文件，更新文件大小和进度
	 */
	PROGRESS,
	/**
	 * 暂停阶段
	 * 除了{@link RunState#END}和{@link RunState#ERROR}两个状态下，其他的状态都可以进入暂停
	 */
	PAUSE,
	/**
	 * 完成阶段
	 * 只有下载成功才进入这个阶段
	 */
	END,
	/**
	 * 出错阶段
	 * 任何出错异常都进入这个阶段
	 */
	ERROR;
}
