package com.example.user.fueldriver;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.fueldriver.NotificationPOJO.notificationBean;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
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

public class Duty extends Fragment implements View.OnClickListener {

    SupportMapFragment mSupportMapFragment;
    LinearLayout offDuty, onDuty;
    ProgressBar bar;
    ImageView offImage, onImage, profile;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    CardView notiBox;
    Timer timer;
    TextView notiName, pickupLocation;
    String notiId = " ";


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

                        googleMap.getUiSettings().setAllGesturesEnabled(true);

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

                                googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)))
                                        .title("" + subLocality + ", " + state + ", " + country + "")
                                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.pin)));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        googleMap.setMyLocationEnabled(false);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(15.0f).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);


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
                            Toast.makeText(getContext(), "You duty is off", Toast.LENGTH_SHORT).show();
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


}
