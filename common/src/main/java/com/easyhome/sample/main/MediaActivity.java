package com.easyhome.sample.main;

import com.easyhome.common.uikit.app.BaseFragment;
import com.easyhome.sample.R;

/**
 * 播放模块数据
 */
public class MediaActivity {


    public static class MediaFragment extends BaseFragment {

        @Override
        public CharSequence getTitle() {
            return getString(R.string.title_section2);
        }

        public static BaseFragment newInstance() {
            MediaFragment fragment = new MediaFragment();
            return fragment;
        }
    }
}
