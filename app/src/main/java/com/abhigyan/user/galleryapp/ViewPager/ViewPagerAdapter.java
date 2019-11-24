package com.abhigyan.user.galleryapp.ViewPager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.abhigyan.user.galleryapp.Utility.Image;
import com.abhigyan.user.galleryapp.R;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context activity;
    private ArrayList<Image> images;

    public ViewPagerAdapter(Context activity, ArrayList<Image> images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            LayoutInflater layoutInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = layoutInflater.inflate(R.layout.view_pager_content, container, false);

            PhotoView imageView = myView.findViewById(R.id.content_img);
            Glide.with(activity)
                    .asDrawable()
                    .load(images.get(position).getImageData())
                    .into(imageView);
            container.addView(myView);
            return myView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}