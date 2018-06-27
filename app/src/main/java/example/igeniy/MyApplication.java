package example.igeniy;

import android.app.Application;
import android.content.Context;

/**
 * Created by Турал on 24.06.2017.
 */

public class MyApplication extends Application {

    private static MyApplication appContext;

    static public Context getAppContext() {
        return appContext;
    }

    public void onCreate() {
        super.onCreate();
        appContext = this;
    }
}
