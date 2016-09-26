package com.e46bimmerscommunity.utils;

import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;

public class initLocation {
    private static LocationManager locManager;
    private static LocationListener locListener;
    private static boolean isGPSEnabled = false;
	private static Location location = null;
	
	/**
     * Initialize the location manager.
     */
    public static Location initLocationManager(final ActionBarActivity context) {
        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSEnabled) {
            // no network provider is enabled
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");
            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivityForResult(intent, 1);
                }
            });
            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            // Showing Alert Message
            if (!context.isFinishing()) { alertDialog.show(); }
        } else {
            locListener = new LocationListener() {
                // method ini akan dijalankan apabila koordinat GPS berubah
                public void onLocationChanged(Location newLocation) {
                    location = newLocation;
                }
                public void onProviderDisabled(String arg0) { }
                public void onProviderEnabled(String arg0) {
                    location = getLastKnownLocation();
                }
                public void onStatusChanged(String arg0, int arg1, Bundle arg2) { }
            };
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            location = getLastKnownLocation();
            return location;
        }
        return location;
    }
    
    private static Location getLastKnownLocation() {
        List<String> providers = locManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
