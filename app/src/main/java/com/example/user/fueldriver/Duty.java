package com.example.user.fueldriver;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.fueldriver.AcceptDenyPOJO.AcceptDenyBean;
import com.example.user.fueldriver.BookingStatusPOJO.BookingStatusBean;
import com.example.user.fueldriver.FinishRidePOJO.FinishRideBean;
import com.example.user.fueldriver.GoogleMapPOJO.DirectionFinder;
import com.example.user.fueldriver.GoogleMapPOJO.DirectionFinderListener;
import com.example.user.fueldriver.GoogleMapPOJO.Route;
import com.example.user.fueldriver.NotificationPOJO.notificationBean;
import com.example.user.fueldriver.RideStatusPOJO.DriverStatusBean;
import com.example.user.fueldriver.UpdateStatusPOJO.UpdateStatusBean;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by USER on 01-02-2018.
 */

public class Duty extends Fragment implements View.OnClickListener, DirectionFinderListener {

    SupportMapFragment mSupportMapFragment;
    LinearLayout offDuty, onDuty;
    ProgressBar bar;
    ImageView offImage, onImage, profile;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    CardView notiBox;
    Timer timer;
    TextView notiName, pickupLocation, confirm, cancle, price, quantity, type;
    String notiId = " ";
    String currentlat, currentLng;

    String pickUpLat = "";
    String pickUpLng = "";

