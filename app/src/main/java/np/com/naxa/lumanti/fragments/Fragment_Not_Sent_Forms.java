package np.com.naxa.lumanti.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import java.text.SimpleDateFormat;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import np.com.naxa.lumanti.activity.MainActivity;
import np.com.naxa.lumanti.activity.SavedFormsActivity;
import np.com.naxa.lumanti.adapter.GridSpacingItemDecorator;
import np.com.naxa.lumanti.adapter.Not_Sent_Forms_Adapter;
import np.com.naxa.lumanti.adapter.RecyclerItemClickListener;
import np.com.naxa.lumanti.database.DataBaseForm_NotSent;
import np.com.naxa.lumanti.database.DataBaseForm_Sent;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.SavedFormParameters;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Not_Sent_Forms extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static List<SavedFormParameters> resultCur = new ArrayList<>();
    Not_Sent_Forms_Adapter ca;
    Context context = getActivity() ;

    String jsonToSend ;
    String DBid, form_name;

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
        final CharSequence[] items = {"Send","Open", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Action");

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (items[item] == "Send") {
                    String id = resultCur.get(position).formId;
                    String jSon = resultCur.get(position).jSON;
                    jsonToSend = resultCur.get(position).jSON;
                    String photo = resultCur.get(position).photo;
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
                        sendDatToserver();
                    }else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }
                else if (items[item] == "Open") {
                    String id = resultCur.get(position).formId;
                    String jSon = resultCur.get(position).jSON;
                    String photo = resultCur.get(position).photo;
                    String gps = resultCur.get(position).gps;
                    String DBid = resultCur.get(position).dbId;
                    String sent_Status = resultCur.get(position).status;
                    String form_name = resultCur.get(position).formName;
                    loadForm(id, jSon, DBid, sent_Status, form_name);

                }
                else if (items[item] == "Delete") {
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




    public void loadForm(String formId, String jsonData , String DBid, String status, String form_name){
        switch (formId){

            case "1" :

                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                Constant.isFomSavedForm = true ;
                intent1.putExtra("JSON1", jsonData);
//                intent1.putExtra("photo" , photo);
//                intent1.putExtra("gps" , gps) ;
                intent1.putExtra("DBid", DBid);
                intent1.putExtra("sent_Status", status);
                intent1.putExtra("form_name", form_name);
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
            Log.d("Capacity Building", "RAW resposne" + text);

            return text.toString();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (mProgressDlg != null && mProgressDlg.isShowing()) {
                mProgressDlg.dismiss();
            }


            Log.d("Not sent Fragment", "on post resposne" + result);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                dataSentStatus = jsonObject.getString("status");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (dataSentStatus.equals("200")) {

                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                dateString = sdf.format(date);

                String[] data = new String[]{"1", form_name, dateString, jsonToSend, "",
                        "" + "", "Sent", "0"};

                DataBaseForm_Sent dataBaseFormSent = new DataBaseForm_Sent(getActivity());
                dataBaseFormSent.open();
                long id = dataBaseFormSent.insertIntoTable_Main(data);
                Log.e("dbID", "" + id);
                dataBaseFormSent.close();

                DataBaseForm_NotSent dataBaseForm_notSent = new DataBaseForm_NotSent(getActivity());
                dataBaseForm_notSent.open();
                dataBaseForm_notSent.dropRowNotSentForms(DBid);

                Toast.makeText(getActivity(), "Data sent successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(),SavedFormsActivity.class);
                startActivity(intent);
//                createList();

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

}

