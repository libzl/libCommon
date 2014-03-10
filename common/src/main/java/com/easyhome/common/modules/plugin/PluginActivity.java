package com.easyhome.common.modules.plugin;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.easyhome.common.uikit.app.BaseActionBarActivity;
import com.easyhome.common.uikit.app.BaseFragment;
import com.easyhome.sample.R;

/**
 * <description>
 *
 * @author zhoulu
 * @date 13-12-20
 */
public class PluginActivity extends BaseActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static class PluginFragment extends BaseFragment {

        public static BaseFragment newInstance() {
            PluginFragment pluginFragment = new PluginFragment();
            pluginFragment.setHasOptionsMenu(true);
            return pluginFragment;
        }

        @Override
        public CharSequence getTitle() {
            return getString(R.string.title_section5);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.plugin, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_bind:
                    break;
                case R.id.action_unbind:
                    break;
            }

            return super.onOptionsItemSelected(item);
        }
    }
}
