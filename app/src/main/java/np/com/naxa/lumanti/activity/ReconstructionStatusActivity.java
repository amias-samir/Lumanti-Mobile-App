package np.com.naxa.lumanti.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.lumanti.R;

public class ReconstructionStatusActivity extends AppCompatActivity {

    Toolbar toolbar;

    @BindView(R.id.reconstruction_status_GpsStart)
    Button btnGpsStart;
    @BindView(R.id.reconstruction_status_preview_map)
    Button btnPreviewMap;
    @BindView(R.id.reconstruction_status_photo_site1)
    ImageButton btnPhotoSite1;
    @BindView(R.id.preconstruction_PhotographSiteimageViewPreview1)
    ImageView ivPhotographSiteimageViewPreview1;
    @BindView(R.id.reconstruction_status_photo_site2)
    ImageButton btnPhotoSite2;
    @BindView(R.id.preconstruction_PhotographSiteimageViewPreview2)
    ImageView ivPhotographSiteimageViewPreview2;
    @BindView(R.id.reconstruction_status_photo_site3)
    ImageButton btnPhotoSite3;
    @BindView(R.id.preconstruction_PhotographSiteimageViewPreview3)
    ImageView ivPhotographSiteimageViewPreview3;
    @BindView(R.id.reconstruction_status_photo_site4)
    ImageButton btnPhotoSite4;
    @BindView(R.id.preconstruction_PhotographSiteimageViewPreview4)
    ImageView ivPhotographSiteimageViewPreview4;
    @BindView(R.id.reconstruction_status_living_situation)
    Spinner spinnerLivingSituation;
    @BindView(R.id.reconstruction_status_build_by)
    Spinner spinnerBuildBy;
    @BindView(R.id.reconstruction_status_construction_type)
    Spinner spinnerConstructionType;
    @BindView(R.id.reconstruction_status_others_specify)
    AutoCompleteTextView tvOthersSpecify;
    @BindView(R.id.reconstruction_status_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconstruction_status);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Reconstruction Information");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @OnClick(R.id.reconstruction_status_next)
    public void NextPage() {


        Intent intent = new Intent(ReconstructionStatusActivity.this, EarthquakeReliefStatusActivity.class);
        startActivity(intent);
    }
}
