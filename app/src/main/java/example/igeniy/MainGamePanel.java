package example.igeniy;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;


import static example.igeniy.Menu.i;
import static example.igeniy.Menu.j;
import static example.igeniy.Menu.pauseGame;
import static example.igeniy.Time.pSecondsElapsed;

/**
 * Created by Турал on 23.11.2016.
 */

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private SoundPool sounds;
    private int sExplosion;
    private int mStreamID;
    private AssetManager mAssetManager;
    private static final String TAG = MainThread.class.getSimpleName();
    Chronometer chronometer = new Chronometer();
    private MainCharacter Character, Character1, Character2, Character3, Character4; //Экземпляры игровых персонажей
    ThreadGameMode1 threadGameMode1;
    ThreadGameMode2 threadGameMode2;
    Menu menu = new Menu();
    public static long mLastTick;
    Bitmap touchpadON, touchpadOFF, field;
    Drawable shape;
    Boolean touchDOWN = false;
    boolean Character1Flag, Character2Flag, Character3Flag, Character4Flag;
    static int GameMode = 2;



    // public static String mUserId;
    //Rect touchpad,gradientLine1,gradientLine2;

    Paint p;
    IGetDialog mGetDialog;
    private IGetImage mGetImage;
    MoveRandom moveRandom = new MoveRandom();


    final float a = 1.4f; // a - чувствительность (полученную от сенсора координату увеличивает и передает двигаемому объекту)
    final float b = 2.5f; // b - чувствительность по вертикали
    final float c = 0.62f; //c - коэффициент для регулирования   области экрана, которая управляет главным спрайтом
    final float d = 0.7f;  //d - коээфициент  установления области экрана , которая принимает касания (коснувшись в этой области включаю подсветку точпада)
    float initX, initY; //сперва начальная координата касания пальца, позже предыдущая координата касания, а event.getRawX() и event.getRawY() текущие координаты касания пальца

    public static boolean NewTimerFlag = false;

    float CharacterWidth, CharacterHeight, Character1Width, Character1Height, Character2Width, Character2Height, Character3Width, Character3Height, Character4Width, Character4Height;


    public MainGamePanel(Context context) {
        super(context);


        getHolder().addCallback(this);


        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Для устройств до Android 5
            createOldSoundPool();
        } else {
            // Для новых устройств
            createNewSoundPool();
        }

        mAssetManager = getContext().getAssets();
        // получим идентификаторы
        sExplosion = loadSound("explosion.wav");


        // Добавляем этот класс, как содержащий функцию обратного
        // вызова для взаимодействия с событиями


        //touchpad = BitmapFactory.decodeResource(getResources(), R.mipmap.touchpad);


        NewGame();


        // создаем поток для игрового цикла


        // делаем GamePanel focusable, чтобы она могла обрабатывать сообщения
        setFocusable(true);

        //sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        // sExplosion = sounds.load(context, R.raw.explosion, 1);

    }

    public MainGamePanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Для устройств до Android 5
            createOldSoundPool();
        } else {
            // Для новых устройств
            createNewSoundPool();
        }

        mAssetManager = getContext().getAssets();
        // получим идентификаторы
        sExplosion = loadSound("explosion.wav");

        getHolder().addCallback(this);


        touchpadON = BitmapFactory.decodeResource(getResources(), R.drawable.touchpadon);
        touchpadOFF = BitmapFactory.decodeResource(getResources(), R.drawable.touchpadoff);

        field = BitmapFactory.decodeResource(getResources(), R.drawable.gamefield);
       /* try {
            InputStream  ims    = mAssetManager.open("gamefield1.png");
            InputStream inputStream = this.getResources().openRawResource(R.raw.gamefield);
            field = BitmapFactory.decodeStream(inputStream);
        }
        catch (IOException ex){
            return;
        }*/

        shape = getContext().getResources().getDrawable(R.drawable.field);
        shape.setBounds(0, 0, (int) menu.getWidth(), (int) (menu.getHeight() * 0.7)); // ПЕРЕменные  получаемые через геттер СТАТИЧеские . разобраться.


        NewGame();
        setFocusable(true);
        SizeCharacter();

        /*touchpad = new Rect(0,(int)(menu.getHeight()*0.7),(int) menu.getWidth(),(int)menu.getHeight());
        p = new Paint();
        p.setColor(Color.argb(255,107, 172, 245));
        p.setStrokeWidth(1);*/

    }




    public interface IGetImage {
        void setImage(int Millisecond3, int Millisecond2, int Millisecond1,
                      int Seconds1, int Seconds2, int Minutes1, int Minutes2, String currentTime);
    }


    public void setMyInterface(IGetImage myInterface) {
        mGetImage = myInterface;
    }

    public void setInterface(IGetDialog name) {
        mGetDialog = name;
    }


    public void NewGame() {
        Character = new MainCharacter(BitmapFactory.decodeResource(getResources(), R.drawable.box_red), (int) (menu.getWidth() / 2), (int) (menu.getHeight() * 0.7 / 2), 100, 100);
        Character = new MainCharacter(BitmapFactory.decodeResource(getResources(), R.drawable.box_red), (int) ((menu.getWidth() - Character.getWidthbitmap()) / 2), (int) ((menu.getHeight() * 0.7 - Character.getHeightbitmap()) / 2), 100, 100); //Чтобы получить размер изображения экземпляра игрового персонажа, сперва он создается, а потом используя его же методы в этой строке получается размер.
        Character1 = new MainCharacter(BitmapFactory.decodeResource(getResources(), R.mipmap.face_circle_tiled), 100, 300, moveRandom.newMoveRandom(), moveRandom.newMoveRandom());
        Character2 = new MainCharacter(BitmapFactory.decodeResource(getResources(), R.mipmap.face_circle_tiled), 400, 200, moveRandom.newMoveRandom(), moveRandom.newMoveRandom());
        Character3 = new MainCharacter(BitmapFactory.decodeResource(getResources(), R.mipmap.face_circle_tiled), 300, 30, moveRandom.newMoveRandom(), moveRandom.newMoveRandom());
        Character4 = new MainCharacter(BitmapFactory.decodeResource(getResources(), R.mipmap.face_circle_tiled), 34, 30, moveRandom.newMoveRandom(), moveRandom.newMoveRandom());
    }

    public void DefaultPositions() {
        Character.setX((int) ((menu.getWidth() - Character.getWidthbitmap()) / 2));
        Character.setY((int) ((menu.getHeight() * 0.7 - Character.getHeightbitmap()) / 2));
        Character.setq(100);
        Character.setw(100);
        Character1.setX(100);
        Character1.setY(300);
        Character1.setq(moveRandom.newMoveRandom());
        Character1.setw(moveRandom.newMoveRandom());
        Character2.setX(400);
        Character2.setY(200);
        Character2.setq(moveRandom.newMoveRandom());
        Character2.setw(moveRandom.newMoveRandom());
        Character3.setX(300);
        Character3.setY(30);
        Character3.setq(moveRandom.newMoveRandom());
        Character3.setw(moveRandom.newMoveRandom());
        Character4.setX(34);
        Character4.setY(30);
        Character4.setq(moveRandom.newMoveRandom());
        Character4.setw(moveRandom.newMoveRandom());
    }


    private void SizeCharacter() {
        CharacterWidth = Character.getWidthbitmap();
        CharacterHeight = Character.getHeightbitmap();
        Character1Width = Character1.getWidthbitmap();
        Character1Height = Character1.getHeightbitmap();
        Character2Width = Character2.getWidthbitmap();
        Character2Height = Character2.getHeightbitmap();
        Character3Width = Character3.getWidthbitmap();
        Character3Height = Character3.getHeightbitmap();
        Character4Width = Character4.getWidthbitmap();
        Character4Height = Character4.getHeightbitmap();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
        mGetDialog.ReadRecords(); // не лучшая реализация . Для того чтобы использовать метод addTime приходится создавать новый экземпляр РекордАдаптера, и статическая переменная которая хранит рекорды создается
        //по новой и она пустая. Здесь происходит повторное считывае из документа рекордов. Если рекордов много и считывание тормозит загрузку игрового поля, придется изменить реализацию.
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mLastTick = System.nanoTime();// время от которого отсчитывается   время на Update игровой ситуации
        Log.d(TAG, "surfaceCreated");
        switch (GameMode) {
            case 1:
                threadGameMode1 = new ThreadGameMode1(getHolder(), this);
                threadGameMode1.setRunning(true);
                threadGameMode1.start();
                return;
            case 2:
                threadGameMode2 = new ThreadGameMode2(getHolder(), this);
                threadGameMode2.setRunning(true);
                threadGameMode2.start();
        }

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        switch (GameMode) {
            case 1:
                destroy(threadGameMode1);
                return;
            case 2:
                destroy(threadGameMode2);
        }
    }

    public synchronized void destroy(MainThread thread) {

        if (thread == null) {
            return;
        }
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException ignored) {
            }
        }
        thread = null; // Эта строчка в данный момент лишняя. На заметку: Если переменная tread обявлена как переменная класса, то я должен выполнить эту строчку кода. Иначе , т.е. как есть сейчас, студия пишет, что tread нигде не используется, потому что после выхода из метода, ссылка на этот объект итак исчезнет и сборщик мусора уничтожит объект. А если переменная объявлена в классе, то её за ненадобностью надо присвоить к null.
        Log.d(TAG, "surfaceDestroyed");

        i = 1;
        j = 1; //обновляем переменные только после того , как поток умер, иначе если сделать это в onPause, то в это время поток еще жив и изменит их
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pauseGame = false;
                switch (GameMode) {
                    case 1:
                        threadGameMode1.TimeAfterPause();
                        threadGameMode1.NewTimer();
                    case 2:
                        threadGameMode2.TimeAfterPause();
                        threadGameMode2.NewTimer();
                }

                initX = event.getRawX();
                initY = event.getRawY();
                if (event.getRawY() > menu.getHeight() * d) {
                    touchDOWN = true;         //Флаг для определения касания точпада
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (pauseGame)
                    return true; //чтобы после постановки игры на паузу, не убранный с экрана палец не продолжал изменять координаты

                if (event.getRawY() > menu.getHeight() * c) {
                    touchDOWN = true;
                    if ((Math.abs(event.getRawX() - initX)) < 150 && (Math.abs(event.getRawY() - initY)) < 150) { // условие, которое проверяет что расстояние между прошлой и текущей координатой не слишком большое, которое получается, если поставить два пальца и потом один убрать.

                        if ((Character.getX() + a * (event.getRawX() - initX) > 0) && (Character.getX() + a * (event.getRawX() - initX)) + Character.getWidthbitmap() < menu.getWidth()) {
                            Character.setX(Character.getX() + a * (event.getRawX() - initX));
                            initX = event.getRawX();
                        } else {
                            if ((Character.getX() + a * (event.getRawX() - initX)) + Character.getWidthbitmap() >= menu.getWidth()) {
                                Character.setX(menu.getWidth() - Character.getWidthbitmap());
                            } else {
                                Character.setX(0);
                            }
                            initX = event.getRawX();
                        }

                        if ((Character.getY() + b * (event.getRawY() - initY)) > 0 && Character.getY() + b * (event.getRawY() - initY) + Character.getWidthbitmap() < menu.getHeight() * 0.7) {
                            Character.setY(Character.getY() + b * (event.getRawY() - initY));
                            initY = event.getRawY();
                        } else {
                            if ((Character.getY() + b * (event.getRawY() - initY)) <= 0) {
                                Character.setY(0);
                            } else {
                                Character.setY(menu.getHeight() * 0.7F - Character.getHeightbitmap());
                            }
                            initY = event.getRawY();
                        }
                    }
                }
                if (event.getRawY() < menu.getHeight() * d) {
                    touchDOWN = false;
                }
                initY = event.getRawY();
                initX = event.getRawX();
                return true;
            case MotionEvent.ACTION_UP:
                touchDOWN = false;
        }
        return false;
    }

    protected void Draw(Canvas canvas) {

        // Заливаем canvas белым цветом
        //canvas.drawColor(Color.rgb(154, 207, 204));// PorterDuff.Mode.CLEAR - используется, чтобы ничего не отображать подробнее в PorterDuff режимы
        // Вызываем метод, который выводит рисунок главного персонажа
        //canvas.drawBitmap(touchpad, 0, menu.getHeight() * 0.7F, null);
        //canvas.drawRect(touchpad,p);

        canvas.drawBitmap(field, 0, 0, null);
        if (touchDOWN) {
            canvas.drawBitmap(touchpadON, 0, menu.getHeight() * 0.7F, null);
        } else canvas.drawBitmap(touchpadOFF, 0, menu.getHeight() * 0.7F, null);

        Character1.draw(canvas);
        Character2.draw(canvas);
        Character3.draw(canvas);
        Character4.draw(canvas);
        Character.draw(canvas);
        mGetImage.setImage(chronometer.getMillisecond3(), chronometer.getMillisecond2(), chronometer.getMillisecond1(),
                chronometer.getSeconds1(), chronometer.getSeconds2(), chronometer.getMinutes1(), chronometer.getMinutes2(), chronometer.getCurrentTime());
    }


    public void update() {
        Character1.update();
        Character2.update();
        Character3.update();
        Character4.update();
       /* if (Character1.getq() > 0 ){
            System.out.println("Character1  плюс");
        }else{
            System.out.println("Character1  минус");
        }
        if (Character2.getq() > 0 ){
            System.out.println("Character2  плюс");
        }else{
            System.out.println("Character2  минус");
        }
        if (Character3.getq() > 0 ){
            System.out.println("Character3  плюс");
        }else{
            System.out.println("Character3  минус");
        }
        if (Character4.getq() > 0 ){
            System.out.println("Character4  плюс");
        }else{
            System.out.println("Character4  минус");
        }*/
    }

    public void StopWatch() {
        chronometer.StopWatch();
    }

    public void collision() {

        if (Character.getX() + CharacterWidth > Character1.getX() && Character.getX() < Character1.getX() + Character1Width && Character.getY() + CharacterHeight > Character1.getY() && Character.getY() < Character1.getY() + Character1Height ||
                Character.getX() + CharacterWidth > Character2.getX() && Character.getX() < Character2.getX() + Character2Width && Character.getY() + CharacterHeight > Character2.getY() && Character.getY() < Character2.getY() + Character2Height ||
                Character.getX() + CharacterWidth > Character3.getX() && Character.getX() < Character3.getX() + Character3Width && Character.getY() + CharacterHeight > Character3.getY() && Character.getY() < Character3.getY() + Character3Height ||
                Character.getX() + CharacterWidth > Character4.getX() && Character.getX() < Character4.getX() + Character4Width && Character.getY() + CharacterHeight > Character4.getY() && Character.getY() < Character4.getY() + Character4Height) {

            CollisionTrue();

            //sounds.play(sExplosion, 1.0f, 1.0f, 0, 0, 1.5f);// для определения пересенчения прямоугольников использована теорема о разделяющих осях. Она гласит: если проекции фигур не нагладываются хотя бы по одной из осей, то они не пересекаются.
        }

    }

    public void collisionMode() {
        Character1Flag = Character2Flag = Character3Flag = Character4Flag = false;
        if (Character1.getX() + Character1Width > Character2.getX() && Character1.getX() < Character2.getX() + Character2Width && Character1.getY() + Character1Height > Character2.getY() && Character1.getY() < Character2.getY() + Character2Height) {
            Character1Flag = true;
            Character2Flag = true;
            maxCollision(Character1, Character2);

        }
        if (Character1.getX() + Character1Width > Character3.getX() && Character1.getX() < Character3.getX() + Character3Width && Character1.getY() + Character1Height > Character3.getY() && Character1.getY() < Character3.getY() + Character3Height) {
            Character1Flag = true;
            Character3Flag = true;
            maxCollision(Character1, Character3);
        }
        if (Character1.getX() + Character1Width > Character4.getX() && Character1.getX() < Character4.getX() + Character4Width && Character1.getY() + Character4Height > Character4.getY() && Character1.getY() < Character4.getY() + Character4Height) {
            Character1Flag = true;
            Character4Flag = true;
            maxCollision(Character1, Character4);
        }
        if (Character2.getX() + Character2Width > Character3.getX() && Character2.getX() < Character3.getX() + Character3Width && Character2.getY() + Character2Height > Character3.getY() && Character2.getY() < Character3.getY() + Character3Height) {
            Character2Flag = true;
            Character3Flag = true;
            maxCollision(Character2, Character3);
        }
        if (Character2.getX() + Character2Width > Character4.getX() && Character2.getX() < Character4.getX() + Character4Width && Character2.getY() + Character4Height > Character4.getY() && Character2.getY() < Character4.getY() + Character4Height) {
            Character2Flag = true;
            Character4Flag = true;
            maxCollision(Character2, Character4);
        }
        if (Character3.getX() + Character3Width > Character4.getX() && Character3.getX() < Character4.getX() + Character4Width && Character3.getY() + Character4Height > Character4.getY() && Character3.getY() < Character4.getY() + Character4Height) {
            Character3Flag = true;
            Character4Flag = true;
            maxCollision(Character3, Character4);
        }

        collision();

        collisionBuffer();

    }

    public void collisionBuffer() {
        if (Character1Flag) {
            Character1.setq(-Character1.getq());
            Character1.setw(-Character1.getw());
        }
        if (Character2Flag) {
            Character2.setq(-Character2.getq());
            Character2.setw(-Character2.getw());
        }
        if (Character3Flag) {
            Character3.setq(-Character3.getq());
            Character3.setw(-Character3.getw());
        }
        if (Character4Flag) {
            Character4.setq(-Character4.getq());
            Character4.setw(-Character4.getw());
        }
    }

    public void CollisionTrue() {
        playSound(sExplosion);
        pauseGame = true;

        mGetDialog.addTime(chronometer.getTimeToCompareInt(), chronometer.getCurrentTime());
        mGetDialog.WriteRecords();  //запись рекордов из переменной в документ можно производить на выходе из игры
        mGetDialog.showDialog(chronometer.getCurrentTime());
        chronometer.resetStopwatch();
        NewTimerFlag = true;

    }

    public void maxCollision(MainCharacter mainCharacter1, MainCharacter mainCharacter2) {
        float w1 = (mainCharacter1.getw()) * pSecondsElapsed / 2f;
        float q1 = (mainCharacter1.getq()) * pSecondsElapsed / 2f;
        float w2 = (mainCharacter2.getw()) * pSecondsElapsed / 2f;
        float q2 = (mainCharacter2.getq()) * pSecondsElapsed / 2f;
        float x1 = mainCharacter1.getX();
        float y1 = mainCharacter1.getY();
        float x2 = mainCharacter2.getX();
        float y2 = mainCharacter2.getY();
        mainCharacter1.setX(x1 - q1);
        mainCharacter1.setY(y1 - w1);
        mainCharacter2.setX(x2 - q2);
        mainCharacter2.setY(y2 - w2);
        if (mainCharacter1.getX() + mainCharacter1.getWidthbitmap() > mainCharacter2.getX() && mainCharacter1.getX() < mainCharacter2.getX() + mainCharacter2.getWidthbitmap() && mainCharacter1.getY() + mainCharacter1.getHeightbitmap() > mainCharacter1.getY() && mainCharacter1.getY() < mainCharacter2.getY() + mainCharacter2.getHeightbitmap()) {
            /*float q = (mainCharacter1.getq()- 0.1f) * pSecondsElapsed / 1.5f;
            mainCharacter1.setX(x1 - q);
            mainCharacter1.setY(y1 - q);
            mainCharacter2.setX(x2 - q);
            mainCharacter2.setY(y2 - q);
            if (!(mainCharacter1.getX() + mainCharacter1.getWidthbitmap() > mainCharacter2.getX() && mainCharacter1.getX() < mainCharacter2.getX() + mainCharacter2.getWidthbitmap() && mainCharacter1.getY() + mainCharacter1.getHeightbitmap() > mainCharacter1.getY() && mainCharacter1.getY() < mainCharacter2.getY() + mainCharacter2.getHeightbitmap())) {
                mainCharacter1.setX(x1 - w);
                mainCharacter1.setY(y1 - w);
                mainCharacter2.setX(x2 - w);
                mainCharacter2.setY(y2 - w);
            }*/

        } else {
           /* float n1 = (mainCharacter1.getw()) * pSecondsElapsed / 2f;
            float v1 = (mainCharacter1.getq()) * pSecondsElapsed / 2f;
            float n2 = (mainCharacter2.getw()) * pSecondsElapsed / 2f;
            float v2 = (mainCharacter2.getq()) * pSecondsElapsed / 2f;
            mainCharacter1.setX(x1 - n1);
            mainCharacter1.setY(y1 - v1);
            mainCharacter2.setX(x2 - n2);
            mainCharacter2.setY(y2 - v2);*/
           /* if (mainCharacter1.getX() + mainCharacter1.getWidthbitmap() > mainCharacter2.getX() && mainCharacter1.getX() < mainCharacter2.getX() + mainCharacter2.getWidthbitmap() && mainCharacter1.getY() + mainCharacter1.getHeightbitmap() > mainCharacter1.getY() && mainCharacter1.getY() < mainCharacter2.getY() + mainCharacter2.getHeightbitmap()){
                float m = (mainCharacter1.getq() - 0.1f) * pSecondsElapsed / 3;
                mainCharacter1.setX(x1 - m);
                mainCharacter1.setY(y1 - m);
                mainCharacter2.setX(x2 - m);
                mainCharacter2.setY(y2 - m);
                if (!(mainCharacter1.getX() + mainCharacter1.getWidthbitmap() > mainCharacter2.getX() && mainCharacter1.getX() < mainCharacter2.getX() + mainCharacter2.getWidthbitmap() && mainCharacter1.getY() + mainCharacter1.getHeightbitmap() > mainCharacter1.getY() && mainCharacter1.getY() < mainCharacter2.getY() + mainCharacter2.getHeightbitmap())){
                    mainCharacter1.setX(x1 - n);
                    mainCharacter1.setY(y1 - n);
                    mainCharacter2.setX(x2 - n);
                    mainCharacter2.setY(y2 - n);
                }

            }*/

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sounds = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    private void createOldSoundPool() {
        sounds = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
    }

    private int playSound(int sound) {
        if (sound > 0) {
            mStreamID = sounds.play(sound, 1, 1, 1, 0, 1);
        }
        return mStreamID;
    }

    private int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Не могу загрузить файл " + fileName,
                    Toast.LENGTH_SHORT).show();
            return -1;
        }
        return sounds.load(afd, 1);
    }


}