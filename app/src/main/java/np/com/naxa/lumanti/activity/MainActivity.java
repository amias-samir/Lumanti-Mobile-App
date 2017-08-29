package np.com.naxa.lumanti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.GeneralFormModel;
import np.com.naxa.lumanti.sugar.Municipality_ward_list;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    String rural_municipality;

    Toolbar toolbar;
    GeneralFormModel generalFormModel;

    static int count = 0;


    @BindView(R.id.general_info_damage_type)
    Spinner spinnerDamageType;
    @BindView(R.id.general_info_district_name)
    Spinner spinnerDistrictName;
    @BindView(R.id.general_info_district_code)
    AutoCompleteTextView tvDistrictCode;

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
//    @BindView(R.id.ruralCardLayout)
//    CardView rlRuralCardLayout;
//    @BindView(R.id.inputlatoutRural)
//    TextInputLayout inputlatoutRural;

    @BindView(R.id.general_info_current_ward_spinner)
    Spinner CurrentWardSpinner;
    @BindView(R.id.general_info_previous_vdc_spinner)
    Spinner PreviousVdcSpinner;
    @BindView(R.id.general_info_previous_ward_spinner)
    Spinner PreviousWardSpinner;


    ArrayAdapter districtNameadpt, currentWardNoadpt, prevWardNoadpt, currentMuniVDCNameadpt, prevMuniVDCNameadpt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("General Information");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        generalFormModel = new GeneralFormModel();

        if (Constant.countNissaNoInput == 1) {
//            autoset nissa related field
            autoSetNissaRelatedField();
            Constant.countNissaNoInput = 0;
        }


