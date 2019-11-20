package com.group18.sustainucd;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class BinImageHelper {

    private static String filePrefix = "BIN_";
    private static String fileSuffix = ".jpg";

    private static File GetPicturesStorageDir(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public static String GetBinImagePath(Context context, String binFileName) {
        String storagePath = GetPicturesStorageDir(context).getAbsolutePath();
        return storagePath+"/"+binFileName+fileSuffix;
    }

    public static File CreateBinImageFile(Context context) throws IOException {
        return File.createTempFile(filePrefix, fileSuffix, GetPicturesStorageDir(context));
    }

    public static String GetImageName(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }

}
