package np.com.naxa.lumanti.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.lumanti.R;
import np.com.naxa.lumanti.model.Constant;
import np.com.naxa.lumanti.model.GeneralFormModel;
import np.com.naxa.lumanti.sugar.NissaNo_Details;

public class NissaNoInputActivity extends AppCompatActivity {

    GeneralFormModel generalFormModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nissa_input_nissa_no)
    AutoCompleteTextView tvNissaNo;
    @BindView(R.id.nissa_input_next)
    Button btnNext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nissa_no_input);
        ButterKnife.bind(this);

        toolbar.setTitle("Shelter Status");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        generalFormModel = new GeneralFormModel();

        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               nextButtonClickListner();
            }
        });



    }





//
//    @OnClick(R.id.nissa_input_next)
//    public void onViewClicked() {
//
////        generalFormModel.setG_10(tvNissaNo.getText().toString());
//
//
//    }


    public void nextButtonClickListner (){
        generalFormModel.setG_10(tvNissaNo.getText().toString());

        if(!tvNissaNo.getText().toString().equals("") && !tvNissaNo.getText().toString().equals(null) && !tvNissaNo.getText().toString().isEmpty()) {


            List<NissaNo_Details> nissaNo_detailses = NissaNo_Details.findWithQuery(NissaNo_Details.class, "Select * from NISSA_NODETAILS where NISSANO = ?", tvNissaNo.getText().toString());

            for (NissaNo_Details details : nissaNo_detailses) {
                generalFormModel.setA1(details.name_of_househead);
                generalFormModel.setG8(details.tole);
                generalFormModel.setG9(details.household_no);
                generalFormModel.setG_10(tvNissaNo.getText().toString());
                generalFormModel.setG_11(details.citizenship_no);
                generalFormModel.setG_12(details.pa_no);
            }
            Log.e("NissaNoDetails SAMIR", "onViewClicked: " + generalFormModel.getA1());
            Log.e("NissaNoDetails SAMIR", "onViewClicked: " + generalFormModel.getG8());
            Log.e("NissaNoDetails SAMIR", "onViewClicked: " + generalFormModel.getG9());
            Log.e("NissaNoDetails SAMIR", "onViewClicked: " + generalFormModel.getG_10());
            Log.e("NissaNoDetails SAMIR", "onViewClicked: " + generalFormModel.getG_11());
            Log.e("NissaNoDetails SAMIR", "onViewClicked: " + generalFormModel.getG_12());

            Constant.countNissaNoInput = 1 ;

            Intent intent_main = new Intent(NissaNoInputActivity.this, MainActivity.class);
            intent_main.putExtra("generalFormModel", generalFormModel);
            startActivity(intent_main);

        }else {
            Toast.makeText(this, "Please enter Nissa No", Toast.LENGTH_SHORT).show();
        }




    }
}
