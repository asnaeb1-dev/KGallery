package com.abhigyan.user.galleryapp.GridAdapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhigyan.user.galleryapp.Activities.ViewActivity;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.FileHandler;
import com.abhigyan.user.galleryapp.Utility.Video;
import com.bumptech.glide.RequestManager;

import java.io.File;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    public FrameLayout media_container;
    public TextView title;
    public ImageView thumbnail, volumeControl, moreControls;
    public ProgressBar progressBar;
    public View parent;
    public RequestManager requestManager;

    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.titleTV);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volume_control);
        moreControls = itemView.findViewById(R.id.more_video);
    }

    public void onBind(Video mediaObject, RequestManager requestManager) {
        this.requestManager = requestManager;
        parent.setTag(this);
        if(mediaObject.getVideoUniqueName().length()<15) {
            title.setText(mediaObject.getVideoUniqueName());
        }else{
            title.setText(mediaObject.getVideoUniqueName().substring(0,15)+"...");
        }
        Uri uri = Uri.fromFile(new File(mediaObject.getVideoData()));

        this.requestManager
                .load(uri)
                .thumbnail(0.1f)
                .into(this.thumbnail);
    }

}
