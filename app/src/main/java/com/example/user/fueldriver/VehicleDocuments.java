package com.example.user.fueldriver;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VehicleDocuments extends AppCompatActivity {

    Toolbar toolbar;
    List<String> company;
    Spinner spine;
    TextView rc, insu;
    private static final int GALLERY_REQUEST = 1;
    private static final int GALLERY_REQUEST2 = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_documents);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spine = (Spinner)findViewById(R.id.spinner);
        rc = (TextView)findViewById(R.id.uploadRC);
        insu = (TextView)findViewById(R.id.uploadInsu);
        company = new ArrayList<>();

        company.add("Insurance Company");
        company.add("Chaloo Company");
        company.add("Badiya Company");
        company.add("Jhakkas Company");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, company);
        spine.setAdapter(dataAdapter);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitleTextColor(Color.BLACK);

        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        insu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST2);
            }
        });

    }
}
