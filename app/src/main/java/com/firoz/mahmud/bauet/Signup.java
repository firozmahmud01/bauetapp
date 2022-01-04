package com.firoz.mahmud.bauet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        findViewById(R.id.signup_student_cardview).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent me) {
                if(me.getAction()==MotionEvent.ACTION_DOWN){
                    view.setAlpha(0.5f);
                }else if(me.getAction()==MotionEvent.ACTION_UP){
                    view.setAlpha(1f);
                    startActivity(new Intent(Signup.this,StudentForm.class));
                }

                return true;
            }
        });
        findViewById(R.id.signup_teacher_cardview).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent me) {
                if(me.getAction()==MotionEvent.ACTION_DOWN){
                    view.setAlpha(0.5f);
                }else if(me.getAction()==MotionEvent.ACTION_UP){
                    view.setAlpha(1f);
                    startActivity(new Intent(Signup.this,TeacherForm.class));
                }

                return true;
            }
        });
    }
}