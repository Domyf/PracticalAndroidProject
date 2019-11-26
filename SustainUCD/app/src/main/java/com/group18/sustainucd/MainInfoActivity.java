package com.group18.sustainucd;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;

public class MainInfoActivity extends AppCompatActivity {

    public static final String WHAT_TO_SHOW = "What";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SetCards();

    }

    private void SetCards() {
        CardView plasticCardView = findViewById(R.id.pasticCardView);
        plasticCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.PLASTIC);
                startActivity(intent);
            }
        });
        CardView paperCardView = findViewById(R.id.paperCardView);
        paperCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.PAPER);
                startActivity(intent);
            }
        });
        CardView foodCardView = findViewById(R.id.foodCardView);
        foodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.FOOD);
                startActivity(intent);
            }
        });
        CardView glassCardView = findViewById(R.id.glassCardView);
        glassCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.GLASS);
                startActivity(intent);
            }
        });
        CardView batteryCardView = findViewById(R.id.batteryCardView);
        batteryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.BATTERY);
                startActivity(intent);
            }
        });
        CardView electronicCardView = findViewById(R.id.electronicCardView);
        electronicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInfoActivity.this, SubInfoActivity.class);
                intent.putExtra(WHAT_TO_SHOW, SubInfoActivity.ELECTRONICS);
                startActivity(intent);
            }
        });
    }

}
