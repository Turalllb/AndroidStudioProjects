package example.igeniy;

import android.media.MediaPlayer;

public class MediaPlayerSingleton implements MediaPlayer.OnCompletionListener {

    private static MediaPlayerSingleton mpSingleton;
    private MediaPlayer mediaPlayer;

    private MediaPlayerSingleton() {
    }

    static MediaPlayerSingleton getInstance() {
        if (mpSingleton == null) mpSingleton = new MediaPlayerSingleton();
        return mpSingleton;
    }


    void play() {
        mediaPlayer = MediaPlayer.create(MyApplication.getAppContext(), R.raw.melodiya);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        releaseMP();
        mediaPlayer = MediaPlayer.create(MyApplication.getAppContext(), R.raw.acdc);
        mediaPlayer.start();
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
