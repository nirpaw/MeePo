package il.ac.hit.meepo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import il.ac.hit.meepo.Models.User;

import static java.security.AccessController.getContext;

public class EditInfoActivity extends AppCompatActivity {

    private static final String TAG = "EditInfoActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseDatabase mDatabase;


    private Toolbar mToolbar;
    private String userId;
    private User mUser;

    // Android UI
    private ImageView mMainPhoto, mMainPhotoEditor, mPhoto2, mPhoto2Editor;
    private ImageView mPhoto3, mPhoto3Editor, mPhoto4, mPhoto4Editor, mPhoto5, mPhoto5Editor, mPhoto6, mPhoto6Editor;

    private EditText mAbout, mJobTitle;
    private TextView mAge;

    private RadioGroup mGenderRadioGroup;
    private RadioButton mMale, mFemale;

    private ImageButton mSaveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("Users");

        mMainPhoto = findViewById(R.id.editinfo_main_photo);
        mMainPhotoEditor = findViewById(R.id.editinfo_edit_main_photo);

        mAbout = findViewById(R.id.edit_info_about_text);
        mJobTitle = findViewById(R.id.edit_info_work_text);
        mAge = findViewById(R.id.edit_info_age_text);
        mGenderRadioGroup = findViewById(R.id.edit_info_gender_group);
        mMale = findViewById(R.id.edit_info_gender_radio_male);
        mFemale = findViewById(R.id.edit_info_gender_radio_female);





        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    if (user.getId().equals(userId)) {
                        mUser = new User(user);
                        Log.d(TAG, "onDataChange: my user" + mUser.toString());
                    }
                }

                mAbout.setText(mUser.getAbout());
                mJobTitle.setText(mUser.getJobtitle());
                mAge.setText(mUser.getAge()+"");
                if(mUser.getGender().equals("male")){
                    mMale.setChecked(true);
                }else if(mUser.getGender().equals("female")){
                    mFemale.setChecked(true);
                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mToolbar = findViewById(R.id.edit_info_bar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hideSoftKeyboard();

        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUser = dataSnapshot.getValue(User.class);

                if(user.getImageURL().equals("default")){
                    mMainPhoto.setImageResource(R.mipmap.ic_launcher);
                }else{

                        Glide.with(getApplicationContext()).load(user.getImageURL()).centerCrop().into(mMainPhoto);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
