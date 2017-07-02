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

public class SocioEconomicInfoctivity extends AppCompatActivity {

    Toolbar toolbar;
    GeneralFormModel generalFormModel ;

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
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        generalFormModel = new GeneralFormModel();
        generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
//        Toast.makeText(this, ""+ generalFormModel.getG1(), Toast.LENGTH_SHORT).show();

        tvLandAtOtherPlace.setVisibility(View.INVISIBLE);
        tvLandArea.setVisibility(View.INVISIBLE);
        tvOtherIncomeSource.setVisibility(View.INVISIBLE);
        spinnerSavingsPerMonth.setVisibility(View.INVISIBLE);

    }

    @OnClick(R.id.socio_economic_info_next)
    public void NextPage() {

        generalFormModel.setD1(spinnerLandWithLandTitle.getSelectedItem().toString());
        generalFormModel.setD2(tvLandAtOtherPlace.getText().toString());
        generalFormModel.setD2_a(tvLandArea.getText().toString());
        generalFormModel.setD3(spinnerMajorIncomeSource.getSelectedItem().toString());
        generalFormModel.setD3_a(tvOtherIncomeSource.getText().toString());
        generalFormModel.setD4(spinnerSavingCooperativeMember.getSelectedItem().toString());
        generalFormModel.setD4_a(spinnerSavingsPerMonth.getSelectedItem().toString());
        generalFormModel.setD5(spinnerTentativeMonthlyIncome.getSelectedItem().toString());

        Intent intent = new Intent(SocioEconomicInfoctivity.this, WaterSanitationActivity.class);
        intent.putExtra("generalFormModel", generalFormModel);
        startActivity(intent);
    }



    @OnItemSelected(R.id.socio_economic_land_with_land_title)
    public void spinnerItemSelected(Spinner spinner, int position) {
        // code here
        final String[] values = getResources().getStringArray(R.array.yes_no);
        int id = position ;
        String selected_item = spinnerLandWithLandTitle.getSelectedItem().toString();
        if (selected_item.equals(values[1])){
            tvLandAtOtherPlace.setVisibility(View.VISIBLE);
            tvLandArea.setVisibility(View.VISIBLE);

        }
        else {
            tvLandAtOtherPlace.setVisibility(View.INVISIBLE);
            tvLandArea.setVisibility(View.INVISIBLE);
            tvOtherIncomeSource.setVisibility(View.INVISIBLE);
            spinnerSavingsPerMonth.setVisibility(View.INVISIBLE);
        }

    }

    @OnItemSelected(R.id.socio_economic_major_income_source)
    public void spinnerMajorIncomeSelected(Spinner spinner, int position) {
        // code here
        final String[] values = getResources().getStringArray(R.array.family_income_source);

        int id = position ;
        String selected_item = spinnerMajorIncomeSource.getSelectedItem().toString();
        if (selected_item.equals(values[9])){
            tvOtherIncomeSource.setVisibility(View.VISIBLE);
        }
        else {
            tvOtherIncomeSource.setVisibility(View.INVISIBLE);
        }
    }


    @OnItemSelected(R.id.socio_economic_saving_cooperative_member)
    public void spinnerSavingsGroupSelected(Spinner spinner, int position) {
        // code here
        final String[] values = getResources().getStringArray(R.array.yes_no);
        int id = position ;
        String selected_item = spinnerSavingCooperativeMember.getSelectedItem().toString();
        if (selected_item.equals(values[1])){
            spinnerSavingsPerMonth.setVisibility(View.VISIBLE);
        }
        else {
            spinnerSavingsPerMonth.setVisibility(View.INVISIBLE);
        }
    }
}
