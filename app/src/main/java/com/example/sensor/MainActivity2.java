package com.example.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sensor.R;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity2";
    private SensorManager sensorManager;
    private Sensor accelerometer,mGyro,mMagno,mLight,mPressure,mTemp,mHumi;
    TextView xValue,yValue,zValue,xGyroValue,yGyroValue,zGyroValue,xMagnoValue,yMagnoValue,zMagnoValue,light,pressure,temp,humi;
    private Button tutup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        xValue = (TextView) findViewById(R.id.xValue);
        yValue = (TextView) findViewById(R.id.yValue);
        zValue = (TextView) findViewById(R.id.zValue);


        xGyroValue = (TextView) findViewById(R.id.xGyroValue);
        yGyroValue = (TextView) findViewById(R.id.yGyroValue);
        zGyroValue = (TextView) findViewById(R.id.zGyroValue);
//
        xMagnoValue = (TextView) findViewById(R.id.xMagnoValue);
        yMagnoValue = (TextView) findViewById(R.id.yMagnoValue);
        zMagnoValue = (TextView) findViewById(R.id.zMagnoValue);

        light = (TextView) findViewById(R.id.light);
        pressure = (TextView) findViewById(R.id.pressure);
        temp = (TextView) findViewById(R.id.temp);
        humi = (TextView) findViewById(R.id.humi);
        tutup = (Button)findViewById(R.id.tutup);
        tutup.setOnClickListener(operasi);
                
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
//
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

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tutup:finish();break;
            }
        }
    };

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
    }
}
