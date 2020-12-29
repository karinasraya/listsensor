package com.example.sensor;

import com.google.gson.annotations.SerializedName;

public class PostPutDel {
    @SerializedName("status")
    String status;
    @SerializedName("data")
    DataSensor dataSensor;
    @SerializedName("message")
    String message;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDataPhoto(DataSensor dataPhoto) {
        this.dataSensor = dataSensor;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public DataSensor getDataPhoto() {
        return dataSensor;
    }

    public String getMessage() {
        return message;
    }
}