package com.group18.sustainucd;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.group18.sustainucd.database.Bin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
    public static String GetUserBinImagePath(Context context, String binFileName) {
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

    /**
     * ASyncTask that will load a picture in background from a file.
     * It receive an ImageView, the path of the file and a Bin.
     * After loading the picture will set the bin bitmap and the imageview.
     * It take care to load from assets folder if the bin is a premade bin.
     */
    public static class LoadPictureTask extends AsyncTask<Void, Void, Bitmap> {
        private ImageView imageView;
        private Context context;
        private Bin bin;

        public LoadPictureTask(Bin bin, ImageView imageView, Context context) {
            this.imageView = imageView;
            this.bin = bin;
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            if (bin.addedByUser) {
                String path = BinImageHelper.GetUserBinImagePath(context, bin.pictureFileName);
                return BitmapFactory.decodeFile(path);
            }

            AssetManager assetManager = context.getAssets();
            InputStream istr;
            Bitmap bitmap = null;
            try {
                istr = assetManager.open(bin.pictureFileName+fileSuffix);
                bitmap = BitmapFactory.decodeStream(istr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            bin.bitmap = bitmap;
        }
    }
}
