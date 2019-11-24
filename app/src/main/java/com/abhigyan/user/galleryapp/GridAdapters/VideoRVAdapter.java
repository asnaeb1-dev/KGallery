package com.abhigyan.user.galleryapp.GridAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhigyan.user.galleryapp.Activities.VideoContentActivity;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.Video;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class VideoRVAdapter extends RecyclerView.Adapter<VideoRVAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Video> videoArrayList;
    private int width;

    public VideoRVAdapter(Context context, ArrayList<Video> imgList, int width) {
        this.context = context;
        this.videoArrayList = imgList;
        this.width = width;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_ui, parent, false);
        // set the view's size, margins, paddings and layout parameters
        // pass the view to View Holder
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Uri uri = Uri.fromFile(new File(videoArrayList.get(holder.getAdapterPosition()).getVideoData()));
        Glide.with(context).
                load(uri).
                thumbnail(0.1f).
                into(holder.imageView);

        holder.titleText.setText(videoArrayList.get(holder.getAdapterPosition()).getVideoName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoContentActivity.class);
                intent.putExtra("video_bucket", videoArrayList.get(holder.getAdapterPosition()).getVideoName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;
        TextView titleText, countText;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.gridUnitUI);
            imageView = itemView.findViewById(R.id.unitImg);
            titleText = itemView.findViewById(R.id.albNmUnit);
            countText = itemView.findViewById(R.id.countImg);
            cardView.setRadius(10f);
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width);
            layoutParams.setMargins(0, 10, 0, 10);
            cardView.setLayoutParams(layoutParams);
        }
    }
}
