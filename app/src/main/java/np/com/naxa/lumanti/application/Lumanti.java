package np.com.naxa.lumanti.application;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.orm.SugarApp;

import java.io.File;

/**
 * Created by Samir on 8/21/2017.
 */

public class Lumanti extends SugarApp {


    public static String mainFolder = "/Lumanti";
    public static String photoFolder = "/Photos";
    public static String dataFolder = "/Data";
    public static String extSdcard = Environment.getExternalStorageDirectory().toString();

    public static String PHOTO_PATH = extSdcard + mainFolder + photoFolder;

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        setupContext();
        Stetho.initializeWithDefaults(this);
    }

    private void setupContext(){
        context = getApplicationContext();
    }

    public static Context getInstance() {
        return context;
    }

    public static void createFolder() throws Exception {

        Log.i("MapboxApplication", "Trying to create required folders");

        File dirPhoto = new File(extSdcard + mainFolder + photoFolder);
        File dirData = new File(extSdcard + mainFolder + dataFolder);


        if(!dirPhoto.exists()){
            if (!dirPhoto.mkdirs()){
                throw new Exception("Failed to create photo folder");
            }
        }


        if(!dirData.exists()){
            if (!dirData.mkdirs()){
                throw new Exception("Failed to create database folder");
            }
        }

    }
}

