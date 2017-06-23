package np.com.naxa.lumanti.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.model.GeneralFormModel;

public class CapacityBuildingActivity extends AppCompatActivity {

    Toolbar toolbar;
    GeneralFormModel generalFormModel ;

    NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacity_building);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Capacity Building");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        generalFormModel = new GeneralFormModel();
        generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();


        spinnerDisasterTrainingType.setVisibility(View.INVISIBLE);
        tvOtherDisasterTraining.setVisibility(View.INVISIBLE);

        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


    }





    @OnClick({R.id.water_sanitation_send, R.id.water_sanitation_save})
    public void SaveSend(View view) {
        switch (view.getId()) {
            case R.id.water_sanitation_send:

                setGeneralFormModelValue();

                if (networkInfo != null && networkInfo.isConnected()) {



                }
                 else {
            final View coordinatorLayoutView = findViewById(R.id.main_activity);
            Snackbar.make(coordinatorLayoutView, "No internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Retry", null).show();
        }


                break;

            case R.id.water_sanitation_save:

                break;
        }
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




    public void setGeneralFormModelValue(){

        generalFormModel.setF1(spinnerParticipateInCommunityMeeting.getSelectedItem().toString());
        generalFormModel.setF2(spinnerParticipateInDisasterTraining.getSelectedItem().toString());
        generalFormModel.setF2_a(spinnerDisasterTrainingType.getSelectedItem().toString());
        generalFormModel.setF2_b(tvOtherDisasterTraining.getText().toString());
        generalFormModel.setF3(spinnerStockpilingIdea.getSelectedItem().toString());
        generalFormModel.setF4(spinnerDisasterCommunityMember.getSelectedItem().toString());
        generalFormModel.setF5(tvRecommendationRegardingReconstruction.getText().toString());
        generalFormModel.setF6(tvAnySpecificInfo.getText().toString());
        generalFormModel.setF7(tvIdentifiedGap.getText().toString());

    }

}
