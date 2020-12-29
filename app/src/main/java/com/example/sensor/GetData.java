package com.example.sensor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetData {
    @SerializedName("status")
    String status;
    @SerializedName("data")
    List<DataSensor> listDataSensor;
    @SerializedName("message")
    String message;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setListDataPhoto(List<DataSensor> listDataPhoto) {
        this.listDataSensor = listDataPhoto;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public List<DataSensor> getListDataPhoto() {
        return listDataSensor;
    }

    public String getMessage() {
        return message;
    }
}
