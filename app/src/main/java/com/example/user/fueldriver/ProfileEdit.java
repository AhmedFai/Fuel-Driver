package com.example.user.fueldriver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.fueldriver.ProfilePOJO.ProfileBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by USER on 01-02-2018.
 */

public class ProfileEdit extends Fragment {

    TextView address , legal , vehicle,personaldocuments , aadhar, name, contact, email;
    ProgressBar bar;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    ConnectionDetector cd;
    CircleImageView image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profileedit , container , false);

        address = view.findViewById(R.id.address);
        legal = view.findViewById(R.id.legal);
        vehicle = view.findViewById(R.id.vehicle);
        personaldocuments = view.findViewById(R.id.pd);
        aadhar = view.findViewById(R.id.complete);
        name = (TextView)view.findViewById(R.id.name);
        contact = (TextView)view.findViewById(R.id.contact);
        email = (TextView)view.findViewById(R.id.email);
        bar = (ProgressBar)view.findViewById(R.id.progress);
        cd = new ConnectionDetector(getContext());
        image = (CircleImageView) view.findViewById(R.id.image);
        pref = getContext().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        edit = pref.edit();

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext() , Address.class);
                startActivity(i);
            }
        });


        legal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext() , LegalInformation.class);
                startActivity(i);
            }
        });

        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext() , VehicleDocuments.class);
                startActivity(i);
            }
        });


        personaldocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext() , PersonalDocument.class);
                startActivity(i);
            }
        });


        aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext() , CompleteKYC.class);
                startActivity(i);
            }
        });



        if (cd.isConnectingToInternet()){

            String id = pref.getString("driverId", "");
            bar.setVisibility(View.VISIBLE);
            final Bean b = (Bean) getContext().getApplicationContext();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseURL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Allapi cr = retrofit.create(Allapi.class);
            Call<ProfileBean> call = cr.profile(id);
            call.enqueue(new Callback<ProfileBean>() {
                @Override
                public void onResponse(Call<ProfileBean> call, Response<ProfileBean> response) {
                    if (Objects.equals(response.body().getStatus(),"1")){

                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                                .cacheOnDisk(true).resetViewBeforeLoading(true).build();

                        ImageLoader loader = ImageLoader.getInstance();
                        loader.displayImage(response.body().getData().getImage(), image, options);

                        name.setText(response.body().getData().getUsername());
                        contact.setText(response.body().getData().getPhone());
                        email.setText(response.body().getData().getEmail());
                        bar.setVisibility(View.GONE);



                    }else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        bar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ProfileBean> call, Throwable t) {
                    bar.setVisibility(View.GONE);

                }
            });


        }else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }






        return view;
    }
}
