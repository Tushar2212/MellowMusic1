package com.example.mukeshsharma.mymusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    ImageView back;
    ProgressBar bar;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        back=(ImageView)findViewById(R.id.inSplashScreen);
        bar=(ProgressBar)findViewById(R.id.barSplashScreen);
        textView=(TextView)findViewById(R.id.tvSplashScreen);
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    sleep(3000);
                }catch (Exception e)
                {
                    ;
                }
                finally {
                    Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
