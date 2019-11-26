package com.group18.sustainucd;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.group18.sustainucd.home.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class SubInfoActivity extends AppCompatActivity {
    public static final int INVALID = 0;
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

        Intent intent = getIntent();

        int category = intent.getIntExtra(MainInfoActivity.EXTRA_MESSAGE, SubInfoActivity.INVALID);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
