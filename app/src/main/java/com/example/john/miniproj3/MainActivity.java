package com.example.john.miniproj3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mCurrentFragment;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle =
                new ActionBarDrawerToggle(this, drawer, mToolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_clsoe);
        drawer.addDrawerListener(toggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_card_mode);

        changeFragment(new CardModeFragment());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_card_mode:
                changeFragment(new CardModeFragment());
                break;
            case R.id.nav_list_mode:
                changeFragment(new ListModeFragment());
                break;
            case R.id.nav_list:
                startActivity(new Intent(this, DatabaseActivity.class));
                break;
            case R.id.nav_share:
                new MaterialDialog.Builder(MainActivity.this)
                        .title("About this App")
                        .icon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_about, null))
                        .positiveText("Okay")
                        .content("This app helps you manage English phrase")
                        .show();
                break;
            case R.id.nav_personal:
                new MaterialDialog.Builder(MainActivity.this)
                        .title("About the Author")
                        .icon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_personal, null))
                        .positiveText("Okay")
                        .content("Siu Wai King, John")
                        .show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // change fragment
    private void changeFragment(Fragment fragment) {
        if (mCurrentFragment == null || !fragment.getClass().getName().equals(mCurrentFragment.getClass().getName())) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragent_content, fragment).commit();
            mCurrentFragment = fragment;
        }
    }
}
