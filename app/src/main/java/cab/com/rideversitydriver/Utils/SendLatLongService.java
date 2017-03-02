package cab.com.rideversitydriver.Utils;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kalidoss on 08/11/16.
 */
public class SendLatLongService extends Service implements LocationListener {
    private static Timer timer;
    private Context ctx;
    public static boolean isGPSEnabled = false;
    public static boolean isNetworkEnabled = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 2; // 2 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    protected LocationManager locationManager;
    BroadcastReceiver receiver;
    String UserId,Latitude,Longitude;


    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        ctx = this;
        timer = new Timer();
        startService();
    }



    private void startService() {
        //Timer timer = new Timer();
        timer.scheduleAtFixedRate(new mainTask(), 0, 10000);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //Bundle extras = intent.getExtras();
        SharedPref sharedPref = new SharedPref(ctx);
        UserId=sharedPref.getString("userId");
        //UserId=(String) intent.getExtras().get("UserId");
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private class mainTask extends TimerTask {
        public void run() {
            toastHandler.sendEmptyMessage(0);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
       //Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
    }

    private final Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
            latitudeAndLongtitude();
            Log.e("getLatitude()", ""+ getLatitude());

             Latitude = String.valueOf(getLatitude());
             Longitude = String.valueOf(getLongitude());

            CallJson();

        }
    };


    public JSONObject CallJson()
    {
        URL url = null;
        String requestType="POST";
        //String requestURL="http://colanapps.in/rideversity/admin/webservice/driver/setuserlocation";
        String requestURL="http://rideversity.co/admin/webservice/driver/setuserlocation";
        String requestData;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.LATITUDE, Latitude);
            objData.putOpt(Constants.LONGITUDE, Longitude);
            objData.putOpt(Constants.USER_ID, UserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("Success", ""+objData.toString());

        requestData=objData.toString();
        Log.e("requestData", ""+requestData);

        // create the HttpURLConnection
        try {
            url = new URL(requestURL.replace(" ", "%20"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*if (requestType.equalsIgnoreCase(Constants.REQUEST_TYPE_POST)) {
            connection.setDoOutput(true);
        } else {
            connection.setDoOutput(false);
        }*/

        connection.setDoOutput(true);
        try {
            connection.setRequestMethod(requestType);
            connection.setReadTimeout(25 * 1000);
            connection.setRequestProperty(Constants.KEY_CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);
            connection.setRequestProperty(Constants.KEY_ACCEPT, Constants.VALUE_CONTENT_TYPE);

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            connection.connect();

           // OutputStreamWriter streamWriter;
            OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
            streamWriter.write(requestData);
           // streamWriter.close();

            if(streamWriter!=null){
                streamWriter.close();/**HERE*/
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder("");

            String line = null;
            while ((line = reader.readLine()) != null) {
                //Toast.makeText(context, "line " + line, Toast.LENGTH_SHORT).show();
                stringBuilder.append(line + "\n");

            }

            Log.e("stringBuilder",""+stringBuilder.toString());
            if(!TextUtils.isEmpty(stringBuilder.toString()))
            {
                return new JSONObject(stringBuilder.toString());
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // read the output from the server
        return null;
    }

    public void latitudeAndLongtitude() {
        //Log.e("latitudeAndLongtitude", "latitudeAndLongtitude");
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            Toast.makeText(getApplicationContext(), "No provider found!", Toast.LENGTH_SHORT).show();
        } else {
            //GPSTrackers.canGetLocation = true;
            //Log.e("GPSTrackers", "GPSTrackers");
            if (isGPSEnabled)
            {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                    Log.d("Network", "GPS");
                    if (locationManager != null) {
                        Log.d("locationManager", "is not null ");
                        //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        location=getLastKnownLocation();

                        if (location != null) {
                            Log.d("Network", "GPS lat and long gets");
                            //Toast.makeText(getApplicationContext(), "isNetworkEnabled!", Toast.LENGTH_SHORT).show();
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            //Toast.makeText(getApplicationContext(), "gps" + location.getLatitude(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("location", "is getting null");
                        }
                    }
                }
            }else
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                Log.d("Network", "Network");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        //Toast.makeText(getApplicationContext(), "isGPSEnabled!", Toast.LENGTH_SHORT).show();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Toast.makeText(getApplicationContext(), "isNetworkEnabled" + location.getLatitude(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }
    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;

        for (String provider : providers)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return location;
            }
            Location l = locationManager.getLastKnownLocation(provider);
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
