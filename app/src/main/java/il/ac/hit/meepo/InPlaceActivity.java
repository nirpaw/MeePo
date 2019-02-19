package il.ac.hit.meepo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import il.ac.hit.meepo.Models.Place;

public class InPlaceActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private static final String TAG = "InPlaceActivity";
    private Place mPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_place);

        mToolbar = findViewById(R.id.place_app_toolbar);
        mPlace = (Place)getIntent().getSerializableExtra("CurrentPlace");
        setSupportActionBar(mToolbar);
        String PlaceTitle = mPlace.getmPlaceName();
        getSupportActionBar().setTitle(PlaceTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
