package example.igeniy;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static example.igeniy.GameOverFragment.newRecord;
import static example.igeniy.Сryptographer.decrypt;
import static example.igeniy.Сryptographer.encrypt;

/**
 * Created by Турал on 22.04.2017.
 */

public class RecordAdapter {

    // Название файла в котором хранятся данные
    private static String FILE_RECORDS = "memoria-records";
    Context mContext;
    static ArrayList<Integer> TimeRecord;
    static ArrayList<String>  TimeRecordStr;



    RecordAdapter(Context context) {
        mContext = context;
        TimeRecord = new ArrayList<Integer>();
        TimeRecordStr = new ArrayList<String>();
    }


     void readRecords() {
        // читаем из файла два массива с рекордами
        String DecryptStr;
         TimeRecord.clear();
         TimeRecordStr.clear();
        try {
            FileInputStream fis = mContext.openFileInput(FILE_RECORDS);
            ObjectInputStream is = new ObjectInputStream(fis);
            String recordRead = (String) is.readObject();
            is.close();
            DecryptStr = decrypt(recordRead);
            String[] strings = DecryptStr.split(", ");
            for (int i = 0; i < strings.length; i++) {
                try {
                    //region добавляет в каждую строку точки, чтобы перевести в формат времени   . Не работает
                    switch (strings[i].length()){
                        case 3:
                            TimeRecordStr.add(strings[i]);
                            break;
                        case 4:
                            TimeRecordStr.add(new StringBuffer(strings[i]).insert(1,".").toString());
                            break;
                        case 5:
                            TimeRecordStr.add(new StringBuffer(strings[i]).insert(2,".").toString());
                            break;
                        case 6:
                            TimeRecordStr.add(new StringBuffer(strings[i]).insert(1,".").insert(4,".").toString());
                            //TimeRecordStr.add(new StringBuffer(strings[i]).insert(4,".").toString());
                            break;
                        case 7:
                            TimeRecordStr.add(new StringBuffer(strings[i]).insert(2,".").insert(5,".").toString());
                           // TimeRecordStr.add(new StringBuffer(strings[i]).insert(5,".").toString());
                            break;
                    }

                    //endregion
                    TimeRecord.add(Integer.valueOf(strings[i]));
                } catch (NumberFormatException exc) {
                }
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "Произошла ошибка чтения таблицы рекордов или это Ваша первая игра", Toast.LENGTH_LONG).show();
        }
    }


    void WriteRecords() {
        StringBuilder record = new StringBuilder();

        for (int i = 0; i < TimeRecord.size(); i++) {          // Перевод ArrayList<Integer> в строку
            if (i != 0)
                record.append(", ");
            record.append(TimeRecord.get(i).toString());
        }
        String recordWrite = record.toString();
        try {
            OutputStream fos = mContext.openFileOutput(FILE_RECORDS, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(encrypt(recordWrite));
            os.close();
        } catch (Exception e) {
            //Toast.makeText(mContext, "Произошла ошибка записи в таблицу рекордов", Toast.LENGTH_LONG).show(); // не будет работать , так как вызывается не из главного потока
        }
    }


    void addTime(Integer TimeToCompareInt,String CurrentTime) {

        try {
            if (TimeRecord.get(0) < TimeToCompareInt)
                newRecord = true;
            else newRecord = false;
        } catch (Exception e) {
            newRecord = true;
        }


        if (!TimeRecord.contains(TimeToCompareInt)) {
            TimeRecord.add(TimeToCompareInt);
        }

        Collections.sort(TimeRecord, Collections.reverseOrder());  // Сортирую по убыванию

        for (int i = 5; i < TimeRecord.size(); i++) {  //Оставляю в листе только определенное колличество  значений
            TimeRecord.remove(i);
        }
    }
}
