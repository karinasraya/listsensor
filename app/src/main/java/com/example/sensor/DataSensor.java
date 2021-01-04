package com.example.sensor;

import com.google.gson.annotations.SerializedName;
import com.opencsv.CSVWriter;

import java.io.File;

public class DataSensor {

    @SerializedName("id")
    private int id;
    @SerializedName("file")
    private CSVWriter writer;
    @SerializedName("keterangan")
    private String keterangan;

    public DataSensor() { }

    public DataSensor(int id) {
        this.id = id;
    }

    public DataSensor(int id, CSVWriter writer, String keterangan) {
        this.id = id;
        this.writer = writer;
        this.keterangan = keterangan;
    }

    public void setFile(CSVWriter writer) {
        this.writer = writer;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public int getId() {
        return id;
    }

    public CSVWriter getFile() {
        return writer;
    }

    public String getKeterangan() {
        return keterangan;
    }

}
