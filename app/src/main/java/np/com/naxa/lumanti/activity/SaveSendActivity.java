package np.com.naxa.lumanti.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.database.DataBaseForm_NotSent;
import np.com.naxa.lumanti.database.DataBaseForm_Sent;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.GeneralFormModel;
import np.com.naxa.lumanti.model.ImageSavedFormModel;

public class SaveSendActivity extends AppCompatActivity {

    Toolbar toolbar;

    GeneralFormModel generalFormModel;
    ImageSavedFormModel imageSavedFormModel;

    Gson gson = new Gson();
    String jsonToSend;
    String jsonPhotoPath;

    ProgressDialog mProgressDlg;
    Context context = this;
    String dataSentStatus, dateString;

    String encodedImage1 = "", encodedImage2 = "", encodedImage3 = "", encodedImage4 = "";

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;


    NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;


    @BindView(R.id.capacity_building_recommendation_regarding_reconstruction)
    AutoCompleteTextView tvRecommendationRegardingReconstruction;
    @BindView(R.id.capacity_building_any_specific_info)
    AutoCompleteTextView tvAnySpecificInfo;
    @BindView(R.id.capacity_building_identified_gap)
    AutoCompleteTextView tvIdentifiedGap;

    @BindView(R.id.capacity_building_surveyors_name)
    AutoCompleteTextView SurveyorsName;
    @BindView(R.id.capacity_building_date_of_interview)
    AutoCompleteTextView DateOfInterview;

    @BindView(R.id.water_sanitation_send)
    Button btnSend;
    @BindView(R.id.water_sanitation_save)
    Button btnSave;
    @BindView(R.id.reconstruction_status_prev)
    Button btnPrev;
    private String TAG = "SaveSendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_send);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Suggestions/Feedback");
        setSupportActionBar(toolbar);

        setCurrentDateOnView();

        generalFormModel = new GeneralFormModel();
        imageSavedFormModel = new ImageSavedFormModel();

        if (Constant.countSaveSend == 0) {
            generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
//        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();
            Log.e(" MAIN ACTIVITY SAMIR", "onCreate: " + "" + Constant.countEarthquakeRelief);
        }

        if (Constant.countSaveSend != 0) {
            generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("PgeneralFormModel");
            initializeUI();
        }


        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

    }

    @OnClick({R.id.water_sanitation_send, R.id.water_sanitation_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.water_sanitation_send:
                setGeneralFormModelValue();

//                convertDataToJson();

                if (networkInfo != null && networkInfo.isConnected()) {

//                  sendDatToserver();

                    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;

                    final Dialog showDialog = new Dialog(context);
                    showDialog.setContentView(R.layout.alert_dialog_before_send);
                    final Button yes = (Button) showDialog.findViewById(R.id.alertButtonYes);
                    final Button no = (Button) showDialog.findViewById(R.id.alertButtonNo);

                    showDialog.setTitle("WARNING !!!");
                    showDialog.setCancelable(false);
                    showDialog.show();
                    showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog.dismiss();
                            mProgressDlg = new ProgressDialog(context);
                            mProgressDlg.setMessage("Please wait...");
                            mProgressDlg.setIndeterminate(false);
                            mProgressDlg.setCancelable(false);
                            mProgressDlg.show();

                            imageB64Encoder();
                            convertDataToJson();
                            sendDatToserver();
//                                finish();
                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog.dismiss();
                        }
                    });


                } else {
                    final View coordinatorLayoutView = findViewById(R.id.main_activity);
                    Snackbar.make(coordinatorLayoutView, "No internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Retry", null).show();
                }


                break;
            case R.id.water_sanitation_save:

                setGeneralFormModelValue();

                convertPhotoPathToJson();

                convertDataToJson();

                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                final int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                final Dialog showDialog = new Dialog(context);
                showDialog.setContentView(R.layout.date_input_layout);
                final EditText FormNameToInput = (EditText) showDialog.findViewById(R.id.input_tableName);
                final EditText dateToInput = (EditText) showDialog.findViewById(R.id.input_date);

