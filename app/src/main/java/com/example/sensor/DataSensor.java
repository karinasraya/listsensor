package com.example.sensor;

import com.google.gson.annotations.SerializedName;
import com.opencsv.CSVWriter;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DataSensor {

    @SerializedName("file")
    private MultipartBody.Part file;
    @SerializedName("keterangan")
    private RequestBody keterangan;

    public DataSensor() { }

    public DataSensor(MultipartBody.Part writer, RequestBody keterangan) {
        this.file = writer;
        this.keterangan = keterangan;
    }

    public void setFile(MultipartBody.Part file) {
        this.file = file;
    }

    public void setKeterangan(RequestBody keterangan) {
        this.keterangan = keterangan;
    }

    public MultipartBody.Part getFile() {
        return file;
    }

    public RequestBody getKeterangan() {
        return keterangan;
    }

}
