package com.example.user.fueldriver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.fueldriver.EarnListPOJO.Datum;
import com.example.user.fueldriver.EarnListPOJO.EarnListBean;

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

public class Accounts extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;

    AccountAdapter adapter;


    List<Datum> list;
    ConnectionDetector cd;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.accounts, container, false);

        grid = view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext(), 1);
        cd = new ConnectionDetector(getContext());


        list = new ArrayList<>();
        adapter = new AccountAdapter(getContext(),list);
        grid.setLayoutManager(manager);
        grid.setAdapter(adapter);

        if (cd.isConnectingToInternet()){
            final Bean b = (Bean) getContext().getApplicationContext();

            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl(b.baseURL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Allapi cr = retrofit2.create(Allapi.class);
            Call<EarnListBean> call = cr.earnList(b.driverId);
            call.enqueue(new Callback<EarnListBean>() {
                @Override
                public void onResponse(Call<EarnListBean> call, Response<EarnListBean> response) {

                    try{

                        if (Objects.equals(response.body().getStatus(),"1")){

                            b.dayId = response.body().getData().get(0).getDayId();
                            adapter.setGridData(response.body().getData());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<EarnListBean> call, Throwable t) {

                }
            });
        }

        return view;


    }

    /*public class AccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        Context context;

        List<ItemObject> itemObjects = new ArrayList<>();

        public AccountAdapter(Context context, List<ItemObject> itemObjects) {

            this.context = context;

            this.itemObjects = itemObjects;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TYPE_HEADER) {

                View layoutView = LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.header_list_model, parent, false);
               return new HeaderViewHolder(layoutView);



            } else if (viewType == TYPE_ITEM) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.account_list_model, parent, false);
                return new ItemViewHolder(layoutView);
            }
            throw new RuntimeException("No match for " + viewType + ".");
        }

        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemObject mObject = itemObjects.get(position);
            if(holder instanceof HeaderViewHolder){
                ((HeaderViewHolder) holder).month.setText(mObject.getContents());
            }else if(holder instanceof ItemViewHolder){
                ((ItemViewHolder) holder).date.setText(mObject.getContents());
            }
        }



        private ItemObject getItem(int position) {
            return itemObjects.get(position);
        }

        @Override
        public int getItemCount() {
            return itemObjects.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;
            return TYPE_ITEM;
        }


        private boolean isPositionHeader(int position) {
            return position == 0;
        }


        public class ViewHolder {
        }



    }


   *//* public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

*//*
    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView month;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            month = itemView.findViewById(R.id.month);

        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {


        TextView date, rupee;

        public ItemViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            rupee = itemView.findViewById(R.id.rupee);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent i = new Intent(getContext(),AccountSummary.class);
                    getContext().startActivity(i);



                }
            });
        }
    }
*/

}


