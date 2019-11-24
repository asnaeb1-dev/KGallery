package com.abhigyan.user.galleryapp.Activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.abhigyan.user.galleryapp.ExoPlayerRecyclerView;
import com.abhigyan.user.galleryapp.GridAdapters.VideoPlayerRVAdapter;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.MemoryAccess;
import com.abhigyan.user.galleryapp.Utility.Video;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;

public class VideoContentActivity extends AppCompatActivity {

    private ExoPlayerRecyclerView mRecyclerView;
    private ArrayList<Video> videoList;
    private String videoBucket;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_content);

        videoBucket = getIntent().getStringExtra("video_bucket");
        collapsingToolbarLayout = findViewById(R.id.col_toolbar_layout_video);
        collapsingToolbarLayout.setTitle(videoBucket);
        mRecyclerView = findViewById(R.id.recycler_view_video);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        MemoryAccess memoryAccess = new MemoryAccess(this);
        videoList = memoryAccess.getVideos(videoBucket);
        initRecyclerView();
    }

    private void initRecyclerView(){
        mRecyclerView.setMediaObjects(videoList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        Collections.reverse(videoList);
        VideoPlayerRVAdapter adapter = new VideoPlayerRVAdapter(videoList, initGlide());
        mRecyclerView.setAdapter(adapter);
    }

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(android.R.color.white)
                .error(android.R.color.white);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mRecyclerView!=null){
            mRecyclerView.pausePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        if(mRecyclerView!=null)
            mRecyclerView.releasePlayer();
        super.onDestroy();
    }

}
