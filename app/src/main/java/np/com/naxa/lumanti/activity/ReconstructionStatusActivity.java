package np.com.naxa.lumanti.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.gps.GPS_TRACKER_FOR_POINT;
import np.com.naxa.lumanti.gps.MapPointActivity;
import np.com.naxa.lumanti.model.CheckValues;
import np.com.naxa.lumanti.model.Default_DIalog;
import np.com.naxa.lumanti.model.GeneralFormModel;
import np.com.naxa.lumanti.model.StaticListOfCoordinates;

import static android.R.attr.onClick;

public class ReconstructionStatusActivity extends AppCompatActivity {

    Toolbar toolbar;
    GeneralFormModel generalFormModel ;

    int CAMERA_PIC1_REQUEST = 02;
    int CAMERA_PIC2_REQUEST = 03;
    int CAMERA_PIC3_REQUEST = 04;
    int CAMERA_PIC4_REQUEST = 05;
    String imagePath1, encodedImage1 = "", imageName1 = "no_photo1";
    String imagePath2, encodedImage2 = "", imageName2 = "no_photo2";
    String imagePath3, encodedImage3 = "", imageName3 = "no_photo3";
    String imagePath4, encodedImage4 = "", imageName4 = "no_photo4";
    Bitmap thumbnail1, thumbnail2, thumbnail3, thumbnail4;

    private boolean booimg1 = false, booimg2 = false, booimg3 = false, booimg4 = false;

    boolean isGpsTaken = false;
    double initLat;
    double finalLat;
    double initLong;
    double finalLong;
    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();

    static final Integer LOCATION = 0x1;
    static final Integer GPS_SETTINGS = 0x8;
    GPS_TRACKER_FOR_POINT gps;
    Context context = this;
    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;


    @BindView(R.id.reconstruction_status_GpsStart)
    Button btnGpsStart;
    @BindView(R.id.reconstruction_status_preview_map)
    Button btnPreviewMap;
    @BindView(R.id.reconstruction_status_photo_site1)
    ImageButton btnPhotoSite1;
    @BindView(R.id.preconstruction_PhotographSiteimageViewPreview1)
    ImageView ivPhotographSiteimageViewPreview1;
    @BindView(R.id.reconstruction_status_photo_site2)
    ImageButton btnPhotoSite2;
    @BindView(R.id.preconstruction_PhotographSiteimageViewPreview2)
    ImageView ivPhotographSiteimageViewPreview2;
    @BindView(R.id.reconstruction_status_photo_site3)
    ImageButton btnPhotoSite3;
    @BindView(R.id.preconstruction_PhotographSiteimageViewPreview3)
    ImageView ivPhotographSiteimageViewPreview3;
    @BindView(R.id.reconstruction_status_photo_site4)
    ImageButton btnPhotoSite4;
    @BindView(R.id.preconstruction_PhotographSiteimageViewPreview4)
    ImageView ivPhotographSiteimageViewPreview4;
    @BindView(R.id.reconstruction_status_living_situation)
    Spinner spinnerLivingSituation;
    @BindView(R.id.reconstruction_status_build_by)
    Spinner spinnerBuildBy;
    @BindView(R.id.reconstruction_status_construction_type)
    Spinner spinnerConstructionType;
    @BindView(R.id.reconstruction_status_others_specify)
    AutoCompleteTextView tvOthersSpecify;
    @BindView(R.id.reconstruction_status_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconstruction_status);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Reconstruction Information");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerConstructionType.setVisibility(View.INVISIBLE);
        spinnerBuildBy.setVisibility(View.INVISIBLE);
        tvOthersSpecify.setVisibility(View.INVISIBLE);

        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .build();
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
        askForPermission(Manifest.permission.CAMERA, CAMERA_PIC1_REQUEST);
        askForPermission(Manifest.permission.CAMERA, CAMERA_PIC2_REQUEST);
        askForPermission(Manifest.permission.CAMERA, CAMERA_PIC3_REQUEST);
        askForPermission(Manifest.permission.CAMERA, CAMERA_PIC4_REQUEST);

