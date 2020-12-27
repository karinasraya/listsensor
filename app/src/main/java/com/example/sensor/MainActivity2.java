package com.example.sensor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.sensor.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity2";
    private SensorManager sensorManager;
    private Sensor accelerometer, mLight, proximity;
    TextView xValue, yValue, zValue, light, prox, pocket;
    private Button export;
    float xval, zval, yval, proxval, lightval;
    double lati, longi;
    int counter;
    FusedLocationProviderClient mFusedLocation;
    String csv;
    List<String[]> data = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentDate = dateFormat.format(date);
        String currentTime = timeFormat.format(date);
        csv = (getExternalFilesDir(null).getAbsolutePath() + "/Rekap_" + currentDate + "_" + currentTime + ".csv");

        Log.d(TAG, "onCreate: " + csv);

        xValue = (TextView) findViewById(R.id.xValue);
        yValue = (TextView) findViewById(R.id.yValue);
        zValue = (TextView) findViewById(R.id.zValue);

        light = (TextView) findViewById(R.id.light);
        prox = (TextView) findViewById(R.id.proxi);
        pocket = (TextView) findViewById(R.id.pocket);
        pocket.setText("No");
        export = (Button) findViewById(R.id.export);

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CSVWriter writer = null;
                try {
                    writer = new CSVWriter(new FileWriter(csv));
                    writer.writeAll(data);
                    Toast.makeText(MainActivity2.this, "File tersimpan", Toast.LENGTH_LONG).show();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(MainActivity2.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered accelerometer listener");
        } else {
            xValue.setText("Accelerometer Not Supported");
            yValue.setText("Accelerometer Not Supported");
            zValue.setText("Accelerometer Not Supported");
        }

        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximity != null) {
            sensorManager.registerListener(MainActivity2.this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Proximity listener");
        } else {
            prox.setText("Proximity Not Supported");
        }

        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            sensorManager.registerListener(MainActivity2.this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Light listener");
        } else {
            light.setText("Light Not Supported");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "onSensorChanged: X:" + sensorEvent.values[0] + "Y: " + sensorEvent.values[1] + "Z: " + sensorEvent.values[2]);
            xValue.setText("xValue : " + sensorEvent.values[0]);
            yValue.setText("yValue : " + sensorEvent.values[1]);
            zValue.setText("zValue : " + sensorEvent.values[2]);
            xval = sensorEvent.values[0];
            yval = sensorEvent.values[1];
            zval = sensorEvent.values[2];
            int inclination = (int) Math.round(Math.toDegrees(Math.acos(zval)));
            if ((proxval < 1) && (lightval < 2) && (yval < -0.6) && ((inclination > 75) || (inclination < 100))) {
                pocket.setText("Yes");
                mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Do it all with location
                            Log.d(TAG, "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                            lati = location.getLatitude();
                            longi = location.getLongitude();
                        }
                    }
                });
                data.add(new String[]{String.valueOf(xval),String.valueOf(yval),String.valueOf(zval),String.valueOf(lati),String.valueOf(longi)});
                Log.d(TAG, "MASUK GAN");
                counter = counter + 1;
                if (counter==2042){
                    CSVWriter writer = null;
                    try {
                        writer = new CSVWriter(new FileWriter(csv));
                        writer.writeAll(data);
                        Toast.makeText(MainActivity2.this, "File tersimpan", Toast.LENGTH_LONG).show();
                        writer.close();
                        counter=0;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if(sensor.getType() == Sensor.TYPE_LIGHT){
            light.setText("Light : " + sensorEvent.values[0]);
            lightval = sensorEvent.values[0];
        }
        else if (sensor.getType() == Sensor.TYPE_PROXIMITY){
            prox.setText("Proximity : " + sensorEvent.values[0]);
            proxval = sensorEvent.values[0];
        }
    }

}
