package com.example.user.fueldriver;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.fueldriver.EarnByDayPOJO.Datum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 01-02-2018.
 */

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MyViewHolder> {
    List<Datum> list = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public SummaryAdapter(Context context, List<Datum> list){
        this.context  = context;
        this.list = list;
    }


    public void setGridData(List<Datum> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.summary_list_model, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Datum item = list.get(position);



        holder.crn.setText(item.getCrn());
        holder.amount.setText(item.getPrice());
        holder.quantity.setText(item.getQuantity() + " Ltr");
        holder.type.setText(item.getFuelTypeName());
        holder.date.setText(item.getDate());




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView day , dayend,crn,amount,type,quantity,date;
        LinearLayout data;

        public MyViewHolder(View itemView) {
            super(itemView);



            crn = (TextView)itemView.findViewById(R.id.crn);
            date = (TextView)itemView.findViewById(R.id.date);
            amount = (TextView)itemView.findViewById(R.id.amount);
            type = (TextView)itemView.findViewById(R.id.type);
            quantity = (TextView)itemView.findViewById(R.id.quantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
