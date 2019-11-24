package com.abhigyan.user.galleryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavouritesDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION = 1;
    public static  final String TABLE_NAME = "MY_FAVORITES";
    private static final String COL_0 = "ID";
    private static final String COL_1 = "PATH";
    private static final String COL_2 = "ALBUM_NAME";
    private static final String COL_3 = "SIZE";
    private static final String COL_4 = "UNIQUE_NAME";
    private static final String COL_5 = "DATE_MOD";


    public FavouritesDB(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME
                +" ("+COL_0+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COL_1+" TEXT,"
                +COL_2+" TEXT,"
                +COL_3+" TEXT,"
                +COL_4+" TEXT,"
                +COL_5+" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String path_name, String album_name, String size, String unique_name, String date_mod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, path_name);
        cv.put(COL_2, album_name);
        cv.put(COL_3, size);
        cv.put(COL_4, unique_name);
        cv.put(COL_5, date_mod);
        long result = db.insert(TABLE_NAME,null, cv);

        if(result == -1) {
            return  false;
        } else {
            Log.i("DONE**********", "done");
            return  true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
    }

    public Boolean deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"NAME = ?", new String[]{ name });
        return true;
    }

    public boolean deleteThisDatabase(Context context,String databasename) {
        return context.deleteDatabase(databasename);
    }
}
