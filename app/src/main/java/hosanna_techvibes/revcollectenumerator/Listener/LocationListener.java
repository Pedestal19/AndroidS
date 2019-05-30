package hosanna_techvibes.revcollectenumerator.Listener;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import hosanna_techvibes.revcollectenumerator.model.App;
import hosanna_techvibes.revcollectenumerator.model.CoordinateModel;


/**
 * Created by EbukaProf on 02/10/2016.
 */
public class LocationListener implements android.location.LocationListener {
    App app;
    CoordinateModel coordinateModel = new CoordinateModel();
    private static final String TAG = "LocationListener";
    Location mLastLocation;

    public LocationListener(Context context, String provider) {
        app = ((App)context.getApplicationContext());
        Log.e(TAG, "LocationListener " + provider);
        mLastLocation = new Location(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: " + location);

        Log.e("Latitude: ", String.valueOf(location.getLatitude()));
        Log.e("Longitude: ", String.valueOf(location.getLongitude()));
        app.setLatitude(String.valueOf(location.getLatitude()));
        app.setLongitude(String.valueOf(location.getLongitude()));
        mLastLocation.set(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged: " + provider);
    }
}
