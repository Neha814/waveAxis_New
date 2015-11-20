package waveaxis.com.waveaxis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);


            Thread t = new  Thread(){
                public void run(){
                    try {
                        sleep(4 * 1000);
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    Intent i = new Intent(SplashScreen.this , Home.class);
                    startActivity(i);
                    finish();

                }
            };t.start();
    }



}
