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
    private static final String TAG = "BINS_MANAGER";
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

    public static void CalculateDistances(double currentLatitude, double currentLongitude) {
        for (Bin bin: databaseBins) {
            bin.distance = Utils.CalculateDistance(currentLatitude, currentLongitude, bin.latitude, bin.longitude);
            //Log.e(TAG, "from "+currentLatitude+", "+currentLongitude+" to: "+bin.latitude+", "+bin.longitude);
        }
    }

    public synchronized static List<Bin> GetNearestKBins(int k, double currentLatitude, double currentLongitude)
    {
        //TODO calculate and return the nearest k bins from the current location
        //List<Double> binDistances = new ArrayList<Double>();
        List<Bin> closestBins = new ArrayList<>();
        List<Integer> binsIndexes = new ArrayList<>();
        /*for (Bin bin : databaseBins){
            binDistances.add(Utils.CalculateDistance(currentLatitude, currentLongitude, bin));
        }*/

        //double LargestSmallDistance = 0;

        if (k > databaseBins.size())
            k = databaseBins.size();

        double highestMin = 0;
        int index = 0;

        for(int i=0; i<k; i++) {
            double min = 999999;
            for (int j = 0; j < databaseBins.size(); j++) {
                double distance = databaseBins.get(j).distance;
                if (distance <= min && !binsIndexes.contains(j)) {
                    index = j;
                    min = distance;
                }
            }
            closestBins.add(databaseBins.get(index));
            binsIndexes.add(index);
            highestMin = min;
        }

        /*for (int i = 0; i < k; i++){
            double minDistance = 99999;
            for (double distance : binDistances){
                if(distance <= minDistance && distance > LargestSmallDistance)
                    minDistance = distance;
            }
            int index = databaseBins.indexOf(minDistance);
            closestBins.add(databaseBins.get(index));
            LargestSmallDistance = minDistance;
        }*/

        return closestBins;


    }

    public static List<Bin> GetUserBins() {
        List<Bin> list = new ArrayList<>();
        for(Bin bin: databaseBins) {
            if (bin.addedByUser)
                list.add(bin);
        }
        return list;
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
