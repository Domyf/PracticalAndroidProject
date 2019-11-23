package com.group18.sustainucd.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.group18.sustainucd.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will take track in RAM of all the bins while
 * updating the local database with AsyncTasks.
 * In this way the access to data is faster.
 */
public class BinsManager {
    private static final String TAG = "DATABASE_MANAGER";
    private static List<Bin> databaseBins = null;
    private static BinDao binDao;
    private static boolean initialized;

    public static void Initialize(final Context context, final BinsDatabaseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (binDao == null)
                    binDao = BinsDatabase.getInstance(context).binDao();
                databaseBins = binDao.getAll();
                Log.d(TAG, "Bins in database: "+databaseBins.size());
                initialized = true;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                listener.OnBinsDatabaseLoaded();
            }
        }.execute();
    }

    public static boolean HasBeenInitialized() {
        return initialized;
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

    public synchronized static List<Bin> GetNearestKBins(int k, double currentLatitude, double currentLongitude)
    {
        //TODO calculate and return the nearest k bins from the current location
        List<Double> BinDistances = new ArrayList<Double>();
        List<Bin> ClosestBins = new ArrayList<Bin>();

        for (Bin bin : databaseBins){
            BinDistances.add(Utils.CalculateDistance(currentLatitude, currentLongitude, bin));
        }


        double LargestSmallDistance = 0;
        if (k > databaseBins.size())
            k = databaseBins.size();

        for (int i = 0; i < k; i++){
            double MinDistance = 99999;
            for (double distance : BinDistances){
                if(distance <= MinDistance && distance > LargestSmallDistance)
                    MinDistance = distance;
            }
            int index = databaseBins.indexOf(MinDistance);
            ClosestBins.add(databaseBins.get(index));
            LargestSmallDistance = MinDistance;
    }

        return ClosestBins;


    }
    
    public static int GetBinsQuantity()
    {
        if (databaseBins == null)
            return -1;
        return databaseBins.size();
    }

    public interface BinsDatabaseListener {
        void OnBinsDatabaseLoaded();
    }
}
