package com.firoz.mahmud.bauet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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