        btnPhotoSite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC1_REQUEST);
                booimg1 = true ;
                booimg2 = false ;
                booimg3 = false ;
                booimg4 = false ;
            }
        });

        btnPhotoSite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC2_REQUEST);
                booimg1 = false ;
                booimg2 = true ;
                booimg3 = false ;
                booimg4 = false ;
            }
        });

        btnPhotoSite3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC3_REQUEST);
                booimg1 = false ;
                booimg2 = false ;
                booimg3 = true ;
                booimg4 = false ;
            }
        });

        btnPhotoSite4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC4_REQUEST);
                booimg1 = false ;
                booimg2 = false ;
                booimg3 = false ;
                booimg4 = true ;
            }
        });


        generalFormModel = new GeneralFormModel();
        generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, ""+ generalFormModel.getA1(), Toast.LENGTH_SHORT).show();

    }





    @OnClick(R.id.reconstruction_status_next)
    public void NextPage() {

        generalFormModel.setB1_lat(finalLat+"");
        generalFormModel.setB1_long(finalLong+"");
        generalFormModel.setB1_img1(encodedImage1);
        generalFormModel.setB1_img2(encodedImage2);
        generalFormModel.setB1_img3(encodedImage3);
        generalFormModel.setB1_img4(encodedImage4);
        generalFormModel.setB2(spinnerLivingSituation.getSelectedItem().toString());
        generalFormModel.setB2_a(spinnerBuildBy.getSelectedItem().toString());
        generalFormModel.setB2_b(spinnerConstructionType.getSelectedItem().toString());
        generalFormModel.setB2_c(tvOthersSpecify.getText().toString());

        Intent intent = new Intent(ReconstructionStatusActivity.this, EarthquakeReliefStatusActivity.class);
        intent.putExtra("generalFormModel", generalFormModel);
        startActivity(intent);
    }

    @OnClick(R.id.reconstruction_status_GpsStart)
    public void StartGPS() {


        if (GPS_SETTINGS.equals(true) || GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {

            if (gps.canGetLocation()) {
                gpslocation.add(gps.getLocation());
                finalLat = gps.getLatitude();
                finalLong = gps.getLongitude();
                if (finalLat != 0) {
                    btnPreviewMap.setEnabled(true);
                    try {
                        JSONObject data = new JSONObject();
                        data.put("latitude", finalLat);
                        data.put("longitude", finalLong);

//                        jsonArrayGPS.put(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    LatLng d = new LatLng(finalLat, finalLong);

                    listCf.add(d);
                    isGpsTaken = true;
                    Toast.makeText(
                            getApplicationContext(),
                            "Your Location is - \nLat: " + finalLat
                                    + "\nLong: " + finalLong, Toast.LENGTH_SHORT)
                            .show();
//                    stringBuilder.append("[" + finalLat + "," + finalLong + "]" + ",");
                }

            }
        } else {
            askForGPS();
            gps = new GPS_TRACKER_FOR_POINT(ReconstructionStatusActivity.this);
            Default_DIalog.showDefaultDialog(context, R.string.app_name, "Please try again, Gps not initialized");
//                        gps.showSettingsAlert();
        }
    }

    @OnClick(R.id.reconstruction_status_preview_map)
    public void PreviewMap() {
//        if (CheckValues.isFromSavedFrom) {
//            StaticListOfCoordinates.setList(listCf);
//            startActivity(new Intent(ReconstructionStatusActivity.this, MapPointActivity.class));
//        } else {

            if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                StaticListOfCoordinates.setList(listCf);
                startActivity(new Intent(ReconstructionStatusActivity.this, MapPointActivity.class));
            } else {
                Default_DIalog.showDefaultDialog(context, R.string.app_name, "Please try again, Gps not initialized");

            }
//        }
    }


    private void askForGPS() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(ReconstructionStatusActivity.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);

//                image_name_tv.setText(filePath);
                if(booimg1) {
                    imagePath1 = filePath;
                    addImage();
                }
                if(booimg2) {
                    imagePath2 = filePath;
                    addImage();
                }
                if(booimg3) {
                    imagePath3 = filePath;
                    addImage();
                }
                if(booimg4) {
                    imagePath4 = filePath;
                    addImage();
                }
//                Toast.makeText(getApplicationContext(),""+encodedImage,Toast.LENGTH_SHORT).show();
//                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
//                    //FINE
//
//                }
//                else{
//                    //NOT IN REQUIRED FORMAT
//                }
            }
        if (requestCode == CAMERA_PIC1_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                thumbnail1 = (Bitmap) data.getExtras().get("data");
                //  ImageView image =(ImageView) findViewById(R.id.Photo);
                // image.setImageBitmap(thumbnail);
                ivPhotographSiteimageViewPreview1.setVisibility(View.VISIBLE);
                ivPhotographSiteimageViewPreview1.setImageBitmap(thumbnail1);
                saveToExternalSorage(thumbnail1);
                addImage();
//                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CAMERA_PIC2_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                thumbnail2 = (Bitmap) data.getExtras().get("data");
                //  ImageView image =(ImageView) findViewById(R.id.Photo);
                // image.setImageBitmap(thumbnail);
                ivPhotographSiteimageViewPreview2.setVisibility(View.VISIBLE);
                ivPhotographSiteimageViewPreview2.setImageBitmap(thumbnail2);
                saveToExternalSorage(thumbnail2);
                addImage();
//                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CAMERA_PIC3_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                thumbnail3 = (Bitmap) data.getExtras().get("data");
                //  ImageView image =(ImageView) findViewById(R.id.Photo);
                // image.setImageBitmap(thumbnail);
                ivPhotographSiteimageViewPreview3.setVisibility(View.VISIBLE);
                ivPhotographSiteimageViewPreview3.setImageBitmap(thumbnail3);
                saveToExternalSorage(thumbnail3);
                addImage();
//                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CAMERA_PIC4_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                thumbnail4 = (Bitmap) data.getExtras().get("data");
                //  ImageView image =(ImageView) findViewById(R.id.Photo);
                // image.setImageBitmap(thumbnail);
                ivPhotographSiteimageViewPreview4.setVisibility(View.VISIBLE);
                ivPhotographSiteimageViewPreview4.setImageBitmap(thumbnail4);
                saveToExternalSorage(thumbnail4);
                addImage();
//                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void saveToExternalSorage(Bitmap thumbnail) {
        // TODO Auto-generated method stub
        //String merocinema="Mero Cinema";
//        String movname=getIntent().getExtras().getString("Title");
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();


        if(booimg1) {
            imageName1 = "lumanti" + timeInMillis;

            Log.e("Reconstruction", "saveToExternalSorage: "+imageName1 );

            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), imageName1);
