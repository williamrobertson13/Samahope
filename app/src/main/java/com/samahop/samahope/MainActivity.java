package com.samahop.samahope;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.samahop.samahope.doctors.DoctorFragment;

public class MainActivity extends AppCompatActivity {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar actionBar;

    private int currentSelectedItem;
    private String mActionBarTitle;

    private FragmentManager.OnBackStackChangedListener
            mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            syncActionBarArrowState();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupNavigationView();
        getSupportFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
        currentSelectedItem = 1;

        DoctorFragment frag = new DoctorFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, frag);
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getPaymentFragment().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        getSupportFragmentManager().removeOnBackStackChangedListener(mOnBackStackChangedListener);
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.isDrawerIndicatorEnabled() && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationView() {

        // initializing NavigationView
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        // setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                // checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else menuItem.setChecked(true);

                // closing drawer on item click
                mDrawerLayout.closeDrawers();

                // check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.nav_item_home:
                        selectHome();
                        mActionBarTitle = "Samahope";
                        break;
                    case R.id.nav_item_activity:
                        selectActivity();
                        mActionBarTitle = "Activity";
                        break;
                    case R.id.nav_item_feedback:
                        selectSendFeedback();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                actionBar.setTitle(mActionBarTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                actionBar.setTitle(mActionBarTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectHome() {
        selectFragmentFromNav(new DoctorFragment(), 1);
    }

    private void selectActivity() {
        selectFragmentFromNav(new DoctorFragment(), 2);
    }

    private void selectSendFeedback() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SENDTO");
        StringBuilder stringBuilder = new StringBuilder("mailto:contact@samahope.org?subject=");
        stringBuilder.append(Uri.encode("Samahope for Android App Feedback"))
                .append("&body=\n\n\n\n")
                .append(Build.MANUFACTURER)
                .append(' ').append(Build.DEVICE)
                .append("\nBuild version: ")
                .append("1.0.0").append(" (")
                .append(")\nOS version: ")
                .append(android.os.Build.VERSION.SDK_INT)
                .append("\nLanguage: ")
                .append(Resources.getSystem().getConfiguration().locale);
        intent.setData(Uri.parse(stringBuilder.toString()));
        startActivity(Intent.createChooser(intent, "Send mail using... "));
        currentSelectedItem = 3;
    }

    private void selectFragmentFromNav(Fragment fragment, int itemId) {
        if (currentSelectedItem != itemId) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, fragment);
            transaction.commit();
            currentSelectedItem = itemId;
        }
    }

    private void syncActionBarArrowState() {
        int backStackEntryCount =
                getSupportFragmentManager().getBackStackEntryCount();
        mDrawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
    }

    private Fragment getPaymentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.frame_layout);
    }

    public DrawerLayout getDrawerLayout() { return mDrawerLayout; }
}