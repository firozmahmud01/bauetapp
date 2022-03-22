package com.firoz.mahmud.bauet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firoz.mahmud.bauet.Api.LoginApi;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences(MKeys.sp.database,MODE_PRIVATE);
        //mainauthtoken will be available after login
        if(sp.getString(MKeys.sp.mainauthtoken,"").isEmpty()){
            LoginApi log=new LoginApi(this);
            //getposition will return string to recode where left in signup
            String pos=log.getPosition();
            if(pos.isEmpty()) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }else{
                startActivity(new Intent(MainActivity.this,SignupDIs.class));
            }
        }else{
            startActivity(new Intent(MainActivity.this,Home.class));
        }




        finish();

    }
}