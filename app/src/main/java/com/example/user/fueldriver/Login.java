package com.example.user.fueldriver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.fueldriver.LoginPOJO.LoginBean;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {

    TextView register , forgot;
    EditText phone, pass;

    Button signin;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    ConnectionDetector cd;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        edit = pref.edit();
        signin = findViewById(R.id.signin);
        bar = (ProgressBar) findViewById(R.id.progress);
        phone = (EditText)findViewById(R.id.mobile);
        pass = (EditText)findViewById(R.id.password);
        cd = new ConnectionDetector(getApplication());



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cd.isConnectingToInternet()){

                    final String p = phone.getText().toString();
                    final String ps = pass.getText().toString();

                    if (!TextUtils.isEmpty(p)){

                        if (!TextUtils.isEmpty(ps)){

                            bar.setVisibility(View.VISIBLE);
                            final Bean b = (Bean) getApplicationContext();
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.baseURL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            Allapi cr = retrofit.create(Allapi.class);
                            Call<LoginBean> call = cr.login(p,ps);
                            call.enqueue(new Callback<LoginBean>() {
                                @Override
                                public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {

                                    if (Objects.equals(response.body().getStatus(),"1")){

                                        b.phone = "";
                                        b.password = "";
                                        b.driverId = response.body().getData().getDriverId();
                                        Log.d("driver Name", response.body().getData().getDrivername());


                                        edit.putString("driverId", response.body().getData().getDriverId());
                                        edit.apply();

                                        Intent i = new Intent(Login.this , MainActivity.class);
                                        startActivity(i);
                                        finish();

                                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();



                                    }else {
                                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        bar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoginBean> call, Throwable t) {
                                    bar.setVisibility(View.GONE);

                                }
                            });

                        }else {
                            pass.setError("Field is Empty");
                            pass.requestFocus();
                        }

                    }else {
                        phone.setError("Field is Empty");
                        phone.requestFocus();
                    }

                }else {
                    Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
