package com.example.user.fueldriver;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AccountSummary extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView grid;

    List<ItemObject>itemObjects;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    GridLayoutManager manager;
    SummaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_summary);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolbar.setTitleTextColor(Color.BLACK);

        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        grid = findViewById(R.id.grid);
        manager = new GridLayoutManager(this , 1);
        adapter = new SummaryAdapter(this);


        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


    }

   /* public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        Context context;



        public SummaryAdapter(Context context,) {

            this.context = context;


        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TYPE_HEADER) {

                View layoutView = LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.header_list_model1, parent, false);
                return new HeaderViewHolder(layoutView);



            } else if (viewType == TYPE_ITEM) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate
                        (R.layout.summary_list_model, parent, false);
                return new ItemViewHolder(layoutView);
            }
            throw new RuntimeException("No match for " + viewType + ".");
        }

        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemObject mObject = itemObjects.get(position);
            if(holder instanceof Accounts.HeaderViewHolder){
                ((Accounts.HeaderViewHolder) holder).month.setText(mObject.getContents());
            }else if(holder instanceof Accounts.ItemViewHolder){
                ((Accounts.ItemViewHolder) holder).date.setText(mObject.getContents());
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



                }
            });
        }
    }*/




}
