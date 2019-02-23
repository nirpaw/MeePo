package il.ac.hit.meepo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import il.ac.hit.meepo.Models.User;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Toolbar mToolbar;
    private User mUser;


    // Android UI
    private TextView mCurrentLocation;
    private Switch mShowMeMen;
    private Switch mShowMeWomen;
    private ImageButton mSaveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mToolbar = findViewById(R.id.settings_bar);
        mCurrentLocation = findViewById(R.id.settings_discovery_settings_location);
        mShowMeMen = findViewById(R.id.settings_male_sw);
        mShowMeWomen = findViewById(R.id.settings_female_sw);
        mSaveButton = findViewById(R.id.settings_save_btn);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mShowMeMen.setChecked(false);
        mShowMeWomen.setChecked(false);

        if(mUser.getLooking().equals("men")){
            mShowMeMen.setChecked(true);
        }

        if(mUser.getLooking().equals("women")){
            mShowMeWomen.setChecked(true);
        }




    }
}
