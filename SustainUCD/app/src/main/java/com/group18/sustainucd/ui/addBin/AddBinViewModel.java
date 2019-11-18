package com.group18.sustainucd.ui.addBin;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddBinViewModel extends ViewModel {

    private MutableLiveData<String> locationText;
    private MutableLiveData<Bitmap> imageBitmap;

    public AddBinViewModel() {
        locationText = new MutableLiveData<>();
        imageBitmap = new MutableLiveData<>();
    }

    public LiveData<String> getLocationText() {
        return locationText;
    }

    public LiveData<Bitmap> getImageBitmap() { return imageBitmap; }

    public void setImageBitmap(Bitmap bitmap) {
        imageBitmap.setValue(bitmap);
    }

    public void setLocationText(String text)
    {
        locationText.setValue(text);
    }
}