package com.firoz.mahmud.bauet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firoz.mahmud.bauet.Api.LoginApi;

public class SignupDIs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_dis);
        LoginApi log=new LoginApi(this);
        String pos=log.getPosition();
        if(pos.isEmpty()){
            startActivity(new Intent(SignupDIs.this,Signup.class));
        }else if(pos.equals("1st")){
            startActivity(new Intent(SignupDIs.this,VerificationCode.class));
        }else if(pos.equals("2nd")){
            startActivity(new Intent(SignupDIs.this,FrontImageUpload.class));
        }

            finish();
    }
}