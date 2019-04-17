package com.kubatov.fileapptest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CAMERA = 01;
    final static int REQUEST_VIDEO = 02;

    RecyclerView recyclerView;
    Button cameraButton;
    public File file;
    ArrayList<File> list;
    FileAdapter adapter;
    int count = 0;
    String videoPathUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        cameraButton = findViewById(R.id.buttonCamera);
        getFiles();
    }

    public void getFiles() {
        File folder = new File(Environment.getExternalStorageDirectory()
                + File.separator + "DCIM/Collage");

        if (folder.exists()) {
            list = new ArrayList<>(Arrays.asList(folder.listFiles()));
            initList();
            File[] files = folder.listFiles();
            for (File file : files) {
                Log.e("TAG", "file" + file.getAbsolutePath());

        }
        }
    }

    private void initList() {
        adapter = new FileAdapter(list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy HH:mm:ss", Locale.getDefault());

    public void onCamera(View view) {
        File folder = new File(Environment.getExternalStorageDirectory(), "myFolder");
        if (folder.exists()) {
            folder.mkdir();
            file = new File(folder, count++ + ".IMG" + sdf.format(date) + ".jpg");
            Uri uri = FileProvider.getUriForFile(this, "com.kubatov.fileapptest.provider", file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {
            list.add(0, file);
            adapter.notifyDataSetChanged();
        }

    }
}
