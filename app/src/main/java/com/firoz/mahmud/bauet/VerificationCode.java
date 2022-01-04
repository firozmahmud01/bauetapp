package com.firoz.mahmud.bauet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firoz.mahmud.bauet.Api.LoginApi;

public class VerificationCode extends AppCompatActivity {
    LoginApi api;
    EditText code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        api=new LoginApi(this);
        code=findViewById(R.id.verification_code_edittext);
        findViewById(R.id.verification_code_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(code.getText().toString().isEmpty()){
                    code.setError("Enter the code");
                    return ;
                }
                try{
                    api.verifycode(code.getText().toString());
                    startActivity(new Intent(VerificationCode.this,SignupDIs.class));
                    finish();
                }catch (Exception e){
                    Toast.makeText(VerificationCode.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }





    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(VerificationCode.this).setMessage("Are you sure about cancle signup?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface di, int i) {
                api.setPosition("");
                startActivity(new Intent(VerificationCode.this,SignupDIs.class));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface di, int i) {
                di.dismiss();
            }
        });
    }
}