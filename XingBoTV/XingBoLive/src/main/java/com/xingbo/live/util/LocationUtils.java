package com.xingbo.live.util;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by xingbo_szd on 2016/8/15.
 */
public class LocationUtils {
    public static void getLocationCity(final Context context) {
        final String city="";
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    //高精度
        criteria.setAltitudeRequired(false);    //不要求海拔
        criteria.setBearingRequired(false);    //不要求方位
        criteria.setCostAllowed(false);    //不允许有话费
        criteria.setPowerRequirement(Criteria.POWER_LOW);    //低功耗

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String currentCity;
                Log.e("onLocationChanged", location.getProvider());
                double latitude = 0;
                double longitude = 0;
                if (location != null) {
                    latitude = location.getLatitude(); // 经度
                    longitude = location.getLongitude(); // 纬度
                }
                String latLongString = "纬度:" + latitude + "\n经度:" + longitude;
                Geocoder geocoder=new Geocoder(context);
                try {
                    List<Address> addList = geocoder.getFromLocation(37.73,112.57,1);
                    if(addList!=null && addList.size()>0){
                        for(int i=0; i<addList.size(); i++){
                            Address ad = addList.get(i);
                            latLongString += "\n";
                            latLongString += ad.getCountryName() + ";" + ad.getLocality();

                        }
                    }
                    currentCity=latLongString;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.e("onStatusChanged", s);
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.e("onProviderEnabled", s);
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.e("onProviderDisabled", s);
            }
        });
    }
}