//        get intent from next page();
        Log.e(" MAIN ACTIVITY SAMIR", "onCreate: countGeneral" + "" + Constant.countGeneral);
        if (Constant.countGeneral != 0) {
            generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("PgeneralFormModel");

            initializeSpinnerAdapterSaved();
            initializeUI();
        } else {

            //VDC name spinner
            List<Municipality_ward_list> allContacts = Municipality_ward_list.listAll(Municipality_ward_list.class);
            ArrayList<String> arr = new ArrayList<>();
            for (Municipality_ward_list contact : allContacts) {
                if (!arr.contains(contact.district_name)) {
                    arr.add(contact.district_name); // or arr.add(contact.name); if it's public
                }
            }
//        String[] distArray = arr.toArray(new String[0]);

            Log.e("MAIN_ACTIVITY", "onCreate: districtArray SAMIR" + arr);

            districtNameadpt = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, arr);
            districtNameadpt
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDistrictName.setAdapter(districtNameadpt);

        }


        Intent intent = getIntent();
        if (intent.hasExtra("JSON1")) {
            Bundle bundle = intent.getExtras();
            Constant.isFomSavedForm = true;

            String jsonToParse = (String) bundle.get("JSON1");
            Log.e("MainActivity", "onCreate: Json " + jsonToParse);
            String formid = (String) bundle.get("DBid");
            Constant.formID = formid;
            String sent_Status = (String) bundle.get("sent_Status");

            Gson gson = new Gson();
            // 2. JSON to Java object, read it from a Json String.
            generalFormModel = gson.fromJson(jsonToParse, GeneralFormModel.class);
            reinitializeConstantVariable();

            initializeSpinnerAdapterSaved();
            initializeUI();


        }


    }

    @OnClick(R.id.general_info_next)
    public void NextPage() {
        tvNissaNo.setError(null);
        final String[] values = getResources().getStringArray(R.array.district_name);

        if ((!(tvNissaNo.getText().toString()).isEmpty()) && !(tvNissaNo.getText().toString()).equals("")) {

////            final String[] values = getResources().getStringArray(R.array.district_name);
//            if (!(spinnerDistrictName.getSelectedItem().toString()).equals(values[1])) {
//                rural_municipality = tvRuralMunicipality.getText().toString();
//            }


            generalFormModel.setG1(spinnerDamageType.getSelectedItem().toString());
            generalFormModel.setG2(spinnerDistrictName.getSelectedItem().toString());
            generalFormModel.setG3(tvDistrictCode.getText().toString());
            generalFormModel.setG4(spinnerRuralMunicipality.getSelectedItem().toString());
            generalFormModel.setG5(CurrentWardSpinner.getSelectedItem().toString());
            generalFormModel.setG6(PreviousVdcSpinner.getSelectedItem().toString());
            generalFormModel.setG7(PreviousWardSpinner.getSelectedItem().toString());
            generalFormModel.setG8(tvTole.getText().toString());
            generalFormModel.setG9(tvHouseCode.getText().toString());
            generalFormModel.setG_10(tvNissaNo.getText().toString());
            generalFormModel.setG_11(tvCitizenshipNo.getText().toString());
            generalFormModel.setG_12(tvPaNo.getText().toString());

            Constant.countGeneral = 1;
            Intent intent = new Intent(MainActivity.this, DemographicInfoActivity.class);
            if (Constant.countDemographic == 0) {
                intent.putExtra("generalFormModel", generalFormModel);
            } else {
                intent.putExtra("PgeneralFormModel", generalFormModel);

            }
            startActivity(intent);
        } else {
            Toast.makeText(this, "District name and Nissa no. is required", Toast.LENGTH_SHORT).show();
            tvNissaNo.setError("The field cannot be empty");
            tvDistrictLBL.setError("The field cannot be empty");
        }
    }

    @OnItemSelected(R.id.general_info_district_name)
    public void ruralSpinner(Spinner spinner, int position) {
        //VDC name spinner
        List<Municipality_ward_list> allContacts = Municipality_ward_list.listAll(Municipality_ward_list.class);
        ArrayList<String> municipalityArray = new ArrayList<>();
        for (Municipality_ward_list contact : allContacts) {
            if ((contact.district_name).equals(spinnerDistrictName.getSelectedItem().toString())) {
                if (!municipalityArray.contains(contact.municipality_name)) {
                    municipalityArray.add(contact.municipality_name); // or arr.add(contact.name); if it's public
                }
            }
        }
        Log.e("MAIN_ACTIVITY", "onCreate: districtArray SAMIR" + municipalityArray);

        currentMuniVDCNameadpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, municipalityArray);
        currentMuniVDCNameadpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRuralMunicipality.setAdapter(currentMuniVDCNameadpt);


    }

    @OnItemSelected(R.id.general_info_rural_municipality_spinner)
    public void currentWardSpinner(Spinner spinner, int position) {
        //Current Ward no spinner
        List<Municipality_ward_list> allContacts = Municipality_ward_list.listAll(Municipality_ward_list.class);
        ArrayList<String> currentWardArray = new ArrayList<>();
        for (Municipality_ward_list contact : allContacts) {
            if (((contact.district_name).equals(spinnerDistrictName.getSelectedItem().toString())) &&
                    ((contact.municipality_name).equals(spinnerRuralMunicipality.getSelectedItem().toString()))) {

                if (!currentWardArray.contains(contact.current_ward)) {
                    currentWardArray.add(contact.current_ward); // or arr.add(contact.name); if it's public
                }
            }
        }
        Log.e("MAIN_ACTIVITY", "onCreate: districtArray SAMIR" + currentWardArray);

        currentWardNoadpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, currentWardArray);
        currentWardNoadpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CurrentWardSpinner.setAdapter(currentWardNoadpt);

    }


    @OnItemSelected(R.id.general_info_current_ward_spinner)
    public void prevMUNIVDCSpinner(Spinner spinner, int position) {
        //Previous MuniVDC spinner
        List<Municipality_ward_list> allContacts = Municipality_ward_list.listAll(Municipality_ward_list.class);
        ArrayList<String> prevMuniArray = new ArrayList<>();
        for (Municipality_ward_list contact : allContacts) {
            if (((contact.district_name).equals(spinnerDistrictName.getSelectedItem().toString())) &&
                    ((contact.municipality_name).equals(spinnerRuralMunicipality.getSelectedItem().toString())) &&
                    ((contact.current_ward).equals(CurrentWardSpinner.getSelectedItem().toString()))) {

                if (!prevMuniArray.contains(contact.prev_municipality_name)) {
                    prevMuniArray.add(contact.prev_municipality_name); // or arr.add(contact.name); if it's public
                }
            }
        }
        Log.e("MAIN_ACTIVITY", "onCreate: districtArray SAMIR" + prevMuniArray);

        prevMuniVDCNameadpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, prevMuniArray);
        prevMuniVDCNameadpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreviousVdcSpinner.setAdapter(prevMuniVDCNameadpt);

    }


    @OnItemSelected(R.id.general_info_previous_vdc_spinner)
    public void prevWardNoSpinner(Spinner spinner, int position) {
        //Previous Ward no spinner
        List<Municipality_ward_list> allContacts = Municipality_ward_list.listAll(Municipality_ward_list.class);
        ArrayList<String> prevWardArray = new ArrayList<>();
        for (Municipality_ward_list contact : allContacts) {
            if (((contact.district_name).equals(spinnerDistrictName.getSelectedItem().toString())) &&
                    ((contact.municipality_name).equals(spinnerRuralMunicipality.getSelectedItem().toString())) &&
                    ((contact.current_ward).equals(CurrentWardSpinner.getSelectedItem().toString())) &&
                    ((contact.prev_municipality_name).equals(PreviousVdcSpinner.getSelectedItem().toString()))) {

                if (!prevWardArray.contains(contact.prev_ward)) {
                    prevWardArray.add(contact.prev_ward); // or arr.add(contact.name); if it's public
                }
            }
        }
        Log.e("MAIN_ACTIVITY", "onCreate: districtArray SAMIR" + prevWardArray);

        prevWardNoadpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, prevWardArray);
        prevWardNoadpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreviousWardSpinner.setAdapter(prevWardNoadpt);

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

            case android.R.id.home:
                Intent intent_back = new Intent(MainActivity.this, HomeListActivity.class);
                startActivity(intent_back);

