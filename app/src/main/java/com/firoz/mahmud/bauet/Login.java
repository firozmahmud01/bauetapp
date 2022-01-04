package com.firoz.mahmud.bauet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firoz.mahmud.bauet.Api.LoginApi;

public class Login extends AppCompatActivity {
    EditText email,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.login_email_edittext);
        pass=findViewById(R.id.login_password_edittext);


        findViewById(R.id.login_signin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Fill it before login");
                    return;
                }
                if(pass.getText().toString().isEmpty()){
                    pass.setError("Fill it before login");
                    return;
                }

                LoginApi api=new LoginApi(Login.this);
                try {
                    api.login(email.getText().toString(), pass.getText().toString());
                }catch (Exception e){
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });
        findViewById(R.id.login_signup_textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,SignupDIs.class));
            }
        });
    }
}