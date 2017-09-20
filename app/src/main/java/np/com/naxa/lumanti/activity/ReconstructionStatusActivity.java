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
import android.support.v4.content.FileProvider;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.gps.GPS_TRACKER_FOR_POINT;
import np.com.naxa.lumanti.gps.GeoPointActivity;
import np.com.naxa.lumanti.gps.MapPointActivity;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.Default_DIalog;
import np.com.naxa.lumanti.model.GeneralFormModel;
import np.com.naxa.lumanti.model.ImageSavedFormModel;
import np.com.naxa.lumanti.model.StaticListOfCoordinates;

public class ReconstructionStatusActivity extends AppCompatActivity {

    Toolbar toolbar;
    GeneralFormModel generalFormModel;

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

    ImageSavedFormModel imageSavedFormModel;

    String B64Eimage1 = "", B64Eimage2 = "", B64Eimage3 = "", B64Eimage4 = "";


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
    @BindView(R.id.reconstruction_status_construction_status)
    Spinner spinnerConstructionStatus;
    @BindView(R.id.reconstruction_status_others_specify)
    AutoCompleteTextView tvOthersSpecify;
    @BindView(R.id.reconstruction_status_next)
    Button btnNext;
    @BindView(R.id.reconstruction_status_prev)
    Button btnPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconstruction_status);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Reconstruction Information");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        spinnerConstructionType.setVisibility(View.INVISIBLE);
        spinnerBuildBy.setVisibility(View.INVISIBLE);
        tvOthersSpecify.setVisibility(View.INVISIBLE);
        btnPreviewMap.setEnabled(false);
        spinnerConstructionStatus.setVisibility(View.INVISIBLE);

        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .build();
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, CAMERA_PIC1_REQUEST);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, CAMERA_PIC2_REQUEST);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, CAMERA_PIC3_REQUEST);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, CAMERA_PIC4_REQUEST);


        imageSavedFormModel = new ImageSavedFormModel();

        btnPhotoSite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_PIC1_REQUEST);

                booimg1 = true;
                booimg2 = false;
                booimg3 = false;
                booimg4 = false;
                dispatchTakePictureIntent();
            }
        });

        btnPhotoSite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_PIC2_REQUEST);
                booimg1 = false;
                booimg2 = true;
                booimg3 = false;
                booimg4 = false;
                dispatchTakePictureIntent();
            }
        });

        btnPhotoSite3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_PIC3_REQUEST);
                booimg1 = false;
                booimg2 = false;
                booimg3 = true;
                booimg4 = false;
                dispatchTakePictureIntent();
            }
        });

        btnPhotoSite4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_PIC4_REQUEST);
                booimg1 = false;
                booimg2 = false;
                booimg3 = false;
                booimg4 = true;
                dispatchTakePictureIntent();
            }
        });


        generalFormModel = new GeneralFormModel();
        Log.e(" MAIN ACTIVITY SAMIR", "onCreate: countReconstruction" + "" + Constant.countReconstruction);

