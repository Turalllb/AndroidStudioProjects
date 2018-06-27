package example.igeniy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static example.igeniy.MediaPlayerSingleton.getInstance;

/**
 * Created by Турал on 04.01.2017.
 */


public class SplashScreen extends AppCompatActivity {
    boolean flag;

     public SplashScreen(){

     }


        @Override
        protected void onCreate (@Nullable Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            getInstance().play();

            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException e){
            }
            flag = true;
            Intent intent = new Intent();
            intent.setClass(this, StartActivity.class);
            startActivity(intent);
        }

        @Override
         protected void onRestart() {
             finish();
             super.onRestart();

         }

}
