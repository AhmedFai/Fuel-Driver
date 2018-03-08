package com.example.user.fueldriver;


import com.example.user.fueldriver.AcceptDenyPOJO.AcceptDenyBean;
import com.example.user.fueldriver.BookingStatusPOJO.BookingStatusBean;
import com.example.user.fueldriver.EarnByDayPOJO.ByDayBean;
import com.example.user.fueldriver.EarnListPOJO.EarnListBean;
import com.example.user.fueldriver.FinishRidePOJO.FinishRideBean;
import com.example.user.fueldriver.LoginPOJO.LoginBean;
import com.example.user.fueldriver.NotificationPOJO.notificationBean;
import com.example.user.fueldriver.OrderHistoryPOJO.OrderHistoryBean;
import com.example.user.fueldriver.ProfilePOJO.ProfileBean;
import com.example.user.fueldriver.RideStatusPOJO.DriverStatusBean;
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

    @Multipart
    @POST("fuel/api/driver_accept_deny.php")
    Call<AcceptDenyBean> accept(
            @Part("driverid") String m,
            @Part("notificationId") String n,
            @Part("status") String o

    );

    @Multipart
    @POST("fuel/api/get_driver_status.php")
    Call<DriverStatusBean> driver(
            @Part("driverId") String m,
            @Part("bookingId") String n,
            @Part("latitude") String o,
            @Part("longitude") String p

    );


    @Multipart
    @POST("fuel/api/driver_get_profile.php")
    Call<ProfileBean> profile(
            @Part("driverId") String m

    );

    @Multipart
    @POST("fuel/api/get_booking_status.php")
    Call<BookingStatusBean> booking(
            @Part("driverId") String m

    );

    @Multipart
    @POST("fuel/api/driver-earn-list.php")
    Call<EarnListBean> earnList(
            @Part("driverId") String m

    );



    @Multipart
    @POST("fuel/api/driver_finished_ride.php")
    Call<FinishRideBean> finishRide(
            @Part("driverId") String m,
            @Part("bookingId") String n,
            @Part("latitude") String o,
            @Part("longitude") String p

    );


    @Multipart
    @POST("fuel/api/driver-earn-byday.php")
    Call<ByDayBean> byDay(
            @Part("driverId") String m,
            @Part("dayId") String n

    );


    @Multipart
    @POST("fuel/api/driver-order-history.php")
    Call<OrderHistoryBean> order(
            @Part("driverId") String m


    );

}
