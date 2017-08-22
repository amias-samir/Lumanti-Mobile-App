package np.com.naxa.lumanti.application;

import android.content.Context;

import com.orm.SugarApp;

/**
 * Created by Samir on 8/21/2017.
 */

public class Lumanti extends SugarApp {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        setupContext();
    }

    private void setupContext(){
        context = getApplicationContext();
    }

    public static Context getInstance() {
        return context;
    }
}

