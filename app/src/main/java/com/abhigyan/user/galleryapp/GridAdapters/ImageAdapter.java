package com.abhigyan.user.galleryapp.GridAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abhigyan.user.galleryapp.Activities.SlideShowActivity;
import com.abhigyan.user.galleryapp.Activities.ViewActivity;
import com.abhigyan.user.galleryapp.Utility.Config;
import com.abhigyan.user.galleryapp.Utility.FileHandler;
import com.abhigyan.user.galleryapp.Utility.Image;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.ViewPager.ViewPagerAdapter;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Image> imgList;
    private int dimensions;

    private int checkVal;
    private SparseBooleanArray itemStateArray= new SparseBooleanArray();
    private HashMap<Integer, Boolean> hashMap = new HashMap<>();

    private ArrayList<Integer> checkedItems = new ArrayList<>();

    public ImageAdapter(Context context, ArrayList<Image> imgList, int dimensions, int checkVal) {
        this.context = context;
        this.imgList = imgList;
        this.dimensions = dimensions;
        this.checkVal = checkVal;

        //register all receivers
        context.registerReceiver(slideShow, new IntentFilter(Config.ACTIVATE_SLIDE_SHOW));
        context.registerReceiver(shareImages, new IntentFilter(Config.SHARE_ALL));
        context.registerReceiver(deleteAll, new IntentFilter(Config.DELETE_ALL));
        context.registerReceiver(selectAll, new IntentFilter(Config.SELECT_ALL));
        context.registerReceiver(deselectAll, new IntentFilter(Config.DESELECT_ALL));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
         // pass the view to View Holder
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final int pos = holder.getAdapterPosition();
        final MyViewHolder temp = holder;
        Glide.with(context).load(imgList.get(pos).getImageData()).into(holder.imageView);

        if(itemStateArray.get(pos)){
            temp.checkBox.setVisibility(View.VISIBLE);
            temp.checkBox.setChecked(true);
        }else{
            temp.checkBox.setVisibility(View.GONE);
            temp.checkBox.setChecked(false);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!itemStateArray.get(pos)) {
                    //if item is not checked then move to view the picture
                    Intent intent = new Intent((Activity) context, ViewActivity.class);
                    intent.putExtra("albName", imgList.get(pos).getImageName());
                    intent.putExtra("pos", pos);
                    intent.putExtra("callsc", checkVal);
                    ((Activity) context).startActivity(intent);
                }else{
                    //do something
                    temp.checkBox.setVisibility(View.GONE);
                    temp.checkBox.setChecked(false);
                    itemStateArray.put(pos, false);
                    hashMap.remove(pos);
                    if(hashMap.size()<1){
                        hideInstrumentCluster();
                        itemStateArray.clear();
                    }
                }
            }
        });

        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!itemStateArray.get(pos)) {
                    temp.checkBox.setVisibility(View.VISIBLE);
                    temp.checkBox.setChecked(true);
                    itemStateArray.put(pos, true);
                    //hashmap edits
                    hashMap.put(pos, true);
                    //show the instrument cluster
                    checkedItems.add(pos);
                    showInstrumentCluster();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    private void showInstrumentCluster(){
        Intent intent = new Intent(Config.ACTIVATE_SECONDARY_TOOLBAR);
        context.sendBroadcast(intent);
    }

    private void hideInstrumentCluster(){
        Intent intent = new Intent(Config.DEACTIVATE_SECONDARY_TOOLBAR);
        context.sendBroadcast(intent);
    }

    private BroadcastReceiver slideShow = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> imgData = new ArrayList<>();
            for(int i = 0;i<itemStateArray.size();i++){
                if(itemStateArray.valueAt(i)){
                    imgData.add(imgList.get(itemStateArray.keyAt(i)).getImageData());
                }
            }
            Intent intent1 = new Intent(context, SlideShowActivity.class);
            intent1.putExtra("image_list", imgData);
            context.startActivity(intent1);
        }
    };
    //this is a broadcast reciever that deletes all the selected items
    private BroadcastReceiver deleteAll = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle(context.getString(R.string.delete));
            alert.setMessage("Are you sure you want to delete "+hashMap.size()+" file(s)?");
            alert.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ArrayList<Image> imgData = new ArrayList<>();
                    for(int ix = 0;ix<itemStateArray.size();ix++){
                        if(itemStateArray.valueAt(ix)){
                            imgData.add(imgList.get(itemStateArray.keyAt(ix)));
                        }
                    }
                    new FileHandler(context).deleteAllFiles(imgData);
                }
            }).setNegativeButton(context.getString(R.string.no),null).show();
        }
    };

    private BroadcastReceiver selectAll = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            for(int i = 0;i<imgList.size();i++){
                itemStateArray.put(i, true);
                hashMap.put(i, true);
            }
            showInstrumentCluster();
        }
    };

    private BroadcastReceiver deselectAll = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            itemStateArray.clear();
            hashMap.clear();
            hideInstrumentCluster();
        }
    };

    //share all images that are selected
    private BroadcastReceiver shareImages = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> imgData = new ArrayList<>();
            for(int i = 0;i<itemStateArray.size();i++){
                if(itemStateArray.valueAt(i)){
                    imgData.add(imgList.get(itemStateArray.keyAt(i)).getImageData());
                }
            }
            new FileHandler(context).shareAllImages(imgData);
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CheckBox checkBox;
        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.blkImg);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            if(checkVal == 2) {
                imageView.setLayoutParams(new ConstraintLayout.LayoutParams(dimensions / 4, dimensions / 4));
            }else{
                imageView.setLayoutParams(new ConstraintLayout.LayoutParams(dimensions / 4, dimensions / 4));
            }
            imageView.setPadding(3,3,3,3);
            checkBox = itemView.findViewById(R.id.blkChkBox);
            checkBox.setVisibility(View.GONE);
            checkBox.setChecked(false);
        }
    }
}