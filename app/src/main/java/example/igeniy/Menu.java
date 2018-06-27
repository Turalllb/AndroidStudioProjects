package example.igeniy;


import android.app.Activity;
import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.util.VKUtil;

import java.io.File;

import static example.igeniy.MainGamePanel.NewTimerFlag;
import static example.igeniy.MainThread.TimePause;
import static example.igeniy.R.mipmap.face_box;
import static example.igeniy.StartActivity.typeface;

public class Menu extends AppCompatActivity implements MainGamePanel.IGetImage, IGetDialog, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = Menu.class.getSimpleName();
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";

    MainGamePanel mainGamePanel;
    static float width, height;
    static boolean pauseGame = true;
    public GameOverFragment GameOverFragment;   //Вместо GameOverFragment всегда стоял DialogFragment . Работает одинаково, но когда DialogFragment нельзя вызвать метод регистрации обратного вызова
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9;
    Bitmap[] myArray = new Bitmap[11];
    TextView Time;
    public static int i = 1, j = 1;
    RecordAdapter mRecordAdapter;
    private static ProgressDialog pd;
    static Context context;
    File file;
    File[] paths;



    public Menu(){
        GameOverFragment = new GameOverFragment();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display currentDisplay = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        System.out.println("РаЗМЕР" + metrics);
        width = currentDisplay.getWidth();
        height = currentDisplay.getHeight();
        System.out.println("width = " + width + "height  =" + height);
        // запрос на отключение строки заголовка
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // перевод приложения в полноэкранный режим
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu);
        Time = (TextView) findViewById(R.id.Time);
        Time.setTypeface(typeface);
        mainGamePanel = (MainGamePanel) findViewById(R.id.surface);
        mainGamePanel.setMyInterface(this);
        mainGamePanel.setInterface(this);
        mRecordAdapter = new RecordAdapter(this);
        //setContentView(mainGamePanel);
        //setContentView(new MainGamePanel(this));
        Log.d(TAG, "onCreate...");
        // android.os.Debug.waitForDebugger();

        //region initArray
        myArray[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.zero);
        myArray[1] = BitmapFactory.decodeResource(getResources(), R.mipmap.one);
        myArray[2] = BitmapFactory.decodeResource(getResources(), R.mipmap.two);
        myArray[3] = BitmapFactory.decodeResource(getResources(), R.mipmap.three);
        myArray[4] = BitmapFactory.decodeResource(getResources(), R.mipmap.four);
        myArray[5] = BitmapFactory.decodeResource(getResources(), R.mipmap.five);
        myArray[6] = BitmapFactory.decodeResource(getResources(), R.mipmap.six);
        myArray[7] = BitmapFactory.decodeResource(getResources(), R.mipmap.seven);
        myArray[8] = BitmapFactory.decodeResource(getResources(), R.mipmap.eight);
        myArray[9] = BitmapFactory.decodeResource(getResources(), R.mipmap.nine);
        myArray[10] = BitmapFactory.decodeResource(getResources(), R.mipmap.point);
        //endregion

        //region findImage
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) this.findViewById(R.id.imageView2);
        imageView3 = (ImageView) this.findViewById(R.id.imageView3);
        imageView4 = (ImageView) this.findViewById(R.id.imageView4);
        imageView5 = (ImageView) this.findViewById(R.id.imageView5);
        imageView6 = (ImageView) this.findViewById(R.id.imageView6);
        imageView7 = (ImageView) this.findViewById(R.id.imageView7);
        imageView8 = (ImageView) this.findViewById(R.id.imageView8);
        imageView9 = (ImageView) this.findViewById(R.id.imageView9);
        //endregion
        /*imageView3.setImageBitmap(myArray[10]);
        imageView6.setImageBitmap(myArray[10]);*/
        context = this;


        //System.out.println( "Fingerprint:"+fingerprints[i]);


        file = new File("");
        if (file.exists()) {
            System.out.println("ЕСТь");
        } else System.out.println("НЕТ");
        paths = file.listFiles();
        System.out.println(paths);
    }





    @Override

    public void setImage(final int Millisecond3, final int Millisecond2, final int Millisecond1,       //узнать почему нельзя без final и вывести точки в отдельный метод, чтобы не каждый раз одно и то же перерисовывать
                         final int Seconds1, final int Seconds2, final int Minutes1, final int Minutes2, final String currentTime) {
        runOnUiThread(new Runnable() {
            public void run() {
                Time.setText(currentTime);
                /*imageView1.setImageBitmap(myArray[Minutes2]);
                imageView2.setImageBitmap(myArray[Minutes1]);
                imageView4.setImageBitmap(myArray[Seconds2]);
                imageView5.setImageBitmap(myArray[Seconds1]);
                imageView7.setImageBitmap(myArray[Millisecond3]);
                imageView8.setImageBitmap(myArray[Millisecond2]);
                imageView9.setImageBitmap(myArray[Millisecond1]);*/
            }
        });
    }


    public void ReadRecords() {
        mRecordAdapter.readRecords();
    }

    public void addTime(Integer TimeToCompareInt, String CurrentTime) {
        mRecordAdapter.addTime(TimeToCompareInt, CurrentTime);
    }

    public void WriteRecords() {
        mRecordAdapter.WriteRecords();
    }

    public void showDialog(String currentTime) {
        Bundle bundle = new Bundle();
        bundle.putString("currentTime", currentTime);
        GameOverFragment.setArguments(bundle);
        GameOverFragment.show(getSupportFragmentManager(), "GameOverFragment");
    }

    public GameOverFragment GetFragment(){
        return GameOverFragment;
    }





    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        NewTimerFlag = true;
        Log.d(TAG, "Destroying...");
        mRecordAdapter.readRecords();
    }

    @Override
    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        /*super.onCreateThumbnail(outBitmap, canvas);
        outBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.face_box);
        canvas.drawBitmap(outBitmap, 0, 0, new Paint());*/
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), face_box);
        canvas.drawBitmap(myBitmap, 0, 0, null);
        Log.d(TAG, "Скрин");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                Log.d(TAG, "qweqweqw...");
                // действие по нажатию на кнопку home
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRecordAdapter.WriteRecords();
        Log.d(TAG, "Stopping ");
    }


    @Override
    protected void onPause() {
        if (!pauseGame) {     //это условие нужно,чтобы при повторных сворачиваниях игры, не начав  играть, переменная времени паузы не  обновилась
            TimePause = System.currentTimeMillis();
        }
        pauseGame = true;
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        j = 1;   //для случая ,когда кнопкой питания останавливается игра и поток не уничтожается.
        Log.d(TAG, "onResume ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }


    @Override
    public void onBackStackChanged() {
        homeAsUpByBackStack();
    }

    private void homeAsUpByBackStack() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    protected static void showProgress(String message) {
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    protected static void hideProgress() {
        pd.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            super.onActivityResult(requestCode, resultCode, data);

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


   /* static public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getAppContext().getSystemService(Application.CONNECTIVITY_SERVICE);

        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            return true; // есть соединение
        } else {
            return false; // нет соединения
        }
    }*/


}

 /*public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("Hello World!");
                break;

        }

        return true;
    }*/
