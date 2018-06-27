package example.igeniy;

/**
 * Created by Турал on 08.01.2017.
 */

public interface IGetDialog {

    void showDialog(String currentTime);
    void ReadRecords();
    void WriteRecords();
    void addTime(Integer TimeToCompareInt, String CurrentTime);
    GameOverFragment GetFragment();

}
