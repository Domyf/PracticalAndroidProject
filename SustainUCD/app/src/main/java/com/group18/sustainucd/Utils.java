package com.group18.sustainucd;

import android.content.Intent;
import android.net.Uri;

import com.group18.sustainucd.Database.Bin;

/**
 * This class will provide some general utilities to all the classes in the app.
 * - Get the intent to show a bin on Google Maps
 * - Calculate distance from two locations
 */
public class Utils {

    /**
     * Returns an intent to show show on google maps the bin passed by parameter with the
     * String label passed by parameter
     */
    public static Intent GetMapIntent(Bin binToShow, String mapsLabel) {
        Uri gmmIntentUri = Uri.parse("geo:"+binToShow.latitude+","+binToShow.longitude
                +"?z=18&q="+binToShow.latitude+","+binToShow.longitude+"("+mapsLabel+")");

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        return mapIntent;
    }

    /**
     *  Returns the distance from two locations. The locations are described by latitude and longitude
     */
    public static double CalculateDistance(double CurrentLatitude, double CurrentLongitude, double binLatitude, double binLongitude){

        // Convert lat and long values from decimal degrees to radians
        double lat1 = Math.toRadians(CurrentLatitude);
        double long1 = Math.toRadians(CurrentLongitude);
        double lat2 = Math.toRadians(binLatitude);
        double long2 = Math.toRadians(binLongitude);

        // Earth's mean radius (km)
        double R = 6371;

        // series of formulas used to calculate distance between points on Earth's surface using lat
        // and long
        double a = Math.sin((lat2 - lat1)/2)*Math.sin((lat2 - lat1)/2) + Math.cos(lat1)*Math.cos(lat2)*Math.sin((long2-long1)/2)*Math.sin((long2-long1));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c; // distance between two bins in meters

        return distance;
    }
}
