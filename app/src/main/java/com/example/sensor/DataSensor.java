package com.example.sensor;

import com.google.gson.annotations.SerializedName;
import com.opencsv.CSVWriter;

import java.io.File;

public class DataSensor {

    @SerializedName("id")
    private int id;
    @SerializedName("file")
    private File writer;
    @SerializedName("keterangan")
    private String keterangan;

    public DataSensor() { }

    public DataSensor(int id) {
        this.id = id;
    }

    public DataSensor(int id, File writer, String keterangan) {
        this.id = id;
        this.writer = writer;
        this.keterangan = keterangan;
    }

    public void setFile(File writer) {
        this.writer = writer;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public int getId() {
        return id;
    }

    public File getFile() {
        return writer;
    }

    public String getKeterangan() {
        return keterangan;
    }

}
