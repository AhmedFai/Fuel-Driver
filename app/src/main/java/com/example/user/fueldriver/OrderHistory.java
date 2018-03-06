package com.example.user.fueldriver;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by USER on 01-02-2018.
 */

public class OrderHistory extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;

    OrderAdaper adaper;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_orders , container , false);


        grid = view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext() , 1);
        adaper = new OrderAdaper(getContext());
        grid.setAdapter(adaper);
        grid.setLayoutManager(manager);

        return view;
    }

    public class OrderAdaper extends RecyclerView.Adapter<OrderAdaper.MyViewHolder> {


        Context context;

        public OrderAdaper(Context context){

            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.orders_list_model , parent , false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


}
