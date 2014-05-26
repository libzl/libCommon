package com.easyhome.common.modules.download;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认下载器实现
 *
 * @author zhoulu
 * @date 2014/5/21
 */
public class Downloader implements IDownloader {

    private RunWorker mWorker;
	private DownloadList mDownloadList;
	private List<DownloadListener> mListeners;

    /**
     * 是否已经初始化
     */
    private boolean mInited;

    /**
     *
     * @param config
     */
	public Downloader(IDownloadConfig config) {
		init(config);
	}

	@Override
	public void init(IDownloadConfig config) {
        if (config == null) {
            return;
        }

        mDownloadList = new DownloadList(config);
        mListeners = new ArrayList<DownloadListener>();

        if (config.isEnableScatter()) {
            mWorker = new ScatterDownloadWorker(this, config);
        } else {
            mWorker = new DownloadWorker(this, config);
        }

        mInited = true;
    }

	@Override
	public void add(Downloadable item) {
        if (mInited) {
            mDownloadList.add(item);

            Downloadable current = mDownloadList.next();
            mWorker.start(current);
        }
    }

    @Override
	public void addAll(List<Downloadable> list) {
        if (mInited) {
            mDownloadList.addAll(list);

            Downloadable current = mDownloadList.next();
            mWorker.start(current);
        }
	}

	@Override
	public void remove(Downloadable item) {
        if (mInited) {
            mDownloadList.remove(item);
            mWorker.stop(item);
        }
	}

	@Override
	public void removeAll(List<Downloadable> list) {
        if (mInited) {
            mDownloadList.removeAll(list);
            mWorker.stopBatch(list);
        }
	}

	@Override
	public void clear() {
        if (mInited) {
            List<Downloadable> list = mDownloadList.getAll();
            mWorker.stopBatch(list);
        }
	}

	@Override
	public void destory() {
        if (mInited) {
            mDownloadList.clear();
            mWorker.release();
        }
	}

	@Override
	public void addDownloadListener(DownloadListener listener) {
        if (mInited) {
            if (listener != null && !mListeners.contains(listener)) {
                mListeners.add(listener);
            }
        }
    }

	@Override
	public void removeDownloadListener(DownloadListener listener) {
        if (mInited) {
            if (listener != null) {
                mListeners.remove(listener);
            }
        }
	}

    /**
     * 通知回调
     * @param item
     * @param state
     */
    void notifyListeners(Downloadable item, RunState state) {

        if (!mInited) {
            return;
        }

        for (int i = 0; i < mListeners.size(); i++) {
            DownloadListener listener = mListeners.get(i);
            switch (state) {
                case PREPARE:
                    listener.onPrepare(item);
                    break;
                case START:
                    listener.onStart(item);
                    break;
                case RESUME:
                    listener.onResume(item);
                    break;
                case PROGRESS:
                    listener.onProgress(item);
                    break;
                case PAUSE:
                    listener.onPause(item);
                    break;
                case END:
                    listener.onEnd(item);
                    break;
                case ERROR:
                    listener.onError(item);
                    break;
            }

        }
    }
}
