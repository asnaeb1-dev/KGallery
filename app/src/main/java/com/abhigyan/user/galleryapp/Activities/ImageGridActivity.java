package com.abhigyan.user.galleryapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhigyan.user.galleryapp.Utility.Config;
import com.abhigyan.user.galleryapp.Utility.Image;
import com.abhigyan.user.galleryapp.GridAdapters.ImageAdapter;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.MemoryAccess;

import java.util.ArrayList;
import java.util.Collections;

public class ImageGridActivity extends AppCompatActivity {

    private ArrayList<Image> imgList = new ArrayList<>();

    private String albumName;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout instrumentCluster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_grid_layout);

        toolbar = findViewById(R.id.toolbarGrid);
        instrumentCluster = findViewById(R.id.inst_cluster);

        collapsingToolbarLayout = findViewById(R.id.col_toolbar_layout);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        albumName = getIntent().getStringExtra("albName");
        collapsingToolbarLayout.setTitle(albumName);
        getImages();
        RecyclerView recyclerView = findViewById(R.id.grid);

        recyclerView.setAdapter(new ImageAdapter(this, imgList, width, 2));
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        //register receiver
        registerReceiver(activate, new IntentFilter(Config.ACTIVATE_SECONDARY_TOOLBAR));
        registerReceiver(deactivate, new IntentFilter(Config.DEACTIVATE_SECONDARY_TOOLBAR));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void activateInstrumentCluster(){
        ImageView share = findViewById(R.id.shareIns),
                    delete = findViewById(R.id.deleteIns),
                    slideshow = findViewById(R.id.slideIns),
                    lock = findViewById(R.id.lock_ins);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImageGridActivity.this, "share pics", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImageGridActivity.this, "delete pics", Toast.LENGTH_SHORT).show();

            }
        });

        slideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImageGridActivity.this, "slide pics", Toast.LENGTH_SHORT).show();
                Intent activateSlideShow = new Intent(Config.ACTIVATE_SLIDE_SHOW);
                sendBroadcast(activateSlideShow);
            }
        });

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ImageGridActivity.this, "lock this image", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Config.LOCK_FILES);
                sendBroadcast(intent);
            }
        });
    }

    BroadcastReceiver activate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            instrumentCluster.setVisibility(View.VISIBLE);
            activateInstrumentCluster();
            toolbar.inflateMenu(R.menu.long_press_menu);
        }
    };

    BroadcastReceiver deactivate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            instrumentCluster.setVisibility(View.GONE);
            toolbar.inflateMenu(R.menu.menu_main_tabbed);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_tabbed, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getImages(){
        MemoryAccess memoryAccess = new MemoryAccess(this);
        imgList = memoryAccess.getImages(albumName);
        Collections.reverse(imgList);
    }
}