//        if (!file1.mkdirs()) {
//            Toast.makeText(getApplicationContext(), ""+file1, Toast.LENGTH_SHORT).show();
            Log.e("Reconstruction", "saveToExternalSorage: "+file1 );

//        }

            if (file1.exists()) file1.delete();
            try {
                FileOutputStream out = new FileOutputStream(file1);
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Toast.makeText(getApplicationContext(), "Saved " + imageName1, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(booimg2) {
            imageName2 = "lumanti" + timeInMillis;

            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), imageName2);
//        if (!file1.mkdirs()) {
//            Toast.makeText(getApplicationContext(), "Not Created", Toast.LENGTH_SHORT).show();
//        }

            if (file1.exists()) file1.delete();
            try {
                FileOutputStream out = new FileOutputStream(file1);
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Toast.makeText(getApplicationContext(), "Saved " + imageName2, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(booimg3) {
            imageName3 = "lumanti" + timeInMillis;

            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), imageName3);
//        if (!file1.mkdirs()) {
//            Toast.makeText(getApplicationContext(), "Not Created", Toast.LENGTH_SHORT).show();
//        }

            if (file1.exists()) file1.delete();
            try {
                FileOutputStream out = new FileOutputStream(file1);
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Toast.makeText(getApplicationContext(), "Saved " + imageName3, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(booimg4) {
            imageName1 = "lumanti" + timeInMillis;

            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), imageName4);
//        if (!file1.mkdirs()) {
//            Toast.makeText(getApplicationContext(), "Not Created", Toast.LENGTH_SHORT).show();
//        }

            if (file1.exists()) file1.delete();
            try {
                FileOutputStream out = new FileOutputStream(file1);
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Toast.makeText(getApplicationContext(), "Saved " + imageName4, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public void addImage() {

        if(booimg1) {
            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), imageName1);
            String path = file1.toString();

            Log.e("Reconstruction addimage", "saveToExternalSorage: "+imageName1 );


            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 1;
            options.inPurgeable = true;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
//        Bitmap bm = BitmapFactory.decodeFile( imagePath ,options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);


            // bitmap object

            byte[] byteImage_photo = baos.toByteArray();

            //generate base64 string of image
            encodedImage1 = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
            Log.e("IMAGE STRING", "-" + encodedImage1);
        }

        if(booimg2) {
            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), imageName2);
            String path = file1.toString();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 1;
            options.inPurgeable = true;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
//        Bitmap bm = BitmapFactory.decodeFile( imagePath ,options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);


            // bitmap object

            byte[] byteImage_photo = baos.toByteArray();

            //generate base64 string of image
            encodedImage2 = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
            Log.e("IMAGE STRING", "-" + encodedImage2);
        }

        if(booimg3) {
            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), imageName3);
            String path = file1.toString();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 1;
            options.inPurgeable = true;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
//        Bitmap bm = BitmapFactory.decodeFile( imagePath ,options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);


            // bitmap object

            byte[] byteImage_photo = baos.toByteArray();

            //generate base64 string of image
            encodedImage3 = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
            Log.e("IMAGE STRING", "-" + encodedImage3);
        }

        if(booimg4) {
            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), imageName4);
            String path = file1.toString();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 1;
            options.inPurgeable = true;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
