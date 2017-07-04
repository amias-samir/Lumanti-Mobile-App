package np.com.naxa.lumanti.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.model.GeneralFormModel;

public class MainActivity extends AppCompatActivity {

    String rural_municipality;

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
    @BindView(R.id.districtLbl)
    TextView tvDistrictLBL;
    @BindView(R.id.general_info_rural_municipality_spinner)
    Spinner spinnerRuralMunicipality;
    @BindView(R.id.ruralCardLayout)
    CardView rlRuralCardLayout;
    @BindView(R.id.inputlatoutRural)
    TextInputLayout inputlatoutRural;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("General Information");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        NextPage();

    }

    @OnClick(R.id.general_info_next)
    public void NextPage() {
        tvNissaNo.setError(null);
        final String[] values = getResources().getStringArray(R.array.district_name);

        if ((!(spinnerDistrictName.getSelectedItem().toString()).equals(values[0]) || !(tvNissaNo.getText().toString()).isEmpty()) && !(tvNissaNo.getText().toString()).equals("")) {

//            final String[] values = getResources().getStringArray(R.array.district_name);
            if(!(spinnerDistrictName.getSelectedItem().toString()).equals(values[1])){
                rural_municipality = tvRuralMunicipality.getText().toString();
            }


            GeneralFormModel generalFormModel = new GeneralFormModel();
            generalFormModel.setG1(spinnerDamageType.getSelectedItem().toString());
            generalFormModel.setG2(spinnerDistrictName.getSelectedItem().toString());
            generalFormModel.setG3(tvDistrictCode.getText().toString());
            generalFormModel.setG4(rural_municipality);
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
        } else {
            Toast.makeText(this, "District name and Nissa no. is required", Toast.LENGTH_SHORT).show();
            tvNissaNo.setError("The field cannot be empty");
            tvDistrictLBL.setError("The field cannot be empty");
        }
    }

    @OnItemSelected(R.id.general_info_district_name)
    public void spinner(Spinner spinner, int position) {
        final String[] values = getResources().getStringArray(R.array.district_name);
        int id = position;
        String selected_item = spinnerDistrictName.getSelectedItem().toString();

        if (selected_item.equals(values[0])) {
            tvDistrictLBL.setError(null);
            rlRuralCardLayout.setVisibility(View.GONE);
            inputlatoutRural.setVisibility(View.GONE);
            tvPreviousVdcMun.setText("");
            tvPreviousVdcMun.setText("");

        } else if (selected_item.equals(values[1])) {
            rlRuralCardLayout.setVisibility(View.VISIBLE);
            inputlatoutRural.setVisibility(View.GONE);

        } else {
            rlRuralCardLayout.setVisibility(View.GONE);
            inputlatoutRural.setVisibility(View.VISIBLE);
            tvPreviousVdcMun.setText("");
            tvPreviousVdcMun.setText("");



        }
    }


    @OnItemSelected(R.id.general_info_rural_municipality_spinner)
    public void ruralSpinner(Spinner spinner, int position) {
        final String[] values = getResources().getStringArray(R.array.rural_municipality);
        int id = position;
        String selected_item = spinnerRuralMunicipality.getSelectedItem().toString();
        if (selected_item.equals(values[1])) {
            rural_municipality = values[1];
            tvPreviousVdcMun.setText("Dhaibung VDC ((धैबुङ्ग गाविस))");

        } else if (selected_item.equals(values[2])) {
            tvPreviousVdcMun.setText("Laharapauwa VDC (धैबुङ्ग गाविस)");
            rural_municipality = values[2];

        }else {
            rural_municipality = values[0];
            tvPreviousVdcMun.setText("");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_saved_forms:
                Intent intent = new Intent(MainActivity.this, SavedFormsActivity.class);
                startActivity(intent);
                break;

            default:

                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Exit From App")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.onBackPressed();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        } else {
                            finish();
                        }
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }


}