//        generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
        if (Constant.countReconstruction == 0) {
            generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
//        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();
        }

        if (Constant.countReconstruction != 0) {
            generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("PgeneralFormModel");
            initializeUI();
        }

    }


    @OnClick(R.id.reconstruction_status_next)
    public void NextPage() {

//        addImage();

//        if (Constant.isFomSavedForm) {
//            imageSavedFormModel.setB1_img1_encode(B64Eimage1);
//            imageSavedFormModel.setB1_img2_encode(B64Eimage2);
//            imageSavedFormModel.setB1_img3_encode(B64Eimage3);
//            imageSavedFormModel.setB1_img4_encode(B64Eimage4);
////            encodedImage1 = B64Eimage1;
////            encodedImage2 = B64Eimage2;
////            encodedImage3 = B64Eimage3;
////            encodedImage4 = B64Eimage4;
//        }

        if (Constant.countReconstructionGPS == 2) {
            generalFormModel.setB1_lat(finalLat + "");
            generalFormModel.setB1_long(finalLong + "");

//            generalFormModel.setB1_img1(imageSavedFormModel.getB1_img1_encode());
//            generalFormModel.setB1_img2(imageSavedFormModel.getB1_img2_encode());
//            generalFormModel.setB1_img3(imageSavedFormModel.getB1_img3_encode());
//            generalFormModel.setB1_img4(imageSavedFormModel.getB1_img4_encode());

            generalFormModel.setB1_img1("");
            generalFormModel.setB1_img2("");
            generalFormModel.setB1_img3("");
            generalFormModel.setB1_img4("");


            generalFormModel.setB2(spinnerLivingSituation.getSelectedItem().toString());
            generalFormModel.setB2_a(spinnerBuildBy.getSelectedItem().toString());
            generalFormModel.setB2_b(spinnerConstructionType.getSelectedItem().toString());
            generalFormModel.setB2_c(tvOthersSpecify.getText().toString());
            generalFormModel.setB3(spinnerConstructionStatus.getSelectedItem().toString());


//            Constant.countReconstructionGPS = 2;

            Intent intent = new Intent(ReconstructionStatusActivity.this, EarthquakeReliefStatusActivity.class);
            if (Constant.countEarthquakeRelief == 0) {
                intent.putExtra("generalFormModel", generalFormModel);
            } else {
                intent.putExtra("PgeneralFormModel", generalFormModel);

            }
            startActivity(intent);
        } else if (isGpsTaken) {
            generalFormModel.setB1_lat(finalLat + "");
            generalFormModel.setB1_long(finalLong + "");
//            generalFormModel.setB1_img1(encodedImage1);
//            generalFormModel.setB1_img2(encodedImage2);
//            generalFormModel.setB1_img3(encodedImage3);
//            generalFormModel.setB1_img4(encodedImage4);
//            generalFormModel.setB1_img1(imageSavedFormModel.getB1_img1_encode());
//            generalFormModel.setB1_img2(imageSavedFormModel.getB1_img2_encode());
//            generalFormModel.setB1_img3(imageSavedFormModel.getB1_img3_encode());
//            generalFormModel.setB1_img4(imageSavedFormModel.getB1_img4_encode());

            generalFormModel.setB1_img1("");
            generalFormModel.setB1_img2("");
            generalFormModel.setB1_img3("");
            generalFormModel.setB1_img4("");

            generalFormModel.setB2(spinnerLivingSituation.getSelectedItem().toString());
            generalFormModel.setB2_a(spinnerBuildBy.getSelectedItem().toString());
            generalFormModel.setB2_b(spinnerConstructionType.getSelectedItem().toString());
            generalFormModel.setB2_c(tvOthersSpecify.getText().toString());
            generalFormModel.setB3(spinnerConstructionStatus.getSelectedItem().toString());


            Constant.countReconstructionGPS = 2;
            Constant.countReconstruction = 1;

            Intent intent = new Intent(ReconstructionStatusActivity.this, EarthquakeReliefStatusActivity.class);
            if (Constant.countEarthquakeRelief == 0) {
                intent.putExtra("generalFormModel", generalFormModel);
            } else {
                intent.putExtra("PgeneralFormModel", generalFormModel);

            }
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "You need to take at least one gps cooordinate", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.reconstruction_status_prev)
    public void PreviousPage() {

//        addImage();

        generalFormModel.setB1_lat(finalLat + "");
        generalFormModel.setB1_long(finalLong + "");

        if (Constant.isFomSavedForm) {
            encodedImage1 = B64Eimage1;
            encodedImage2 = B64Eimage2;
            encodedImage3 = B64Eimage3;
            encodedImage4 = B64Eimage4;
        }

//        generalFormModel.setB1_img1(encodedImage1);
//        generalFormModel.setB1_img2(encodedImage2);
//        generalFormModel.setB1_img3(encodedImage3);
//        generalFormModel.setB1_img4(encodedImage4);

//        generalFormModel.setB1_img1(imageSavedFormModel.getB1_img1_encode());
//        generalFormModel.setB1_img2(imageSavedFormModel.getB1_img2_encode());
//        generalFormModel.setB1_img3(imageSavedFormModel.getB1_img3_encode());
//        generalFormModel.setB1_img4(imageSavedFormModel.getB1_img4_encode());

        generalFormModel.setB1_img1("");
        generalFormModel.setB1_img2("");
        generalFormModel.setB1_img3("");
        generalFormModel.setB1_img4("");

        generalFormModel.setB2(spinnerLivingSituation.getSelectedItem().toString());
        generalFormModel.setB2_a(spinnerBuildBy.getSelectedItem().toString());
        generalFormModel.setB2_b(spinnerConstructionType.getSelectedItem().toString());
        generalFormModel.setB2_c(tvOthersSpecify.getText().toString());
        generalFormModel.setB3(spinnerConstructionStatus.getSelectedItem().toString());


        Constant.countReconstruction = 1;

        Intent intent = new Intent(ReconstructionStatusActivity.this, DemographicInfoActivity.class);
        intent.putExtra("PgeneralFormModel", generalFormModel);
        startActivity(intent);
    }

    @OnClick(R.id.reconstruction_status_GpsStart)
    public void StartGPS() {


        Intent toGeoPointActivity = new Intent(this.context, GeoPointActivity.class);
        startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);
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

//        if (requestCode == 1)
//            if (resultCode == Activity.RESULT_OK) {
//                Uri selectedImage = data.getData();
//
//                String filePath = getPath(selectedImage);
//                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
//
////                image_name_tv.setText(filePath);
//                if (booimg1) {
//                    imagePath1 = filePath;
//                    addImage();
//                }
//                if (booimg2) {
//                    imagePath2 = filePath;
//                    addImage();
//                }
//                if (booimg3) {
//                    imagePath3 = filePath;
//                    addImage();
//                }
//                if (booimg4) {
//                    imagePath4 = filePath;
//                    addImage();
//                }
////                Toast.makeText(getApplicationContext(),""+encodedImage,Toast.LENGTH_SHORT).show();
////                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
////                    //FINE
////
////                }
////                else{
////                    //NOT IN REQUIRED FORMAT
////                }
//            }

        if (requestCode == CAMERA_PIC1_REQUEST && resultCode == RESULT_OK) {
            galleryAddPic1();
            setPic(ivPhotographSiteimageViewPreview1, imagePath1);
            imageSavedFormModel.setB1_img1_path(imagePath1);


        }

        if (requestCode == CAMERA_PIC2_REQUEST && resultCode == RESULT_OK) {
            galleryAddPic2();
            setPic(ivPhotographSiteimageViewPreview2, imagePath2);
            imageSavedFormModel.setB1_img2_path(imagePath2);
        }

        if (requestCode == CAMERA_PIC3_REQUEST && resultCode == RESULT_OK) {
            galleryAddPic3();
            setPic(ivPhotographSiteimageViewPreview3, imagePath3);
            imageSavedFormModel.setB1_img3_path(imagePath3);
        }

        if (requestCode == CAMERA_PIC4_REQUEST && resultCode == RESULT_OK) {
            galleryAddPic4();
            setPic(ivPhotographSiteimageViewPreview4, imagePath4);
            imageSavedFormModel.setB1_img4_path(imagePath4);
        }


//        if (requestCode == CAMERA_PIC1_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                thumbnail1 = (Bitmap) data.getExtras().get("data");
//                //  ImageView image =(ImageView) findViewById(R.id.Photo);
//                // image.setImageBitmap(thumbnail);
//                ivPhotographSiteimageViewPreview1.setVisibility(View.VISIBLE);
//                ivPhotographSiteimageViewPreview1.setImageBitmap(thumbnail1);
//                saveToExternalSorage(thumbnail1);
//                addImage();
////                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        if (requestCode == CAMERA_PIC2_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                thumbnail2 = (Bitmap) data.getExtras().get("data");
//                //  ImageView image =(ImageView) findViewById(R.id.Photo);
//                // image.setImageBitmap(thumbnail);
//                ivPhotographSiteimageViewPreview2.setVisibility(View.VISIBLE);
//                ivPhotographSiteimageViewPreview2.setImageBitmap(thumbnail2);
//                saveToExternalSorage(thumbnail2);
//                addImage();
////                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        if (requestCode == CAMERA_PIC3_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                thumbnail3 = (Bitmap) data.getExtras().get("data");
//                //  ImageView image =(ImageView) findViewById(R.id.Photo);
//                // image.setImageBitmap(thumbnail);
//                ivPhotographSiteimageViewPreview3.setVisibility(View.VISIBLE);
//                ivPhotographSiteimageViewPreview3.setImageBitmap(thumbnail3);
//                saveToExternalSorage(thumbnail3);
//                addImage();
////                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        if (requestCode == CAMERA_PIC4_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                thumbnail4 = (Bitmap) data.getExtras().get("data");
//                //  ImageView image =(ImageView) findViewById(R.id.Photo);
//                // image.setImageBitmap(thumbnail);
//                ivPhotographSiteimageViewPreview4.setVisibility(View.VISIBLE);
//                ivPhotographSiteimageViewPreview4.setImageBitmap(thumbnail4);
//                saveToExternalSorage(thumbnail4);
//                addImage();
////                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
//            }
//        }

        if (requestCode == GEOPOINT_RESULT_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    String location = data.getStringExtra(LOCATION_RESULT);

                    String string = location;
                    String[] parts = string.split(" ");
                    String split_lat = parts[0]; // 004
                    String split_lon = parts[1]; // 034556

                    finalLat = Double.parseDouble(split_lat);
                    finalLong = Double.parseDouble(split_lon);

                    LatLng d = new LatLng(finalLat, finalLong);
//
                    listCf.add(d);
                    isGpsTaken = true;

                    if (!split_lat.equals("") && !split_lon.equals("")) {
                        GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED = true;
                        btnPreviewMap.setEnabled(true);
                        btnGpsStart.setText("Location Recorded");
                    }


//                    Toast.makeText(this.context, location, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    private void saveToExternalSorage(Bitmap thumbnail) {
        // TODO Auto-generated method stub
        //String merocinema="Mero Cinema";
//        String movname=getIntent().getExtras().getString("Title");
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();


        if (booimg1) {
            imageName1 = "lumanti" + timeInMillis;
            Constant.takenimg1Name = imageName1;

            Log.e("Reconstruction", "saveToExternalSorage: " + imageName1);

            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), imageName1);
//        if (!file1.mkdirs()) {
//            Toast.makeText(getApplicationContext(), ""+file1, Toast.LENGTH_SHORT).show();
            Log.e("Reconstruction", "saveToExternalSorage: " + file1);

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
        if (booimg2) {
            imageName2 = "lumanti" + timeInMillis;
            Constant.takenimg2Name = imageName2;


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
        if (booimg3) {
            imageName3 = "lumanti" + timeInMillis;
            Constant.takenimg3Name = imageName3;


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
        if (booimg4) {
            imageName4 = "lumanti" + timeInMillis;
            Constant.takenimg4Name = imageName4;


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

//        check encoded string is in model class or not
        if (Constant.countReconstruction != 0) {
            imageName1 = Constant.takenimg1Name;
            imageName2 = Constant.takenimg2Name;
            imageName3 = Constant.takenimg3Name;
            imageName4 = Constant.takenimg4Name;
        }


        if (booimg1 || Constant.takenimg1) {
            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), imageName1);
            String path = file1.toString();

            Log.e("Reconstruction addimage", "saveToExternalSorage: " + imageName1);


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
            Constant.takenimg1 = true;
            Log.e("IMAGE STRING", "-" + encodedImage1);
        }

        if (booimg2 || Constant.takenimg2) {
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
            Constant.takenimg2 = true;
            Log.e("IMAGE STRING", "-" + encodedImage2);
        }

        if (booimg3 || Constant.takenimg3) {
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
            Constant.takenimg3 = true;
            Log.e("IMAGE STRING", "-" + encodedImage3);
        }

        if (booimg4 || Constant.takenimg4) {
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
            Constant.takenimg4 = true;
            Log.e("IMAGE STRING", "-" + encodedImage4);
        }

    }


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
        final String[] values = getResources().getStringArray(R.array.current_living_situation);
        int id = position;
        String living_situation = spinnerLivingSituation.getSelectedItem().toString();
        if (living_situation.equals(values[1])) {
            spinnerBuildBy.setVisibility(View.VISIBLE);
            spinnerConstructionType.setVisibility(View.VISIBLE);
            spinnerConstructionStatus.setVisibility(View.VISIBLE);
            tvOthersSpecify.setVisibility(View.INVISIBLE);


        } else if (living_situation.equals(values[5])) {
            spinnerBuildBy.setVisibility(View.INVISIBLE);
            spinnerConstructionType.setVisibility(View.INVISIBLE);
            spinnerConstructionStatus.setVisibility(View.INVISIBLE);
            tvOthersSpecify.setVisibility(View.VISIBLE);

        } else {
            spinnerBuildBy.setVisibility(View.INVISIBLE);
            spinnerConstructionType.setVisibility(View.INVISIBLE);
            spinnerConstructionStatus.setVisibility(View.INVISIBLE);
            tvOthersSpecify.setVisibility(View.INVISIBLE);
        }

    }

