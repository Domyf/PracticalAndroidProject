package com.group18.sustainucd;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * This class will provide the logic and the decisions about:
 * - Where to store the user pictures
 * - The prefix of every picture file
 * - The suffix of every picture file
 * Every class in the app will use this class when they want to do something with the user picture
 * files. In this way it's more easy and safe to do maintenance.
 */
public class BinImageHelper {

    private static String filePrefix = "BIN_";
    private static String fileSuffix = ".jpg";

    /** Returns the directory that stores the user pictures */
    private static File GetPicturesStorageDir(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    /** Returns the whole path of a picture starting from the file name */
    public static String GetBinImagePath(Context context, String binFileName) {
        String storagePath = GetPicturesStorageDir(context).getAbsolutePath();
        return storagePath+"/"+binFileName+fileSuffix;
    }

    /** Create a new (empty) picture file */
    public static File CreateBinImageFile(Context context) throws IOException {
        return File.createTempFile(filePrefix, fileSuffix, GetPicturesStorageDir(context));
    }

    /** Get the name of the picture file without the file suffix */
    public static String GetImageName(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }

}
