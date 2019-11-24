package com.abhigyan.user.galleryapp.Activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhigyan.user.galleryapp.Utility.Config;
import com.abhigyan.user.galleryapp.Fragments.CameraFragment;
import com.abhigyan.user.galleryapp.Fragments.PhotoAlbums;
import com.abhigyan.user.galleryapp.Fragments.VideosFragment;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.ViewPager.CubeInTransformer;

import java.util.HashMap;

public class MainTabbedActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;
    private TextView headerText;

    private ImageView shareAll, selectAll, deleteAll, burgerIcon, more;

    private boolean instrumentClusterShow = false;
    private boolean selectAllEnabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        fab = findViewById(R.id.fab);
        headerText = findViewById(R.id.tagText);
        burgerIcon = findViewById(R.id.burger);
        more = findViewById(R.id.more);

        shareAll = findViewById(R.id.shareAll);
        selectAll = findViewById(R.id.selectAll);
        deleteAll = findViewById(R.id.deleteAll);
        shareAll.animate().translationX(2000f);
        selectAll.animate().translationX(2000f);
        deleteAll.animate().translationX(2000f);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new CubeInTransformer());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                int pos = mViewPager.getCurrentItem();
                switch (pos){
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_black_24dp));
                        //toolbar.setVisibility(View.VISIBLE);
                        headerText.setText("Camera");
                        if(instrumentClusterShow){
                            selectAll.setVisibility(View.GONE);
                            shareAll.setVisibility(View.GONE);
                            deleteAll.setVisibility(View.GONE);
                            fab.setVisibility(View.GONE);
                        }else{
                            fab.setVisibility(View.VISIBLE);
                        }

                        return;
                    case 1:
                        headerText.setText("Albums");
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                        //toolbar.setVisibility(View.GONE);
                        selectAll.setVisibility(View.GONE);
                        shareAll.setVisibility(View.GONE);
                        deleteAll.setVisibility(View.GONE);
                        return;

                    case 2:
                        headerText.setText("Videos");
                        fab.setVisibility(View.INVISIBLE);
                        //toolbar.setVisibility(View.VISIBLE);
                        selectAll.setVisibility(View.GONE);
                        shareAll.setVisibility(View.GONE);
                        deleteAll.setVisibility(View.GONE);
                        return;
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        registerReceiver(activate, new IntentFilter(Config.ACTIVATE_SECONDARY_TOOLBAR));
        registerReceiver(deactivate, new IntentFilter(Config.DEACTIVATE_SECONDARY_TOOLBAR));
    }

    private void activateInstrumentCluster(){

        shareAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Config.SHARE_IMAGES);
                sendBroadcast(shareIntent);
            }
        });

        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent select = new Intent(Config.SELECT_ALL);
                if(!selectAllEnabled) {
                    selectAll.setImageDrawable(getResources().getDrawable(android.R.drawable.checkbox_off_background));
                    select.putExtra("select", true);
                    sendBroadcast(select);
                    selectAllEnabled = true;
                }else{
                    selectAll.setImageDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
                    select.putExtra("select", false);
                    sendBroadcast(select);
                    selectAllEnabled = false;
                    //hide instrument cluster
                    hideInstrumentCluster();
                }
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private BroadcastReceiver activate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showInstrumentCluster();
        }
    };

    private BroadcastReceiver deactivate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideInstrumentCluster();
        }
    };

    @SuppressLint("RestrictedApi")
    private void showInstrumentCluster(){
        selectAll.setVisibility(View.VISIBLE);
        shareAll.setVisibility(View.VISIBLE);
        deleteAll.setVisibility(View.VISIBLE);
        selectAll.animate().translationXBy(-2000f);
        shareAll.animate().translationXBy(-2000f);
        deleteAll.animate().translationXBy(-2000f);
        activateInstrumentCluster();
        fab.setVisibility(View.GONE);
        instrumentClusterShow = true;
    }

    @SuppressLint("RestrictedApi")
    private void hideInstrumentCluster(){
        selectAll.setVisibility(View.GONE);
        shareAll.setVisibility(View.GONE);
        deleteAll.setVisibility(View.GONE);
        selectAll.animate().translationXBy(2000f);
        shareAll.animate().translationXBy(2000f);
        deleteAll.animate().translationXBy(2000f);
        instrumentClusterShow = false;
        fab.setVisibility(View.VISIBLE);
    }

    public void openCam(View view){
        openCamera();
        new CameraFragment();
    }

    //open camera
    private void openCamera(){
        Toast.makeText(this, "Opening camera", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_tabbed, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return new CameraFragment();

                case 1:
                    return new PhotoAlbums();

                case 2:
                    return new VideosFragment();
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }
}
