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

public class SocioEconomicInfoctivity extends AppCompatActivity {

    Toolbar toolbar;
    @BindView(R.id.socio_economic_land_with_land_title)
    Spinner spinnerLandWithLandTitle;
    @BindView(R.id.socio_economic_land_at_other_place)
    AutoCompleteTextView tvLandAtOtherPlace;
    @BindView(R.id.socio_economic_land_area)
    AutoCompleteTextView tvLandArea;
    @BindView(R.id.socio_economic_major_income_source)
    Spinner spinnerMajorIncomeSource;
    @BindView(R.id.socio_economic_other_income_source)
    AutoCompleteTextView tvOtherIncomeSource;
    @BindView(R.id.socio_economic_saving_cooperative_member)
    Spinner spinnerSavingCooperativeMember;
    @BindView(R.id.socio_economic_savings_per_month)
    Spinner spinnerSavingsPerMonth;
    @BindView(R.id.socio_economic_tentative_monthly_income)
    Spinner spinnerTentativeMonthlyIncome;
    @BindView(R.id.socio_economic_info_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socio_economic_infoctivity);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Socio Economic Information");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @OnClick(R.id.socio_economic_info_next)
    public void NextPage() {


        Intent intent = new Intent(SocioEconomicInfoctivity.this, WaterSanitationActivity.class);
        startActivity(intent);
    }
}
