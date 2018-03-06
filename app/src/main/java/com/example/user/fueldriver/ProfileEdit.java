package com.example.user.fueldriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by USER on 01-02-2018.
 */

public class ProfileEdit extends Fragment {

    TextView address , legal , vehicle,personaldocuments , aadhar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profileedit , container , false);

        address = view.findViewById(R.id.address);
        legal = view.findViewById(R.id.legal);
        vehicle = view.findViewById(R.id.vehicle);
        personaldocuments = view.findViewById(R.id.pd);
        aadhar = view.findViewById(R.id.complete);

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
        return view;
    }
}
