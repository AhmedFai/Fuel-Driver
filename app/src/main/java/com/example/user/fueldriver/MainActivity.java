package com.example.user.fueldriver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class MainActivity extends AppCompatActivity {

    AHBottomNavigation bottom;

    Toolbar toolbar;
    static SharedPreferences pref;

    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        pref = getApplication().getSharedPreferences("pref", MODE_PRIVATE);
        edit = pref.edit();

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //toolbar.setTitleTextColor(Color.BLACK);

        //toolbar.setNavigationIcon(R.drawable.arrow);

       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/


        bottom = findViewById(R.id.bottom_navigation);


        AHBottomNavigationItem item1 =
                new AHBottomNavigationItem("Profile", R.drawable.user);

        AHBottomNavigationItem item2 =
                new AHBottomNavigationItem("Account", R.drawable.calculatorsvg);

        AHBottomNavigationItem item3 =
                new AHBottomNavigationItem("Duty", R.drawable.deliverysvg);

        AHBottomNavigationItem item4 =
                new AHBottomNavigationItem("Order History", R.drawable.order);

        AHBottomNavigationItem item5 =
                new AHBottomNavigationItem("Logout", R.drawable.logout);

        bottom.addItem(item1);
        bottom.addItem(item2);
        bottom.addItem(item3);
        bottom.addItem(item4);
        bottom.addItem(item5);

        bottom.setBehaviorTranslationEnabled(false);

        bottom.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottom.setDefaultBackgroundColor(Color.parseColor("#222222"));
        bottom.setAccentColor(Color.parseColor("#ea382e"));
        bottom.setInactiveColor(Color.parseColor("#ffffff"));
        bottom.setCurrentItem(2);


        bottom.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position) {
                    case 0:

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                        ProfileEdit frag1 = new ProfileEdit();
                        ft.replace(R.id.replace, frag1);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft.commit();

                       // toolbar.setTitle("Profile");

                        return true;
                    case 1:


                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();

                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }

                        Accounts frag3 = new Accounts();
                        ft2.replace(R.id.replace, frag3);
                        //ft2.addToBackStack(null);
                        ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft2.commit();

                        //toolbar.setTitle("Account");

                        return true;
                    case 2:


                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }

                        Duty frag2 = new Duty();
                        ft1.replace(R.id.replace, frag2);
                        // ft1.addToBackStack(null);
                        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft1.commit();

                        //toolbar.setTitle("Duty");

                        return true;
                    case 3:


                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();

                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }

                        OrderHistory frag4 = new OrderHistory();
                        ft3.replace(R.id.replace, frag4);
                        //ft3.addToBackStack(null);
                        ft3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft3.commit();


                      //  toolbar.setTitle("Order History");
                        return true;
                    case 4:


                        edit.remove("phone");
                        edit.remove("password");
                        edit.remove("driverId");
                        edit.apply();

                        Intent i = new Intent(MainActivity.this, Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

                        return false;
                }

                return false;
            }
        });
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }

        Duty frag2 = new Duty();
        ft1.replace(R.id.replace, frag2);
        // ft1.addToBackStack(null);
        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft1.commit();

       // toolbar.setTitle("Duty");

    }
}
