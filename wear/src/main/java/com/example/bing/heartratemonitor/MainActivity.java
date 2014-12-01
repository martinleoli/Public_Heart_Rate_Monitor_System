package com.example.bing.heartratemonitor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import java.io.IOException;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private final String TAG = MainActivity.class.getName();
    private TextView mTextView;
    private TextView mGPS;

    private double longitude = 0;
    private double latitude = 0;
    private int hr;

    //private TCPConnection tcp;

    private SensorManager sm;
    private Sensor heartrate;

    private GoogleApiClient googleClient;

    final SensorEventListener heartRateMonitorListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent event){
            Log.d(TAG,"Heart Rate Changed: " + event.values[0]);
            hr = (int)event.values[0];
            if(mTextView!=null){
                mTextView.setText(String.valueOf(event.values[0]));
                mTextView.setText("HR: "+String.valueOf(event.values[0])+" bpm");
                mTextView.getRootView().setBackgroundColor(HeartRate.getHeartRateColor(event.values[0]));

            }

            //TCP
           /* if(tcp!=null){
                try {
                    tcp.send(latitude,longitude,hr);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG,"Can not send data");
                }
            }*/


        }
        @Override
        public void onAccuracyChanged(Sensor sensor,int accuracy){}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mGPS = (TextView) findViewById(R.id.GPS);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        heartrate = sm.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sm.registerListener(heartRateMonitorListener,heartrate,SensorManager.SENSOR_DELAY_NORMAL);



        googleClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        //TCP
       /* try {
            tcp = new TCPConnection();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"Can not create TCP connection");
        }*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleClient.connect();
    }

    @Override
    protected void onStop() {

        if (googleClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleClient, this);
            googleClient.disconnect();
        }
        super.onStop();
    }



    //GPS part

    @Override
    public void onConnected(Bundle connectionHint) {

        // Create the LocationRequest object
        LocationRequest locationRequest = LocationRequest.create();
        // Use high accuracy
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 2 seconds
        locationRequest.setInterval(2);
        // Set the fastest update interval to 2 seconds
        locationRequest.setFastestInterval(2);
        // Set the minimum displacement
        locationRequest.setSmallestDisplacement(2);

        // Register listener using the LocationRequest object
        LocationServices.FusedLocationApi.requestLocationUpdates(googleClient, locationRequest,  this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mGPS.setText("Latitude:  " + String.valueOf( location.getLatitude()) +
                "\nLongitude:  " + String.valueOf( location.getLongitude()));
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
