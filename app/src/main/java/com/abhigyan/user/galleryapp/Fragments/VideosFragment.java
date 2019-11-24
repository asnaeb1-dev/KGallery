package com.abhigyan.user.galleryapp.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhigyan.user.galleryapp.GridAdapters.VideoRVAdapter;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.Config;
import com.abhigyan.user.galleryapp.Utility.MemoryAccess;
import com.abhigyan.user.galleryapp.Utility.Video;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;

public class VideosFragment extends Fragment {

    private ArrayList<Video> al = new ArrayList<>();
    private MemoryAccess memoryAccess;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_videos, container, false);
        recyclerView = myView.findViewById(R.id.videoRV);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getVideosBucketNames();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        recyclerView.setAdapter(new VideoRVAdapter(getContext(), al, width));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Intent intent = new Intent(Config.DEACTIVATE_SECONDARY_TOOLBAR);
        Objects.requireNonNull(getContext()).sendBroadcast(intent);

    }

    private void getVideosBucketNames(){
        memoryAccess = new MemoryAccess(getContext());
        LinkedHashSet<String> bucket = memoryAccess.getBucketNames();
        for(String str : bucket){
            if(!str.equalsIgnoreCase("music'")) {
                Cursor ces = Objects.requireNonNull(getContext()).getContentResolver().
                        query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"*"}, MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " = " + "'" + str + "'", null, null);
                {
                    if (ces != null) {
                        if (ces.moveToLast()) {
                            String videoName = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                            String videoSize = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                            String videoResolution = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                            String videoData = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                            String videoID = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                            String videoDate = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
                            String unique = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                            al.add(new Video(videoName, videoID, videoData, videoSize, videoDate, videoResolution,unique));
                        }
                    }
                }
            }
        }
    }
}
