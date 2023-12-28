package com.firoz.mahmud.bauet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.firoz.mahmud.bauet.Api.LoginApi;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    ImageView iv;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv=findViewById(R.id.main_image_view);
        tv=findViewById(R.id.main_textview);
        showAnim();

//        startActivity(new Intent(MainActivity.this,Home.class));
//
//
//        finish();

    }
    public void showAnim(){
        iv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_image_scal));
        Animation anim=AnimationUtils.loadAnimation(this, R.anim.splash_text_move);
        tv.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Thread th =new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {

                        }
                        startSomething();
                    }
                };
                th.start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    public void startSomething(){
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(new Intent(MainActivity.this,Login.class));
        }else{
            startActivity(new Intent(MainActivity.this,Home.class));
        }
//        sp=getSharedPreferences(MKeys.sp.database,MODE_PRIVATE);
//        //mainauthtoken will be available after login
//        if(sp.getString(MKeys.sp.mainauthtoken,"").isEmpty()){
//            LoginApi log=new LoginApi(this);
//            //getposition will return string to recode where left in signup
//            String pos=log.getPosition();
//            if(pos.isEmpty()) {
//                startActivity(new Intent(MainActivity.this, Login.class));
//            }else{
//                startActivity(new Intent(MainActivity.this,SignupDIs.class));
//            }
//        }else{
//            startActivity(new Intent(MainActivity.this,Home.class));
//        }
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }
}