package com.firoz.mahmud.bauet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Signup extends AppCompatActivity {
    EditText email,name,pass,batch,id,regid,phone,roomno;
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




            }
        });

    }
}