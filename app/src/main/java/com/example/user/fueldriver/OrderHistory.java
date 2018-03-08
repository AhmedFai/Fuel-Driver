package com.example.user.fueldriver;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.fueldriver.OrderHistoryPOJO.Datum;
import com.example.user.fueldriver.OrderHistoryPOJO.OrderHistoryBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by USER on 01-02-2018.
 */

public class OrderHistory extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;
    List<Datum> list;
    OrderAdaper adaper;
    ProgressBar bar;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    ConnectionDetector cd;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_orders , container , false);


        list = new ArrayList<>();
        bar = (ProgressBar) view.findViewById(R.id.progress);
        grid = view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext() , 1);
        adaper = new OrderAdaper(getContext(),list);
        grid.setAdapter(adaper);
        grid.setLayoutManager(manager);
        cd = new ConnectionDetector(getContext());

        if (cd.isConnectingToInternet()) {

            bar.setVisibility(View.VISIBLE);

            final Bean b = (Bean) getContext().getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseURL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Allapi cr = retrofit.create(Allapi.class);
            Call<OrderHistoryBean> call = cr.order(b.driverId);
            call.enqueue(new Callback<OrderHistoryBean>() {
                @Override
                public void onResponse(Call<OrderHistoryBean> call, Response<OrderHistoryBean> response) {
                    if (Objects.equals(response.body().getStatus(),"1")){
                        bar.setVisibility(View.GONE);
                        adaper.setgrid(response.body().getData());


                    }else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        bar.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onFailure(Call<OrderHistoryBean> call, Throwable t) {
                    bar.setVisibility(View.GONE);

                }
            });
            }else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }

            return view;
    }

    public class OrderAdaper extends RecyclerView.Adapter<OrderAdaper.MyViewHolder> {


        Context context;
        List<Datum> list = new ArrayList<>();

        public OrderAdaper(Context context, List<Datum> list){

            this.context = context;
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.orders_list_model , parent , false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            final Datum item = list.get(position);

            holder.crn.setText(item.getCrn());
            holder.date.setText(item.getDate());
            holder.type.setText(item.getFuelType());
            holder.quantity.setText(item.getQuantity() + " Ltr");
            holder.status.setText(item.getBookingStatus());
            holder.price.setText(item.getPrice());

            if (Objects.equals(item.getBookingStatus(),"Completed")){
                holder.status.setTextColor(Color.parseColor("#008000"));
            }else {
                holder.status.setTextColor(Color.parseColor("#ea382e"));
            }


        }

        public void setgrid( List<Datum>list){


            this.list = list;
            notifyDataSetChanged();
        }
        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView crn, type, quantity, status, date, price;
            public MyViewHolder(View itemView) {
                super(itemView);
                crn = (TextView)itemView.findViewById(R.id.crn);
                type = (TextView)itemView.findViewById(R.id.type);
                quantity = (TextView)itemView.findViewById(R.id.quantity);
                status = (TextView)itemView.findViewById(R.id.status);
                date = (TextView)itemView.findViewById(R.id.date);
                price = (TextView)itemView.findViewById(R.id.price);


            }
        }
    }


}
