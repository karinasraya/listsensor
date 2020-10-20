package com.example.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button ceksensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> allSensor = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(new MySensorsAdapter(this, R.layout.row_item, allSensor));
        ceksensor = (Button)findViewById(R.id.ceksensor);
        ceksensor.setOnClickListener(operasi);
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ceksensor:pindah();break;
            }
        }
    };

    private void pindah() {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }
}