//    @Override
//    public void onBackPressed() {
//        return;
//
//    }

    public void initializeUI() {

//        Log.e("Demographics SAMIR", "initializeUI: "+ generalFormModel.toString() );
//=========================== map ===============================//
        finalLat = Double.parseDouble(generalFormModel.getB1_lat());
        finalLong = Double.parseDouble(generalFormModel.getB1_long());
        LatLng d = new LatLng(finalLat, finalLong);
        listCf.add(d);
        if (finalLat != 0 && finalLong != 0) {
            btnGpsStart.setText("Location Recorded");
            btnPreviewMap.setEnabled(true);
        }

//        ========================================image ==================================//
//        B64Eimage1 = generalFormModel.getB1_img1();
//        B64Eimage2 = generalFormModel.getB1_img2();
//        B64Eimage3 = generalFormModel.getB1_img3();
//        B64Eimage4 = generalFormModel.getB1_img4();
//
//        Log.e("Reconstruction", "SAMIR setPic1: " + B64Eimage1);
//        Log.e("Reconstruction", "SAMIR setPic2: " + B64Eimage2);
//        Log.e("Reconstruction", "SAMIR setPic3: " + B64Eimage3);
//        Log.e("Reconstruction", "SAMIR setPic4: " + B64Eimage4);

        imagePath1 = imageSavedFormModel.getB1_img1_path();
        imagePath2 = imageSavedFormModel.getB1_img2_path();
        imagePath3 = imageSavedFormModel.getB1_img3_path();
        imagePath4 = imageSavedFormModel.getB1_img4_path();




        try {
            if (!imagePath1.equals(null) && !imagePath1.equals("")) {
//                byte[] decodedString1 = Base64.decode(B64Eimage1, Base64.DEFAULT);
//                Bitmap decodedByteimage1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
//                ivPhotographSiteimageViewPreview1.setImageBitmap(decodedByteimage1);
                ivPhotographSiteimageViewPreview1.setVisibility(View.VISIBLE);

                galleryAddPic1();
//                setPic(ivPhotographSiteimageViewPreview1, imagePath1);
                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imagePath1, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;


                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / 480, photoH / 640);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;

                //bmOptions.inSampleSize = scaleFactor;
                bmOptions.inSampleSize = scaleFactor;

                bmOptions.inPurgeable = true;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath1, bmOptions);
                ivPhotographSiteimageViewPreview1.setImageBitmap(bitmap);

                Constant.takenimg1 = true;

            }
        } catch (NullPointerException e) {
            Log.d("Reconstruction", "initializeUI: exception img1");
        }


        try {
            if (!imagePath2.equals(null) && !imagePath2.equals("")) {
//                byte[] decodedString2 = Base64.decode(B64Eimage2, Base64.DEFAULT);
//                Bitmap decodedByteimage2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
//                ivPhotographSiteimageViewPreview2.setImageBitmap(decodedByteimage2);
                ivPhotographSiteimageViewPreview2.setVisibility(View.VISIBLE);
                galleryAddPic2();
//                setPic(ivPhotographSiteimageViewPreview2, imagePath2);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imagePath2, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;


                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / 480, photoH / 640);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;

                //bmOptions.inSampleSize = scaleFactor;
                bmOptions.inSampleSize = scaleFactor;

                bmOptions.inPurgeable = true;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath2, bmOptions);
                ivPhotographSiteimageViewPreview2.setImageBitmap(bitmap);

                Constant.takenimg2 = true;
            }
        } catch (NullPointerException e) {
            Log.d("Reconstruction", "initializeUI: exception img2");

        }

        try {

            if (!imagePath3.equals(null) && !imagePath3.equals("")) {
//                byte[] decodedString3 = Base64.decode(B64Eimage3, Base64.DEFAULT);
//                Bitmap decodedByteimage3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
//                ivPhotographSiteimageViewPreview3.setImageBitmap(decodedByteimage3);
                ivPhotographSiteimageViewPreview3.setVisibility(View.VISIBLE);
                galleryAddPic3();
//                setPic(ivPhotographSiteimageViewPreview3, imagePath3);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imagePath3, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;


                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / 480, photoH / 640);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;

                //bmOptions.inSampleSize = scaleFactor;
                bmOptions.inSampleSize = scaleFactor;

                bmOptions.inPurgeable = true;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath3, bmOptions);
                ivPhotographSiteimageViewPreview3.setImageBitmap(bitmap);

                Constant.takenimg3 = true ;
            }
        } catch (NullPointerException e) {
            Log.d("Reconstruction", "initializeUI: exception img3");

        }


        try {
            if (!imagePath4.equals(null) && !imagePath4.equals("")) {
//                byte[] decodedString4 = Base64.decode(B64Eimage4, Base64.DEFAULT);
//                Bitmap decodedByteimage4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
//                ivPhotographSiteimageViewPreview4.setImageBitmap(decodedByteimage4);
                ivPhotographSiteimageViewPreview4.setVisibility(View.VISIBLE);
                galleryAddPic4();
//                setPic(ivPhotographSiteimageViewPreview4, imagePath4);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imagePath4, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;


                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / 480, photoH / 640);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;

                //bmOptions.inSampleSize = scaleFactor;
                bmOptions.inSampleSize = scaleFactor;

                bmOptions.inPurgeable = true;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath4, bmOptions);
                ivPhotographSiteimageViewPreview4.setImageBitmap(bitmap);

                Constant.takenimg4 = true;
            }
        } catch (NullPointerException e) {
            Log.d("Reconstruction", "initializeUI: exception img4");

        }


