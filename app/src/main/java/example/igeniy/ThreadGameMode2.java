package example.igeniy;

import android.view.SurfaceHolder;

import static example.igeniy.GameOverFragment.CloseDialog;
import static example.igeniy.Menu.pauseGame;

/**
 * Created by Турал on 06.07.2017.
 */

public class ThreadGameMode2 extends MainThread  {

    ThreadGameMode2 (SurfaceHolder surfaceHolder, MainGamePanel gamePanel){
        super(surfaceHolder,gamePanel);

    }


    public  void GameMode(){
        gamePanel.collisionMode();
    }

    public void IncreasedCollisionDetection() {
        for (int i = 1; !pauseGame && i < 10; i++){
            this.time.onTickUpdate();
            this.gamePanel.update();
            this.gamePanel.StopWatch();
            GameMode();
        }
    }
}
