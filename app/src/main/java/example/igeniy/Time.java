package example.igeniy;


public  class  Time {
    MainGamePanel mainGamePanel;
   // private long mLastTick;
    private float mSecondsElapsedTotal;
    static  float pSecondsElapsed; // разобраться почему без статик не работает




    void onTickUpdate()  {

        final long secondsElapsed = this.getNanosecondsElapsed();
        this.onUpdate(secondsElapsed);
    }

    private long getNanosecondsElapsed() {
        final long now = System.nanoTime();

        return now - mainGamePanel.mLastTick; //this.mLastTick;
    }

    private void onUpdate(final long pNanosecondsElapsed)  {
         this.pSecondsElapsed = pNanosecondsElapsed * 0.000000001f;

       // this.mSecondsElapsedTotal += pSecondsElapsed;
        mainGamePanel.mLastTick += pNanosecondsElapsed;
    }

}