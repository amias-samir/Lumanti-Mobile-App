package np.com.naxa.lumanti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.GeneralFormModel;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    @BindView(R.id.general_info_damage_type)
    Spinner spinnerDamageType;
    @BindView(R.id.general_info_district_name)
    Spinner spinnerDistrictName;
    @BindView(R.id.general_info_district_code)
    AutoCompleteTextView tvDistrictCode;
    @BindView(R.id.general_info_rural_municipality)
    AutoCompleteTextView tvRuralMunicipality;
    @BindView(R.id.general_info_current_ward_no)
    AutoCompleteTextView tvCurrentWardNo;
    @BindView(R.id.general_info_previous_vdc_mun)
    AutoCompleteTextView tvPreviousVdcMun;
    @BindView(R.id.general_info_previous_ward_no)
    AutoCompleteTextView tvPreviousWardNo;
    @BindView(R.id.general_info_tole)
    AutoCompleteTextView tvTole;
    @BindView(R.id.general_info_house_code)
    AutoCompleteTextView tvHouseCode;
    @BindView(R.id.general_info_nissa_no)
    AutoCompleteTextView tvNissaNo;
    @BindView(R.id.general_info_citizenship_no)
    AutoCompleteTextView tvCitizenshipNo;
    @BindView(R.id.general_info_pa_no)
    AutoCompleteTextView tvPaNo;
    @BindView(R.id.general_info_next)
    Button btnNext;

    ArrayAdapter distNamedpt, damageTypeadpt ;

    String h_damage, dist_name, dist_code, rural_mun, current_ward, prev_vdc_name, prev_ward_num, tole, h_code, nissa_num,
            citizen_num, pa_num ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("General Information");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        NextPage();

    }

    @OnClick(R.id.general_info_next)
    public void NextPage() {


//        dist_code = tvDistrictCode.getText().toString();


        GeneralFormModel generalFormModel = new GeneralFormModel();
        generalFormModel.setG1(spinnerDamageType.getSelectedItem().toString());
        generalFormModel.setG2(spinnerDistrictName.getSelectedItem().toString());
        generalFormModel.setG3(tvDistrictCode.getText().toString());
        generalFormModel.setG4(tvRuralMunicipality.getText().toString());
        generalFormModel.setG5(tvCurrentWardNo.getText().toString());
        generalFormModel.setG6(tvPreviousVdcMun.getText().toString());
        generalFormModel.setG7(tvPreviousWardNo.getText().toString());
        generalFormModel.setG8(tvTole.getText().toString());
        generalFormModel.setG9(tvHouseCode.getText().toString());
        generalFormModel.setG_10(tvNissaNo.getText().toString());
        generalFormModel.setG_11(tvCitizenshipNo.getText().toString());
        generalFormModel.setG_12(tvPaNo.getText().toString());




        Intent intent = new Intent(MainActivity.this, DemographicInfoActivity.class);

        intent.putExtra("generalFormModel", generalFormModel);
        startActivity(intent);
    }

}
