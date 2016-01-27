package com.xd.phonedefender.hw.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by hhhhwei on 16/1/24.
 */
public class LocationService extends Service {

    private LocationManager locationManager;
    private MyLocationListener myLocationListener;
    private Criteria criteria;
    private SharedPreferences sp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initViews();
        bindLocation();
    }

    private void initViews() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        myLocationListener = new MyLocationListener();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        criteria = new Criteria();
    }

    private void bindLocation() {
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);

        String bestProvider = locationManager.getBestProvider(criteria, true);

        locationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            sp.edit().putString("loaction", "latitude:" + latitude + ";" + "longitude" + longitude).commit();
            stopSelf();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
