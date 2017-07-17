package np.com.naxa.lumanti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.GeneralFormModel;

public class EarthquakeReliefStatusActivity extends AppCompatActivity {

    Toolbar toolbar;
    GeneralFormModel generalFormModel;

    @BindView(R.id.earthquake_relief_reconstraction_grant)
    Spinner spinnerReconstructionGrant;
    @BindView(R.id.earthquake_relief_received_support)
    Spinner spinnerReceivedSupport;
    @BindView(R.id.earthquake_relief_installment)
    Spinner receivedInstallment;
    @BindView(R.id.earthquake_relief_kind_support)
    AutoCompleteTextView tvKindSupport;
    @BindView(R.id.earthquake_relief_status_next)
    Button btnNext;
    @BindView(R.id.earthquake_relief_status_prev)
    Button btnPrev;
    @BindView(R.id.earthquake_relief_cb_in_cash)
    CheckBox cbInCash;
    @BindView(R.id.earthquake_relief_cb_in_kind)
    CheckBox cbInKind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_relief_status);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Earthquake Relief Status");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        receivedInstallment.setVisibility(View.INVISIBLE);
//        tvKindSupport.setVisibility(View.INVISIBLE);

        generalFormModel = new GeneralFormModel();

        if (Constant.countEarthquakeRelief == 0) {
            generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
//        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();
            Log.e(" MAIN ACTIVITY SAMIR", "onCreate: " + "" + Constant.countEarthquakeRelief);
        }

        if (Constant.countEarthquakeRelief != 0) {
            generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("PgeneralFormModel");
            initializeUI();
        }


    }

    @OnClick(R.id.earthquake_relief_status_next)
    public void NextPage() {

        generalFormModel.setC1(spinnerReconstructionGrant.getSelectedItem().toString());

        if(cbInCash.isChecked() && !cbInKind.isChecked()){
            generalFormModel.setC2("In Cash");
            generalFormModel.setC2_a(receivedInstallment.getSelectedItemId() + "");

        }else if(!cbInCash.isChecked() && cbInKind.isChecked()){
            generalFormModel.setC2("In Kind");
            generalFormModel.setC2_a("0");

        }else if(cbInCash.isChecked() && cbInKind.isChecked()){
            generalFormModel.setC2("In Cash , In Kind");
            generalFormModel.setC2_a(receivedInstallment.getSelectedItemId() + "");


        }


//        generalFormModel.setC2(spinnerReceivedSupport.getSelectedItem().toString());
//
//        if (spinnerReceivedSupport.getSelectedItem().toString().equals("In Kind")) {
//            generalFormModel.setC2_a("0");
//        } else {
//            generalFormModel.setC2_a(receivedInstallment.getSelectedItemId() + "");
//
//        }

        generalFormModel.setC2_b(tvKindSupport.getText().toString());

        Constant.countEarthquakeRelief = 1;

        Intent intent = new Intent(EarthquakeReliefStatusActivity.this, SaveSendActivity.class);
        if (Constant.countSaveSend == 0) {
            intent.putExtra("generalFormModel", generalFormModel);
        } else {
            intent.putExtra("PgeneralFormModel", generalFormModel);

        }
        startActivity(intent);
    }

    @OnClick(R.id.earthquake_relief_status_prev)
    public void PreviousPage() {

        generalFormModel.setC1(spinnerReconstructionGrant.getSelectedItem().toString());

        if(cbInCash.isChecked() && !cbInKind.isChecked()){
            generalFormModel.setC2("In Cash");
            generalFormModel.setC2_a(receivedInstallment.getSelectedItemId() + "");

        }else if(!cbInCash.isChecked() && cbInKind.isChecked()){
            generalFormModel.setC2("In Kind");
            generalFormModel.setC2_a("0");

        }else if(cbInCash.isChecked() && cbInKind.isChecked()){
            generalFormModel.setC2("In Cash , In Kind");
            generalFormModel.setC2_a(receivedInstallment.getSelectedItemId() + "");


        }

        generalFormModel.setC2_b(tvKindSupport.getText().toString());

        Constant.countEarthquakeRelief = 1;

        Intent intent = new Intent(EarthquakeReliefStatusActivity.this, ReconstructionStatusActivity.class);
        intent.putExtra("PgeneralFormModel", generalFormModel);
        startActivity(intent);
    }


    @OnItemSelected(R.id.earthquake_relief_received_support)
    public void spinnerItemSelected(Spinner spinner, int position) {
        // code here
        final String[] values = getResources().getStringArray(R.array.received_support);
        int id = position;
        String living_situation = spinnerReceivedSupport.getSelectedItem().toString();
//        if (living_situation.equals(values[1])) {
//            receivedInstallment.setVisibility(View.VISIBLE);
//            tvKindSupport.setVisibility(View.INVISIBLE);
//        } else if (living_situation.equals(values[2])) {
//            tvKindSupport.setVisibility(View.VISIBLE);
//            receivedInstallment.setVisibility(View.INVISIBLE);
//
//        } else {
//            receivedInstallment.setVisibility(View.INVISIBLE);
//            tvKindSupport.setVisibility(View.INVISIBLE);
//        }

    }




    public void initializeUI() {

//        Log.e("Demographics SAMIR", "initializeUI: "+ generalFormModel.toString() );

        List<String> ReconstructionGrant = Arrays.asList(getResources().getStringArray(R.array.yes_no));
        int setReconstructionGrant = ReconstructionGrant.indexOf(generalFormModel.getC1());
        spinnerReconstructionGrant.setSelection(setReconstructionGrant);


//        List<String> ReceivedSupport = Arrays.asList(getResources().getStringArray(R.array.received_support));
//        int setReceivedSupport = ReceivedSupport.indexOf(generalFormModel.getC2());
//        spinnerReceivedSupport.setSelection(setReceivedSupport);

        if(generalFormModel.getC2().equals("In Cash")){
            cbInCash.setChecked(true);
            int setInstallment = Integer.parseInt(generalFormModel.getC2_a());
            receivedInstallment.setSelection(setInstallment);

        }else if(generalFormModel.getC2().equals("In Kind")){
            cbInKind.setChecked(true);
            tvKindSupport.setText(generalFormModel.getC2_b());


        }else if(generalFormModel.getC2().equals("In Cash , In Kind")){
            cbInCash.setChecked(true);
            cbInKind.setChecked(true);

            receivedInstallment.setVisibility(View.VISIBLE);

            int setInstallment = Integer.parseInt(generalFormModel.getC2_a());
            receivedInstallment.setSelection(setInstallment);

            tvKindSupport.setText(generalFormModel.getC2_b());

        }




//        List<String> Installment = Arrays.asList(getResources().getStringArray(R.array.specify_dis_lac_preg));
////        int setInstallment = Installment.indexOf(generalFormModel.getC2_a());
//        int setInstallment = Integer.parseInt(generalFormModel.getC2_a());
//        receivedInstallment.setSelection(setInstallment);
//
//        tvKindSupport.setText(generalFormModel.getC2_b());

    }


    @OnClick({R.id.earthquake_relief_cb_in_cash, R.id.earthquake_relief_cb_in_kind})
    public void checkboxListner(View view) {
        switch (view.getId()) {
            case R.id.earthquake_relief_cb_in_cash:
                if(cbInCash.isChecked()) {
                    receivedInstallment.setVisibility(View.VISIBLE);
                }else {
                    receivedInstallment.setVisibility(View.INVISIBLE);

                }
//            tvKindSupport.setVisibility(View.INVISIBLE);

                break;


            case R.id.earthquake_relief_cb_in_kind:
                if(cbInKind.isChecked()) {
                    tvKindSupport.setVisibility(View.VISIBLE);
                }else {
                    tvKindSupport.setVisibility(View.INVISIBLE);
                }
//            receivedInstallment.setVisibility(View.INVISIBLE);

                break;

            default:
                cbInCash.setChecked(false);
                cbInKind.setChecked(false);


        }
    }
}
