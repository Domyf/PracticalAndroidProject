package com.group18.sustainucd.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * This class will take track in RAM of all the bins while
 * updating the local database with AsyncTasks.
 * In this way the access to data is faster.
 */
public class BinsManager {
    private static final String TAG = "DATABASE_MANAGER";
    private static List<Bin> databaseBins;
    private static BinDao binDao;

    public static void Initialize(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (binDao == null)
                    binDao = BinsDatabase.getInstance(context).binDao();
                databaseBins = binDao.getAll();
                Log.d(TAG, "Bins in database: "+databaseBins.size());
                return null;
            }
        }.execute();
    }

    /** Insert the new bin in the list of bins before starting
     *  a new AsyncTask to insert the bin into the database.
     *
     * @param context
     * @param newBin Bin that should be added
     */
    public static void Insert(final Context context, final Bin newBin) {
        databaseBins.add(newBin);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (binDao == null)
                    binDao = BinsDatabase.getInstance(context).binDao();
                binDao.insertBin(newBin);
                Log.d(TAG, "Bin added");
                return null;
            }
        }.execute();
    }

    public static List<Bin> GetAllBins()
    {
        return databaseBins;
    }

    public static int GetBinsQuantity()
    {
        if (databaseBins == null)
            return -1;
        return databaseBins.size();
    }
}
