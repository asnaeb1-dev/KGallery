package com.abhigyan.user.galleryapp.Utility;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class FileHandler {

    private Context context;

    public FileHandler(Context context) {
        this.context = context;
    }

    public void shareImage(String filepath){
        File image = new File(filepath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath));
        context.startActivity(Intent.createChooser(share, "Share Image"));
    }

    public boolean deleteImageFile(String FilePath){
        File file = new File(FilePath);
        if(file.exists()) {
            file.delete();
        }

        context.getContentResolver().delete( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.DATA + "=?", new String[]{ FilePath});

        if(!file.exists())
        {
            return true;
        }
        return false;
    }

    public void deleteAllFiles(ArrayList<Image> files){
        for(Image img : files){
            File file = new File(img.getImageData());
            if(file.exists()) {
                file.delete();
            }

            context.getContentResolver().delete( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media.DATA + "=?", new String[]{ img.getImageData()});

            if(!file.exists())
            {
                Log.i("file delete", img.getUniqueName());
                continue;
            }
            Toast.makeText(context, "Could not delete- "+img.getUniqueName(), Toast.LENGTH_SHORT).show();

        }
            Intent intent = new Intent(Config.REFRESH_VIEWS);
            context.sendBroadcast(intent);
            Intent deactivate = new Intent(Config.DEACTIVATE_SECONDARY_TOOLBAR);
            context.sendBroadcast(deactivate);
    }

    public void changeWallpaper(String filepath){
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(context);

        try {
            File f = new File(filepath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            myWallpaperManager.setBitmap(bitmap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void shareAllImages(ArrayList<String>filesToSend){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        intent.setType("image/jpeg"); /* This example is sharing jpeg images. */

        ArrayList<Uri> files = new ArrayList<>();

        for(int i = 0;i<filesToSend.size();i++) {
            File image = new File(filesToSend.get(i));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            files.add(Uri.parse(filesToSend.get(i)));
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        context.startActivity(Intent.createChooser(intent, "Share Image"));
    }

    public boolean deleteVideoFile(String filePath){
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        }

        context.getContentResolver().delete( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Video.Media.DATA + "=?", new String[]{ filePath});

        if(!file.exists())
        {
            return true;
        }
        return false;
    }
}
