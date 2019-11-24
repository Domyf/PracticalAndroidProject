package com.group18.sustainucd.ui.addBin;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddBinViewModel extends ViewModel {

    private MutableLiveData<Bitmap> imageBitmap;
    private MutableLiveData<Drawable> paperDrawable;
    private MutableLiveData<Drawable> batteryDrawable;
    private MutableLiveData<Drawable> foodDrawable;

    public AddBinViewModel() {
        imageBitmap = new MutableLiveData<>();
        paperDrawable = new MutableLiveData<>();
        batteryDrawable = new MutableLiveData<>();
        foodDrawable = new MutableLiveData<>();
    }

    public LiveData<Bitmap> getImageBitmap() { return imageBitmap; }

    public MutableLiveData<Drawable> getPaperDrawable() {
        return paperDrawable;
    }

    public MutableLiveData<Drawable> getBatteryDrawable() {
        return batteryDrawable;
    }

    public MutableLiveData<Drawable> getFoodDrawable() {
        return foodDrawable;
    }
}