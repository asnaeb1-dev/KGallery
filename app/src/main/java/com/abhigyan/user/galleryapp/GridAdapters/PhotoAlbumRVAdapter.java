package com.abhigyan.user.galleryapp.GridAdapters;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhigyan.user.galleryapp.Activities.ImageGridActivity;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.Image;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PhotoAlbumRVAdapter extends RecyclerView.Adapter<PhotoAlbumRVAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Image> imgList;
    private int width;

    public PhotoAlbumRVAdapter(Context context, ArrayList<Image> imgList, int width) {
        this.context = context;
        this.imgList = imgList;
        this.width = width;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_ui, parent, false);
        // set the view's size, margins, paddings and layout parameters
        // pass the view to View Holder
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final int pos = holder.getAdapterPosition();
        final MyViewHolder temp = holder;
        Glide.with(context).load(imgList.get(pos).getImageData()).into(temp.imageView);
        if(imgList.get(pos).getImageName().length()<20) {
            holder.titleText.setText(imgList.get(pos).getImageName());
        }else{
            holder.titleText.setText(imgList.get(pos).getImageName().substring(0, 18)+"...");
        }
        temp.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageGridActivity.class);
                intent.putExtra("albName", imgList.get(pos).getImageName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgList.size();
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
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            cardView.setRadius(10f);
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width);
            layoutParams.setMargins(0,10,0,10);
            cardView.setLayoutParams(layoutParams);
        }
    }
}
