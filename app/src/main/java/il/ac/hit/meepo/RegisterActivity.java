package il.ac.hit.meepo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText mFirstName, mEmail, mPassword, mLastName, mAbout, mJobTitle;
    private TextView mAge;
    private CheckBox mCheckMan, mCheckWoman;
    private RadioGroup mRadioGroup;
    private RadioButton mGenderMale, mGenderFemale;
    private Button mCreateAccountBtn;
    private Toolbar mToolbar;
    private static final String TAG = "RegisterActivity";

    //Firebase Auth
    private FirebaseAuth mAuth;
    private DatabaseReference reference, mRefTokens;


    //ProgressDialog
    private ProgressDialog mRegProgress;

    private String currentAge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mRefTokens = FirebaseDatabase.getInstance().getReference("Tokens");

        mFirstName = findViewById(R.id.reg_first_name);
        mLastName = findViewById(R.id.reg_last_name);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mCreateAccountBtn = findViewById(R.id.reg_create_btn);
        mToolbar = findViewById(R.id.register_app_toolbar);
        // New addon new Parameters
        mAbout = findViewById(R.id.reg_about_yourself);
        mJobTitle = findViewById(R.id.reg_jobtitle);
        mAge = findViewById(R.id.reg_age);
        mCheckMan = findViewById(R.id.reg_looking_for_male);
        mCheckWoman = findViewById(R.id.reg_looking_for_female);

        mRadioGroup = findViewById(R.id.reg_radio_group_gender);
        mGenderMale = findViewById(R.id.reg_gender_male);
        mGenderFemale = findViewById(R.id.reg_gender_female);



        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                currentAge = getAge(year,month,day) + "";
                                mAge.setText(currentAge);

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        mRegProgress = new ProgressDialog(this);

        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = mFirstName.getText().toString();
                String last_name = mLastName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String about = mAbout.getText().toString();
                String jobtitle = mJobTitle.getText().toString();
                String age = mAge.getText().toString() + "";
                String gender = "";
                String lookingfor = "";
                if(mCheckMan.isChecked()){
                    lookingfor = "men";
                }
                if(mCheckWoman.isChecked()){
                    lookingfor = "women";
                }
                if (mRadioGroup.getCheckedRadioButtonId() == mGenderMale.getId()){
                    gender = "male";
                }

                if(mRadioGroup.getCheckedRadioButtonId() == mGenderFemale.getId()){
                    gender = "female";
                }


                if(!TextUtils.isEmpty(first_name) && !TextUtils.isEmpty(last_name) && !TextUtils.isEmpty(email)
                        && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(gender)
                        && !TextUtils.isEmpty(lookingfor) && !TextUtils.isEmpty(about) && !TextUtils.isEmpty(jobtitle) ){

                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(first_name, last_name,email, password, currentAge,gender,lookingfor, about, jobtitle);
                }
                else{
                    Snackbar.make(findViewById(R.id.reg_layout),"Fill all fields please",Snackbar.LENGTH_SHORT).show();

                }

            }
        });


    }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private void register_user(final String first_name, final String last_name , String email, String password, final String age, final String gender, final String lookingfor, final String about, final String jobtitle) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mRegProgress.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            String userid = user.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("firstName", first_name);
                            hashMap.put("lastName", last_name);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("gender",gender);
                            hashMap.put("age", age);
                            hashMap.put("looking",lookingfor);
                            hashMap.put("search", first_name.toLowerCase() + " " + last_name.toLowerCase());
                            hashMap.put("about",about);
                            hashMap.put("jobtitle",jobtitle);
                            List<String> arrayListUrls = new ArrayList<>();
                            arrayListUrls.add("default");
                            arrayListUrls.add("default");
                            arrayListUrls.add("default");
                            arrayListUrls.add("default");
                            arrayListUrls.add("default");
                            hashMap.put("imagesUrlList",arrayListUrls);
                            hashMap.put("lastLocationPlaceId","Not in place");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        String currentUserId = mAuth.getCurrentUser().getUid();
                                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                        mRefTokens.child(currentUserId).child("device_token")
                                                .setValue(deviceToken)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(mainIntent);
                                                            finish();
                                                        }
                                                    }
                                                });



                                    }
                                    else{
                                        Log.d(TAG, "onComplete: Something Get Wrong!");
                                    }
                                }
                            });



                        } else {
                            // If sign in fails, display a message to the user.
                            mRegProgress.hide();
                            Log.d(TAG, "onComplete: Fails to display a message!");
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }
}