//        ====================================spinner =======================================//
        List<String> CurrentLiving = Arrays.asList(getResources().getStringArray(R.array.current_living_situation));
        int setCurrentLiving = CurrentLiving.indexOf(generalFormModel.getB2());
        spinnerLivingSituation.setSelection(setCurrentLiving);


        List<String> BuiltBy = Arrays.asList(getResources().getStringArray(R.array.build_by));
        int setBuiltBy = BuiltBy.indexOf(generalFormModel.getB2_a());
        spinnerBuildBy.setSelection(setBuiltBy);

        List<String> ConstructionType = Arrays.asList(getResources().getStringArray(R.array.consctruction_type));
        int setConstructionType = ConstructionType.indexOf(generalFormModel.getB2_b());
        spinnerConstructionType.setSelection(setConstructionType);

        List<String> ConstructionStatus = Arrays.asList(getResources().getStringArray(R.array.construction_status));
        int setConstructionStatus = ConstructionStatus.indexOf(generalFormModel.getB3());
        spinnerConstructionStatus.setSelection(setConstructionStatus);

        tvOthersSpecify.setText(generalFormModel.getB2_c());

    }


//    ================================================ image full size =============================================================//


    private void dispatchTakePictureIntent() {

        //scaling down needs the imageview to be visible
        if (booimg1) {
            //so start early
            ivPhotographSiteimageViewPreview1.setVisibility(View.VISIBLE);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "np.com.naxa.lumanti.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_PIC1_REQUEST);
                }
            }
        }

        if (booimg2) {
            //so start early
            ivPhotographSiteimageViewPreview2.setVisibility(View.VISIBLE);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "np.com.naxa.lumanti.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_PIC2_REQUEST);
                }
            }
        }

        if (booimg3) {
            //so start early
            ivPhotographSiteimageViewPreview3.setVisibility(View.VISIBLE);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "np.com.naxa.lumanti.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_PIC3_REQUEST);
                }
            }
        }


        if (booimg4) {
            //so start early
            ivPhotographSiteimageViewPreview4.setVisibility(View.VISIBLE);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "np.com.naxa.lumanti.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_PIC4_REQUEST);
                }
            }
        }


    }


    File image;

    private File createImageFile() throws IOException {
        if (booimg1) {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            imagePath1 = image.getAbsolutePath();
        }

        if (booimg2) {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            imagePath2 = image.getAbsolutePath();
        }

        if (booimg3) {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            imagePath3 = image.getAbsolutePath();
        }

        if (booimg4) {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            imagePath4 = image.getAbsolutePath();
        }

        return image;
    }


    private void galleryAddPic1() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath1);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void galleryAddPic2() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath2);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void galleryAddPic3() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath3);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void galleryAddPic4() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath4);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private void setPic(ImageView mImageView, String imagePath) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();


        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;


        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;

        //bmOptions.inSampleSize = scaleFactor;
        bmOptions.inSampleSize = scaleFactor;

        bmOptions.inPurgeable = true;


        if (booimg1) {

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath1, bmOptions);
            mImageView.setImageBitmap(bitmap);
            Constant.takenimg1 = true;

