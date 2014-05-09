package com.easyhome.sample.main;

import com.easyhome.common.uikit.app.BaseFragment;
import com.easyhome.sample.R;

/**
 * <description>
 *
 * @author zhoulu
 * @date 13-12-11
 */
public class DownloadActivity {
    public static class DownloadFragment extends BaseFragment {
        @Override
        public CharSequence getTitle() {
            return getString(R.string.title_section3);
        }

        public static BaseFragment newInstance() {
            DownloadFragment fragment = new DownloadFragment();
            return fragment;
        }
    }
}
