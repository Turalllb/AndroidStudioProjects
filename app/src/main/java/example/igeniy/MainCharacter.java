package example.igeniy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import static example.igeniy.Time.pSecondsElapsed;

/**
 * Created by Турал on 25.11.2016.
 */

class MainCharacter {

    private Bitmap bitmap;// картинка с роботом
    private float x;// координата X
    private float y;// координата Y
    private boolean touched;// переменная состояния
    private float q, w; // скорость по координатам X и Y
    private  float a = 1f ;   //ускорение
    private Menu menu = new Menu();



    MainCharacter(Bitmap bitmap, float x, float y, float q, float w) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.q = q;
        this.w = w;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    float getX() {
        return x;
    }

    void setX(float x) {
        this.x = x;
    }

    float getY() {
        return y;
    }

    void setY(float y) {
        this.y = y;
    }

    void setq(float q) {
        this.q = q;
    }

    void setw(float w) {
        this.w = w;
    }

    float getq() {
        return q;
    }

    float getw( ) {
        return w;
    }


    boolean isTouched() {
        return touched;
    }

    void setTouched(boolean touched) {
        this.touched = touched;
    }

    float getWidthbitmap() {              //понять как работает
        return bitmap.getWidth();
    }

    float getHeightbitmap() {
        return bitmap.getHeight();
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void handleActionDown(int eventX, int eventY) {
    }

    void update() {
       // System.out.println(pSecondsElapsed  );


        //условия для добавления ускорения



        if (this.q > 0) {
            this.q += a;
        } else {
            this.q -= a;
        }

        if (this.w > 0) {
            this.w += a;
        } else {
            this.w -= a;
        }

        this.x += this.q * pSecondsElapsed;
        this.y += this.w * pSecondsElapsed;

        //Условия при которых объект меняет скорость на противоположную
        if (this.x < 0) {
            this.x = 0;   // при умножении на pSecondsElapsed (кадров в секунду или время нарисования одного кадра ) возникает проблема из-за того , что эта величина не постоянная.
            // Если  Mx<0,когда скорость должна измениться  на противоположную ,pSecondsElapsed окажется меньше ,чем был  в прошлом цикле,когда Mx стал <0, то этого значения q*pSecondsElapsed не хватит чтобы Mx<0
            // (Но знак скорости изменился) при повторном проходе цикла знак скосроти еще раз изменится и объект снова попадет за границу.
            this.q *= -1;
        } else if (this.x + getWidthbitmap() > menu.getWidth()) {
            this.x = menu.getWidth() - getWidthbitmap();
            this.q *= -1;
        }
        if (this.y < 0) {
            this.y = 0;
            this.w *= -1;
        } else if (this.y + getHeightbitmap() > menu.getHeight() * 0.7) {
            this.y = menu.getHeight() * 0.7F - getHeightbitmap();
            this.w *= -1;
        }



    }

}


