package com.group18.sustainucd;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;

public class MainInfoActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.group18.sustainucd.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CardView plasticCardView = findViewById(R.id.pasticCardView);
        plasticCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(EXTRA_MESSAGE, SubInfoActivity.PLASTIC);
                startActivity(intent);
            }
        });
        CardView paperCardView = findViewById(R.id.paperCardView);
        paperCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(EXTRA_MESSAGE, SubInfoActivity.PAPER);
                startActivity(intent);
            }
        });
        CardView foodCardView = findViewById(R.id.foodCardView);
        foodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(EXTRA_MESSAGE, SubInfoActivity.FOOD);
                startActivity(intent);
            }
        });
        CardView glassCardView = findViewById(R.id.glassCardView);
        foodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(EXTRA_MESSAGE, SubInfoActivity.GLASS);
                startActivity(intent);
            }
        });
        CardView batteryCardView = findViewById(R.id.batteryCardView);
        batteryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(EXTRA_MESSAGE, SubInfoActivity.BATTERY);
                startActivity(intent);
            }
        });
        CardView electronicCardView = findViewById(R.id.electronicCardView);
        electronicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(EXTRA_MESSAGE, SubInfoActivity.ELECTRONICS);
                startActivity(intent);
            }
        });
    }

}
