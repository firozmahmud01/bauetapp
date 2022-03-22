package com.firoz.mahmud.bauet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class Home extends AppCompatActivity {
    ViewPager page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        page=findViewById(R.id.homeviewpager);
        page.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "Attandance";
                    case 1:
                        return "Student ID";

                }
                return "Something else";
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new Attandace();
                    case 1:
                        return new StudentID();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });


        ((TabLayout)findViewById(R.id.home_tablayout)).setupWithViewPager(page);
    }






    boolean is1st=false;
    @Override
    public void onBackPressed() {
        if(!is1st){
            is1st=true;
            Toast.makeText(Home.this, "Click again to exit.", Toast.LENGTH_SHORT).show();
        }else{
            finishAffinity();
        }
    }
}