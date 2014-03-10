package com.easyhome.common.modules.tables;

import com.easyhome.common.uikit.app.BaseFragment;
import com.easyhome.sample.R;

/**
 * <description>
 *
 * @author zhoulu
 * @date 13-12-11
 */
public class DatabaseActivity {

    public static class DatabaseFragment extends BaseFragment {
        @Override
        public CharSequence getTitle() {
            return getString(R.string.title_section4);
        }

        public static BaseFragment newInstance() {
            DatabaseFragment fragment = new DatabaseFragment();
            return fragment;
        }
    }
}
