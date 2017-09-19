package np.com.naxa.lumanti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

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
    @BindView(R.id.demographic_info_prev)
    Button btnPrev;
    @BindView(R.id.demographic_info_interviewee_name)
    AutoCompleteTextView IntervieweeName;
    @BindView(R.id.demographic_info_interviewee_head_relationship)
    AutoCompleteTextView IntervieweeHeadRelationship;
    @BindView(R.id.demographic_info_interviewee_contact_no)
    AutoCompleteTextView IntervieweeContactNo;
    @BindView(R.id.demographic_info_house_type)
    Spinner SpinnerHouseType;
    @BindView(R.id.dis_preg_lacLbl)
    TextView disPregLacLbl;
    @BindView(R.id.specifydis_preg_lacLbl)
    TextView specifydisPregLacLbl;
    @BindView(R.id.before_after_disableLbl)
    TextView beforeAfterDisableLbl;
    @BindView(R.id.cv_SaveSend)
    CardView cvSaveSend;

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

        if (Constant.countDemographic == 0) {
            generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
            try {


            if(!generalFormModel.getA1().equals(null)){
                tvHeadName.setText(generalFormModel.getA1());
            }else if (generalFormModel.getA1().equals(null)) {
                tvHeadName.setText("");
            }
            }catch (NullPointerException e){

            }
//        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();
            Log.e(" MAIN ACTIVITY SAMIR", "onCreate: countDemographic" + "" + Constant.countDemographic);
        }

        if (Constant.countDemographic != 0) {
            generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("PgeneralFormModel");
            initializeUI();
        }
    }


    @OnClick(R.id.demographic_info_next)
    public void NextPage() {
        String b_5no, b15_64no, b5_14no, ab_65no, maleno, femaleno, otherno;


        b_5no = tvBelow_5_No.getText().toString();
        b5_14no = tvBetween_5_14_No.getText().toString();
        b15_64no = tvBetween_15_64_No.getText().toString();
        ab_65no = tvAbove_65_No.getText().toString();
        maleno = tvMaleFamilyNo.getText().toString();
        femaleno = tvFemaleFamilyNo.getText().toString();
        otherno = tvOthreFamilyNo.getText().toString();

        if (b_5no.isEmpty() || b_5no.equals("")) {
            b_5no = "0";
        }
        if (b5_14no.isEmpty() || b5_14no.equals("")) {
            b5_14no = "0";
        }
        if (b15_64no.isEmpty() || b15_64no.equals("")) {
            b15_64no = "0";
        }
        if (ab_65no.isEmpty() || ab_65no.equals("")) {
            ab_65no = "0";
        }
        if (maleno.isEmpty() || maleno.equals("")) {
            maleno = "0";
        }
        if (femaleno.isEmpty() || femaleno.equals("")) {
            femaleno = "0";
        }
        if (otherno.isEmpty() || otherno.equals("")) {
            otherno = "0";
        }


        int below5 = Integer.parseInt(b_5no);
        int betn5_14 = Integer.parseInt(b5_14no);
        int betn15_64 = Integer.parseInt(b15_64no);
        int above65 = Integer.parseInt(ab_65no);
        int male = Integer.parseInt(maleno);
        int female = Integer.parseInt(femaleno);
        int other = Integer.parseInt(otherno);

        int total1 = below5 + betn5_14 + betn15_64 + above65;
        int total2 = male + female + other;

        if (total1 == total2) {
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
            generalFormModel.setA4(IntervieweeName.getText().toString());
            generalFormModel.setA5(IntervieweeHeadRelationship.getText().toString());
            generalFormModel.setA6(IntervieweeContactNo.getText().toString());
            generalFormModel.setA7(SpinnerHouseType.getSelectedItem().toString());

            Constant.countDemographic = 1;

            Intent intent = new Intent(DemographicInfoActivity.this, ReconstructionStatusActivity.class);
            if (Constant.countReconstruction == 0) {
                intent.putExtra("generalFormModel", generalFormModel);
            } else {
                intent.putExtra("PgeneralFormModel", generalFormModel);

            }
            startActivity(intent);

        } else {
            Toast.makeText(this, getString(R.string.total_mem_validation_error), Toast.LENGTH_SHORT).show();
            tvBelow_5_No.setError(getString(R.string.total_mem_validation_error));
            tvBetween_5_14_No.setError(getString(R.string.total_mem_validation_error));
            tvBetween_15_64_No.setError(getString(R.string.total_mem_validation_error));
            tvAbove_65_No.setError(getString(R.string.total_mem_validation_error));
            tvMaleFamilyNo.setError(getString(R.string.total_mem_validation_error));
            tvFemaleFamilyNo.setError(getString(R.string.total_mem_validation_error));
            tvOthreFamilyNo.setError(getString(R.string.total_mem_validation_error));
        }

    }

    @OnClick(R.id.demographic_info_prev)
    public void PreviousPage() {

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
        generalFormModel.setA4(IntervieweeName.getText().toString());
        generalFormModel.setA5(IntervieweeHeadRelationship.getText().toString());
        generalFormModel.setA6(IntervieweeContactNo.getText().toString());
        generalFormModel.setA7(SpinnerHouseType.getSelectedItem().toString());

        Constant.countDemographic = 1;

        Intent intent = new Intent(DemographicInfoActivity.this, MainActivity.class);
        intent.putExtra("PgeneralFormModel", generalFormModel);
        startActivity(intent);
    }


    @OnItemSelected(R.id.demographic_info_dis_preg_lac)
    public void spinnerItemSelected(Spinner spinner, int position) {
        // code here
        int id = position;
        String disable = spinnerDisPregLac.getSelectedItem().toString();
        if (disable.equals("Yes(छ)")) {
            spinnerSpecifyDisPregLac.setVisibility(View.VISIBLE);

        } else {
            spinnerSpecifyDisPregLac.setVisibility(View.INVISIBLE);
            tvDisabilityType.setVisibility(View.INVISIBLE);
        }

    }

    @OnItemSelected(R.id.demographic_info_specify_dis_preg_lac)
    public void DisableSelected(Spinner spinner, int position) {
        // code here
        final String[] values = getResources().getStringArray(R.array.specify_dis_lac_preg);
        int id = position;
        String specifyDisable = spinnerSpecifyDisPregLac.getSelectedItem().toString();
        if (specifyDisable.equals(values[1])) {
            tvDisabilityType.setVisibility(View.VISIBLE);
            spinnerBeforeAfterDisabled.setVisibility(View.VISIBLE);
        } else {
            tvDisabilityType.setVisibility(View.INVISIBLE);
            tvDisabilityType.setText("");
            spinnerBeforeAfterDisabled.setVisibility(View.INVISIBLE);
        }

    }

    @OnItemSelected(R.id.demographic_info_before_after_disabled)
    public void BeforeAfterEQ(Spinner spinner, int position) {
        // code here
        final String[] values = getResources().getStringArray(R.array.before_after_earthquake);
        int id = position;
        String specifyDisable = spinnerBeforeAfterDisabled.getSelectedItem().toString();
//        if (specifyDisable.equals(values[1])) {
//            tvDisabilityType.setVisibility(View.VISIBLE);
//            spinnerBeforeAfterDisabled.setVisibility(View.VISIBLE);
//        } else {
//            tvDisabilityType.setVisibility(View.INVISIBLE);
//            spinnerBeforeAfterDisabled.setVisibility(View.INVISIBLE);
//        }

    }