//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            encodedImage1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            generalFormModel.setB1_img1(encodedImage1);
//
//            imageSavedFormModel.setB1_img1_encode(encodedImage1);
//
//            Log.e("Reconstruction", "setPic1: " + generalFormModel.getB1_img1());
        }
        if (booimg2) {

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath2, bmOptions);
            mImageView.setImageBitmap(bitmap);

            Constant.takenimg2 = true;

//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            encodedImage2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            generalFormModel.setB1_img2(encodedImage2);
//
//            imageSavedFormModel.setB1_img2_encode(encodedImage2);
//
//
//            Log.e("Reconstruction", "setPic2: " + generalFormModel.getB1_img2());


        }

        if (booimg3) {

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath3, bmOptions);
            mImageView.setImageBitmap(bitmap);

            Constant.takenimg3 = true ;

//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            encodedImage3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            generalFormModel.setB1_img3(encodedImage3);
//
//            imageSavedFormModel.setB1_img3_encode(encodedImage3);
//
//            Log.e("Reconstruction", "setPic3: " + generalFormModel.getB1_img3());


        }

        if (booimg4) {

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath4, bmOptions);
            mImageView.setImageBitmap(bitmap);

            Constant.takenimg4 = true;

//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            encodedImage4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            generalFormModel.setB1_img4(encodedImage4);
//
//            imageSavedFormModel.setB1_img4_encode(encodedImage4);
//
//            Log.e("Reconstruction", "setPic4: " + generalFormModel.getB1_img4());


        }

    }

//    ================================================ end of image full size code =================================================//
}
