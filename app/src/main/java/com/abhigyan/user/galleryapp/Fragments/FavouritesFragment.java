package com.abhigyan.user.galleryapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhigyan.user.galleryapp.FavouritesDB;
import com.abhigyan.user.galleryapp.GridAdapters.ImageAdapter;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.Config;
import com.abhigyan.user.galleryapp.Utility.Image;
import com.abhigyan.user.galleryapp.Utility.MemoryAccess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class FavouritesFragment extends Fragment {

    private ArrayList<Image> imgList = new ArrayList<>();
    private RecyclerView recyclerView;
    private int called = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerView = myView.findViewById(R.id.favsRV);
        Objects.requireNonNull(getContext()).registerReceiver(refreshReceiver, new IntentFilter(Config.REFRESH_VIEWS));

        return myView;
    }

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onStart();
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if(called == 0) {
            getImages();
            called = 1;
        }
        recyclerView.setAdapter(new ImageAdapter(Objects.requireNonNull(getContext()),imgList,width,3));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        Intent intent = new Intent(Config.DEACTIVATE_SECONDARY_TOOLBAR);
        getContext().sendBroadcast(intent);

    }

    private void getImages(){
        FavouritesDB favouritesDB = new FavouritesDB(getContext());
        Cursor ces = favouritesDB.getAllData();
        if(ces!=null){
            if(ces.moveToFirst()){
                do{
                    imgList.add(new Image(ces.getString(2),
                                            ces.getString(3),
                                            ces.getString(1),
                                            "",
                                            ces.getString(4)
                                            ));
                }while(ces.moveToNext());
            }
        }
    }
}
