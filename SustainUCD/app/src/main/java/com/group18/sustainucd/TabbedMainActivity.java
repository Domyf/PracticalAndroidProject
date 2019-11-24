package com.group18.sustainucd;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;

import com.group18.sustainucd.home.HomeFragment;
import com.group18.sustainucd.ui.tabbed_main.SectionsPagerAdapter;
import com.group18.sustainucd.ui.notifications.NotificationsFragment;

public class TabbedMainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_main);
        //Tabs setup
        //Adapter, ViewPager, TabLayout initialization
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabs = findViewById(R.id.tabs);
        //Add fragment
        sectionsPagerAdapter.addFragment(new HomeFragment(), R.string.tab_text_1);
        sectionsPagerAdapter.addFragment(new NotificationsFragment(), R.string.tab_text_2);
        //Set Adapter and then view pager
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        //Floating button setup
        FloatingActionButton fab = findViewById(R.id.done_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent for phone camera app
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //Check again if the permission has been granted
                //and if the intent will resolve to an activity
                if (Permissions.HasExternalStoragePermission(getApplicationContext()) &&
                        takePhotoIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Snackbar.make(findViewById(R.id.done_fab), "Photo taken successfully!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}