package com.firoz.mahmud.bauet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firoz.mahmud.bauet.Api.LoginApi;

public class TeacherForm extends AppCompatActivity {
    EditText name,email,pass,phone,designation;
    Spinner department;
    String darr[]=new String[]{"CSE","CE"};
    String dep=darr[0];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_form);


        department=findViewById(R.id.teacher_form_department_spinner);
        designation=findViewById(R.id.teacher_form_designation_edittext);
        name=findViewById(R.id.teacher_form_full_name_edittext);
        email=findViewById(R.id.teacher_form_email_edittext);
        pass=findViewById(R.id.teacher_form_password_edittext);
        phone=findViewById(R.id.teacher_form_phone_edittext);


        findViewById(R.id.teacher_form_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty()){
                    name.setError("Fill it before submit");
                    return ;
                }
                if(email.getText().toString().isEmpty()){
                    email.setError("Fill it before submit");
                    return ;
                }
                if(pass.getText().toString().isEmpty()){
                    pass.setError("Fill it before submit");
                    return ;
                }

                if(phone.getText().toString().isEmpty()){
                    phone.setError("Fill it before submit");
                    return ;
                }
                if(designation.getText().toString().isEmpty()){
                    designation.setError("Fill it before submit");
                    return ;
                }

                LoginApi api=new LoginApi(TeacherForm.this);
                try {
                    api.signupteacher(name.getText().toString(),email.getText().toString(),
                            pass.getText().toString(),designation.getText().toString(),
                            phone.getText().toString(),dep);
                    startActivity(new Intent(TeacherForm.this,SignupDIs.class));
                    finish();
                } catch (Exception e) {
                    Toast.makeText(TeacherForm.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dep=darr[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }
}