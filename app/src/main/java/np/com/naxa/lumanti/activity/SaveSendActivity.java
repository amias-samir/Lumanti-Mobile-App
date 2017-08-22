package np.com.naxa.lumanti.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.database.DataBaseForm_NotSent;
import np.com.naxa.lumanti.database.DataBaseForm_Sent;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.GeneralFormModel;

public class SaveSendActivity extends AppCompatActivity {

    Toolbar toolbar;

    GeneralFormModel generalFormModel;

    Gson gson = new Gson();
    String jsonToSend;

    ProgressDialog mProgressDlg;
    Context context = this;
    String dataSentStatus, dateString;


    NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;


    @BindView(R.id.capacity_building_recommendation_regarding_reconstruction)
    AutoCompleteTextView tvRecommendationRegardingReconstruction;
    @BindView(R.id.capacity_building_any_specific_info)
    AutoCompleteTextView tvAnySpecificInfo;
    @BindView(R.id.capacity_building_identified_gap)
    AutoCompleteTextView tvIdentifiedGap;
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

        generalFormModel = new GeneralFormModel();
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
                                    "" + "", "Not Sent", "0"};

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
                                    Intent intent = new Intent(SaveSendActivity.this, MainActivity.class);
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

        Constant.countSaveSend = 1;

        Intent intent = new Intent(SaveSendActivity.this, EarthquakeReliefStatusActivity.class);
        intent.putExtra("PgeneralFormModel", generalFormModel);
        startActivity(intent);
    }


    public void setGeneralFormModelValue() {


        generalFormModel.setF5(tvRecommendationRegardingReconstruction.getText().toString());
        generalFormModel.setF6(tvAnySpecificInfo.getText().toString());
        generalFormModel.setF7(tvIdentifiedGap.getText().toString());

    }


    public void convertDataToJson() {

        jsonToSend = gson.toJson(generalFormModel);

        Log.e("capacity_building", "convertDatToJson: " + jsonToSend);
    }

    public void sendDatToserver() {

        if (jsonToSend.length() > 0) {

            RestApii restApii = new RestApii();
            restApii.execute();
        }
    }



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

                if(!Constant.formID.equals("")){
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

                        Intent intent = new Intent(SaveSendActivity.this, MainActivity.class);
                        startActivity(intent);
//                                finish();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog.dismiss();
//                            Intent intent = new Intent(PregnentWomenActivity.this, MainActivity.class);
//                            startActivity(intent);
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

    public void initializeUI(){
        tvRecommendationRegardingReconstruction.setText(generalFormModel.getF5());
        tvAnySpecificInfo.setText(generalFormModel.getF6());
        tvIdentifiedGap.setText(generalFormModel.getF7());
    }

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

}
