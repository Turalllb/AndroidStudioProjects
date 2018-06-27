package example.igeniy;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import static android.content.ContentValues.TAG;


/**
 * Created by Турал on 04.01.2017.
 */

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    static Typeface typeface;
    private  Animation slideInLeftAnimation;
    Animation slideOutRightAnimation;
    Animation slideInRightAnimation;
    Animation slideOutLeftAnimation;
    RecordAdapter mRecordAdapter;
    TextSwitcher mTextSwitcher;
    int mCurrentIndex;
    private String[] gameModes = {"Режим 1", "Режим 2", "Режим 3"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* // если хотим, чтобы приложение постоянно имело портретную ориентацию
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // если хотим, чтобы приложение было полноэкранным
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // и без заголовка
        requestWindowFeature(Window.FEATURE_NO_TITLE);
*/
        setContentView(R.layout.start);



        slideInLeftAnimation = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        slideOutRightAnimation = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
        slideInRightAnimation = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_right);
        slideOutLeftAnimation = AnimationUtils.loadAnimation(this,
                R.anim.slide_out_left);


        mTextSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);



        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(StartActivity.this);
                textView.setTextSize(20);
                textView.setBackgroundResource(R.drawable.box_red);
                textView.setTextColor(Color.RED);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setShadowLayer(10, 10, 10, Color.BLACK);
                return textView;
            }
        });
        mTextSwitcher.setText(gameModes[mCurrentIndex]);

        mRecordAdapter = new RecordAdapter(this);
        //читаю рекорды в статическую переменную TimeRecord из класса RecordAdapter
        mRecordAdapter.readRecords();

        //setContentView(new GameView(this,null));

        Button startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(this);

        Button gameModeRight = (Button) findViewById(R.id.gameModeRight);
        gameModeRight.setOnClickListener(this);

        Button gameModeLeft = (Button) findViewById(R.id.gameModeLeft);
        gameModeLeft.setOnClickListener(this);

        Button RecordsButton = (Button) findViewById(R.id.records);
        RecordsButton.setOnClickListener(this);

        Button exitButton = (Button) findViewById(R.id.settings);
        exitButton.setOnClickListener(this);

        Button button5 = (Button) findViewById(R.id.exit);
        button5.setOnClickListener(this);

        typeface = Typeface.createFromAsset(getAssets(), "inky.ttf");

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroying menu");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause menu");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Stopping menu");
    }


    /**
     * Обработка нажатия кнопок
     */
    public void onClick(View v) {
        switch (v.getId()) {
            //переход на сюрфейс
            case R.id.start: {
                Intent intent = new Intent();
                intent.setClass(this, Menu.class);
                startActivity(intent);
            }
            break;

            case R.id.gameModeRight:
                 mTextSwitcher.setInAnimation(slideInLeftAnimation);
                 mTextSwitcher.setOutAnimation(slideOutRightAnimation);
                if (mCurrentIndex == gameModes.length - 1) {
                    mCurrentIndex = 0;
                    mTextSwitcher.setText(gameModes[mCurrentIndex]);
                } else {
                    mTextSwitcher.setText(gameModes[++mCurrentIndex]);
                }
                break;

            case R.id.gameModeLeft:
                mTextSwitcher.setInAnimation(slideInRightAnimation);
                mTextSwitcher.setOutAnimation(slideOutLeftAnimation);
                if (mCurrentIndex == 0) {
                    mCurrentIndex = gameModes.length -1;
                    mTextSwitcher.setText(gameModes[mCurrentIndex]);
                } else {
                    mTextSwitcher.setText(gameModes[--mCurrentIndex]);
                }
                break;

            //выход
            case R.id.records:
                Intent intent = new Intent();
                intent.setClass(this, RecordList.class);
                startActivity(intent);
                break;

            case R.id.settings:
                break;

            case R.id.exit:
                finish();
                break;
        }
    }





}