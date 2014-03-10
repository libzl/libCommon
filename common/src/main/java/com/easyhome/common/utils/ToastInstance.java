package com.easyhome.common.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easyhome.sample.R;

class ToastInstance {
	private static final int Y_OFFSET = 120;
	private static ToastInstance instance = null;
	private Context mApplicationContext;
	private LayoutInflater mInflater;

	private Handler mUiHandler = new Handler(Looper.getMainLooper());
	private static Object mLock = new Object();

	private Toast mToast;
	private Toast mLongToast;

	private ToastInstance(Context context) {
		mApplicationContext = context;
		mInflater = LayoutInflater.from(mApplicationContext);
	}

	public static ToastInstance getInstance(Context context) {
		if (instance == null) {
			instance = new ToastInstance(context.getApplicationContext());
		}
		return instance;
	}

	public void showShortToast2(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(mApplicationContext, text,
					Toast.LENGTH_SHORT);
			int xOffset = mToast.getXOffset();
			int yOffset = mToast.getYOffset() + Y_OFFSET;
			mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
					xOffset, yOffset);
		} else {
			// mToast.cancel();
			mToast.setText(text);
		}
		if (mLongToast != null)
			mLongToast.cancel();

		mToast.show();
	}

	public void showShortToast(Context context, final String text) {
		mUiHandler.post(new Runnable() {

			@Override
			public void run() {
				synchronized (mLock) {
					showShortToast2(mApplicationContext, text);
				}
			}
		});

	}

	public void showShortToast(Context context, int resId) {
		String text = mApplicationContext.getString(resId);
		showShortToast(context, text);
	}

	public void showLongToast2(Context context, String text) {
		if (mLongToast == null) {
			mLongToast = Toast.makeText(mApplicationContext, text,
					Toast.LENGTH_LONG);
			int xOffset = mLongToast.getXOffset();
			int yOffset = mLongToast.getYOffset() + Y_OFFSET;
			mLongToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
					xOffset, yOffset);
		} else {
			// mLongToast.cancel();
			mLongToast.setText(text);
		}
		if (mToast != null)
			mToast.cancel();
		mLongToast.show();
	}

	public void showLongToast(Context context, final String text) {
		mUiHandler.post(new Runnable() {

			@Override
			public void run() {
				synchronized (mLock) {
					showLongToast2(mApplicationContext, text);
				}
			}
		});

	}

	public void showLongToast(Context context, int resId) {
		String text = mApplicationContext.getString(resId);
		showLongToast(context, text);
	}

	private Toast mPopToast;
	private TextView mTxtMessage;

	public void showPopToast(Context context, int resId) {
		String text = mApplicationContext.getResources().getString(resId);
		showPopToast(context, text);
	}

	public void showPopToast(Context context, String text) {
		if (mPopToast == null) {
			makePopToast(text);
		} else {
			mPopToast.cancel();
			mTxtMessage.setText(text);
		}
		mPopToast.show();
	}

	private void makePopToast(String text) {
		LinearLayout layout = (LinearLayout) mInflater.inflate(
				R.layout.layout_pop_toast, null);

		mTxtMessage = (TextView) layout.findViewById(R.id.toast_text);
		mTxtMessage.setText(text);

		mPopToast = new Toast(mApplicationContext.getApplicationContext());
		mPopToast.setGravity(
				Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
				0,
				mApplicationContext.getResources().getDimensionPixelSize(
						R.dimen.tiptext_bottom_margin));
		mPopToast.setView(layout);
	}

	private void showTextToast(final String text, int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(mApplicationContext, text, duration);
			int xOffset = mToast.getXOffset();
			int yOffset = mToast.getYOffset() + Y_OFFSET;
			mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
					xOffset, yOffset);
		} else {
			mToast.setText(text);
		}
		if (mLongToast != null)
			mLongToast.cancel();

		mToast.show();
	}

}
