package com.easyhome.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

/**
 * UI线程Handler
 *
 * 支持循环、单一功能
 *
 * @author by kevin on 3/11/14.
 */
public class UiHandler {
    /**
     * 循环执行
     */
    private static final int ACTION_LOOP = 0x001;

    /**
     * 循环执行有限次数
     */
    private static final int ACTION_LIMIT_TIME_LOOP = 0x002;

	private static Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int action = msg.what;
            switch (action) {
                case ACTION_LOOP:
                    Runnable runnable = (Runnable) msg.obj;
                    new ImplRunnable(runnable).run();
                    loopImplDelay(runnable, msg.arg1);
                    break;
                case ACTION_LIMIT_TIME_LOOP:
                    LoopHandler handler = (LoopHandler) msg.obj;
                    loopByLimitDelay(handler, msg.arg1, (--msg.arg2));
                    break;
            }

        }
    };

	private static Object token = new Object();

    public static Handler getUiHandler() {
        return uiHandler;
    }

    /**
     * 在UI线程中执行操作
     * @param r 操作逻辑
     * @return
     */
	public static boolean post(Runnable r) {
		return uiHandler != null && uiHandler.post(new ImplRunnable(r));
	}

    /**
     * 延时执行UI线程中的操作
     * @param r 操作逻辑
     * @param delayMillis 等待时间
     * @return
     */
    public static boolean postDelayed(Runnable r, long delayMillis) {
        return uiHandler != null && uiHandler.postDelayed(new ImplRunnable(r), delayMillis);
    }

    /**
     * 执行逻辑过程中仅保留一次执行
     * @param r
     * @return
     */
	public static boolean postOnce(Runnable r) {
		if (uiHandler == null) {
			return false;
		}
		uiHandler.removeCallbacks(new ImplRunnable(r), token);
		return uiHandler.postAtTime(new ImplRunnable(r), token, SystemClock.uptimeMillis());
	}

    /**
     * 延时执行逻辑过程中仅保留一次执行
     * @param r
     * @param delayMillis
     * @return
     */
    public static boolean postOnceDelayed(Runnable r, long delayMillis) {
        if (uiHandler == null) {
            return false;
        }
        uiHandler.removeCallbacks(new ImplRunnable(r), token);
        return uiHandler.postAtTime(new ImplRunnable(r), token, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 循环执行操作
     * @param runnable 操作
     * @param frequency 频次
     */
    public static void loop(Runnable runnable, int frequency) {
        if (uiHandler == null)  return;

        uiHandler.removeMessages(ACTION_LOOP);
        Message msg = uiHandler.obtainMessage(ACTION_LOOP);
        msg.obj = runnable;
        msg.arg1 = frequency;
        msg.sendToTarget();
    }

    private static void loopImplDelay(Runnable runnable, int frequency) {
        if (uiHandler == null)  return;

        uiHandler.removeMessages(ACTION_LOOP);
        Message msg = uiHandler.obtainMessage(ACTION_LOOP);
        msg.obj = runnable;
        msg.arg1 = frequency;
        uiHandler.sendMessageDelayed(msg, frequency);
    }
    /**
     * 停止循环
     */
    public static void stopLoop() {
        if (uiHandler == null)  return;

        uiHandler.removeMessages(ACTION_LOOP);
    }

    /**
     * 执行有限次数的循环
     * @param handler
     * @param frequency
     * @param time
     */
    public static void loopByLimit(LoopHandler handler, int frequency, int time) {
        if (uiHandler == null)  return;

        uiHandler.removeMessages(ACTION_LIMIT_TIME_LOOP);
        if (time == 0) {
            new ImplLoopHandler(handler).end();
            return;
        }

        Message msg = uiHandler.obtainMessage(ACTION_LIMIT_TIME_LOOP);
        msg.obj = handler;
        msg.arg1 = frequency;
        msg.sendToTarget();
    }

    private static void loopByLimitDelay(LoopHandler handler, int frequency, int time) {
        if (uiHandler == null)  return;

        uiHandler.removeMessages(ACTION_LIMIT_TIME_LOOP);
        if (time == 0) {
            new ImplLoopHandler(handler).end();
            return;
        }

        Message msg = uiHandler.obtainMessage(ACTION_LIMIT_TIME_LOOP);
        msg.obj = handler;
        msg.arg1 = frequency;
        uiHandler.sendMessageDelayed(msg, frequency);
    }

    public static interface LoopHandler {
        void run();
        void end();
    }

    private static class ImplLoopHandler implements LoopHandler {

        LoopHandler runer;

        public ImplLoopHandler(LoopHandler r) {
            runer = r;
        }

        @Override
        public void run() {
            try {
                if (runer != null) {
                    runer.run();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        @Override
        public void end() {
            try {
                if (runer != null) {
                    runer.end();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

    }

    private static class ImplRunnable implements Runnable {

        Runnable runer;

        public ImplRunnable(Runnable r) {
            runer = r;
        }

        @Override
        public void run() {
            try {
                if (runer != null) {
                    runer.run();
                }
            } catch (Throwable t) {
               t.printStackTrace();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Runnable) {
                return runer.equals((Runnable)o);
            }
            return super.equals(o);
        }
    }
}
