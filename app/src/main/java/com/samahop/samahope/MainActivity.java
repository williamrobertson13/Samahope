package com.samahop.samahope;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.samahop.samahope.doctors.DoctorProfileAdapter;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DoctorProfileAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mActionBar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(mActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_listview);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mActionBar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getTitle());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getTitle());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.doctors_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        // setup a linear view layout for the list of doctor profiles
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);

        // initialize the adapter now that the parse data is set up
        dataAdapter = new DoctorProfileAdapter(this, mRecyclerView);
        mRecyclerView.setAdapter(dataAdapter);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.sama_blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataAdapter.loadDoctors();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFeedbackClicked(View view) {
        Log.e("NO","BP");
                Intent intent = new Intent();
        intent.setAction("android.intent.action.SENDTO");
        StringBuilder stringbuilder = new StringBuilder("mailto:contact@samahope.org?subject=");
        stringbuilder.append(Uri.encode("Samahope for Android App Feedback"))
                .append("&body=\n\n\n\n")
                .append(Build.MANUFACTURER)
                .append(' ').append(Build.DEVICE)
                .append("\nBuild version: ")
                .append("1.0.0").append(" (")
                .append(")\nOS version: ")
                .append(android.os.Build.VERSION.SDK_INT)
                .append("\nLanguage: ")
                .append(Resources.getSystem().getConfiguration().locale);
        intent.setData(Uri.parse(stringbuilder.toString()));
        startActivity(Intent.createChooser(intent, "Send mail using... "));
    }

    public DoctorProfileAdapter getDataAdapter() {
        return dataAdapter;
    }
}
