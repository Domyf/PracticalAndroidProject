package com.group18.sustainucd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinImageHelper {

    private static String filePrefix = "BIN_";
    private static String fileSuffix = ".jpg";

    private static File GetPicturesStorageDir(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public static String getBinImagePath(Context context, String binFileName) {
        String storagePath = GetPicturesStorageDir(context).getAbsolutePath();
        return storagePath+"/"+binFileName+fileSuffix;
    }

    public static File createBinImageFile(Context context) throws IOException {
        return File.createTempFile(filePrefix, fileSuffix, GetPicturesStorageDir(context));
    }

}
