package np.com.naxa.lumanti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.model.GeneralFormModel;

public class DemographicInfoActivity extends AppCompatActivity {

    Toolbar toolbar;
    @BindView(R.id.demographic_info_head_name)
    AutoCompleteTextView tvHeadName;
    @BindView(R.id.demographic_info_head_sex)
    Spinner spinnerHeadSex;
    @BindView(R.id.general_info_district_code)
    AutoCompleteTextView tvDistrictCode;
    @BindView(R.id.demographic_info_below_5_no)
    AutoCompleteTextView tvBelow_5_No;
    @BindView(R.id.demographic_info_between5_14_no)
    AutoCompleteTextView tvBetween_5_14_No;
    @BindView(R.id.demographic_info_between_15_64_no)
    AutoCompleteTextView tvBetween_15_64_No;
    @BindView(R.id.demographic_info_above_65_no)
    AutoCompleteTextView tvAbove_65_No;
    @BindView(R.id.demographic_info_male_family_no)
    AutoCompleteTextView tvMaleFamilyNo;
    @BindView(R.id.demographic_info_female_family_no)
    AutoCompleteTextView tvFemaleFamilyNo;
    @BindView(R.id.demographic_info_othre_family_no)
    AutoCompleteTextView tvOthreFamilyNo;
    @BindView(R.id.general_info_family_mem_total)
    AutoCompleteTextView tvFamilyMemTotal;
    @BindView(R.id.demographic_info_dis_preg_lac)
    Spinner spinnerDisPregLac;
    @BindView(R.id.demographic_info_disability_type)
    AutoCompleteTextView tvDisabilityType;
    @BindView(R.id.demographic_info_before_after_disabled)
    Spinner spinnerBeforeAfterDisabled;
    @BindView(R.id.demographic_info_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographic_info);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Demographic Information");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



         GeneralFormModel generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
        Toast.makeText(this, ""+ generalFormModel.getA1(), Toast.LENGTH_SHORT).show();

    }



    @OnClick(R.id.demographic_info_next)
    public void NextPage() {


        Intent intent = new Intent(DemographicInfoActivity.this, ReconstructionStatusActivity.class);
        startActivity(intent);
    }
}
