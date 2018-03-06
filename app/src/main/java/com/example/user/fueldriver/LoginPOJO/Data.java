package com.example.user.fueldriver.LoginPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by faizan on 3/6/2018.
 */

public class Data {

    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("drivername")
    @Expose
    private String drivername;
    @SerializedName("cabName")
    @Expose
    private String cabName;
    @SerializedName("cabTypeId")
    @Expose
    private String cabTypeId;
    @SerializedName("cabId")
    @Expose
    private String cabId;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getCabName() {
        return cabName;
    }

    public void setCabName(String cabName) {
        this.cabName = cabName;
    }

    public String getCabTypeId() {
        return cabTypeId;
    }

    public void setCabTypeId(String cabTypeId) {
        this.cabTypeId = cabTypeId;
    }

    public String getCabId() {
        return cabId;
    }

    public void setCabId(String cabId) {
        this.cabId = cabId;
    }


}