//                this.onBackPressed();
                break;

            default:

                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    public void initializeUI() {

        List<String> DamageType = Arrays.asList(getResources().getStringArray(R.array.house_damage));
        int setDamageType = DamageType.indexOf(generalFormModel.getG1());
        spinnerDamageType.setSelection(setDamageType);


//        List<String> DistrictName = Arrays.asList(getResources().getStringArray(R.array.district_name));
//        int setDistrictName = DistrictName.indexOf(generalFormModel.getG2());
//        spinnerDistrictName.setSelection(setDistrictName);

//        List<String> ruralMunicipalityName = Arrays.asList(getResources().getStringArray(R.array.rural_municipality));
//        int setMunicipalityName = ruralMunicipalityName.indexOf(generalFormModel.getG4());
//        spinnerRuralMunicipality.setSelection(setMunicipalityName);


        int setDistrict = districtNameadpt.getPosition(generalFormModel.getG2());
        Log.e("SPINNER Position", "District: SAMIR " + setDistrict);
        spinnerDistrictName.setSelection(setDistrict);
        currentMuniVDCNameadpt.notifyDataSetChanged();

        Log.e("Set Spinner SAMIR", "initializeUI: " + generalFormModel.getG2() + ", " + generalFormModel.getG4() + ", " + generalFormModel.getG5() + ", " + generalFormModel.getG6() + ", " + generalFormModel.getG7());

        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    int setCurrentMuniVDC = currentMuniVDCNameadpt.getPosition(generalFormModel.getG4());
                    Log.e("SPINNER Position", "VDC: SAMIR " + setCurrentMuniVDC);
                    spinnerRuralMunicipality.setSelection(setCurrentMuniVDC);
                    currentWardNoadpt.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 400);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    int setCurrentWard = currentWardNoadpt.getPosition(generalFormModel.getG5());
                    Log.e("SPINNER Position", "WARD: SAMIR " + setCurrentWard);
                    CurrentWardSpinner.setSelection(setCurrentWard);
                    prevMuniVDCNameadpt.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 800);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    int setPrevMuniVDC = prevMuniVDCNameadpt.getPosition(generalFormModel.getG6());
                    Log.e("SPINNER Position", "PreVCD: SAMIR " + setPrevMuniVDC);
                    PreviousVdcSpinner.setSelection(setPrevMuniVDC);
                    prevWardNoadpt.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1200);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    int setPrevWardNo = prevWardNoadpt.getPosition(generalFormModel.getG7());
                    Log.e("SPINNER Position", "PreWARD: SAMIR " + setPrevWardNo);
                    PreviousWardSpinner.setSelection(setPrevWardNo);
//                    prevWardNoadpt.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1600);


//        tvRuralMunicipality.setText(generalFormModel.getG4());
//        tvCurrentWardNo.setText(generalFormModel.getG5());
//        tvPreviousVdcMun.setText(generalFormModel.getG6());
//        tvPreviousWardNo.setText(generalFormModel.getG7());

        tvTole.setText(generalFormModel.getG8());
        tvHouseCode.setText(generalFormModel.getG9());
        tvNissaNo.setText(generalFormModel.getG_10());
        tvCitizenshipNo.setText(generalFormModel.getG_11());
        tvPaNo.setText(generalFormModel.getG_12());
    }


    public void reinitializeConstantVariable() {

        Constant.countGeneral = 1;
        Constant.countDemographic = 1;
        Constant.countReconstruction = 1;
        Constant.countEarthquakeRelief = 1;
        Constant.countReconstructionGPS = 2;
        Constant.countSaveSend = 0;

//        Constant.takenimg1 = false;
//        Constant.takenimg2 = false;
//        Constant.takenimg3 = false;
//        Constant.takenimg4 = false;
//
//        Constant.takenimg1Name = "";
//        Constant.takenimg2Name = "";
//        Constant.takenimg3Name = "";
//        Constant.takenimg4Name = "";
    }


    public void initializeSpinnerAdapterSaved() {
        //district name spinner
        List<Municipality_ward_list> allContacts = Municipality_ward_list.listAll(Municipality_ward_list.class);
        ArrayList<String> arr = new ArrayList<>();
        for (Municipality_ward_list contact : allContacts) {
            if (!arr.contains(contact.district_name)) {
                arr.add(contact.district_name); // or arr.add(contact.name); if it's public
            }
        }
//        String[] distArray = arr.toArray(new String[0]);

        Log.e("MAIN_ACTIVITY", "District SAMIR" + arr);

        districtNameadpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr);
        districtNameadpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrictName.setAdapter(districtNameadpt);


        //VDC name spinner
        ArrayList<String> municipalityArray = new ArrayList<>();
        for (Municipality_ward_list contact : allContacts) {
            if ((contact.district_name).equals(generalFormModel.getG2())) {
                if (!municipalityArray.contains(contact.municipality_name)) {
                    municipalityArray.add(contact.municipality_name); // or arr.add(contact.name); if it's public
                }
            }
        }
        Log.e("MAIN_ACTIVITY", "CMuni SAMIR" + municipalityArray);

        currentMuniVDCNameadpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, municipalityArray);
        currentMuniVDCNameadpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRuralMunicipality.setAdapter(currentMuniVDCNameadpt);


