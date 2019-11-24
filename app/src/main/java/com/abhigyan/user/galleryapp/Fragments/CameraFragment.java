package com.abhigyan.user.galleryapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abhigyan.user.galleryapp.Activities.MainActivity;
import com.abhigyan.user.galleryapp.GridAdapters.ImageAdapter;
import com.abhigyan.user.galleryapp.Utility.Config;
import com.abhigyan.user.galleryapp.Utility.Image;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.MemoryAccess;
import com.abhigyan.user.galleryapp.Utility.Permissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class CameraFragment extends Fragment {

    private ArrayList<Image> imgList;
    private RecyclerView gridRV;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.camera_fragment, container, false);
        gridRV = myView.findViewById(R.id.gridMain);
        //register the refresh receiver
        Objects.requireNonNull(getContext()).registerReceiver(refreshReceiver, new IntentFilter(Config.REFRESH_VIEWS));
        Objects.requireNonNull(getContext()).registerReceiver(selectAll, new IntentFilter(Config.SELECT_ALL));
        Objects.requireNonNull(getContext()).registerReceiver(deselectAll, new IntentFilter(Config.DESELECT_ALL));

        return myView;
    }

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show();
            onStart();
        }
    };

    private BroadcastReceiver selectAll = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            gridRV.smoothScrollToPosition(gridRV.getAdapter().getItemCount() - 1);
        }
    };

    private BroadcastReceiver deselectAll = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            gridRV.smoothScrollToPosition(Objects.requireNonNull(gridRV.getAdapter()).getItemCount() - 1);
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Config.REQUEST_PERMISSION: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        getImages();
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.permission_msg), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }*/

    @Override
    public void onStart() {
        super.onStart();

        boolean permission = new Permissions(getContext()).askForPermission();
        if(permission){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            getImages();
            gridRV.setAdapter(new ImageAdapter(Objects.requireNonNull(getContext()),imgList,width,1));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
            gridRV.setLayoutManager(gridLayoutManager);
            Intent intent = new Intent(Config.DEACTIVATE_SECONDARY_TOOLBAR);
            getContext().sendBroadcast(intent);
        }else {
            Toast.makeText(getContext(), getResources().getString(R.string.permission_msg), Toast.LENGTH_LONG).show();
        }
    }

    private void getImages(){
        MemoryAccess memoryAccess = new MemoryAccess(getContext());
        imgList = memoryAccess.getImagesCamera();
        Collections.reverse(imgList);
    }
}