//    @Override
//    public void onBackPressed() {
//        return;
//
//    }

    public void initializeUI() {

//        Log.e("Demographics SAMIR", "initializeUI: "+ generalFormModel.toString() );

        List<String> Sex = Arrays.asList(getResources().getStringArray(R.array.sex));
        int setSex = Sex.indexOf(generalFormModel.getA1_a());
        spinnerHeadSex.setSelection(setSex);


        List<String> DisablePregLact = Arrays.asList(getResources().getStringArray(R.array.yes_no));
        int setDisablePregLact = DisablePregLact.indexOf(generalFormModel.getA3());
        spinnerDisPregLac.setSelection(setDisablePregLact);

        Log.e("Demographic", "initializeUI: "+generalFormModel.getA3()+", "+generalFormModel.getA3_a()+", "+ generalFormModel.getA3_c() );

        List<String> SpecifyDisablePregLact = Arrays.asList(getResources().getStringArray(R.array.specify_dis_lac_preg));
        int setSpecifyDisablePregLact = SpecifyDisablePregLact.indexOf(generalFormModel.getA3_a());
        Log.e("Demographic SAMIRD", "specify type: "+setSpecifyDisablePregLact + ", "+SpecifyDisablePregLact.toString() );
        spinnerSpecifyDisPregLac.setSelection(setSpecifyDisablePregLact);

        List<String> BeforeAfterEarthquake = Arrays.asList(getResources().getStringArray(R.array.before_after_earthquake));
        int setBeforeAfterEarthquake = BeforeAfterEarthquake.indexOf(generalFormModel.getA3_c());
        Log.e("Demographic SAMIRD", "before after: "+setBeforeAfterEarthquake );
        spinnerBeforeAfterDisabled.setSelection(setBeforeAfterEarthquake);

        List<String> HouseType = Arrays.asList(getResources().getStringArray(R.array.house_type));
        int setHouseType = HouseType.indexOf(generalFormModel.getA7());
        SpinnerHouseType.setSelection(setHouseType);

        tvHeadName.setText(generalFormModel.getA1());
        tvHeadAge.setText(generalFormModel.getA1_b());
        tvBelow_5_No.setText(generalFormModel.getA2_a());
        tvBetween_5_14_No.setText(generalFormModel.getA2_b());
        tvBetween_15_64_No.setText(generalFormModel.getA2_c());
        tvAbove_65_No.setText(generalFormModel.getA2_d());
        tvFamilyMemTotal.setText(generalFormModel.getA2_e());
        tvMaleFamilyNo.setText(generalFormModel.getA2_f());
        tvFemaleFamilyNo.setText(generalFormModel.getA2_g());
        tvOthreFamilyNo.setText(generalFormModel.getA2_h());
        tvDisabilityType.setText(generalFormModel.getA3_b());
        IntervieweeName.setText((generalFormModel.getA4()));
        IntervieweeHeadRelationship.setText((generalFormModel.getA5()));
        IntervieweeContactNo.setText((generalFormModel.getA6()));

    }
}
