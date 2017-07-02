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
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.GeneralFormModel;

public class DemographicInfoActivity extends AppCompatActivity {

    GeneralFormModel generalFormModel;

    Toolbar toolbar;
    @BindView(R.id.demographic_info_head_name)
    AutoCompleteTextView tvHeadName;
    @BindView(R.id.demographic_info_head_sex)
    Spinner spinnerHeadSex;
    @BindView(R.id.demographic_info_head_age)
    AutoCompleteTextView tvHeadAge;
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
    @BindView(R.id.demographic_info_specify_dis_preg_lac)
    Spinner spinnerSpecifyDisPregLac;
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
        spinnerSpecifyDisPregLac.setVisibility(View.INVISIBLE);
        tvDisabilityType.setVisibility(View.INVISIBLE);
        spinnerBeforeAfterDisabled.setVisibility(View.INVISIBLE);
        tvFamilyMemTotal.setEnabled(false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Demographic Information");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            generalFormModel = new GeneralFormModel();
         generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
//        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();

    }



    @OnClick(R.id.demographic_info_next)
    public void NextPage() {
        String b_5no,b15_64no,b5_14no,ab_65no,maleno,femaleno,otherno;



        b_5no = tvBelow_5_No.getText().toString();
        b5_14no = tvBetween_5_14_No.getText().toString();
        b15_64no = tvBetween_15_64_No.getText().toString();
        ab_65no = tvAbove_65_No.getText().toString();
        maleno = tvMaleFamilyNo.getText().toString();
        femaleno = tvFemaleFamilyNo.getText().toString();
        otherno = tvOthreFamilyNo.getText().toString();

        if(b_5no.isEmpty() || b_5no.equals("")){
            b_5no = "0";
        }
        if(b5_14no.isEmpty() || b5_14no.equals("")){
            b5_14no = "0";
        }
        if(b15_64no.isEmpty() || b15_64no.equals("")){
            b15_64no = "0";
        }
        if(ab_65no.isEmpty() || ab_65no.equals("")){
            ab_65no = "0";
        }
        if(maleno.isEmpty() || maleno.equals("")){
            maleno = "0";
        }
        if(femaleno.isEmpty() || femaleno.equals("")){
            femaleno = "0";
        }
        if(otherno.isEmpty() || otherno.equals("")){
            otherno = "0";
        }


        int below5 = Integer.parseInt(b_5no);
        int betn5_14 = Integer.parseInt(b5_14no);
        int betn15_64 = Integer.parseInt(b15_64no);
        int above65 = Integer.parseInt(ab_65no);
        int male = Integer.parseInt(maleno);
        int female = Integer.parseInt(femaleno);
        int other = Integer.parseInt(otherno);

        int total1 = below5+betn5_14+betn15_64+above65;
        int total2 = male+female+other ;

        if(total1 == total2){
        generalFormModel.setA1(tvHeadName.getText().toString());
        generalFormModel.setA1_a(spinnerHeadSex.getSelectedItem().toString());
        generalFormModel.setA1_b(tvHeadAge.getText().toString());
        generalFormModel.setA2_a(tvBelow_5_No.getText().toString());
        generalFormModel.setA2_b(tvBetween_5_14_No.getText().toString());
        generalFormModel.setA2_c(tvBetween_15_64_No.getText().toString());
        generalFormModel.setA2_d(tvAbove_65_No.getText().toString());


        generalFormModel.setA2_e(tvFamilyMemTotal.getText().toString());

        generalFormModel.setA2_f(tvMaleFamilyNo.getText().toString());
        generalFormModel.setA2_g(tvFemaleFamilyNo.getText().toString());
        generalFormModel.setA2_h(tvOthreFamilyNo.getText().toString());
        generalFormModel.setA3(spinnerDisPregLac.getSelectedItem().toString());
        generalFormModel.setA3_a(spinnerSpecifyDisPregLac.getSelectedItem().toString());
        generalFormModel.setA3_b(tvDisabilityType.getText().toString());
        generalFormModel.setA3_c(spinnerBeforeAfterDisabled.getSelectedItem().toString());

        Intent intent = new Intent(DemographicInfoActivity.this, ReconstructionStatusActivity.class);
        intent.putExtra("generalFormModel", generalFormModel);
        startActivity(intent);
        }
        else {
            tvBelow_5_No.setError(getString(R.string.total_mem_validation_error));
            tvBetween_5_14_No.setError(getString(R.string.total_mem_validation_error));
            tvBetween_15_64_No.setError(getString(R.string.total_mem_validation_error));
            tvAbove_65_No.setError(getString(R.string.total_mem_validation_error));
            tvMaleFamilyNo.setError(getString(R.string.total_mem_validation_error));
            tvFemaleFamilyNo.setError(getString(R.string.total_mem_validation_error));
            tvOthreFamilyNo.setError(getString(R.string.total_mem_validation_error));
        }

    }





    @OnItemSelected(R.id.demographic_info_dis_preg_lac)
    public void spinnerItemSelected(Spinner spinner, int position) {
        // code here
        int id = position ;
        String disable = spinnerDisPregLac.getSelectedItem().toString();
        if (disable.equals("Yes(छ)")){
            spinnerSpecifyDisPregLac.setVisibility(View.VISIBLE);

        }
        else {
            spinnerSpecifyDisPregLac.setVisibility(View.INVISIBLE);
        }

    }
    @OnItemSelected(R.id.demographic_info_specify_dis_preg_lac)
    public void DisableSelected(Spinner spinner, int position) {
        // code here
        int id = position ;
        String specifyDisable = spinnerSpecifyDisPregLac.getSelectedItem().toString();
        if (specifyDisable.equals("Disable(अपाङ्ग)")){
            tvDisabilityType.setVisibility(View.VISIBLE);
            spinnerBeforeAfterDisabled.setVisibility(View.VISIBLE);
        }
        else {
            tvDisabilityType.setVisibility(View.INVISIBLE);
            spinnerBeforeAfterDisabled.setVisibility(View.INVISIBLE);
        }

    }
}
