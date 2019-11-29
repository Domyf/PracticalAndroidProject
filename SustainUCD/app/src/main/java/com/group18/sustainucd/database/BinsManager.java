package com.group18.sustainucd.database;

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
    private static List<Bin> databaseBins = new ArrayList<>();
    private static BinDao binDao;
    private static boolean initialized;

    /** Method that initalize the bins in RAM and the entire database. It also provide a way
     *  to prepopulate the database */
    public synchronized static void Initialize(final Context context, final BinsDatabaseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (binDao == null)
                    binDao = BinsDatabase.getInstance(context).binDao();
                databaseBins = binDao.getAll();
                if (databaseBins.isEmpty()) {
                    databaseBins.add(new Bin("PRE_BIN_2", 53.3062323, -6.2206444, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_1", 53.3063683, -6.220897, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_3", 53.3060052, -6.2205003, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_4", 53.3055431, -6.2198457, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_5", 53.3054312, -6.2197363, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_6", 53.3048784, -6.2189613, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_7", 53.3059363, -6.2199059, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_8", 53.3055233, -6.2195406, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_9", 53.3039852, -6.2171774, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_10", 53.3043594, -6.2187758, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_11", 53.3039228, -6.2215521, false, true, true, true, true, false, false));
                    databaseBins.add(new Bin("PRE_BIN_12", 53.3046995, -6.2226025, false, true, true, true, true, false, false));
                    for(Bin bin: databaseBins)
                        Log.i(TAG, bin.latitude+" "+bin.longitude);
                    binDao.insertAll(databaseBins);
                }
                Log.i(TAG, "Bins in database: "+databaseBins.size());
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

    /** Insert the new bin in the list of bins before starting a
     *  new AsyncTask to insert the bin into the database.
     */
    public synchronized static void Insert(final Context context, final Bin newBin) {
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

    /** Delete the bin in the list of bins before starting a
     *  new AsyncTask to delete the bin from the database.
     **/
    public synchronized static void Delete(final Context context, final Bin bin) {
        databaseBins.remove(bin);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (binDao == null)
                    binDao = BinsDatabase.getInstance(context).binDao();
                binDao.delete(bin);
                Log.d(TAG, "Bin deleted");
                return null;
            }
        }.execute();
    }

    public static List<Bin> GetAllBins()
    {
        return databaseBins;
    }

    /** This method calculates the distances for each bin inside the database */
    public static void CalculateDistances(double currentLatitude, double currentLongitude) {
        for (Bin bin: databaseBins) {
            bin.distance = Utils.CalculateDistance(currentLatitude, currentLongitude, bin.latitude, bin.longitude);
        }
    }

    /** This method returns an array of the nearest k bins based on the location distance.
     * If k is less than the total number of bins then this method returns all the bins sorted by
     * location distance. */
    public static List<Bin> GetNearestKBins(int k, double currentLatitude, double currentLongitude)
    {
        List<Bin> closestBins = new ArrayList<>();
        List<Integer> binsIndexes = new ArrayList<>();

        if (k > databaseBins.size())
            k = databaseBins.size();

        int index = 0;

        for(int i=0; i<k; i++) {
            double min = Double.POSITIVE_INFINITY;
            for (int j = 0; j < databaseBins.size(); j++) {
                double distance = databaseBins.get(j).distance;
                if (distance <= min && !binsIndexes.contains(j)) {
                    index = j;
                    min = distance;
                }
            }
            closestBins.add(databaseBins.get(index));
            binsIndexes.add(index);
        }

        return closestBins;
    }

    /** This method returns the bins that were added by the user */
    public static List<Bin> GetUserBins() {
        List<Bin> list = new ArrayList<>();
        if (databaseBins == null) {
            return list;
        }
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
