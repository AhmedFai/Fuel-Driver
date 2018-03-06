package com.example.user.fueldriver;


import com.example.user.fueldriver.LoginPOJO.LoginBean;
import com.example.user.fueldriver.NotificationPOJO.notificationBean;
import com.example.user.fueldriver.UpdateStatusPOJO.UpdateStatusBean;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by USER on 11/30/2017.
 */

public interface Allapi {






    @Multipart
    @POST("fuel/api/driver_login.php")
    Call<LoginBean> login(
            @Part("phone") String m,
            @Part("password") String n

    );

    @Multipart
    @POST("fuel/api/update_driver_status.php")
    Call<UpdateStatusBean> status(
            @Part("driverId") String m,
            @Part("status") String n

    );

    @Multipart
    @POST("fuel/api/driver_get_notification.php")
    Call<notificationBean> notify(
            @Part("driverId") String m,
            @Part("latitude") String n,
            @Part("longitude") String o

    );

}
