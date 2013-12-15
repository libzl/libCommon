package com.easyhome.common.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 */
public abstract class BaseFragment extends Fragment implements IFragment{
    private static final boolean DEBUG_FRAGMENT_LIFECYCLE = true;
    private OnFragmentLifeChangeListener mListener;

    public CharSequence getTitle() {
        if (getActivity() != null) {
            return getActivity().getTitle();
        }
        return null;
    }

    private void logFragmentLifecycle(String message) {
        Log.i(((Object) this).getClass().getSimpleName(), "[Fragment Lifecycle] " + message);
    }

    public void setOnFragmentLifeChangeListener(OnFragmentLifeChangeListener lifeChangeListener) {
        mListener = lifeChangeListener;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onActivityCreated);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onActivityCreated()");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onActivityResult);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onActivityResult()");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onAttach);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onAttach()");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onConfigurationChanged()");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onContextItemSelected()");
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onCreate);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onCreate()");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onCreateContextMenu()");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onCreateOptionsMenu()");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onCreateView);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onCreateView()");
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onDestroy);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onDestroy()");
        }
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onDestroyOptionsMenu()");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onDestroyView);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onDestroyView()");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onDetach);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onDetach()");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onHiddenChanged);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onHiddenChanged()");
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onInflate);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onInflate()");
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onLowMemory()");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onOptionsItemSelected()");
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onOptionsMenuClosed()");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onPause);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onPause()");
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onPrepareOptionsMenu()");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onResume);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onResume()");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onSaveInstanceState()");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onStart);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onStart()");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onChange(this, LIFE_STATE_onStop);
        }
        if (DEBUG_FRAGMENT_LIFECYCLE) {
            logFragmentLifecycle("onStop()");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(((Object)this).getClass().getSimpleName(), "method:onTouchEvent() info:" + event);
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(((Object)this).getClass().getSimpleName(), "method:onKeyDown() info: keycode = "
                + keyCode + "| event = " + event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return onBackPressed();
        }
        return false;
    }

    @Override
    public boolean onBackPressed() {
        Log.i(((Object) this).getClass().getSimpleName(), "onBackPressed");
        return false;
    }
}
