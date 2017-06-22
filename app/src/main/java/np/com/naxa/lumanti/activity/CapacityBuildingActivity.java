package np.com.naxa.lumanti.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.lumanti.R;

public class CapacityBuildingActivity extends AppCompatActivity {

    Toolbar toolbar;
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
    }

    @OnClick({R.id.water_sanitation_send, R.id.water_sanitation_save})
    public void SaveSend(View view) {
        switch (view.getId()) {
            case R.id.water_sanitation_send:

                break;

            case R.id.water_sanitation_save:

                break;
        }
    }
}
