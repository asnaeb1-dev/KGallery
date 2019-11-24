package com.abhigyan.user.galleryapp.ViewPager;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class FadeOutTrans implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View view, float v) {
        view.setTranslationX(-v*view.getWidth());

        view.setAlpha(1-Math.abs(v));
    }
}