//current ward no
        ArrayList<String> currentWardArray = new ArrayList<>();
        for (Municipality_ward_list contact : allContacts) {
            if (((contact.district_name).equals(generalFormModel.getG2())) &&
                    ((contact.municipality_name).equals(generalFormModel.getG4()))) {

                if (!currentWardArray.contains(contact.current_ward)) {
                    currentWardArray.add(contact.current_ward); // or arr.add(contact.name); if it's public
                }
            }
        }
        Log.e("MAIN_ACTIVITY", "CWard SAMIR" + currentWardArray);

        currentWardNoadpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, currentWardArray);
        currentWardNoadpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CurrentWardSpinner.setAdapter(currentWardNoadpt);


        //Previous MuniVDC spinner
        ArrayList<String> prevMuniArray = new ArrayList<>();
        for (Municipality_ward_list contact : allContacts) {
            if (((contact.district_name).equals(generalFormModel.getG2())) &&
                    ((contact.municipality_name).equals(generalFormModel.getG4())) &&
                    ((contact.current_ward).equals(generalFormModel.getG5()))) {

                if (!prevMuniArray.contains(contact.prev_municipality_name)) {
                    prevMuniArray.add(contact.prev_municipality_name); // or arr.add(contact.name); if it's public
                }
            }
        }
        Log.e("MAIN_ACTIVITY", "PreMuni SAMIR" + prevMuniArray);

        prevMuniVDCNameadpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, prevMuniArray);
        prevMuniVDCNameadpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreviousVdcSpinner.setAdapter(prevMuniVDCNameadpt);


        //Previous Ward no spinner
        ArrayList<String> prevWardArray = new ArrayList<>();
        for (Municipality_ward_list contact : allContacts) {
            if (((contact.district_name).equals(generalFormModel.getG2())) &&
                    ((contact.municipality_name).equals(generalFormModel.getG4())) &&
                    ((contact.current_ward).equals(generalFormModel.getG5())) &&
                    ((contact.prev_municipality_name).equals(generalFormModel.getG6()))) {

                if (!prevWardArray.contains(contact.prev_ward)) {
                    prevWardArray.add(contact.prev_ward); // or arr.add(contact.name); if it's public
                }
            }
        }
        Log.e("MAIN_ACTIVITY", "PreWard SAMIR" + prevWardArray);

        prevWardNoadpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, prevWardArray);
        prevWardNoadpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PreviousWardSpinner.setAdapter(prevWardNoadpt);

    }


    public void autoSetNissaRelatedField() {
        generalFormModel = (GeneralFormModel) getIntent().getSerializableExtra("generalFormModel");
        try {


            if (!generalFormModel.getG_10().equals(null)) {
                tvNissaNo.setText(generalFormModel.getG_10());

            } else if (generalFormModel.getG_10().equals(null)) {
                tvNissaNo.setText("");
            }
            if (!generalFormModel.getG8().equals(null)) {
                tvTole.setText(generalFormModel.getG8());
            } else if (generalFormModel.getG8().equals(null)) {
                tvTole.setText("");
            }

            if (!generalFormModel.getG9().equals(null)) {
                tvHouseCode.setText(generalFormModel.getG9());

            } else if (generalFormModel.getG9().equals(null)) {
                tvHouseCode.setText("");

            }

            if (!generalFormModel.getG_11().equals(null)) {
                tvCitizenshipNo.setText(generalFormModel.getG_11());

            } else if (generalFormModel.getG_11().equals(null)) {
                tvCitizenshipNo.setText("");

            }
            if (!generalFormModel.getG_12().equals(null)) {
                tvPaNo.setText(generalFormModel.getG_12());

            } else if (generalFormModel.getG_12().equals(null)) {
                tvPaNo.setText("");

            }

        } catch (NullPointerException e) {

        }

    }

}
