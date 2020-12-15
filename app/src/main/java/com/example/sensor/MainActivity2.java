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
    private Sensor accelerometer,mGyro,mMagno,mLight,mPressure,mTemp,mHumi,proximity;
    TextView xValue,yValue,zValue,xGyroValue,yGyroValue,zGyroValue,xMagnoValue,yMagnoValue,zMagnoValue,light,pressure,temp,humi,prox,pocket;
    private Button export;
    float xval,zval,yval,proxval,lightval;
    double lati,longi;
    boolean inpocket;
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

        xGyroValue = (TextView) findViewById(R.id.xGyroValue);
        yGyroValue = (TextView) findViewById(R.id.yGyroValue);
        zGyroValue = (TextView) findViewById(R.id.zGyroValue);
        xMagnoValue = (TextView) findViewById(R.id.xMagnoValue);
        yMagnoValue = (TextView) findViewById(R.id.yMagnoValue);
        zMagnoValue = (TextView) findViewById(R.id.zMagnoValue);

        light = (TextView) findViewById(R.id.light);
        pressure = (TextView) findViewById(R.id.pressure);
        temp = (TextView) findViewById(R.id.temp);
        humi = (TextView) findViewById(R.id.humi);
        prox = (TextView)findViewById(R.id.proxi);
        pocket = (TextView)findViewById(R.id.pocket);
        export = (Button)findViewById(R.id.export);

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
        if(accelerometer != null){
            sensorManager.registerListener(MainActivity2.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered accelerometer listener");
        }
        else{
            xValue.setText("Accelerometer Not Supported");
            yValue.setText("Accelerometer Not Supported");
            zValue.setText("Accelerometer Not Supported");
        }

        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximity != null){
            sensorManager.registerListener(MainActivity2.this,proximity,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Proximity listener");
        }
        else{
            prox.setText("Proximity Not Supported");
        }

        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(mGyro != null){
            sensorManager.registerListener(MainActivity2.this,mGyro,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Gyro listener");
        }
        else{
            xGyroValue.setText("mGyro Not Supported");
            yGyroValue.setText("mGyro Not Supported");
            zGyroValue.setText("mGyro Not Supported");
        }

        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(mMagno != null){
            sensorManager.registerListener(MainActivity2.this,mMagno,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Magno listener");
        }
        else{
            xMagnoValue.setText("Magno Not Supported");
            yMagnoValue.setText("Magno Not Supported");
            zMagnoValue.setText("Magno Not Supported");
        }

        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(mLight != null){
            sensorManager.registerListener(MainActivity2.this,mLight,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Light listener");
        }
        else{
            light.setText("Light Not Supported");
        }

        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(mPressure != null){
            sensorManager.registerListener(MainActivity2.this,mPressure,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Pressure listener");
        }
        else{
            pressure.setText("Pressure Not Supported");
        }

        mTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(mTemp != null){
            sensorManager.registerListener(MainActivity2.this,mTemp,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Temp listener");
        }
        else{
            temp.setText("Temp Not Supported");
        }

        mHumi = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if(mHumi != null){
            sensorManager.registerListener(MainActivity2.this,mHumi,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Humi listener");
        }
        else{
            humi.setText("Humi Not Supported");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "onSensorChanged: X:" + sensorEvent.values[0] + "Y: " + sensorEvent.values[1] + "Z: " + sensorEvent.values[2]);
            xValue.setText("xValue : " + sensorEvent.values[0]);
            yValue.setText("yValue : " + sensorEvent.values[1]);
            zValue.setText("zValue : " + sensorEvent.values[2]);
            xval = sensorEvent.values[0];
            yval = sensorEvent.values[1];
            zval = sensorEvent.values[2];
            int inclination = (int) Math.round(Math.toDegrees(Math.acos(zval)));
            if((proxval<1)&&(lightval<2)&&(yval<-0.6)&&( (inclination>75)||(inclination<100))){
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                LocationListener ll = new lokasiListener();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                data.add(new String[]{String.valueOf(xval),String.valueOf(yval),String.valueOf(zval),String.valueOf(lati),String.valueOf(longi)});
                Log.d(TAG, "MASUK GAN");
            }

        } else if(sensor.getType() == Sensor.TYPE_GYROSCOPE){
            xGyroValue.setText("xGValue : " + sensorEvent.values[0]);
            yGyroValue.setText("yGValue : " + sensorEvent.values[1]);
            zGyroValue.setText("zGValue : " + sensorEvent.values[2]);

        }
        else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            xMagnoValue.setText("xMValue : " + sensorEvent.values[0]);
            yMagnoValue.setText("yMValue : " + sensorEvent.values[1]);
            zMagnoValue.setText("zMValue : " + sensorEvent.values[2]);

        }
        else if(sensor.getType() == Sensor.TYPE_LIGHT){
            light.setText("Light : " + sensorEvent.values[0]);
            lightval = sensorEvent.values[0];

        }
        else if(sensor.getType() == Sensor.TYPE_PRESSURE){
            pressure.setText("Pressure : " + sensorEvent.values[0]);

        }
        else if(sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
            humi.setText("Humidity : " + sensorEvent.values[0]);

        }
        else if(sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            temp.setText("Temperature : " + sensorEvent.values[0]);

        }
        else if (sensor.getType() == Sensor.TYPE_PROXIMITY){
            prox.setText("Proximity : " + sensorEvent.values[0]);
            proxval = sensorEvent.values[0];
            detect();
        }
    }

    public void detect(){
        int inclination = (int) Math.round(Math.toDegrees(Math.acos(zval)));
        if((proxval<1)&&(lightval<2)&&(yval<-0.6)&&( (inclination>75)||(inclination<100))){
            pocket.setText("Yes");
            inpocket = true;
        } else {
            pocket.setText("No");
            inpocket = false;
        }
    }

    private class lokasiListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            lati = location.getLatitude();
            longi = location.getLongitude();
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
    }
}
