package np.com.naxa.lumanti.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import np.com.naxa.lumanti.BaseActivity;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.application.Lumanti;
import np.com.naxa.lumanti.model.Default_DIalog;


/**
 * Created by Samir on 3/17/2017.
 */
public class SplashScreenActivity extends BaseActivity {


    private static final String TAG = "SplashScreenActivity" ;
    private static final int PERMISSION_WRITE = 5654556;


    TextView textView3, textView4;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        Lumanti.createFolder();
                    } catch (Exception e) {
                        Default_DIalog.showDefaultDialog(this, "Unexpected Error Occurred", "Failed to create NaxaSurvey Directories, Forms can't be saved ");
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }

                    break;
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        textView3 = (TextView) findViewById(R.id.textView3);
        Typeface face3= Typeface.createFromAsset(getAssets(), "font/roboto_thin.ttf");
        textView3.setTypeface(face3);

        textView4 = (TextView) findViewById(R.id.textView4);
        Typeface face4= Typeface.createFromAsset(getAssets(), "font/roboto_thin.ttf");
        textView4.setTypeface(face4);

        Thread pause = new Thread() {
            public void run() {
                try {

                    RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.splash_background);
                    Animation relativeAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    relativeLayout.startAnimation(relativeAnim);


                    TextView textView3 = (TextView) findViewById(R.id.textView3);
                    Animation textViewAnimate3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_slow);
                    textView3.startAnimation(textViewAnimate3);

                    sleep(2000);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent stuff = new Intent(SplashScreenActivity.this,HomeListActivity.class);
                    startActivity(stuff);
                }
                finish();
            }
        };
        pause.start();


        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            try {
                Lumanti.createFolder();
            } catch (Exception e) {
                Default_DIalog.showDefaultDialog(this, "Unexpected Error Occurred", "Failed to create NaxaSurvey Directories, Forms can't be saved ");
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

        } else {

            requestPermissionsSafely(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, PERMISSION_WRITE);

        }

    }

}
