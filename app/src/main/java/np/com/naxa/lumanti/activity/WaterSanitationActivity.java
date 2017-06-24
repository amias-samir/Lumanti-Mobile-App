package np.com.naxa.lumanti.activity;

import android.content.Intent;
import android.os.Bundle;
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

public class WaterSanitationActivity extends AppCompatActivity {

    Toolbar toolbar;
    GeneralFormModel generalFormModel;


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
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        generalFormModel = new GeneralFormModel();
        generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
//        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();

        tvOtherDrinkingWaterSource.setVisibility(View.INVISIBLE);

    }

    @OnClick(R.id.water_sanitation_next)
    public void NextPage() {

        generalFormModel.setE1(spinnerDrinkingWaterSource.getSelectedItem().toString());
        generalFormModel.setE1_a(tvOtherDrinkingWaterSource.getText().toString());
        generalFormModel.setE2(spinnerToiletType.getSelectedItem().toString());

        Intent intent = new Intent(WaterSanitationActivity.this, CapacityBuildingActivity.class);
        intent.putExtra("generalFormModel", generalFormModel);
        startActivity(intent);
    }


    @OnItemSelected(R.id.water_sanitation_drinking_water_source)
    public void spinnerOtherWaterSelected(Spinner spinner, int position) {
        // code here
        int id = position ;
        String selected_item = spinnerDrinkingWaterSource.getSelectedItem().toString();
        if (selected_item.equals("Others")){
            tvOtherDrinkingWaterSource.setVisibility(View.VISIBLE);
        }
        else {
            tvOtherDrinkingWaterSource.setVisibility(View.INVISIBLE);
        }
    }
}
