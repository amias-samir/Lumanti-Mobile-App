package np.com.naxa.lumanti.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import java.text.SimpleDateFormat;
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
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.database.DataBaseForm_NotSent;
import np.com.naxa.lumanti.database.DataBaseForm_Sent;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.GeneralFormModel;

public class CapacityBuildingActivity extends AppCompatActivity {

    private static final String TAG = "CapacityBuilding";
    Toolbar toolbar;
    GeneralFormModel generalFormModel;


    @BindView(R.id.capacity_building_participate_in_community_meeting)
    Spinner spinnerParticipateInCommunityMeeting;
    @BindView(R.id.capacity_building_participate_in_disaster_training)
    Spinner spinnerParticipateInDisasterTraining;
    @BindView(R.id.capacity_building_disaster_training_type)
    Spinner spinnerDisasterTrainingType;
    @BindView(R.id.capacity_building_other_disaster_training)
    AutoCompleteTextView tvOtherDisasterTraining;
    @BindView(R.id.capacity_building_stockpiling_idea)
    Spinner spinnerStockpilingIdea;
    @BindView(R.id.capacity_building_disaster_community_member)
    Spinner spinnerDisasterCommunityMember;
    @BindView(R.id.water_sanitation_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacity_building);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Capacity Building");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        generalFormModel = new GeneralFormModel();
        generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
//        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();


        spinnerDisasterTrainingType.setVisibility(View.INVISIBLE);
        tvOtherDisasterTraining.setVisibility(View.INVISIBLE);


    }


    @OnClick(R.id.water_sanitation_next)
    public void NextPage() {

        generalFormModel.setF1(spinnerParticipateInCommunityMeeting.getSelectedItem().toString());
        generalFormModel.setF2(spinnerParticipateInDisasterTraining.getSelectedItem().toString());
        generalFormModel.setF2_a(spinnerDisasterTrainingType.getSelectedItem().toString());
        generalFormModel.setF2_b(tvOtherDisasterTraining.getText().toString());
        generalFormModel.setF3(spinnerStockpilingIdea.getSelectedItem().toString());
        generalFormModel.setF4(spinnerDisasterCommunityMember.getSelectedItem().toString());

        Intent intent = new Intent(CapacityBuildingActivity.this, SaveSendActivity.class);
        intent.putExtra("generalFormModel", generalFormModel);
        startActivity(intent);
    }






    @OnItemSelected(R.id.capacity_building_participate_in_disaster_training)
    public void spinnerDisasterTrainingSelected(Spinner spinner, int position) {
        // code here
        int id = position ;
        String selected_item = spinnerParticipateInDisasterTraining.getSelectedItem().toString();
        if (selected_item.equals("Yes")){
            spinnerDisasterTrainingType.setVisibility(View.VISIBLE);
        }
        else {
            spinnerDisasterTrainingType.setVisibility(View.INVISIBLE);
        }
    }

    @OnItemSelected(R.id.capacity_building_disaster_training_type)
    public void spinnerTrainingTypeSelected(Spinner spinner, int position) {
        // code here
        int id = position ;
        String selected_item = spinnerDisasterTrainingType.getSelectedItem().toString();
        if (selected_item.equals("Others")){
            tvOtherDisasterTraining.setVisibility(View.VISIBLE);
        }
        else {
            tvOtherDisasterTraining.setVisibility(View.INVISIBLE);
        }
    }


    }
