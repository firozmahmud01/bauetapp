package com.firoz.mahmud.bauet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences(MKeys.sp.database,MODE_PRIVATE);
//        if(sp.getString(MKeys.sp.mainauthtoken,"").isEmpty()){
//            startActivity(new Intent(MainActivity.this,Login.class));
//        }else{
//            startActivity(new Intent(MainActivity.this,Home.class));
//        }



        startActivity(new Intent(MainActivity.this,FrontImageUpload.class));
        finish();

    }
}