package com.example.user.fueldriver;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;

public class KVC extends AppCompatActivity {

    Toolbar toolbar;
    RadioGroup group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kvc);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        group = (RadioGroup) findViewById(R.id.radio);

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


        /*group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {



                int id = radioGroup.getCheckedRadioButtonId();

                RadioButton rb = (RadioButton) radioGroup.findViewById(id);



                answer = rb.getText().toString();

                ques1.clearFocus();
                ques1.setError(null);


                b.id1 = id;

            }
        });*/
    }
}
