package com.abhigyan.user.galleryapp.GridAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.Video;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class VideoPlayerRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Video> mediaObjects;
    private RequestManager requestManager;


    public VideoPlayerRVAdapter(ArrayList<Video> mediaObjects, RequestManager requestManager) {
        this.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VideoPlayerViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_list_item_ui, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((VideoPlayerViewHolder)viewHolder).onBind(mediaObjects.get(i), requestManager);
    }

    @Override
    public int getItemCount() {
        return mediaObjects.size();
    }
}
