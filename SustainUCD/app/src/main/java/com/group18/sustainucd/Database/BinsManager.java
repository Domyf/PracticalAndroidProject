package com.group18.sustainucd.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

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
                listener.OnBinsDatabaseLoaded();
                initialized = true;
                return null;
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

    private static double CalculateDistance(double CurrentLatitude, double CurrentLongitude, Bin bin2){

        // Convert lat and long values from decimal degrees to radians
        double lat1 = Math.toRadians(CurrentLatitude);
        double long1 = Math.toRadians(CurrentLongitude);
        double lat2 = Math.toRadians(bin2.latitude);
        double long2 = Math.toRadians(bin2.longitude);

        // Earth's mean radius (km)
        double R = 6371;

        // series of formulas used to calculate distance between points on Earth's surface using lat
        // and long
        double a = Math.sin((lat2 - lat1)/2)*Math.sin((lat2 - lat1)/2) + Math.cos(lat1)*Math.cos(lat2)*Math.sin((long2-long1)/2)*Math.sin((long2-long1));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c; // distance between two bins in meters

        return distance;

    }

    public synchronized static List<Bin> GetNearestKBins(int k, double CurrentLatitude, double CurrentLongitude)
    {
        //TODO calculate and return the nearest k bins from the current location
        List<Double> BinDistances = new ArrayList<Double>();
        List<Bin> ClosestBins = new ArrayList<Bin>();

        for (Bin bin : databaseBins){
            BinDistances.add(CalculateDistance(CurrentLatitude, CurrentLongitude, bin));
        }


        double LargestSmallDistance = 0;

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
