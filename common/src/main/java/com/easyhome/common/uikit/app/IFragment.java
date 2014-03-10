// Copy right Baidu Inc.
package com.easyhome.common.uikit.app;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Fragment的接口
 * 
 * @autor kevin
 * @since 2013-Apr 16, 2013
 */
public interface IFragment {

	public interface OnFragmentLifeChangeListener {
		void onChange(IFragment fragment, int lifeState);
	}

	public static final int LIFE_STATE_onCreateView = 0;
	public static final int LIFE_STATE_onActivityCreated = 1;
	public static final int LIFE_STATE_onActivityResult = 2;
	public static final int LIFE_STATE_onAttach = 3;
	public static final int LIFE_STATE_onCreate = 4;
	public static final int LIFE_STATE_onCreateAnimator = 5;
	public static final int LIFE_STATE_onDestroy = 6;
	public static final int LIFE_STATE_onDestroyView = 7;
	public static final int LIFE_STATE_onDetach = 8;
	public static final int LIFE_STATE_onPause = 9;
	public static final int LIFE_STATE_onStop = 10;
	public static final int LIFE_STATE_onStart = 11;
	public static final int LIFE_STATE_onViewCreated = 12;
	public static final int LIFE_STATE_onResume = 13;
	public static final int LIFE_STATE_onHiddenChanged = 14;
	public static final int LIFE_STATE_onInflate = 15;


	/**
	 * 自定义keyDown
	 * 
	 * @param keyCode
	 * @param event
	 * @return
	 */
	boolean onKeyDown(int keyCode, KeyEvent event);

	/**
	 * 自定义touch事件
	 * 
	 * @param event
	 * @return
	 */
	boolean onTouchEvent(MotionEvent event);

	/**
	 * 自定义返回事件
	 * 
	 * @return
	 */
	boolean onBackPressed();
}
