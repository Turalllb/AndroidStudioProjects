package example.igeniy;

import android.view.MotionEvent;

/**
 * Created by Турал on 26.02.2017.
 */

public class TouchPad {
    MotionEvent event;
    private int a = 2; // a - чувствительность (полученную от сенсора координату увеличивает и передает двигаемому объекту)
    private float initX, initY; //сперва начальная координата касания пальца, позже предыдущая координата касания, а event.getRawX() и event.getRawY() текущие координаты касания пальца

    public TouchPad (MotionEvent event){
        this.event = event;
    }



    public  void ACTION_MOVE() {
        initX = event.getRawX();
        initY = event.getRawY();
    }

}
