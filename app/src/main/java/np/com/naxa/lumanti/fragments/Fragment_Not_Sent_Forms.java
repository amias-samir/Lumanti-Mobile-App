package np.com.naxa.lumanti.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.activity.HomeListActivity;
import np.com.naxa.lumanti.activity.MainActivity;
import np.com.naxa.lumanti.activity.NissaNoInputActivity;
import np.com.naxa.lumanti.activity.SaveSendActivity;
import np.com.naxa.lumanti.activity.SavedFormsActivity;
import np.com.naxa.lumanti.adapter.GridSpacingItemDecorator;
import np.com.naxa.lumanti.adapter.Not_Sent_Forms_Adapter;
import np.com.naxa.lumanti.adapter.RecyclerItemClickListener;
import np.com.naxa.lumanti.database.DataBaseForm_NotSent;
import np.com.naxa.lumanti.database.DataBaseForm_Sent;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.Default_DIalog;
import np.com.naxa.lumanti.model.GeneralFormModel;
import np.com.naxa.lumanti.model.ImageSavedFormModel;
import np.com.naxa.lumanti.model.SavedFormParameters;
import np.com.naxa.lumanti.network.retrofit.ErrorSupportCallback;
import np.com.naxa.lumanti.network.retrofit.NetworkApiClient;
import np.com.naxa.lumanti.network.retrofit.NetworkApiInterface;
import np.com.naxa.lumanti.network.retrofit.UploadResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static np.com.naxa.lumanti.network.retrofit.UrlClass.REQUEST_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Not_Sent_Forms extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static List<SavedFormParameters> resultCur = new ArrayList<>();
    Not_Sent_Forms_Adapter ca;
    Context context = getActivity();

    String jsonToSend;
    String jsonToParse;
    String DBid, form_name;
    String photoPathJSON;
    ImageSavedFormModel imageSavedFormModel;
    GeneralFormModel generalFormModel;
    String encodedImage1 = "", encodedImage2 = "", encodedImage3 = "", encodedImage4 = "";
    String TAG = "NOT_SENT_FRAG";

    ProgressDialog mProgressDlg;
    String dataSentStatus, dateString;
    NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;


    public Fragment_Not_Sent_Forms() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_not_send_form_list, container, false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.NewsList);

        imageSavedFormModel = new ImageSavedFormModel();
        generalFormModel = new GeneralFormModel();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorator(1, 5, true));

        //Check internet connection
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        createList();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                alert_editlist(position);

            }

            @Override
            public void onItemLongClick(View view, int position) {


            }
        }));

        return rootview;
    }

    //-------------------------------Method Dialog Box List for << REPORT DETAIL, SEND and DELETE >>-----------------------------------//
    protected void alert_editlist(final int position) {

        // TODO Auto-generated method stub
        final CharSequence[] items = {"Send", "Open", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Action");

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (items[item] == "Send") {
                    String id = resultCur.get(position).formId;
                    String jSon = resultCur.get(position).jSON;
                    jsonToParse = resultCur.get(position).jSON;
                    photoPathJSON = resultCur.get(position).photo;
                    String gps = resultCur.get(position).gps;
                    DBid = resultCur.get(position).dbId;
                    String sent_Status = resultCur.get(position).status;
                    form_name = resultCur.get(position).formName;

                    if (networkInfo != null && networkInfo.isConnected()) {


                        mProgressDlg = new ProgressDialog(getActivity());
                        mProgressDlg.setMessage("Please wait...");
                        mProgressDlg.setIndeterminate(false);
                        mProgressDlg.setCancelable(false);
                        mProgressDlg.show();

                        mainJSONToModelClass();
                        jsonParserImagePath();
                        //imageB64Encoder();
                        convertDataToJson();
//                        sendDatToserver();
                        sendJsonToServerretrofit();
                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                } else if (items[item] == "Open") {
                    String id = resultCur.get(position).formId;
                    String jSon = resultCur.get(position).jSON;
                    String photo = resultCur.get(position).photo;
                    String gps = resultCur.get(position).gps;
                    String DBid = resultCur.get(position).dbId;
                    String sent_Status = resultCur.get(position).status;
                    String form_name = resultCur.get(position).formName;
                    loadForm(id, jSon, photo, DBid, sent_Status, form_name);


                } else if (items[item] == "Delete") {
                    DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;

                    final Dialog showDialog = new Dialog(getActivity());
                    showDialog.setContentView(R.layout.delete_dialog);
                    TextView tvDisplay = (TextView) showDialog.findViewById(R.id.textViewDefaultDialog);
                    Button btnOk = (Button) showDialog.findViewById(R.id.button_delete);
                    Button cancle = (Button) showDialog.findViewById(R.id.button_cancle);
                    showDialog.setTitle("Are You Sure ??");
                    tvDisplay.setText("Are you sure you want to delete the data ??");
                    showDialog.setCancelable(true);
                    showDialog.show();
                    showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

                    btnOk.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub


                            DataBaseForm_NotSent dataBaseNepalPublicHealthNotSent = new DataBaseForm_NotSent(getActivity());
                            dataBaseNepalPublicHealthNotSent.open();
                            dataBaseNepalPublicHealthNotSent.dropRowNotSentForms(resultCur.get(position).dbId);
//                Toast.makeText(getActivity() ,resultCur.get(position).date+ " Long Clicked "+id , Toast.LENGTH_SHORT ).show();
                            dataBaseNepalPublicHealthNotSent.close();
                            showDialog.dismiss();
                            createList();
                        }
                    });
                    cancle.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            showDialog.dismiss();
                        }
                    });

                }
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    public void loadForm(String formId, String jsonData, String photoJson, String DBid, String status, String form_name) {
        switch (formId) {

            case "1":

                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                Constant.isFomSavedForm = true;
//                Constant.countGeneral = 1 ;
                intent1.putExtra("JSON1", jsonData);
                intent1.putExtra("PhotoJSON", photoJson);
//
//                intent1.putExtra("photo" , photo);
//                intent1.putExtra("gps" , gps) ;
                intent1.putExtra("DBid", DBid);
                intent1.putExtra("sent_Status", status);
                intent1.putExtra("form_name", form_name);

                Log.e("Not Sent Fragment", "loadForm: " + jsonData);
                Log.e("Not Sent Fragment", "loadForm: " + DBid);
                Log.e("Not Sent Fragment", "loadForm: " + status);
                Log.e("Not Sent Fragment", "loadForm: " + form_name);

                startActivity(intent1);
                break;


        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
            Log.e(TAG, "RAW resposne" + text);


            return text.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (mProgressDlg != null && mProgressDlg.isShowing()) {
                mProgressDlg.dismiss();
            }


            Log.e(TAG, "on post resposne" + result);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                dataSentStatus = jsonObject.getString("status");

//                dataSentStatus = null;

            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {

                if (dataSentStatus.equals("200")) {

                    long date = System.currentTimeMillis();

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                    dateString = sdf.format(date);

                    String[] data = new String[]{"1", form_name, dateString, jsonToParse, "",
                            "" + photoPathJSON, "Sent", "0"};

                    DataBaseForm_Sent dataBaseFormSent = new DataBaseForm_Sent(getActivity());
                    dataBaseFormSent.open();
//                long id =
                    dataBaseFormSent.insertIntoTable_Main(data);
//                Log.e("dbID", "" + id);
                    dataBaseFormSent.close();

                    DataBaseForm_NotSent dataBaseForm_notSent = new DataBaseForm_NotSent(getActivity());
                    dataBaseForm_notSent.open();
                    dataBaseForm_notSent.dropRowNotSentForms(DBid);

                    Toast.makeText(getActivity(), "Data sent successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), SavedFormsActivity.class);
                    startActivity(intent);
                    //createList();

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "               Error !!! \n Please retry again later ", Toast.LENGTH_SHORT).show();
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
                Log.e(TAG, "SAMIR RESPONSE CODE : " + responseCode);
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

    private void createList() {
        resultCur.clear();
        DataBaseForm_NotSent dataBaseNepalPublicHealthNotSent = new DataBaseForm_NotSent(getActivity());
        dataBaseNepalPublicHealthNotSent.open();

        resultCur.addAll(dataBaseNepalPublicHealthNotSent.getAllNotSentForms());
        fillTable();
    }

    public void fillTable() {
        Log.e("FILLTABLE", "INSIDE FILL TABLE");
        ca = new Not_Sent_Forms_Adapter(resultCur);
        recyclerView.setAdapter(ca);
        Log.e("FILLTABLE", "AFTER FILL TABLE");
//        CheckValues.setValue();
    }


    //    convert json to model class
    public void mainJSONToModelClass() {
        // 2. JSON to Java object, read it from a Json String.
        Gson gson = new Gson();
        generalFormModel = gson.fromJson(jsonToParse, GeneralFormModel.class);
    }

    //    parse image path from photo json
    public void jsonParserImagePath() {

        try {
            JSONObject jsonObj = new JSONObject(photoPathJSON);

            imageSavedFormModel.setB1_img1_path(jsonObj.getString("B1_img1_path"));
            imageSavedFormModel.setB1_img2_path(jsonObj.getString("B1_img2_path"));
            imageSavedFormModel.setB1_img3_path(jsonObj.getString("B1_img3_path"));
            imageSavedFormModel.setB1_img4_path(jsonObj.getString("B1_img4_path"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    encode image to base64
    public void imageB64Encoder() {

//        image 1 encode
        try {
            if (!imageSavedFormModel.getB1_img1_path().equals(null) && !imageSavedFormModel.getB1_img1_path().equals("")) {
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
                Log.e(TAG, "imageB64Encoder1: " + encodedImage1);

            }
        } catch (Exception e) {
            Log.e(TAG, "imageB64Encoder1: " + e.toString());
        }

        //    ======================    image 2 encode======================================//
        try {
            if (!imageSavedFormModel.getB1_img2_path().equals(null) && !imageSavedFormModel.getB1_img2_path().equals("")) {
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
                Log.e(TAG, "imageB64Encoder2: " + encodedImage2);

            }
        } catch (Exception e) {
            Log.e(TAG, "imageB64Encoder2: " + e.toString());
        }

        //    ======================    image 3 encode======================================//
        try {
            if (!imageSavedFormModel.getB1_img3_path().equals(null) && !imageSavedFormModel.getB1_img3_path().equals("")) {
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
                Log.e(TAG, "imageB64Encoder3: " + encodedImage3);

            }
        } catch (Exception e) {
            Log.e(TAG, "imageB64Encoder3: " + e.toString());
        }

        //    ======================    image 4 encode======================================//
        try {
            if (!imageSavedFormModel.getB1_img4_path().equals(null) && !imageSavedFormModel.getB1_img4_path().equals("")) {
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
                Log.e(TAG, "imageB64Encoder4: " + encodedImage4);

            }
        } catch (Exception e) {
            Log.e(TAG, "imageB64Encoder 4 : " + e.toString());
        }


    }

    //    convert whole data to Mainjson
    public void convertDataToJson() {
        Gson gson = new Gson();
        jsonToSend = gson.toJson(generalFormModel);


        Log.e(TAG, "convertDatToJson: " + jsonToSend);
    }

    public void sendJsonToServerretrofit() {


        if (jsonToSend.length() > 0) {


            int imageCount = 0;
            if (imageSavedFormModel != null && !imageSavedFormModel.getB1_img1_path().equals("")) {
                imageCount++;
            }
            if (imageSavedFormModel != null && !imageSavedFormModel.getB1_img2_path().equals("")) {
                imageCount++;
            }
            if (imageSavedFormModel != null && !imageSavedFormModel.getB1_img3_path().equals("")) {
                imageCount++;
            }
            if (imageSavedFormModel != null && !imageSavedFormModel.getB1_img4_path().equals("")) {
                imageCount++;
            }

            if (imageCount == 0) {
                if (mProgressDlg.isShowing() && mProgressDlg != null) {
                    mProgressDlg.dismiss();
                }
                Default_DIalog.showDefaultDialog(getActivity(), "Error", "Photo doesn't exist in storage");
                return;
            }

            int totalCount = imageCount;
            int counter = imageCount;

//            multiple image upload
            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[imageCount];
            if (imageSavedFormModel != null && !imageSavedFormModel.getB1_img1_path().equals("")) {
                int index = totalCount - counter--;
                Log.d(TAG, "requestUploadSurvey: survey image " + index + "  " + imageSavedFormModel.getB1_img1_path());
                File imageFile = new File(imageSavedFormModel.getB1_img1_path());
//                Uri ImageToBeUploaded = FileProvider.getUriForFile(
//                        getActivity(),
//                        "np.com.naxa.lumanti.fileprovider", imageFile);
//
//                Log.d(TAG, "sendJsonToServerretrofit: "+ImageToBeUploaded.toString());
//                Log.d(TAG, "sendJsonToServerretrofit: "+imageFile.toString());

                if (!imageFile.exists()) {
                    if (mProgressDlg.isShowing() && mProgressDlg != null) {
                        mProgressDlg.dismiss();
                    }
                    Default_DIalog.showDefaultDialog(getActivity(), "Error", "Front Photo doesn't exist in storage");
                    return;
                }
//                RequestBody surveyBody = RequestBody.create(MediaType.parse(getContentResolver().getType(ImageToBeUploaded)), imageFile);
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("photo[]", imageFile.getName(), surveyBody);
            }
            if (imageSavedFormModel != null && !imageSavedFormModel.getB1_img2_path().equals("")) {
                int index = totalCount - counter--;
                Log.d(TAG, "requestUploadSurvey: survey image " + index + "  " + imageSavedFormModel.getB1_img2_path());
                File imageFile = new File(imageSavedFormModel.getB1_img2_path());
//                Uri ImageToBeUploaded = FileProvider.getUriForFile(
//                        getActivity(),
//                        "np.com.naxa.lumanti.fileprovider", imageFile);

                if (!imageFile.exists()) {
                    if (mProgressDlg.isShowing() && mProgressDlg != null) {
                        mProgressDlg.dismiss();
                    }
                    Default_DIalog.showDefaultDialog(getActivity(), "Error", "Left Photo doesn't exist in storage");
                    return;
                }
//                RequestBody surveyBody = RequestBody.create(MediaType.parse(getContentResolver().getType(ImageToBeUploaded)), imageFile);
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("photo[]", imageFile.getName(), surveyBody);
            }
            if (imageSavedFormModel != null && !imageSavedFormModel.getB1_img3_path().equals("")) {
                int index = totalCount - counter--;
                Log.d(TAG, "requestUploadSurvey: survey image " + index + "  " + imageSavedFormModel.getB1_img3_path());
                File imageFile = new File(imageSavedFormModel.getB1_img3_path());
//                Uri ImageToBeUploaded = FileProvider.getUriForFile(
//                        getActivity(),
//                        "np.com.naxa.lumanti.fileprovider", imageFile);

                if (!imageFile.exists()) {
                    if (mProgressDlg.isShowing() && mProgressDlg != null) {
                        mProgressDlg.dismiss();
                    }
                    Default_DIalog.showDefaultDialog(getActivity(), "Error", "Right Photo doesn't exist in storage");
                    return;
                }
//                RequestBody surveyBody = RequestBody.create(MediaType.parse(getContentResolver().getType(ImageToBeUploaded)), imageFile);
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("photo[]", imageFile.getName(), surveyBody);
            }
            if (imageSavedFormModel != null && !imageSavedFormModel.getB1_img4_path().equals("")) {
                int index = totalCount - counter--;
                Log.d(TAG, "requestUploadSurvey: survey image " + index + "  " + imageSavedFormModel.getB1_img4_path());
                File imageFile = new File(imageSavedFormModel.getB1_img4_path());
//                Uri ImageToBeUploaded = FileProvider.getUriForFile(
//                        getActivity(),
//                        "np.com.naxa.lumanti.fileprovider", imageFile);

                if (!imageFile.exists()) {
                    if (mProgressDlg.isShowing() && mProgressDlg != null) {
                        mProgressDlg.dismiss();
                    }
                    Default_DIalog.showDefaultDialog(getActivity(), "Error", "Back Photo doesn't exist in storage");
                    return;
                }
//                RequestBody surveyBody = RequestBody.create(MediaType.parse(getContentResolver().getType(ImageToBeUploaded)), imageFile);
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("photo[]", imageFile.getName(), surveyBody);
            }

            RequestBody data = RequestBody.create(MediaType.parse("text/plain"), jsonToSend);


            NetworkApiInterface apiService = NetworkApiClient.getAPIClient().create(NetworkApiInterface.class);

//        Call<UploadResponse> call = apiService.uploadLumantiForm(jsonToSend);
            Call<UploadResponse> call = apiService.uploadFormWithImageFile(surveyImagesParts, data);
            call.enqueue(new ErrorSupportCallback<>(new Callback<UploadResponse>() {

                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {

                    if (mProgressDlg.isShowing() && mProgressDlg != null) {
                        mProgressDlg.dismiss();
                    }

                    if (response == null) {
                        Toast.makeText(getActivity(), "null response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    handleLoginResponse(response.body());
                }


                private void handleLoginResponse(UploadResponse uploadResponse) {
                    switch (uploadResponse.getStatus()) {
                        case REQUEST_OK:

                            handleLoginSucess();
                            break;
                        default:
                            Toast.makeText(getActivity(), uploadResponse.getData(), Toast.LENGTH_SHORT).show();

                            break;
                    }
                }

                private void handleLoginSucess() {
                    long date = System.currentTimeMillis();

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                    dateString = sdf.format(date);

                    String[] data = new String[]{"1", form_name, dateString, jsonToParse, "",
                            "" + photoPathJSON, "Sent", "0"};

                    DataBaseForm_Sent dataBaseFormSent = new DataBaseForm_Sent(getActivity());
                    dataBaseFormSent.open();
//                long id =
                    dataBaseFormSent.insertIntoTable_Main(data);
//                Log.e("dbID", "" + id);
                    dataBaseFormSent.close();

                    DataBaseForm_NotSent dataBaseForm_notSent = new DataBaseForm_NotSent(getActivity());
                    dataBaseForm_notSent.open();
                    dataBaseForm_notSent.dropRowNotSentForms(DBid);

                    Toast.makeText(getActivity(), "Data sent successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), SavedFormsActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    if (mProgressDlg.isShowing() && mProgressDlg != null) {
                        mProgressDlg.dismiss();
                    }
                    Log.d(TAG, "onFailure: " + t.toString());

                    String message = "Some Error Occured! \n" + t.toString();

                    if (t instanceof SocketTimeoutException) {
                        message = "Socket Time out. Please try again.";
                    }

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                }

            }));
        } else {
            Default_DIalog.showDefaultDialog(getActivity(), "Error", "No data to send");
            return;
        }
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

    public void sendJsonToServerretrofitold() {
        NetworkApiInterface apiService = NetworkApiClient.getAPIClient().create(NetworkApiInterface.class);

        Call<UploadResponse> call = apiService.uploadLumantiForm(jsonToSend);
        call.enqueue(new ErrorSupportCallback<>(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {

                if (mProgressDlg.isShowing() && mProgressDlg != null) {
                    mProgressDlg.dismiss();
                }

                if (response == null) {
                    Toast.makeText(getActivity(), "null response", Toast.LENGTH_SHORT).show();
                    return;
                }

                handleLoginResponse(response.body());
            }


            private void handleLoginResponse(UploadResponse uploadResponse) {
                switch (uploadResponse.getStatus()) {
                    case REQUEST_OK:

                        handleLoginSucess();
                        break;
                    default:
                        Toast.makeText(getActivity(), uploadResponse.getData(), Toast.LENGTH_SHORT).show();

                        break;
                }
            }

            private void handleLoginSucess() {
                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                dateString = sdf.format(date);

                String[] data = new String[]{"1", form_name, dateString, jsonToParse, "",
                        "" + photoPathJSON, "Sent", "0"};

                DataBaseForm_Sent dataBaseFormSent = new DataBaseForm_Sent(getActivity());
                dataBaseFormSent.open();
//                long id =
                dataBaseFormSent.insertIntoTable_Main(data);
//                Log.e("dbID", "" + id);
                dataBaseFormSent.close();

                DataBaseForm_NotSent dataBaseForm_notSent = new DataBaseForm_NotSent(getActivity());
                dataBaseForm_notSent.open();
                dataBaseForm_notSent.dropRowNotSentForms(DBid);

                Toast.makeText(getActivity(), "Data sent successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), SavedFormsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                if (mProgressDlg.isShowing() && mProgressDlg != null) {
                    mProgressDlg.dismiss();
                }
                String message = "Some Error Occured!";

                if (t instanceof SocketTimeoutException) {
                    message = "Socket Time out. Please try again.";
                }

                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            }

        }));
    }

}

