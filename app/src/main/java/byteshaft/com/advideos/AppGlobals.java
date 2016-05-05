package byteshaft.com.advideos;

import android.app.Application;
import android.content.Context;


public class AppGlobals extends Application {

    private static Context sContext;
    public static final String FOLDER = "Videos";
    public static final String PASSWORD_STATUS = "password_status";
    public static final String PASSWORD = "password";
    public static String PATH = "";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
