package np.com.naxa.lumanti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.lumanti.R;

public class WaterSanitationActivity extends AppCompatActivity {

    Toolbar toolbar;
    @BindView(R.id.water_sanitation_drinking_water_source)
    Spinner spinnerDrinkingWaterSource;
    @BindView(R.id.water_sanitation_other_drinking_water_source)
    AutoCompleteTextView tvOtherDrinkingWaterSource;
    @BindView(R.id.water_sanitation_toilet_type)
    Spinner spinnerToiletType;
    @BindView(R.id.water_sanitation_next)
    Button btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_sanitation);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Water Sanitation");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @OnClick(R.id.water_sanitation_next)
    public void NextPage() {
        Intent intent = new Intent(WaterSanitationActivity.this, CapacityBuildingActivity.class);
        startActivity(intent);
    }
}
