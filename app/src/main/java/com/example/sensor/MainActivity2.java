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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
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

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity2";
    private ApiInterface mApiInterface;
    private SensorManager sensorManager;
    private Sensor accelerometer, mLight, proximity;
    TextView xValue, yValue, zValue, light, prox, pocket;
    private Button export;
    float xval, zval, yval, proxval, lightval;
    double lati, longi;
    int counter, nomorfile;
    File file, file2;
    FusedLocationProviderClient mFusedLocation;
    String csv, csv2;
    List<String[]> data = new ArrayList<String[]>();
    List<String[]> header = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        nomorfile = 1;
        counter = 1;
        header.add(new String[]{"accelerometer_X","accelerometer_Y","accelerometer_Z","Latitude","Longitude"});
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
                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                    String currentDate = dateFormat.format(date);
                    String currentTime = timeFormat.format(date);
                    csv = (getExternalFilesDir(null).getAbsolutePath() + "/Rekap" + ".csv");
                    writer = new CSVWriter(new FileWriter(csv,false));
                    writer.writeAll(header);
                    writer.writeAll(data);
                    writer.close();
                    file = new File(csv);
                    Uri myUri = Uri.parse(csv);
                    csv2 = (getExternalFilesDir(null).getAbsolutePath() + "/Final_Rekap" + ".csv");
                    file2 = new File(csv2);
                    InputStream in = new FileInputStream(file);
                    try {
                        OutputStream out = new FileOutputStream(file2);
                        try {
                            // Transfer bytes from in to out
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                        } finally {
                            out.close();
                        }
                    } finally {
                        in.close();
                    }
                    RequestBody requestFile = RequestBody.create(MediaType.parse("text/csv"),file2);
                    MultipartBody.Part finalfile = MultipartBody.Part.createFormData("file",file2.getName(),requestFile);
                    String deviceString = Build.DEVICE;
                    RequestBody device = RequestBody.create(MultipartBody.FORM, deviceString);
                    Log.d(TAG, "file: " + file2);
                    Log.d(TAG, "final file: " + finalfile);
                    Log.d(TAG, "device string: " + deviceString);
                    Log.d(TAG, "device: " + device);
                    Log.d(TAG, "mapi: " + mApiInterface);
                    Call<PostPutDel> postData = mApiInterface.prosesFile(finalfile, device);
                    postData.enqueue(new Callback<PostPutDel>() {
                        @Override
                        public void onResponse(Call<PostPutDel> call, Response<PostPutDel> response) {
                            if (response.isSuccessful()) {
                                PostPutDel storeResult = response.body();
                            } else {
                                try {
                                    String responseBodyString = response.errorBody().string();
                                    Log.d(TAG, "response: " + responseBodyString);
                                    JSONObject jsonObject = new JSONObject(responseBodyString);
                                    Toast.makeText(MainActivity2.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Log.d(TAG, "Error Body JSON: " + e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<PostPutDel> call, Throwable t) {
//                            Toast.makeText(MainActivity2.this, "Terjadi kesalahan", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Error Retrofit Store: " + t.getMessage());
                        }
                    });
                    Toast.makeText(MainActivity2.this, "File tersimpan", Toast.LENGTH_LONG).show();
                    writer.close();
                    nomorfile = nomorfile+1;
                    data.clear();
                    file = null;
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
                data.add(new String[]{String.valueOf(xval),String.valueOf(yval),String.valueOf(zval),String.valueOf(lati),String.valueOf(longi)});
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
                Log.d(TAG, "MASUK GAN");
                Log.d(TAG, String.valueOf(counter));
                Log.d(TAG, String.valueOf(nomorfile));

                if (counter==999){
                    Log.d(TAG, "Buat file tanpa stop");
                    CSVWriter writer = null;
                    try {
                        Date date = Calendar.getInstance().getTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        String currentDate = dateFormat.format(date);
                        String currentTime = timeFormat.format(date);
                        csv = (getExternalFilesDir(null).getAbsolutePath() + "/Rekap" + ".csv");
                        writer = new CSVWriter(new FileWriter(csv, false));
                        writer.writeAll(header);
                        writer.writeAll(data);
                        writer.close();
                        file = new File(csv);
                        Uri myUri = Uri.parse(csv);
                        csv2 = (getExternalFilesDir(null).getAbsolutePath() + "/Final_Rekap" + ".csv");
                        file2 = new File(csv2);
                        InputStream in = new FileInputStream(file);
                        try {
                            OutputStream out = new FileOutputStream(file2);
                            try {
                                // Transfer bytes from in to out
                                byte[] buf = new byte[1024];
                                int len;
                                while ((len = in.read(buf)) > 0) {
                                    out.write(buf, 0, len);
                                }
                            } finally {
                                out.close();
                            }
                        } finally {
                            in.close();
                        }
                        RequestBody requestFile = RequestBody.create(MediaType.parse("text/csv"),file2);
                        MultipartBody.Part finalfile = MultipartBody.Part.createFormData("file",file2.getName(),requestFile);
                        String deviceString = Build.DEVICE;
                        RequestBody device = RequestBody.create(MultipartBody.FORM, deviceString);
                        Log.d(TAG, "file: " + file2);
                        Log.d(TAG, "final file: " + finalfile);
                        Log.d(TAG, "device string: " + deviceString);
                        Log.d(TAG, "device: " + device);
                        Log.d(TAG, "mapi: " + mApiInterface);
                        Call<PostPutDel> postData = mApiInterface.prosesFile(finalfile, device);
                        postData.enqueue(new Callback<PostPutDel>() {
                            @Override
                            public void onResponse(Call<PostPutDel> call, Response<PostPutDel> response) {
                                if (response.isSuccessful()) {
                                    PostPutDel storeResult = response.body();
                                } else {
                                    try {
                                        String responseBodyString = response.errorBody().string();
                                        Log.d(TAG, "response: " + responseBodyString);
                                        JSONObject jsonObject = new JSONObject(responseBodyString);
                                        Toast.makeText(MainActivity2.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Log.d(TAG, "Error Body JSON: " + e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<PostPutDel> call, Throwable t) {
//                                Toast.makeText(MainActivity2.this, "Terjadi kesalahan", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Error Retrofit Store: " + t.getMessage());
                            }
                        });
                        Toast.makeText(MainActivity2.this, "File tersimpan", Toast.LENGTH_LONG).show();
                        counter=0;
                        Log.d(TAG, String.valueOf(nomorfile));
                        nomorfile = nomorfile+1;
                        data.clear();
                        file = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                counter = counter + 1;
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