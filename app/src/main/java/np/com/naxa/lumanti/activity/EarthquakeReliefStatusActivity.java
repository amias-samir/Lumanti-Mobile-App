package np.com.naxa.lumanti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class EarthquakeReliefStatusActivity extends AppCompatActivity {

    Toolbar toolbar;
    GeneralFormModel generalFormModel ;

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
        tvKindSupport.setVisibility(View.INVISIBLE);

        generalFormModel = new GeneralFormModel();
        generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
//        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();


    }

    @OnClick(R.id.earthquake_relief_status_next)
    public void NextPage() {

        generalFormModel.setC1(spinnerReconstructionGrant.getSelectedItem().toString());
        generalFormModel.setC2(spinnerReceivedSupport.getSelectedItem().toString());

        if(spinnerReceivedSupport.getSelectedItem().toString().equals("In Kind")){
            generalFormModel.setC2_a("0");
        }
        else {
            generalFormModel.setC2_a(receivedInstallment.getSelectedItemId()+"");

        }

        generalFormModel.setC2_b(tvKindSupport.getText().toString());


        Intent intent = new Intent(EarthquakeReliefStatusActivity.this, SocioEconomicInfoctivity.class);
        intent.putExtra("generalFormModel", generalFormModel);
        startActivity(intent);
    }




    @OnItemSelected(R.id.earthquake_relief_received_support)
    public void spinnerItemSelected(Spinner spinner, int position) {
        // code here
        int id = position ;
        String living_situation = spinnerReceivedSupport.getSelectedItem().toString();
        if (living_situation.equals("In Cash")){
            receivedInstallment.setVisibility(View.VISIBLE);
        }
        else if(living_situation.equals("In Kind")){
            tvKindSupport.setVisibility(View.VISIBLE);

        }
        else {
            receivedInstallment.setVisibility(View.INVISIBLE);
            tvKindSupport.setVisibility(View.INVISIBLE);
        }

    }


}
