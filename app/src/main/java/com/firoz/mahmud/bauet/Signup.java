package com.firoz.mahmud.bauet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    EditText email,name,pass,batch,id,regid,phone,roomno;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email=findViewById(R.id.student_form_email_edittext);
        name=findViewById(R.id.student_form_full_name_edittext);
        pass=findViewById(R.id.student_form_password_edittext);
        batch=findViewById(R.id.student_form_batch_edittext);
        id=findViewById(R.id.student_form_id_edittext);
        regid=findViewById(R.id.student_form_regid_edittext);
        phone=findViewById(R.id.student_form_phone_edittext);
        roomno=findViewById(R.id.student_form_room_no_edittext);
        pd=new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);

        findViewById(R.id.student_form_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                email,name,pass,batch,id,regid,phone,roomno;
                if(email.getText().toString().isEmpty()){
                    email.setError("Fill it...");
                    return ;
                }
                if(name.getText().toString().isEmpty()){
                    name.setError("Fill it...");
                    return ;
                }
                if(pass.getText().toString().isEmpty()){
                    pass.setError("Fill it...");
                    return ;
                }
                if(batch.getText().toString().isEmpty()){
                    batch.setError("Fill it...");
                    return ;
                }
                if(id.getText().toString().isEmpty()){
                    id.setError("Fill it...");
                    return ;
                }
                if(regid.getText().toString().isEmpty()){
                    regid.setError("Fill it...");
                    return ;
                }
                if(phone.getText().toString().isEmpty()){
                    phone.setError("Fill it...");
                    return ;
                }
                if(roomno.getText().toString().isEmpty()){
                    roomno.setError("Fill it...");
                    return ;
                }
                pd.show();
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(Signup.this, "Signup complete.Please login.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signup.this,MainActivity.class));
                        }else{
                            Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }
}