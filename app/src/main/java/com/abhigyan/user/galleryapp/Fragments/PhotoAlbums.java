package com.abhigyan.user.galleryapp.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhigyan.user.galleryapp.GridAdapters.PhotoAlbumRVAdapter;
import com.abhigyan.user.galleryapp.Utility.Config;
import com.abhigyan.user.galleryapp.Utility.Image;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.MemoryAccess;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;

public class PhotoAlbums extends Fragment {

    private LinkedHashSet<String> ln = new LinkedHashSet<>();
    private ArrayList<Image> imgList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_photo_albums, container, false);
        recyclerView = myView.findViewById(R.id.photoAlbumRV);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getAlbumNames();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        recyclerView.setAdapter(new PhotoAlbumRVAdapter(getContext(), imgList, width));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Intent intent = new Intent(Config.DEACTIVATE_SECONDARY_TOOLBAR);
        getContext().sendBroadcast(intent);

    }

    private void getAlbumNames(){
        MemoryAccess mem = new MemoryAccess(getContext());
        ln = mem.getAlbums();

        for(String str : ln){
            if(!str.equalsIgnoreCase("Camera")) {
                Cursor ces = Objects.requireNonNull(getContext()).getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{"*"}, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = " + "'" + str + "'", null, null);
                {
                    if (ces != null) {
                        if (ces.moveToLast()) {
                            imgList.add(new Image(str, ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)),
                                    ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
                                    ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)),
                                    ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))));
                        }
                        ces.close();
                    }
                }
            }
        }
    }
}
