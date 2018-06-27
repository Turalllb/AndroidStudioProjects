package example.igeniy;


import java.util.Date;

import static example.igeniy.MainThread.start;

/**
 * Created by Турал on 05.01.2017.
 */


class Chronometer {
    //Класс осуществляет перевод времени полученного в миллисекундах в минуты, секунды и миллисекунды держа каждое число в отдельной переменной.
    private int millisecond1 = 0, millisecond2 = 0, millisecond3 = 0, seconds1 = 0, seconds2 = 0, minutes1 = 0, minutes2 = 0, TimeToCompareInt;
    private String currentTime, TimeToCompare;


    void StopWatch() {
        double x, x1, x2; // промежуточные переменные используемые только для расчетов
        long now = System.currentTimeMillis() - start;

        String temp = Long.toString(now);
        int length = temp.length();
        int[] digits = new int[length];
        for (int i = 0; i < length; i++) {
            digits[i] = temp.charAt(length - i - 1) - '0';
        }
        millisecond1 = digits[0];
        currentTime = "0.00" + millisecond1;
        TimeToCompare = "" + millisecond1;
        TimeToCompareInt = Integer.parseInt(TimeToCompare);
        switch (length) {
            case 2:
                millisecond2 = digits[1];
                currentTime = "0.0" + millisecond2 + millisecond1;
                TimeToCompare = "" + millisecond2 + millisecond1;
                TimeToCompareInt = Integer.parseInt(TimeToCompare);
                break;
            case 3:
                millisecond2 = digits[1];
                millisecond3 = digits[2];
                currentTime = "0." + millisecond3 + millisecond2 + millisecond1;
                TimeToCompare = "" + millisecond3 + millisecond2 + millisecond1;
                TimeToCompareInt = Integer.parseInt(TimeToCompare);
                break;
            case 4:
                millisecond2 = digits[1];
                millisecond3 = digits[2];
                seconds1 = digits[3];
                currentTime = seconds1 + "." + millisecond3 + millisecond2 + millisecond1;
                TimeToCompare = "" + seconds1 + millisecond3 + millisecond2 + millisecond1;
                TimeToCompareInt = Integer.parseInt(TimeToCompare);
                break;
            case 5:
                millisecond2 = digits[1];
                millisecond3 = digits[2];
                x = (now / 60000.0);
                minutes1 = (int) Math.floor(now / 60000);
                x1 = (x - minutes1) * 6;
                seconds2 = (int) Math.floor(x1);
                seconds1 = (int) Math.floor((x1 - seconds2) * 10);
                if (x >= 1) {
                    currentTime = minutes1 + "." + seconds2 + seconds1 + "." + millisecond3 + millisecond2 + millisecond1;
                } else {
                    currentTime = "" + seconds2 + seconds1 + "." + millisecond3 + millisecond2 + millisecond1;
                }
                TimeToCompare = "" + seconds2 + seconds1 + millisecond3 + millisecond2 + millisecond1;
                TimeToCompareInt = Integer.parseInt(TimeToCompare);
                break;
            case 6:
                millisecond2 = digits[1];
                millisecond3 = digits[2];
                x = (now / 600000.0);
                minutes2 = (int) Math.floor(now / 600000);
                x1 = (x - minutes2) * 10;
                minutes1 = (int) Math.floor(x1);
                x2 = (x1 - minutes1) * 6;
                seconds2 = (int) Math.floor(x2);
                seconds1 = (int) Math.floor((x2 - seconds2) * 10);
                if (x >= 1) {
                    currentTime = minutes1 + "." + seconds2 + seconds1 + "." + millisecond3 + millisecond2 + millisecond1;
                } else {
                    currentTime = "" + minutes2 + minutes1 + "." + seconds2 + seconds1 + "." + millisecond3 + millisecond2 + millisecond1;
                }
                TimeToCompareInt = Integer.parseInt(TimeToCompare);
                break;
            case 7:
                millisecond2 = digits[1];
                millisecond3 = digits[2];
                x = (now / 600000.0);
                minutes2 = (int) Math.floor(now / 600000);
                x1 = (x - minutes2) * 10;
                minutes1 = (int) Math.floor(x1);
                x2 = (x1 - minutes1) * 6;
                seconds2 = (int) Math.floor(x2);
                seconds1 = (int) Math.floor((x2 - seconds2) * 10);
                currentTime = "" + minutes2 + minutes1 + "." + seconds2 + seconds1 + "." + millisecond3 + millisecond2 + millisecond1;
                TimeToCompare = "" + minutes2 + minutes1 + seconds2 + seconds1 + millisecond3 + millisecond2 + millisecond1;
                TimeToCompareInt = Integer.parseInt(TimeToCompare);
                break;

            //digits[i] = temp.charAt(i) - '0';  Если требуется читать строку с другого конца
        }
    }

    void resetStopwatch() {
        millisecond1 = 0;
        millisecond2 = 0;
        millisecond3 = 0;
        seconds1 = 0;
        seconds2 = 0;
        minutes1 = 0;
        minutes2 = 0;
        currentTime = "";
    }


    String getCurrentTime() {
        return currentTime;
    }

    Integer getTimeToCompareInt() {
        return TimeToCompareInt;
    }

    int getMillisecond1() {
        return millisecond1;
    }

    int getMillisecond2() {
        return millisecond2;
    }

    int getMillisecond3() {
        return millisecond3;
    }

    int getSeconds1() {
        return seconds1;
    }

    int getSeconds2() {
        return seconds2;
    }

    int getMinutes1() {
        return minutes1;
    }

    int getMinutes2() {
        return minutes2;
    }
}
