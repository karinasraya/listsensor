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
    private Button ceksensor, listsensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listsensor = (Button)findViewById(R.id.SensorAndroid);
        ceksensor = (Button)findViewById(R.id.ceksensor);
        listsensor.setOnClickListener(operasi);
        ceksensor.setOnClickListener(operasi);
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ceksensor:pindah();break;
                case R.id.SensorAndroid:pindah2();break;
            }
        }
    };

    private void pindah() {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }

    private void pindah2() {
        Intent intent = new Intent(MainActivity.this, MainActivity3.class);
        startActivity(intent);
    }
}
