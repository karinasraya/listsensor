package com.example.sensor;

import com.google.gson.annotations.SerializedName;
import com.opencsv.CSVWriter;

import java.io.File;

public class DataSensor {
    @SerializedName("file")
    private File file;
    @SerializedName("keterangan")
    private String keterangan;

    public DataSensor() { }

    public DataSensor(File file, String keterangan) {
        this.file = file;
        this.keterangan = keterangan;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public File getFile() {
        return file;
    }

    public String getKeterangan() {
        return keterangan;
    }

}
