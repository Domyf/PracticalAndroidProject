package com.group18.sustainucd.ui.addBin;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddBinViewModel extends ViewModel {

    private MutableLiveData<Bitmap> imageBitmap;

    public AddBinViewModel() {
        imageBitmap = new MutableLiveData<>();
    }

    public LiveData<Bitmap> getImageBitmap() { return imageBitmap; }

    public void setImageBitmap(Bitmap bitmap) {
        imageBitmap.setValue(bitmap);
    }
}