package il.ac.hit.meepo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
    private String currentAge="";

    private RadioGroup mGenderRadioGroup;
    private RadioButton mMale, mFemale;

    private ImageButton mSaveBtn;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    private String currentPhotoForUpload = "";

    private ProgressDialog mLogProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        mMainPhoto = findViewById(R.id.editinfo_main_photo);
        mMainPhotoEditor = findViewById(R.id.editinfo_edit_main_photo);
        mPhoto2 = findViewById(R.id.editinfo_photo_2);
        mPhoto2Editor = findViewById(R.id.imageView6);
        mPhoto3 = findViewById(R.id.editinfo_photo_3);
        mPhoto3Editor = findViewById(R.id.imageView8);
        mPhoto4 = findViewById(R.id.edit_photo_4);
        mPhoto4Editor = findViewById(R.id.imageView10);
        mPhoto5 = findViewById(R.id.edit_photo_6);
        mPhoto5Editor = findViewById(R.id.imageView11);
        mPhoto6 = findViewById(R.id.edit_photo_5);
        mPhoto6Editor = findViewById(R.id.imageView12);

        mAbout = findViewById(R.id.edit_info_about_text);
        mJobTitle = findViewById(R.id.edit_info_work_text);
        mAge = findViewById(R.id.edit_info_age_text);
        mGenderRadioGroup = findViewById(R.id.edit_info_gender_group);
        mMale = findViewById(R.id.edit_info_gender_radio_male);
        mFemale = findViewById(R.id.edit_info_gender_radio_female);
        mSaveBtn = findViewById(R.id.edit_info_save_btn);

        mAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditInfoActivity.this,
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

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Save Btn");

                reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                HashMap<String, Object> map = new HashMap<>();
                String age = mAge.getText().toString();
                if(!age.isEmpty())
                    map.put("age", age);
                String about = mAbout.getText().toString();
                String currentJob= mJobTitle.getText().toString();

                if(!about.isEmpty())
                    map.put("about", about);
                if (!currentJob.isEmpty()){
                    map.put("jobtitle", currentJob);
                }

                if(map.size()>0) {
                    mLogProgress = new ProgressDialog(EditInfoActivity.this);
                    mLogProgress.setTitle("Saving");
                    mLogProgress.setMessage("Please wait while saving...");
                    mLogProgress.setCanceledOnTouchOutside(false);;
                    mLogProgress.show();
                    reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mLogProgress.dismiss();
                                Toast.makeText(EditInfoActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mLogProgress.dismiss();
                            }
                        }
                    });
                }

            }
        });




        mMainPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoForUpload="MainPhoto";
                openImage();
            }
        });

        mPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoForUpload="Photo2";
                openImage();
            }
        });
        mPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoForUpload="Photo3";
                openImage();
            }
        });

        mPhoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoForUpload="Photo4";
                openImage();
            }
        });

        mPhoto5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoForUpload="Photo5";
                openImage();
            }
        });

        mPhoto6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoForUpload="Photo6";
                openImage();
            }
        });





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

                if(user.getImageURL().equals("default") ){
                    mMainPhoto.setImageResource(R.drawable.profile_image);
                }else{

                    Glide.with(getApplicationContext()).load(user.getImageURL()).centerCrop().into(mMainPhoto);

                }
                List<String> listOfImageUrl = new ArrayList<>( user.getImagesUrlList());
                for (int i = 0; i < listOfImageUrl.size() ; i++) {
                    if(listOfImageUrl.get(i).equals("default")){
                        switch (i){
                            case 0:
                                mPhoto2.setImageResource(R.drawable.profile_image);
                                break;
                            case 1:
                                mPhoto3.setImageResource(R.drawable.profile_image);
                                break;
                            case 2:
                                mPhoto4.setImageResource(R.drawable.profile_image);
                                break;
                            case 3:
                                mPhoto5.setImageResource(R.drawable.profile_image);
                                break;
                            case 4:
                                mPhoto6.setImageResource(R.drawable.profile_image);
                                break;
                        }
                    }else{
                        switch (i){
                            case 0:
                                Glide.with(getApplicationContext()).load(listOfImageUrl.get(i)).centerCrop().into(mPhoto2);
                                break;
                            case 1:
                                Glide.with(getApplicationContext()).load(listOfImageUrl.get(i)).centerCrop().into(mPhoto3);
                                break;
                            case 2:
                                Glide.with(getApplicationContext()).load(listOfImageUrl.get(i)).centerCrop().into(mPhoto4);
                                break;
                            case 3:
                                Glide.with(getApplicationContext()).load(listOfImageUrl.get(i)).centerCrop().into(mPhoto5);
                                break;
                            case 4:
                                Glide.with(getApplicationContext()).load(listOfImageUrl.get(i)).centerCrop().into(mPhoto6);
                                break;
                        }
                    }
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


    private void openImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(EditInfoActivity.this);
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        if(currentPhotoForUpload.equals("MainPhoto")) {

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("imageURL", mUri);
                            reference.updateChildren(map);
                        }
                        else{
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            DatabaseReference refForImages = reference.child("imagesUrlList");
                            HashMap<String, Object> map = new HashMap<>();

                            if(currentPhotoForUpload.equals("Photo2")) {
                                map.put("0", mUri);
                                refForImages.updateChildren(map);
                            }

                            if(currentPhotoForUpload.equals("Photo3")) {
                                map.put("1", mUri);
                                refForImages.updateChildren(map);
                            }

                            if(currentPhotoForUpload.equals("Photo4")) {
                                map.put("2", mUri);
                                refForImages.updateChildren(map);
                            }

                            if(currentPhotoForUpload.equals("Photo5")) {
                                map.put("3", mUri);
                                refForImages.updateChildren(map);
                            }

                            if(currentPhotoForUpload.equals("Photo6")) {
                                map.put("4", mUri);
                                refForImages.updateChildren(map);
                            }
                        }

                        pd.dismiss();
                    }
                    else{
//                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else{
            Toast.makeText(EditInfoActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }



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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(EditInfoActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            }
            else{
                uploadImage();
            }
        }
    }
}
