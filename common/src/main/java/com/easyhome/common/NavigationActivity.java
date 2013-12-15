package com.easyhome.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;

import com.easyhome.common.app.BaseActionBarActivity;
import com.easyhome.common.app.BaseFragment;
import com.easyhome.common.app.IFragment;
import com.easyhome.common.share.ShareManager;

public class NavigationActivity extends BaseActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        ShareManager.getInstance().onHandleNewIntent(this, getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareManager.getInstance().onHandleActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        ShareManager.getInstance().onHandleNewIntent(this, intent);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        final BaseFragment fragment = NavigationIndicator.create(position);
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        fragment.setOnFragmentLifeChangeListener(new IFragment.OnFragmentLifeChangeListener() {
            @Override
            public void onChange(IFragment f, int lifeState) {
                fragment.setOnFragmentLifeChangeListener(null);
                if (lifeState == IFragment.LIFE_STATE_onAttach) {
                    mTitle = fragment.getTitle();
                }
            }
        });
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

}
