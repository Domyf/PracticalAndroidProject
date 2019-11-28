package com.group18.sustainucd;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;

public class SubInfoActivity extends AppCompatActivity {

    public static final int PAPER = 1;
    public static final int FOOD = 2;
    public static final int PLASTIC = 3;
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
        SetTitleByCategory(category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void SetTitleByCategory(int category) {
        switch (category) {
            case PAPER:
                getSupportActionBar().setTitle(R.string.paper_label);
                break;
            case PLASTIC:
                getSupportActionBar().setTitle(R.string.plastic_label);
                break;
            case FOOD:
                getSupportActionBar().setTitle(R.string.food_label);
                break;
            case GLASS:
                getSupportActionBar().setTitle(R.string.glass_label);
                break;
            case BATTERY:
                getSupportActionBar().setTitle(R.string.battery_label);
                break;
            case ELECTRONICS:
                getSupportActionBar().setTitle(R.string.electronic_label);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //If the button pressed is the back arrow
            //then will happen the same behavior of onBackPressed()
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
