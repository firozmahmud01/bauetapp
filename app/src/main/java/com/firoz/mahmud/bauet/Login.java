package com.firoz.mahmud.bauet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firoz.mahmud.bauet.Api.LoginApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email,pass;
    ImageView logo;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.login_email_edittext);
        pass=findViewById(R.id.login_password_edittext);
        logo=findViewById(R.id.login_image_view);
        logo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.login_logo));
        pd=new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
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

//                LoginApi api=new LoginApi(Login.this);
//                try {
//                    api.login(email.getText().toString(), pass.getText().toString());
//                }catch (Exception e){
//                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
                pd.show();
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if(task.isSuccessful()){
                            startActivity(new Intent(Login.this,Home.class));
                        }else{
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        findViewById(R.id.login_signup_textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signupdis take dissition where to go in signup
                startActivity(new Intent(Login.this,SignupDIs.class));
            }
        });
    }
}