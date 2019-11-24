package com.abhigyan.user.galleryapp.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abhigyan.user.galleryapp.R;

import java.util.Objects;

public class AboutFragment extends DialogFragment {

    private ImageView github, gmail;
    public static String TAG = "FullScreenDialog";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View myView = inflater.inflate(R.layout.about_ui, container, false);
        github = myView.findViewById(R.id.githubIcon);
        gmail = myView.findViewById(R.id.gmailIcon);
        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/asnaeb1"));
                Objects.requireNonNull(getContext()).startActivity(githubIntent);
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"abhigyanraha76@gmail.com"});
                emailIntent.setType("plain/text");
                Objects.requireNonNull(getContext()).startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            }
        });
    }
}
