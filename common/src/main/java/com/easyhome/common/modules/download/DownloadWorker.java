package com.easyhome.common.modules.download;

import android.text.TextUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 下载任务执行者
 */
class DownloadWorker extends RunWorker {

    private int mMaxCoreThreadCount;
    private CoreThread[] mCoreThread;
    private ExecutorService mExecutorService;

    DownloadWorker(Downloader downloader, IDownloadConfig config) {
        super(downloader, config);
    }

    /**
     * 检查初始化
     */
    private void checkInit() {
        mMaxCoreThreadCount = mConfig.getMaxCoreThreadCount();

        if (mMaxCoreThreadCount == 0) {
            throw new IllegalArgumentException("MaxCoreThreadCount is 0. " +
                    "Please check the DownloadConfig getMaxCoreThreadCount() method return.");
        }
        if (mCoreThread == null) {
            mCoreThread = new CoreThread[mMaxCoreThreadCount];
        }

        if (mExecutorService == null) {
            mExecutorService = mConfig.getExecutor();
        }

        if (mExecutorService == null) {
            throw new IllegalArgumentException("ExecutorService is Null. " +
                    "Please check the DownloadConfig getExecutor() method return.");
        }
    }

    /**
     * 检查释放
     *
     * @return
     */
    private boolean checkRelease() {
        boolean hasRunning = false;
        for (CoreThread thread : mCoreThread) {
            if (thread != null) {
                hasRunning = true;
                break;
            }
        }

        return !hasRunning;
    }

    @Override
    public void start(Downloadable item) {
        checkInit();

        for (CoreThread thread : mCoreThread) {
            if (thread == null) {
                thread = new CoreThread(item);
                thread.future = mExecutorService.submit(thread);
                break;
            }
        }
    }

    @Override
    public void stop(Downloadable item) {
        for (CoreThread thread : mCoreThread) {
            if (thread != null
                    && item != null
                    && thread.item == item) {
                thread.cancel();
                thread = null;
                if (checkRelease()) {
                    release();
                }
                break;
            }
        }
    }

    @Override
    public void stopBatch(List<Downloadable> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (Downloadable item : list) {
            stop(item);
        }
    }

    @Override
    public void release() {
        if (mExecutorService != null
                && !mExecutorService.isShutdown()) {
            mExecutorService.shutdown();
        }

        if (mCoreThread != null) {
            for (CoreThread thread : mCoreThread) {
                if (thread != null) {
                    thread.cancel();
                    thread = null;
                }
            }
            mCoreThread = null;
        }

        mMaxCoreThreadCount = 0;
    }

    class CoreThread implements Runnable {

        public Downloadable item;
        public Future<?> future;
        public boolean running;
        public boolean isResume;

        CoreThread(Downloadable downloadable) {
            item = downloadable;
        }

        void runPrepare() throws DownloadException {
            checkAndSetState(item, RunState.PREPARE);

            if (item == null
                    || TextUtils.isEmpty(item.getSaveDir())
                    || TextUtils.isEmpty(item.getUrl())
                    || TextUtils.isEmpty(item.getName())) {
                throw new DownloadException(DownloadException.ERROR_PREPARE_INVALID,
                        "The downloadable invalid! dump:" + item.toString());
            }

            if (item.getSize() > 0) {
                isResume = true;
            }

            mDownloader.notifyListeners(item);
        }

        void runStart() throws DownloadException {
            checkAndSetState(item, RunState.START);
            mDownloader.notifyListeners(item);
        }

        void runResume() throws DownloadException {
            checkAndSetState(item, RunState.RESUME);
            mDownloader.notifyListeners(item);
        }

        void runProgress() throws DownloadException {
            checkAndSetState(item, RunState.PROGRESS);
            mDownloader.notifyListeners(item);
        }

        void runPause() throws DownloadException {
            checkAndSetState(item, RunState.PAUSE);
            mDownloader.notifyListeners(item);
        }

        void runEnd() throws DownloadException {
            checkAndSetState(item, RunState.END);
            mDownloader.notifyListeners(item);
        }

        void runError() {
            try {
                checkAndSetState(item, RunState.ERROR);
            } catch (DownloadException e) {
                e.printStackTrace();
            }
            mDownloader.notifyListeners(item);
        }

        /**
         * 取消
         */
        public void cancel() {
            if (item.getState() == RunState.END
                    || item.getState() == RunState.ERROR) {
                return;
            }
            if (future != null) {
                future.cancel(true);
            }
            running = false;
            Throwable throwable = null;
            try {
                runPause();
            } catch (DownloadException e) {
                e.printStackTrace();
                throwable = e;
            } catch (Throwable e) {
                e.printStackTrace();
                throwable = e;
            } finally {
                if (throwable != null) {
                    runError();
                }
            }
        }

        @Override
        public void run() {
            Throwable throwable = null;
            try {
                runPrepare();
                if (isResume) {
                    runResume();
                } else {
                    runStart();
                }
                runProgress();
                runEnd();
            } catch (DownloadException e) {
                e.printStackTrace();
                throwable = e;
            } catch (Throwable e) {
                e.printStackTrace();
                throwable = e;
            } finally {
                if (throwable != null) {
                    runError();
                }
            }
        }
    }

}
