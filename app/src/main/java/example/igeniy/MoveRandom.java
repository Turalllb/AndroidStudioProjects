package example.igeniy;


import java.util.Random;



/**
 * Created by Турал on 14.03.2017.
 */

public class MoveRandom {
    private int[] moveArray = new int[2];
    private Random random = new Random();
    private  int x;

    MoveRandom() {
        moveArray[0] = -100;
        moveArray[1] = 100;
    }

    int newMoveRandom() {
        x = moveArray[random.nextInt(2)];
        return x;
    }


}
