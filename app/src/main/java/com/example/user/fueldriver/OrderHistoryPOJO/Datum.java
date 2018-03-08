package com.example.user.fueldriver.OrderHistoryPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by faizan on 3/8/2018.
 */

public class Datum {


    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("crn")
    @Expose
    private String crn;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("fuelType")
    @Expose
    private String fuelType;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("bookingStatus")
    @Expose
    private String bookingStatus;
    @SerializedName("bookingStatuscode")
    @Expose
    private String bookingStatuscode;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingStatuscode() {
        return bookingStatuscode;
    }

    public void setBookingStatuscode(String bookingStatuscode) {
        this.bookingStatuscode = bookingStatuscode;
    }


}
