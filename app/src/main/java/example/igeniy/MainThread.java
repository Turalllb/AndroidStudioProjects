package example.igeniy;

import android.graphics.Canvas;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.SurfaceHolder;

import static example.igeniy.GameOverFragment.CloseDialog;
import static example.igeniy.MainGamePanel.NewTimerFlag;
import static example.igeniy.Menu.i;
import static example.igeniy.Menu.j;
import static example.igeniy.Menu.pauseGame;

/**
 * Created by Турал on 23.11.2016.
 */

abstract class MainThread extends Thread implements Callback {

    private static final String TAG = MainThread.class.getSimpleName();
    // желательный fps
    private final static int MAX_FPS = 60;
    // максимальное число кадров, которые можно пропустить
    private final static int MAX_FRAME_SKIPS = 5;
    // период, который занимает кадр(последовательность обновление-рисование)
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;
    SurfaceHolder surfaceHolder;
    MainGamePanel gamePanel;
    Time time = new Time();
    private boolean running;//флаг, указывающий на то, что игра запущена.
    static long start; //Время старта игровой ситуации
    static long TimePause; //Время, когда игра поставлена на паузу
    Canvas canvas;
    Menu menu;


    void setRunning(boolean running) {
        this.running = running;
    }

    public boolean getRunning() {
        return running;
    }

    MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
        LastFrame();
        gamePanel.mGetDialog.GetFragment().registerCallBack(this);
    }


    //region после закрытия диалога ,нарисовать  кадр новой игры.
    @Override
    public void callingBack() {
        while (CloseDialog) {
            canvas = null;
            try {

                canvas = this.surfaceHolder.lockCanvas(null);
                if (canvas == null)
                    continue;
                synchronized (surfaceHolder) {
                    this.gamePanel.DefaultPositions();
                    this.gamePanel.Draw(canvas);
                }

            } finally {
                // в случае ошибки, плоскость не перешла в
                //требуемое состояние
                if (canvas != null) {
                    CloseDialog = false;
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    //endregion

    // region После разворачивания свернутого приложения, пока игра стоит на паузе ,нарисовать не черный экран, а последний кадр игры
    private void LastFrame() {
        while (i < 2) { // Цикл в данном случае нужен только для того. чтобы  оператор continue запустил заново цикл, до тех пор пока canvas не станет null
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas(null);
                if (canvas == null)
                    continue;
                synchronized (surfaceHolder) {
                    this.gamePanel.Draw(canvas);
                }

            } finally {
                // в случае ошибки, плоскость не перешла в
                //требуемое состояние
                if (canvas != null) {
                    i++;
                    surfaceHolder.unlockCanvasAndPost(canvas);

                }
            }
        }

    } //endregion


    //region Условия, которые контроллируют правильное отображение времени на игровом секундомере
    void TimeAfterPause() {
        if (j < 2 && !pauseGame) {  //Выполняется один раз сразу после снятия с паузы игровой ситуации
            start = System.currentTimeMillis() - TimePause + start;
            j++;
        }
    }

    void NewTimer() {
        if (NewTimerFlag) {  //Выполняется один раз после OnDestroy,сразу после снятия с паузы игровой ситуации
            start = System.currentTimeMillis();
            NewTimerFlag = false;
        }
    }


    //endregion

    @Override
    public void run() {

        long tickCount = 0L;
        Log.d(TAG, "Starting game loop");

        long beginTime;// время начала цикла
        long timeDiff;// время выполнения шага цикла
        int sleepTime;// сколько мс можно спать (<0 если выполнение опаздывает)
        int framesSkipped;// число кадров у которых не выполнялась операция вывода графики на экран


        while (running) {
            beginTime = System.currentTimeMillis();
            this.time.onTickUpdate();

            if (pauseGame) {  //Выполняется пока игровая ситуация на паузе
                continue;
            }

            tickCount++;
            canvas = null;
            // пытаемся заблокировать canvas
            // для изменения картинки на поверхности
            try {
                canvas = this.surfaceHolder.lockCanvas(null);
                if (canvas == null)
                    continue;

                synchronized (surfaceHolder) {


                    framesSkipped = 0;// обнуляем счетчик пропущенных кадров


                    this.gamePanel.update();// формируем новый кадр
                    this.gamePanel.StopWatch();
                    GameMode();
                    //IncreasedCollisionDetection();
                    this.gamePanel.Draw(canvas);//Вызываем метод для рисования



/*                    //region Код для установки верхнего предела FPS
                    // вычисляем время, которое прошло с момента запуска цикла
                    timeDiff = System.currentTimeMillis() - beginTime;

                    sleepTime = (int) (FRAME_PERIOD - timeDiff); // вычисляем время, которое можно спать

                    if (sleepTime > 0) { // если sleepTime > 0 все хорошо, мы идем с опережением
                        try {
                            // отправляем поток в сон на период sleepTime
                            // такой ход экономит к тому же заряд батареи
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                    }
                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                        this.gamePanel.update();
                        sleepTime += FRAME_PERIOD;
                        framesSkipped++;
                    }
                    //endregion*/
                }
            } finally {
                // в случае ошибки, плоскость не перешла в
                //требуемое состояние
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            long x = System.currentTimeMillis() - beginTime;
            long x1 = 1000 / x;
            System.out.println("FPS= " + x1);    // ПОследнее FPS  заметно меньше и вообще почему оно появляется.

        }
        //рисуем GameOver
        Log.d(TAG, "Game loop executed " + tickCount + " times");
    }

    public abstract void GameMode();

    public abstract void IncreasedCollisionDetection(); //Метод должен увеличивать вероятность обнаружения коллизий


}
