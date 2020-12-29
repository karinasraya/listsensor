package com.example.sensor;

import com.google.gson.annotations.SerializedName;

public class DataSensor {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("filename")
    private String filename;
    @SerializedName("path")
    private String path;
    @SerializedName("photofile")
    private String photofile;
    @SerializedName("brightness")
    private double brightness;

    public DataSensor() { }

    public DataSensor(int id) {
        this.id = id;
    }

    public DataSensor(int id, String name, String filename, String path, String photofile, double brightness) {
        this.id = id;
        this.name = name;
        this.filename = filename;
        this.path = path;
        this.photofile = photofile;
        this.brightness = brightness;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPhotofile(String photofile) {
        this.photofile = photofile;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public String getPhotofile() {
        return photofile;
    }

    public double getBrightness() {
        return brightness;
    }
}
