package com.group18.sustainucd;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MaterialInfoActivity extends AppCompatActivity {

    public static final String DATA = "Data";
    private RecycleData dataToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dataToShow = (RecycleData) getIntent().getExtras().get(MaterialInfoActivity.DATA);
        setTitle(dataToShow.getNameID());
        ((TextView)findViewById(R.id.materialInfoText)).setText(dataToShow.getDescriptionID());
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
