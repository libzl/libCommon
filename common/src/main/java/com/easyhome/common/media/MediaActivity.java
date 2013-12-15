package com.easyhome.common.media;

import com.easyhome.common.R;
import com.easyhome.common.app.BaseFragment;

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
