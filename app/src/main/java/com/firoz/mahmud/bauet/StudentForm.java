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

public class StudentForm extends AppCompatActivity {
    EditText name,email,pass,id,regid,phone,batch,session,roomno;
    Spinner program,hallname,department;
    String parr[]=new String[]{"CSE","CE"},harr[]=new String[]{"Boral Hall","Bonolota Hall"},darr[]=new String[]{"CSE","CE"};
    String pro=parr[0],hal=harr[0],dep=darr[0];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);

        program=findViewById(R.id.student_form_program_spinner);
        hallname=findViewById(R.id.student_form_hallname_spinner);
        department=findViewById(R.id.student_form_department_spinner);

        name=findViewById(R.id.student_form_full_name_edittext);
        email=findViewById(R.id.student_form_email_edittext);
        pass=findViewById(R.id.student_form_password_edittext);
        id=findViewById(R.id.student_form_id_edittext);
        regid=findViewById(R.id.student_form_regid_edittext);
        phone=findViewById(R.id.student_form_phone_edittext);
        batch=findViewById(R.id.student_form_batch_edittext);
        session=findViewById(R.id.student_form_session_edittext);
        roomno=findViewById(R.id.student_form_room_no_edittext);

        findViewById(R.id.student_form_submit_button).setOnClickListener(new View.OnClickListener() {
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
                if(id.getText().toString().isEmpty()){
                    id.setError("Fill it before submit");
                    return ;
                }
                if(regid.getText().toString().isEmpty()){
                    regid.setError("Fill it before submit");
                    return ;
                }
                if(phone.getText().toString().isEmpty()){
                    phone.setError("Fill it before submit");
                    return ;
                }
                if(batch.getText().toString().isEmpty()){
                    batch.setError("Fill it before submit");
                    return ;
                }
                if(session.getText().toString().isEmpty()){
                    session.setError("Fill it before submit");
                    return ;
                }
                if(roomno.getText().toString().isEmpty()){
                    roomno.setError("Fill it before submit");
                    return ;
                }

                LoginApi api=new LoginApi(StudentForm.this);
                try {
                    api.signupstudent(name.getText().toString(),email.getText().toString(),pass.getText().toString(),id.getText().toString()
                    ,regid.getText().toString(),phone.getText().toString(),batch.getText().toString(),dep,session.getText().toString(),pro,
                            hal,roomno.getText().toString());
                    startActivity(new Intent(StudentForm.this,SignupDIs.class));
                    finish();
                } catch (Exception e) {
                    Toast.makeText(StudentForm.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        program.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pro=parr[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        hallname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hal=harr[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







    }

}