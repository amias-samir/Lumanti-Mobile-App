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

public class EarthquakeReliefStatusActivity extends AppCompatActivity {

    Toolbar toolbar;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.earthquake_relief_status_next)
    public void NextPage() {


        Intent intent = new Intent(EarthquakeReliefStatusActivity.this, SocioEconomicInfoctivity.class);
        startActivity(intent);
    }
}
