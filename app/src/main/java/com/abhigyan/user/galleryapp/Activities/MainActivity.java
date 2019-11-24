package com.abhigyan.user.galleryapp.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abhigyan.user.galleryapp.FavouritesDB;
import com.abhigyan.user.galleryapp.Fragments.AboutFragment;
import com.abhigyan.user.galleryapp.Fragments.CameraFragment;
import com.abhigyan.user.galleryapp.Fragments.FavouritesFragment;
import com.abhigyan.user.galleryapp.Fragments.PhotoAlbums;
import com.abhigyan.user.galleryapp.Fragments.VideosFragment;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.Config;
import com.abhigyan.user.galleryapp.Utility.Permissions;
import com.abhigyan.user.galleryapp.Utility.ShareApp;
import com.abhigyan.user.galleryapp.ViewPager.CubeInTransformer;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPager principleVP;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DrawerLayout drawer;
    private LinearLayout instCluster;
    private  Toolbar toolbar;
    private FloatingActionButton fab;
    private boolean isCamera = true;

    private boolean perms = false;
    private boolean doubleBackToExitPressedOnce;
    private boolean isClusterShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarX);setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        fab = findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_black_24dp));
        fab.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_gradient));
        collapsingToolbarLayout = findViewById(R.id.col_toolbar_layout_X);
        instCluster = findViewById(R.id.inst_cluster);
        instCluster.setVisibility(View.GONE);
        principleVP = findViewById(R.id.principleVP);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        principleVP.setAdapter(mSectionsPagerAdapter);
        principleVP.setPageTransformer(true, new CubeInTransformer());

        enableScroll();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        registerReceiver(activate, new IntentFilter(Config.ACTIVATE_SECONDARY_TOOLBAR));
        registerReceiver(deactivate, new IntentFilter(Config.DEACTIVATE_SECONDARY_TOOLBAR));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCamera){
                    //open Camera
                    Intent intent = new Intent(Config.CAMERA);
                    startActivity(intent);
                }else{
                    //add new Albums
                }
            }
        });
    }
    //activate instrument cluster
    private void activateInstrumentCluster(){
        ImageView share = findViewById(R.id.shareIns),
                delete = findViewById(R.id.deleteIns),
                slideshow = findViewById(R.id.slideIns),
                lock = findViewById(R.id.lock_ins);

        //share multiple images
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Config.SHARE_ALL);
                sendBroadcast(intent);
            }
        });
        //delete multiple images
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Config.DELETE_ALL);
                sendBroadcast(intent);
            }
        });
        //slideshow Images
        slideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activateSlideShow = new Intent(Config.ACTIVATE_SLIDE_SHOW);
                sendBroadcast(activateSlideShow);
            }
        });

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private BroadcastReceiver activate = new BroadcastReceiver() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onReceive(Context context, Intent intent) {
            //activate instrument cluster
            instCluster.setVisibility(View.VISIBLE);
            activateInstrumentCluster();
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.long_press_menu);
            fab.setVisibility(View.GONE);
            activateMenu();
            isClusterShowing = true;
        }
    };

    private BroadcastReceiver deactivate = new BroadcastReceiver() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onReceive(Context context, Intent intent) {
            //deactivate instrument cluster
            instCluster.setVisibility(View.GONE);
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.main);
            fab.setVisibility(View.VISIBLE);
            isClusterShowing = false;
        }
    };

    private void activateMenu(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id){
                    case R.id.action_select_all:
                            Intent selectAll = new Intent(Config.SELECT_ALL);
                            sendBroadcast(selectAll);
                        return true;

                    case R.id.action_deselect_all:
                        Intent deselectAll = new Intent(Config.DESELECT_ALL);
                        sendBroadcast(deselectAll);
                        return true;
                    case R.id.rotateLeft:
                        return true;

                    case R.id.rotateRight:
                        return true;

                    case R.id.collage:
                        return true;
                }

                return false;
            }
        });
    }

    private void enableScroll(){
        principleVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                int pos = principleVP.getCurrentItem();
                switch(pos){
                    case 0:
                        collapsingToolbarLayout.setTitle(getResources().getString(R.string.photos_tab));
                        if(!isClusterShowing) {
                            fab.setVisibility(View.VISIBLE);
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_black_24dp));
                            isCamera = true;
                        }
                        return;

                    case 1:
                        collapsingToolbarLayout.setTitle(getResources().getString(R.string.albums_tab));
                        if (!isClusterShowing) {
                            fab.setVisibility(View.VISIBLE);
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                            isCamera = false;
                        }
                        return;

                    case 2:
                        collapsingToolbarLayout.setTitle(getResources().getString(R.string.videos_tab));
                        fab.setVisibility(View.GONE);
                        isCamera = false;

                        return;

                    case 3:
                        collapsingToolbarLayout.setTitle(getResources().getString(R.string.favou_tabs));
                        fab.setVisibility(View.GONE);
                        isCamera = false;

                        return;

                        default:
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
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.exit_message), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_favs) {
            boolean b  = new FavouritesDB(getApplicationContext()).deleteThisDatabase(getApplicationContext(), "favourites.db");
            if(b){
                Toast.makeText(this, "Cleared favourites!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Failed to execute!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_camera:
                principleVP.setCurrentItem(0, true);
                break;

            case R.id.nav_albums:
                //open albums tab
                principleVP.setCurrentItem(1, true);
                break;

            case R.id.nav_videos:
                //open videos tab
                principleVP.setCurrentItem(2, true);
                break;

            case R.id.nav_clouds:
                //show all files stored in cloud
                break;

            case R.id.nav_lock:
                //show all files that are in safe zone
                //startActivity(new Intent(getApplicationContext(), SecurityActivity.class));
                Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show();
               break;

            case R.id.nav_about:
                //show about the developer
                AboutFragment dialogFragment = new AboutFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                dialogFragment.show(ft, "dialog");

                break;

            case R.id.nav_share:
                //share the app
                new ShareApp(getApplicationContext()).shareThisApp();
                break;

            case R.id.nav_settings:
                //open settings
                break;

            case R.id.nav_favs:
                principleVP.setCurrentItem(3, true);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                case 3:
                    return new FavouritesFragment();
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }

}
