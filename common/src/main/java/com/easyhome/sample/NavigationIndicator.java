package com.easyhome.sample;

import com.easyhome.common.uikit.app.BaseFragment;
import com.easyhome.sample.animator.AnimatorActivity;
import com.easyhome.sample.main.DatabaseActivity;
import com.easyhome.sample.main.DownloadActivity;
import com.easyhome.sample.main.MediaActivity;
import com.easyhome.sample.main.PluginActivity;
import com.easyhome.sample.share.ShareActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 跳转指示器
 */
public class NavigationIndicator {

    public static BaseFragment create(int position) {
        Section section = Section.valueOfEnable(position);
        BaseFragment fragment = null;
        switch (section) {
            case SHARE:
                fragment = ShareActivity.ShareFragment.newInstance();
                break;
            case MEDIA:
                fragment = MediaActivity.MediaFragment.newInstance();
                break;
            case DOWNLOAD:
                fragment = DownloadActivity.DownloadFragment.newInstance();
                break;
            case DATABASE:
                fragment = DatabaseActivity.DatabaseFragment.newInstance();
                break;
            case PLUGIN:
                fragment = PluginActivity.PluginFragment.newInstance();
                break;
            case ANIMATOR:
                fragment = AnimatorActivity.AnimatorFragment.newInstance();
                break;
        }
        return fragment;
    }

    public enum Section {
        ANIMATOR("动画", true),
        SHARE("分享", true),
        MEDIA("媒体", true),
        DOWNLOAD("下载"),
        DATABASE("数据库"),
        PLUGIN("插件"),
        NONE("无");


        String mName;
        boolean mEnable;

        Section(String name, boolean enable) {
            mName = name;
            mEnable = enable;
        }

        Section(String name) {
            mName = name;
        }

        public static Section valueOfEnable(int position) {
            Section[] sections = values();
            int selectIndex = position;
            for (Section section : sections) {
                if (section.mEnable) {
                    if (selectIndex == 0) {
                        return section;
                    }
                    selectIndex--;
                }
            }
            return NONE;
        }

        public static Section valueOf(int position) {
            Section[] sections = values();
            for (Section section : sections) {
                if (position == section.ordinal()) {
                    return section;
                }
            }
            return NONE;
        }

        public static String[] getNames() {
            Section[] sections = values();
            List<String> names = new ArrayList<String>();
            for (Section section : sections) {
                if (section.mEnable) {
                    names.add(section.mName);
                }
            }

            String[] arrays = new String[names.size()];
            return names.toArray(arrays);
        }

    }

}
