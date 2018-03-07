package com.example.user.fueldriver;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.user.fueldriver.EarnListPOJO.Datum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 01-02-2018.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    List<Datum> list = new ArrayList<>();
    Context context;

    public AccountAdapter(Context context, List<Datum> list){

        this.context = context;
        this.list = list;
    }

    public void setGridData(List<Datum> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.account_list_model , parent , false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Datum item = list.get(position);
        holder.date.setText(item.getDay());
        holder.amount.setText(item.getAmount());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date, amount,hide;
        public MyViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.date);
            amount = (TextView)itemView.findViewById(R.id.amount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context , AccountSummary.class);

                    i.putExtra("day" , list.get(getAdapterPosition()).getDayId());
                    context.startActivity(i);
                }
            });
        }
    }
}
