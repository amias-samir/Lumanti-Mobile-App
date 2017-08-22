package np.com.naxa.lumanti.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.sugar.Municipality_ward_list;

public class HomeListActivity extends AppCompatActivity {

    Toolbar toolbar;
    @BindView(R.id.top_layout_shelter_status)
    RelativeLayout rlShelterStatus;
    @BindView(R.id.top_layout_saved_forms)
    RelativeLayout rlSavedForms;
    //Permission for higher then lollipop devices
    private int MULTIPLE_PERMISSION_CODE = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_list);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Shelter Status");
        setSupportActionBar(toolbar);

        requestPermission();

        //        load municipality dta to database
        loadMunicipalityList();
    }


    //   ============================================ permission code for higher version starts here ===================================//
    public void requestPermission() {
        try {
            //First checking if the app is already having the permission
            if (isPermissionAllowed()) {
                //If permission is already having then showing the toast
//                Toast.makeText(AddYourBusinessActivity.this, "You already have the permission", Toast.LENGTH_LONG).show();
                //Existing the method with return
                return;
            } else {
                //If the app has not the permission then asking for the permission
                requestMultiplePermission();
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.getMessage();
        }

    }

    /**
     * @return Samir Permissions: Camera, Storage, Location, Internet, etc.
     */
    //We are calling this method to check the permission status
    private boolean isPermissionAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }


    //Requesting permission
    private void requestMultiplePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, MULTIPLE_PERMISSION_CODE);

    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == MULTIPLE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

//  ============================================ permission code for higher version ends here ===================================//


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Exit From App")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HomeListActivity.this.onBackPressed();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        } else {
                            finish();
                        }
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }



    @OnClick({R.id.top_layout_shelter_status, R.id.top_layout_saved_forms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_layout_shelter_status:

//====================================== reset constant variable if they set ===========================================//
                reinitializeConstantVariable();

                Intent intent_main = new Intent(HomeListActivity.this, MainActivity.class);
                startActivity(intent_main);
                break;
            case R.id.top_layout_saved_forms:
                Intent intent_saved_forms = new Intent(HomeListActivity.this, SavedFormsActivity.class);
                startActivity(intent_saved_forms);
                break;
        }
    }


    //==================================== menu item =================================================//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_saved_forms:
                Intent intent = new Intent(HomeListActivity.this, SavedFormsActivity.class);
                startActivity(intent);
                break;

            default:

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    //===========================================end of menu item =======================================//


    public void reinitializeConstantVariable (){

        Constant.countGeneral = 0 ;
        Constant.countDemographic = 0 ;
        Constant.countReconstruction = 0 ;
        Constant.countEarthquakeRelief = 0 ;
        Constant.countReconstructionGPS = 0 ;
        Constant.countSaveSend = 0 ;

        Constant.takenimg1 = false;
        Constant.takenimg2 = false;
        Constant.takenimg3 = false;
        Constant.takenimg4 = false;

        Constant.takenimg1Name = "";
        Constant.takenimg2Name = "";
        Constant.takenimg3Name = "";
        Constant.takenimg4Name = "";
    }


//    load local JSON file
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("json/Lumanti_munici_ward.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



//    List<Municipality_ward_list> user_list = new ArrayList<>();
    double count = Municipality_ward_list.count(Municipality_ward_list.class);




    public void loadMunicipalityList () {
        Log.e("Municipality SAMIR", "loadMunicipalityList count: " + "" + count);

        if(count <= 0){

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray municipalityJArray = obj.getJSONArray("Municipality_list");

            for (int i = 0; i < municipalityJArray.length(); i++) {
                JSONObject jObj = municipalityJArray.getJSONObject(i);
                Log.e("Municipality SAMIR", "loadMunicipalityList: "+jObj.toString() );
                String sn = jObj.getString("sn");
                String district = jObj.getString("district");
                String current_municipality = jObj.getString("current_municipality");
                String current_ward = jObj.getString("current_ward");
                String previous_municipality = jObj.getString("previous_municipality");
                String previous_ward = jObj.getString("previous_ward");

                Municipality_ward_list municipalityWardList = new Municipality_ward_list(sn, district, current_municipality, current_ward, previous_municipality, previous_ward);
                municipalityWardList.save();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    }
}