    String dropLat = "";
    String dropLng = "";
    LatLngBounds.Builder builder;
    LatLngBounds bounds;
    GoogleMap map;
    Button finish;
    LinearLayout duty;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.duty, container, false);
        bar = (ProgressBar) view.findViewById(R.id.progress);

        pref = getContext().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        edit = pref.edit();
        offDuty = (LinearLayout) view.findViewById(R.id.off);
        onDuty = (LinearLayout) view.findViewById(R.id.on);
        offImage = (ImageView) view.findViewById(R.id.offI);
        onImage = (ImageView) view.findViewById(R.id.onI);
        notiBox = (CardView) view.findViewById(R.id.noti);
        offDuty.setOnClickListener(this);
        onDuty.setOnClickListener(this);
        notiName = (TextView) view.findViewById(R.id.name);
        profile = (ImageView) view.findViewById(R.id.userPic);
        confirm = (TextView) view.findViewById(R.id.cnfrm);
        cancle = (TextView) view.findViewById(R.id.cncl);
        price = (TextView) view.findViewById(R.id.price);
        quantity = (TextView) view.findViewById(R.id.quantity);
        type = (TextView) view.findViewById(R.id.type);
        finish = (Button) view.findViewById(R.id.finish);
        duty = (LinearLayout) view.findViewById(R.id.duty);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);
                String id = pref.getString("driverId", "");
                final Bean b = (Bean) getContext().getApplicationContext();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseURL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Allapi cr = retrofit.create(Allapi.class);
                Call<FinishRideBean> call = cr.finishRide(id, notiId, currentlat, currentLng);
                call.enqueue(new Callback<FinishRideBean>() {
                    @Override
                    public void onResponse(Call<FinishRideBean> call, Response<FinishRideBean> response) {
                        if (Objects.equals(response.body().getStatus(), "1")) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);
                            finish.setVisibility(View.GONE);
                            duty.setVisibility(View.VISIBLE);
                            map.clear();


                            EasyLocationMod easyLocationMod = new EasyLocationMod(getContext());

                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            double[] l = easyLocationMod.getLatLong();
                            String lat = String.valueOf(l[0]);
                            String lon = String.valueOf(l[1]);
                            LatLng myLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            try {
                                List<Address> listAdresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
                                if (null != listAdresses && listAdresses.size() > 0) {
                                    String address = listAdresses.get(0).getAddressLine(0);
                                    String state = listAdresses.get(0).getAdminArea();
                                    String country = listAdresses.get(0).getCountryName();
                                    String subLocality = listAdresses.get(0).getSubLocality();

                                    originMarkers.add(map.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)))
                                            .title("" + subLocality + ", " + state + ", " + country + "")
                                            .icon(bitmapDescriptorFromVector(getContext(), R.drawable.pin))));


                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Log.d("lat", lat);
                            Log.d("lon", lon);

                            map.setMyLocationEnabled(false);
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(15.0f).build();
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                            map.moveCamera(cameraUpdate);

                            offImage.setBackgroundResource(R.drawable.back_circle);
                            onImage.setBackgroundResource(R.drawable.redcircle);


                            doSomethingRepeatedly();

                        }
                    }

                    @Override
                    public void onFailure(Call<FinishRideBean> call, Throwable t) {

                    }
                });
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer.cancel();
                call.cancel();
                String id = pref.getString("driverId", "");
                bar.setVisibility(View.VISIBLE);
                final Bean b = (Bean) getContext().getApplicationContext();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseURL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Allapi cr = retrofit.create(Allapi.class);
                Call<AcceptDenyBean> call = cr.accept(id, notiId, "1");
                call.enqueue(new Callback<AcceptDenyBean>() {
                    @Override
                    public void onResponse(Call<AcceptDenyBean> call, Response<AcceptDenyBean> response) {

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        notiBox.setVisibility(View.GONE);
                        finish.setVisibility(View.VISIBLE);

                        bar.setVisibility(View.GONE);

                        statusForRide();

                    }

                    @Override
                    public void onFailure(Call<AcceptDenyBean> call, Throwable t) {

                        bar.setVisibility(View.GONE);

                    }
                });

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = pref.getString("driverId", "");


                bar.setVisibility(View.VISIBLE);
                final Bean b = (Bean) getContext().getApplicationContext();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseURL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Allapi cr = retrofit.create(Allapi.class);
                Call<AcceptDenyBean> callden = cr.accept(id, notiId, "2");
                callden.enqueue(new Callback<AcceptDenyBean>() {
                    @Override
                    public void onResponse(Call<AcceptDenyBean> call, Response<AcceptDenyBean> response) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        notiBox.setVisibility(View.GONE);
                        bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<AcceptDenyBean> call, Throwable t) {
                        bar.setVisibility(View.GONE);

                    }
                });

            }
        });


        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.bookRideFragment);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.bookRideFragment, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    if (googleMap != null) {

                        map = googleMap;

                        map.getUiSettings().setAllGesturesEnabled(true);

                        EasyLocationMod easyLocationMod = new EasyLocationMod(getContext());

                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                        double[] l = easyLocationMod.getLatLong();
                        String lat = String.valueOf(l[0]);
                        String lon = String.valueOf(l[1]);

                        LatLng myLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            List<Address> listAdresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
                            if (null != listAdresses && listAdresses.size() > 0) {
                                String address = listAdresses.get(0).getAddressLine(0);
                                String state = listAdresses.get(0).getAdminArea();
                                String country = listAdresses.get(0).getCountryName();
                                String subLocality = listAdresses.get(0).getSubLocality();

                                originMarkers.add(map.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)))
                                        .title("" + subLocality + ", " + state + ", " + country + "")
                                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.pin))));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        map.setMyLocationEnabled(false);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(15.0f).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        map.moveCamera(cameraUpdate);


                    }

                }

            });


        }


        return view;
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.pin);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
//Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
//vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
//vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.off: {
                offImage.setBackgroundResource(R.drawable.redcircle);
                onImage.setBackgroundResource(R.drawable.back_circle);

                try {
                    timer.cancel();
                    notiBox.setVisibility(View.GONE);
                    call.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String id = pref.getString("driverId", "");
                bar.setVisibility(View.VISIBLE);
                final Bean b = (Bean) getContext().getApplicationContext();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseURL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Allapi cr = retrofit.create(Allapi.class);
                Call<UpdateStatusBean> call = cr.status(id, "0");
                call.enqueue(new Callback<UpdateStatusBean>() {
                    @Override
                    public void onResponse(Call<UpdateStatusBean> call, Response<UpdateStatusBean> response) {
                        if (Objects.equals(response.body().getStatus(), "1")) {
                            Toast.makeText(getContext(), "Your duty is off", Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateStatusBean> call, Throwable t) {

                        bar.setVisibility(View.GONE);
                    }
                });


                break;
            }

            case R.id.on: {
                offImage.setBackgroundResource(R.drawable.back_circle);
                onImage.setBackgroundResource(R.drawable.redcircle);

                String id2 = pref.getString("driverId", "");
                bar.setVisibility(View.VISIBLE);
                final Bean b2 = (Bean) getContext().getApplicationContext();


                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl(b2.baseURL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Allapi cr2 = retrofit2.create(Allapi.class);
                Call<UpdateStatusBean> call = cr2.status(id2, "1");
                call.enqueue(new Callback<UpdateStatusBean>() {
                    @Override
                    public void onResponse(Call<UpdateStatusBean> call, Response<UpdateStatusBean> response) {
                        if (Objects.equals(response.body().getStatus(), "1")) {

                            doSomethingRepeatedly();

                            Toast.makeText(getContext(), "You are on duty", Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateStatusBean> call, Throwable t) {

                        bar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }


    Call<notificationBean> call;

    private void doSomethingRepeatedly() {
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                try {
                    EasyLocationMod easyLocationMod = new EasyLocationMod(getContext());


                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    double[] l = easyLocationMod.getLatLong();
                    String lat = String.valueOf(l[0]);
                    String lon = String.valueOf(l[1]);
                    String id = pref.getString("driverId", "");

                    Log.d("jns", id);
                    Log.d("lat", lat);
                    Log.d("lon", lon);


                    final Bean b = (Bean) getContext().getApplicationContext();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseURL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Allapi cr = retrofit.create(Allapi.class);

                    call = cr.notify(id, lat, lon);
                    call.enqueue(new Callback<notificationBean>() {
                        @Override
                        public void onResponse(Call<notificationBean> call, final Response<notificationBean> response) {
                            if (Objects.equals(response.body().getStatus(), "1")) {


                                try {
                                    Log.d("djdf", "jhas");
                                    notiBox.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            notiBox.setVisibility(View.VISIBLE);


                                            if (response.body().getData().size() > 0) {

                                                try {

                                                    notiName.setText(response.body().getData().get(0).getUserName());

                                                    price.setText(" Rs. " + response.body().getData().get(0).getPrice());
                                                    quantity.setText(response.body().getData().get(0).getQuantity() + "L ");
                                                    type.setText(response.body().getData().get(0).getFuelTypeName());


                                                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                                                            .cacheOnDisk(true).resetViewBeforeLoading(true).build();

                                                    ImageLoader loader = ImageLoader.getInstance();

                                                    loader.displayImage(response.body().getData().get(0).getPicture(), profile, options);

                                                    Log.d("nameiudhdu", response.body().getData().get(0).getUserName());

                                                    notiId = response.body().getData().get(0).getBookingId();

                                                    Log.d("NotificationId", notiId);

                                                    String pickLat = response.body().getData().get(0).getPickUpLatitude();
                                                    String pickLon = response.body().getData().get(0).getPickUpLongitude();
                                                    final LatLng myLocation = new LatLng(Double.parseDouble(pickLat), Double.parseDouble(pickLon));
                                                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                                    try {

                                                        List<Address> listAdresses = geocoder.getFromLocation(Double.parseDouble(pickLat), Double.parseDouble(pickLon), 1);
                                                        if (null != listAdresses && listAdresses.size() > 0) {
                                                            String address = listAdresses.get(0).getAddressLine(0);
                                                            String state = listAdresses.get(0).getAdminArea();
                                                            String country = listAdresses.get(0).getCountryName();
                                                            String subLocality = listAdresses.get(0).getSubLocality();
                                                            pickupLocation.setText(address.toString());
                                                            Log.d("addre", address.toString());
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                            } else {
                                                //Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                notiBox.setVisibility(View.GONE);

                                            }

                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                //Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<notificationBean> call, Throwable t) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, 0, 1000 * 1);


    }

    public void statusForRide() {
        EasyLocationMod easyLocationMod = new EasyLocationMod(getContext());


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        double[] l = easyLocationMod.getLatLong();
        currentlat = String.valueOf(l[0]);
        currentLng = String.valueOf(l[1]);
        String id = pref.getString("driverId", "");


        final Bean b = (Bean) getContext().getApplicationContext();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Allapi cr = retrofit.create(Allapi.class);

        Call<DriverStatusBean> callRide = cr.driver(id, notiId, currentlat, currentLng);
        callRide.enqueue(new Callback<DriverStatusBean>() {
            @Override
            public void onResponse(Call<DriverStatusBean> call, Response<DriverStatusBean> response) {

                if (Objects.equals(response.body().getStatus(), "1")) {
                    pickUpLat = response.body().getData().getPickUpLatitude();
                    pickUpLng = response.body().getData().getPickUpLongitude();
                    dropLat = response.body().getData().getDropLatitude();
                    dropLng = response.body().getData().getDropLongitude();
                    Log.d("pickup wale Lat", response.body().getData().getPickUpLatitude());
                    Log.d("pickup wale lng", response.body().getData().getPickUpLongitude());
                    Log.d("drop wale lat", response.body().getData().getDropLatitude());
                    Log.d("drop wale lng", response.body().getData().getDropLongitude());
                    Log.d("dfsdsfgsgsfsgsdgd", response.body().getData().getUserName());

                   /* origin = new LatLng(Double.parseDouble(pickLat), Double.parseDouble(pickLon));
                    destination = new LatLng(Double.parseDouble(dropLat), Double.parseDouble(dropLon));*/

                    sendRequest();

                }

                builder = new LatLngBounds.Builder();
            }

            @Override
            public void onFailure(Call<DriverStatusBean> call, Throwable t) {


                Log.d("dfsgsgsdgsdgsgsdgssssd", t.toString());

            }
        });

    }


    private void sendRequest() {

        Log.d("send request", "hit");

        if (currentLng.length() > 0 && currentLng.length() > 0) {

            Log.d("log ke under", "yahi hai");

            try {
                new DirectionFinder(this, currentlat, currentLng, dropLat, dropLng).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


    }


    @Override
    public void onDirectionFinderStart() {
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        Log.d("success method", "sucssdced");

        // progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        map.clear();
        builder = new LatLngBounds.Builder();

        for (Route route : routes) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            // time.setText(route.duration.text);
            //  value.setText(route.distance.text);

            Log.d("duration", route.duration.text);
            Log.d("distance", route.distance.text);

            Marker marker1 = map.addMarker(new MarkerOptions()
                    .icon(bitmapDescriptorFromVector(getContext(), R.drawable.pin))
                    .title(route.startAddress)
                    .position(route.startLocation));

            originMarkers.add(marker1);
            marker1.showInfoWindow();
            builder.include(marker1.getPosition());

            Marker marker2 = map.addMarker(new MarkerOptions()
                    // .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation));
            destinationMarkers.add(marker2);
            marker2.showInfoWindow();

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLACK).
                    width(10);

            for (int i = 0; i < route.points.size(); i++) {
                polylineOptions.add(route.points.get(i));
                builder.include(route.points.get(i));
            }

            polylinePaths.add(map.addPolyline(polylineOptions));
        }

        bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);


        map.animateCamera(cu);
    }


    @Override
    public void onResume() {
        super.onResume();

        bookingStatus();


    }

    public void bookingStatus() {
        String id = pref.getString("driverId", "");


        final Bean b = (Bean) getContext().getApplicationContext();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Allapi cr = retrofit.create(Allapi.class);
        Call<BookingStatusBean> call = cr.booking(id);
        call.enqueue(new Callback<BookingStatusBean>() {
            @Override
            public void onResponse(Call<BookingStatusBean> call, Response<BookingStatusBean> response) {
                if (Objects.equals(response.body().getData().getStatusCode(), "3")) {

                    duty.setVisibility(View.VISIBLE);
                    notiBox.setVisibility(View.GONE);
                    finish.setVisibility(View.GONE);

                    if (Objects.equals(response.body().getData().getDutyStatusCode(), "1")) {


                        Toast.makeText(getContext(), "You are on duty", Toast.LENGTH_SHORT).show();
                        offImage.setBackgroundResource(R.drawable.back_circle);
                        onImage.setBackgroundResource(R.drawable.redcircle);
                        doSomethingRepeatedly();
                        Log.d("dutywala", response.body().getData().getBookingId());


                    } else {

                        Toast.makeText(getContext(), "Your duty is off", Toast.LENGTH_SHORT).show();
                        offImage.setBackgroundResource(R.drawable.redcircle);
                        onImage.setBackgroundResource(R.drawable.back_circle);


                    }

                } else {


                    duty.setVisibility(View.GONE);
                    notiBox.setVisibility(View.GONE);
                    finish.setVisibility(View.VISIBLE);

                    notiId = response.body().getData().getBookingId();

                    statusForRide();


                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<BookingStatusBean> call, Throwable t) {
                bar.setVisibility(View.GONE);

            }
        });
    }
}
