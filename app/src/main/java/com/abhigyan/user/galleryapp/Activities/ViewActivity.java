package com.abhigyan.user.galleryapp.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.abhigyan.user.galleryapp.FavouritesDB;
import com.abhigyan.user.galleryapp.Utility.Image;
import com.abhigyan.user.galleryapp.R;
import com.abhigyan.user.galleryapp.Utility.FileHandler;
import com.abhigyan.user.galleryapp.Utility.MemoryAccess;
import com.abhigyan.user.galleryapp.ViewPager.CubeInTransformer;
import com.abhigyan.user.galleryapp.ViewPager.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ViewActivity extends AppCompatActivity {

    private String albumName = null;
    private ViewPager viewPager;

    private ImageView   imgLove,
                        imgShare,
                        imgMore,
                        imgDelete,
                        imgLock;

    private ArrayList<Image> imgList = new ArrayList<>();

    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        viewPager = findViewById(R.id.viewPager);
        imgMore = findViewById(R.id.more);
        imgLove = findViewById(R.id.love);
        imgShare = findViewById(R.id.share);
        imgDelete = findViewById(R.id.delete);
        imgLock = findViewById(R.id.lock);

        albumName = getIntent().getStringExtra("albName");
        position = getIntent().getIntExtra("pos", 0);
        int check = getIntent().getIntExtra("callsc", 0);
        if(check == 3){
            getImages();
        }
    }

    private void getImages(){
        FavouritesDB favouritesDB = new FavouritesDB(getApplicationContext());
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

    @Override
    protected void onStart() {
        super.onStart();
        accessMemoryForImagesCarousal();
        activateUI();

        final HashMap<String, String> hm  = new MemoryAccess(getApplicationContext()).populateList();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if(hm.containsValue(imgList.get(viewPager.getCurrentItem()).getUniqueName())){
                    imgLove.setImageDrawable(getResources().getDrawable(R.drawable.favourites_select));
                }else{
                    imgLove.setImageDrawable(getResources().getDrawable(R.drawable.favorite_deselect));
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void accessMemoryForImagesCarousal() {
        MemoryAccess memoryAccess = new MemoryAccess(this);
        imgList = memoryAccess.getImages(albumName);
        Collections.reverse(imgList);
        viewPager.setAdapter(new ViewPagerAdapter(this, imgList));
        viewPager.setPageTransformer(true, new CubeInTransformer());
        viewPager.setCurrentItem(position, true);
    }

    private void activateUI(){
        imgLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouritesDB favouritesDB = new FavouritesDB(getApplicationContext());
                boolean result = favouritesDB.insertData(
                                        imgList.get(viewPager.getCurrentItem()).getImageData(),
                                        imgList.get(viewPager.getCurrentItem()).getImageName(),
                                        imgList.get(viewPager.getCurrentItem()).getImageSize(),
                                        imgList.get(viewPager.getCurrentItem()).getUniqueName(),
                                        String.valueOf(imgList.get(viewPager.getCurrentItem()).getImageDate())
                                                        );
                if(result){
                    Toast.makeText(ViewActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    imgLove.animate().rotationBy(360f).setDuration(1000);
                    imgLove.setImageDrawable(getResources().getDrawable(R.drawable.favourites_select));

                }else{
                    Toast.makeText(ViewActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    imgLove.setImageDrawable(getResources().getDrawable(R.drawable.favorite_deselect));
                }
            }
        });

        imgLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewPager.getCurrentItem();
                new FileHandler(getApplicationContext()).shareImage(imgList.get(position).getImageData());
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = viewPager.getCurrentItem();
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewActivity.this);
                alert.setTitle(getResources().getString(R.string.delete));
                alert.setMessage(getResources().getString(R.string.delete_msg));
                alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean b = new FileHandler(getApplicationContext()).deleteImageFile(imgList.get(position).getImageData());
                        if(b){
                            imgList.remove(position);
                            ViewPagerAdapter vp = new ViewPagerAdapter(getApplicationContext(), imgList);
                            viewPager.setAdapter(vp);
                            viewPager.setCurrentItem(position);
                            Toast.makeText(ViewActivity.this, "File has been removed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(ViewActivity.this, "Failed to remove file", Toast.LENGTH_SHORT).show();

                    }
                }).setNegativeButton(getResources().getString(R.string.no),null).show();
            }
        });

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(getApplicationContext(),imgMore);
                //inflating menu from dailog xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.setAs:

                                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewActivity.this);
                                    alert.setTitle(getResources().getString(R.string.change_wall_title))
                                            .setMessage(getResources().getString(R.string.change_wall_message))
                                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                   new FileHandler(getApplicationContext()).changeWallpaper(imgList.get(viewPager.getCurrentItem()).getImageData());
                                                }
                                            })
                                            .setNegativeButton(getResources().getString(R.string.no), null)
                                            .show();

                                popup.dismiss();
                                break;

                            case R.id.details:

                                popup.dismiss();
                                break;

                                default:
                                    break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
