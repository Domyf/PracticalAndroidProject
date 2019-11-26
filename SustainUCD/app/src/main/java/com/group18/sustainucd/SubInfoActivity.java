package com.group18.sustainucd;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

public class SubInfoActivity extends AppCompatActivity {

    public static final int PAPER = 1;
    public static final int PLASTIC = 2;
    public static final int FOOD = 3;
    public static final int GLASS = 4;
    public static final int BATTERY = 5;
    public static final int ELECTRONICS = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        int category = (Integer) extras.get(MainInfoActivity.WHAT_TO_SHOW);
        Log.d("SubInfoActivity", category+"");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
