package com.abhigyan.user.galleryapp.Utility;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.abhigyan.user.galleryapp.FavouritesDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class MemoryAccess {

    private Context context;
    private String str= " 'Camera' ";

    private LinkedHashSet<String> datesList = new LinkedHashSet<>(),
                                  albumList = new LinkedHashSet<>(),
                                  videoBucketList = new LinkedHashSet<>();

    private ArrayList<Image> imgList = new ArrayList<>();ArrayList<Video> videoList = new ArrayList<>();
    private String[] dateProjection = {MediaStore.Images.Media.DATE_ADDED},
                     infoProjection = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_MODIFIED,
                                        MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME},
                     albumNameProjection = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME},

                    videoProjection = {"*"};

    public MemoryAccess(Context context) {
        this.context = context;
    }

    public ArrayList<Image> getImagesCamera() {
            Cursor ces = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    infoProjection,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = " + str,
                    null, null);
            {
                if(ces!=null){
                    if(ces.moveToFirst()){
                        do{
                            imgList.add(new Image(ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)),
                                    ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)),
                                    ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
                                    ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)),
                                    ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))));
                        }while (ces.moveToNext());
                    }
                    ces.close();
            }
        }

        return imgList;
    }

    public LinkedHashSet<String> getAlbums(){
        Cursor ces = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,albumNameProjection,null, null, null );{
            if(ces!=null){
                if(ces.moveToFirst()){
                    do{
                        albumList.add(ces.getString(ces.getColumnIndexOrThrow(albumNameProjection[0])));
                    }while(ces.moveToNext());
                }
            }
        }
        return albumList;
    }

    public ArrayList<Image> getImages(String str){
        imgList.clear();
        Cursor ces = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                        infoProjection,
                                                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = " +"'" +str+"' ",
                                                        null, null);{
                 if(ces!=null){
                     if(ces.moveToFirst()){
                         do{
                             imgList.add(new Image(ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)),
                                     ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)),
                                     ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
                                     ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)),
                                     ces.getString(ces.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))));

                         }while(ces.moveToNext());
                     }
                 }
        }
        return imgList;
    }

    public HashMap<String, String> populateList(){
        HashMap<String, String> hm = new HashMap<>();
        FavouritesDB favouritesDB = new FavouritesDB(context);
        Cursor ces = favouritesDB.getAllData();
        if(ces!=null){
            if(ces.moveToFirst()){
                do{
                    hm.put("", ces.getString(4));
                }while(ces.moveToNext());
            }
        }
        return hm;
    }

    public ArrayList<Video> getVideos(String bucketName){
        Cursor ces = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoProjection,MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " = " +"'" +bucketName+"' ", null,null );{
            if(ces!=null){
                if(ces.moveToFirst()){
                    do{
                        String videoName = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                        String videoSize = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                        String videoResolution = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                        String videoData = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        String videoID = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        String videoDate = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
                        String videoUniqueName = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                        videoList.add(new Video(videoName,videoID, videoData,videoSize,videoDate, videoResolution, videoUniqueName));
                    }while (ces.moveToNext());
                }
                ces.close();
            }
        }
        return videoList;
    }

    public LinkedHashSet<String> getBucketNames(){
        Cursor ces = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoProjection,null, null,null );{
            if(ces!=null){
                if(ces.moveToFirst()){
                    do{
                        String videoName = ces.getString(ces.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                        videoBucketList.add(videoName);
                    }while (ces.moveToNext());
                }
                ces.close();
            }
        }
        return videoBucketList;
    }
}
