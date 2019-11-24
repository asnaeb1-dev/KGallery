package com.abhigyan.user.galleryapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.abhigyan.user.galleryapp.R;

import java.io.File;

public class SecurityActivity extends AppCompatActivity {

    private String directory_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        //createNewDirectory("private_data_dir");
        File secretDir = getDir("private_data_dir", Context.MODE_PRIVATE);
        if(secretDir.canRead()){
            String[] str = secretDir.list();
            for(String st : str){
                Log.i("file path", st);
            }
        }
    }

    private void newDir(){
        createNewDirectory("");
    }

    private void createNewDirectory(String dir_nm){
        File mydir = getApplicationContext().getDir(dir_nm, Context.MODE_PRIVATE); //Creating an internal dir
        if (!mydir.exists())
        {
            boolean success = mydir.mkdirs();
            if(success){
                SharedPreferences sharedpreferences = getSharedPreferences("FOLDER_NAMES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("folder_name", dir_nm);
                editor.apply();
            }else{
                Toast.makeText(this, "Failed to create new directory!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Directory already exists!", Toast.LENGTH_SHORT).show();
        }
    }
}
