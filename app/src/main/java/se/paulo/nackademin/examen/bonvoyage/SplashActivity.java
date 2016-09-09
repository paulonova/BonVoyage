package se.paulo.nackademin.examen.bonvoyage;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private boolean _active = true;
    private int _splashTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_splash);



        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)){
                        sleep(100);
                        if(_active){
                            waited += 100;
                        }
                    }
                }catch (Exception e){
                    Log.e("ERROR SPLASH", "" + e.getMessage());
                }finally {
                    Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(intent);
                    interrupt();
                }
            }
        };

        splashThread.start();
    }

    // Method to speed the splash presentation..
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _active = false;
        }
        return true;
    }
}