//        Bitmap bm = BitmapFactory.decodeFile( imagePath ,options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);


            // bitmap object

            byte[] byteImage_photo = baos.toByteArray();

            //generate base64 string of image
            encodedImage4 = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
            Log.e("IMAGE STRING", "-" + encodedImage4);
        }

    }

//    private void loadImageFromStorage(String path) {
//
//        try {
//            previewImageSite.setVisibility(View.VISIBLE);
//            File f = new File(path);
//            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
//            previewImageSite.setImageBitmap(b);
//        } catch (FileNotFoundException e) {
//            Toast.makeText(getApplicationContext(), "invalid path", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ReconstructionStatusActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ReconstructionStatusActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(ReconstructionStatusActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(ReconstructionStatusActivity.this, new String[]{permission}, requestCode);
            }
        } else {
//            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (ActivityCompat.checkSelfPermission(ReconstructionStatusActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                askForGPS();
                Log.v("Susan", "Permission: " + permissions[0] + "was " + grantResults[0]);
                //resume tasks needing this permission
                Toast.makeText(ReconstructionStatusActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.getMessage();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }




    @OnItemSelected(R.id.reconstruction_status_living_situation)
    public void spinnerItemSelected(Spinner spinner, int position) {
        // code here
        int id = position ;
        String living_situation = spinnerLivingSituation.getSelectedItem().toString();
        if (living_situation.equals("At own house")){
            spinnerBuildBy.setVisibility(View.VISIBLE);
            spinnerConstructionType.setVisibility(View.VISIBLE);

        }
        else if(living_situation.equals("Others")){
            tvOthersSpecify.setVisibility(View.VISIBLE);

        }
        else {
            spinnerBuildBy.setVisibility(View.INVISIBLE);
            spinnerConstructionType.setVisibility(View.INVISIBLE);
            tvOthersSpecify.setVisibility(View.INVISIBLE);
        }

    }


}