//                if (formNameSavedForm.equals("")){

                if (!generalFormModel.getG_10().equals("") && !generalFormModel.getG2().equals("")) {

                    FormNameToInput.setText(generalFormModel.getG_10() + "_" + generalFormModel.getG2());
                } else {
                    FormNameToInput.setText("Lumanti");
                }
//                }
//                else {
//                    FormNameToInput.setText(formNameSavedForm);
//                    DataBaseForm_NotSent dataBaseNepalPublicHealthNotSent = new DataBaseForm_NotSent(context);
//                    dataBaseNepalPublicHealthNotSent.open();
//                    dataBaseNepalPublicHealthNotSent.dropRowNotSentForms(formid);
//                }

                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                String dateString = sdf.format(date);
                dateToInput.setText(dateString);

                Log.e(TAG, "SaveSend: " + dateToInput);

                AppCompatButton logIn = (AppCompatButton) showDialog.findViewById(R.id.login_button);
                showDialog.setTitle("Save Data");
                showDialog.setCancelable(true);
                showDialog.show();
                showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

                logIn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        String dateDataCollected = dateToInput.getText().toString();
                        String formName = FormNameToInput.getText().toString();
                        if (dateDataCollected == null || dateDataCollected.equals("") || formName == null || formName.equals("")) {
                            Toast.makeText(context, "Please fill the required field. ", Toast.LENGTH_SHORT).show();
                        } else {
                            String[] data = new String[]{"1", formName, dateDataCollected, jsonToSend, "",
                                    jsonPhotoPath, "Not Sent", "0"};

                            DataBaseForm_NotSent dataBaseNepalPublicHealthNotSent = new DataBaseForm_NotSent(context);
                            dataBaseNepalPublicHealthNotSent.open();
                            dataBaseNepalPublicHealthNotSent.insertIntoTable_Main(data);

                            Toast.makeText(SaveSendActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                            showDialog.dismiss();

                            final Dialog showDialog = new Dialog(context);
                            showDialog.setContentView(R.layout.savedform_sent_popup);
                            final Button yes = (Button) showDialog.findViewById(R.id.buttonYes);
                            final Button no = (Button) showDialog.findViewById(R.id.buttonNo);

                            showDialog.setTitle("Successfully Saved");
                            showDialog.setCancelable(false);
                            showDialog.show();
                            showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog.dismiss();

// ====================================== reinitialize constant variable=================================================//
                                    reinitializeConstantVariable();

                                    Intent intent = new Intent(SaveSendActivity.this, SavedFormsActivity.class);
                                    startActivity(intent);
//                                finish();
                                }
                            });

                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog.dismiss();
                                    reinitializeConstantVariable();
                                    Intent intent = new Intent(SaveSendActivity.this, NissaNoInputActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
                break;
        }
    }


    @OnClick(R.id.reconstruction_status_prev)
    public void PreviousPage() {

        generalFormModel.setF5(tvRecommendationRegardingReconstruction.getText().toString());
        generalFormModel.setF6(tvAnySpecificInfo.getText().toString());
        generalFormModel.setF7(tvIdentifiedGap.getText().toString());
        generalFormModel.setF8(SurveyorsName.getText().toString());
        generalFormModel.setF9(DateOfInterview.getText().toString());


        Constant.countSaveSend = 1;

        Intent intent = new Intent(SaveSendActivity.this, EarthquakeReliefStatusActivity.class);
        intent.putExtra("PgeneralFormModel", generalFormModel);
        startActivity(intent);
    }


    public void setGeneralFormModelValue() {


        generalFormModel.setF5(tvRecommendationRegardingReconstruction.getText().toString());
        generalFormModel.setF6(tvAnySpecificInfo.getText().toString());
        generalFormModel.setF7(tvIdentifiedGap.getText().toString());
        generalFormModel.setF8(SurveyorsName.getText().toString());
        generalFormModel.setF9(DateOfInterview.getText().toString());

    }


    public void convertDataToJson() {

        jsonToSend = gson.toJson(generalFormModel);


        Log.e("capacity_building", "convertDatToJson: " + jsonToSend);
    }

    public void convertPhotoPathToJson() {

        ImageSavedFormModel imageSavedFormModel = new ImageSavedFormModel();

        jsonPhotoPath = gson.toJson(imageSavedFormModel);


        Log.e("capacity_building", "convertDatToJson: " + jsonToSend);
    }


    public void imageB64Encoder (){

//        image 1 encode
        try {
            if (Constant.takenimg1) {
                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imageSavedFormModel.getB1_img1_path(), bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / 480, photoH / 640);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;

                //bmOptions.inSampleSize = scaleFactor;
                bmOptions.inSampleSize = scaleFactor;

                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(imageSavedFormModel.getB1_img1_path(), bmOptions);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedImage1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                generalFormModel.setB1_img1(encodedImage1);
            }
        }catch (Exception e){
            Log.e(TAG, "imageB64Encoder1: "+e.toString() );
        }

        //    ======================    image 2 encode======================================//
        try {
            if (Constant.takenimg2) {
                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imageSavedFormModel.getB1_img2_path(), bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / 480, photoH / 640);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;

                //bmOptions.inSampleSize = scaleFactor;
                bmOptions.inSampleSize = scaleFactor;

                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(imageSavedFormModel.getB1_img2_path(), bmOptions);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedImage2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                generalFormModel.setB1_img2(encodedImage2);
            }
        }catch (Exception e){
            Log.e(TAG, "imageB64Encoder2: "+e.toString() );
        }

        //    ======================    image 3 encode======================================//
        try {
            if (Constant.takenimg3) {
                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imageSavedFormModel.getB1_img3_path(), bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / 480, photoH / 640);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;

                //bmOptions.inSampleSize = scaleFactor;
                bmOptions.inSampleSize = scaleFactor;

                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(imageSavedFormModel.getB1_img3_path(), bmOptions);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedImage3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                generalFormModel.setB1_img3(encodedImage3);
            }
        }catch (Exception e){
            Log.e(TAG, "imageB64Encoder3: "+e.toString() );
        }

        //    ======================    image 4 encode======================================//
        try {
            if (Constant.takenimg4) {
                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imageSavedFormModel.getB1_img4_path(), bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / 480, photoH / 640);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;

                //bmOptions.inSampleSize = scaleFactor;
                bmOptions.inSampleSize = scaleFactor;

                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(imageSavedFormModel.getB1_img4_path(), bmOptions);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedImage4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                generalFormModel.setB1_img4(encodedImage4);
            }
        }catch (Exception e){
            Log.e(TAG, "imageB64Encoder 4 : "+e.toString() );
        }


    }

    public void sendDatToserver() {

        if (jsonToSend.length() > 0) {

            RestApii restApii = new RestApii();
            restApii.execute();
        }
    }






    // display current date
    public void setCurrentDateOnView() {

        //dpResult = (DatePicker) findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        // set current date into textview
        DateOfInterview.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(year).append("/").append(month + 1).append("/")
                .append(day).append(""));
    }

    @OnClick(R.id.capacity_building_date_of_interview)
    public void onViewTvDateClickListner() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DateOfInterview.setShowSoftInputOnFocus(false);
        }
        showDialog(DATE_DIALOG_ID);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            DateOfInterview.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(""));
        }
    };





    private class RestApii extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            String text = null;
            text = POST(Constant.URL_DATA_SEND);
            Log.d("Capacity Building", "RAW resposne" + text);

            return text.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (mProgressDlg != null && mProgressDlg.isShowing()) {
                mProgressDlg.dismiss();
            }


            Log.d(TAG, "on post resposne" + result);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                dataSentStatus = jsonObject.getString("status");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (dataSentStatus.equals("200")) {
//                Toast.makeText(context, "Data sent successfully", Toast.LENGTH_SHORT).show();

                String formNameToSend;
                if (!generalFormModel.getG_10().equals("") && !generalFormModel.getG2().equals("")) {

                    formNameToSend = (generalFormModel.getG_10() + "_" + generalFormModel.getG2());
                } else {
                    formNameToSend = ("Lumanti");
                }

                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                dateString = sdf.format(date);
//
                String[] data = new String[]{"1", formNameToSend, dateString, jsonToSend, "",
                        "" + "", "Sent", "0"};

                DataBaseForm_Sent dataBaseFormSent = new DataBaseForm_Sent(context);
                dataBaseFormSent.open();
                long id = dataBaseFormSent.insertIntoTable_Main(data);
                Log.e("dbID", "" + id);
                dataBaseFormSent.close();

                if (!Constant.formID.equals("")) {
                    DataBaseForm_NotSent dataBaseNepalPublicHealthNotSent = new DataBaseForm_NotSent(getApplicationContext());
                    dataBaseNepalPublicHealthNotSent.open();
                    dataBaseNepalPublicHealthNotSent.dropRowNotSentForms(Constant.formID);
//                Toast.makeText(getActivity() ,resultCur.get(position).date+ " Long Clicked "+id , Toast.LENGTH_SHORT ).show();
                    dataBaseNepalPublicHealthNotSent.close();
                }
//
//
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                Toast.makeText(context, R.string.data_sent_successfully, Toast.LENGTH_SHORT).show();

                final Dialog showDialog = new Dialog(context);
                showDialog.setContentView(R.layout.thank_you_popup);
                final Button yes = (Button) showDialog.findViewById(R.id.buttonYes);
                final Button no = (Button) showDialog.findViewById(R.id.buttonNo);

                showDialog.setTitle("Successfully Sent");
                showDialog.setCancelable(false);
                showDialog.show();
                showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog.dismiss();
// ====================================== reinitialize constant variable=================================================//
                        reinitializeConstantVariable();

                        Intent intent = new Intent(SaveSendActivity.this, NissaNoInputActivity.class);
                        startActivity(intent);
//                                finish();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog.dismiss();
                        Intent intent = new Intent(SaveSendActivity.this, HomeListActivity.class);
                        startActivity(intent);
                    }
                });
            }


        }

        public String POST(String urll) {
            String result = "";
            URL url;

            try {
                url = new URL(urll);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("data", jsonToSend);

                String query = builder.build().getEncodedQuery();

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
                } else {
                    result = "";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

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
                Intent intent = new Intent(SaveSendActivity.this, SavedFormsActivity.class);
                startActivity(intent);
                break;

            default:

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
//        return;
//
//    }

    public void initializeUI() {
        tvRecommendationRegardingReconstruction.setText(generalFormModel.getF5());
        tvAnySpecificInfo.setText(generalFormModel.getF6());
        tvIdentifiedGap.setText(generalFormModel.getF7());
        SurveyorsName.setText(generalFormModel.getF8());
        DateOfInterview.setText(generalFormModel.getF9());
    }

    public void reinitializeConstantVariable() {

        Constant.countGeneral = 0;
        Constant.countDemographic = 0;
        Constant.countReconstruction = 0;
        Constant.countEarthquakeRelief = 0;
        Constant.countReconstructionGPS = 0;
        Constant.countSaveSend = 0;

        Constant.takenimg1 = false;
        Constant.takenimg2 = false;
        Constant.takenimg3 = false;
        Constant.takenimg4 = false;

        Constant.takenimg1Name = "";
        Constant.takenimg2Name = "";
        Constant.takenimg3Name = "";
        Constant.takenimg4Name = "";
    }




}
