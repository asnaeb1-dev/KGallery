package com.abhigyan.user.galleryapp.Utility;

import android.content.Context;
import android.content.Intent;

import com.abhigyan.user.galleryapp.BuildConfig;
import com.abhigyan.user.galleryapp.R;

public class ShareApp {
    private Context context;

    public ShareApp(Context context) {
        this.context = context;
    }

    public void shareThisApp()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        String shareMessage= "\nHey! Try this application\n\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        context.startActivity(Intent.createChooser(shareIntent, "Share KGallery app on"));
    }
}
