package il.ac.hit.meepo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
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

import java.util.HashMap;

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

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;


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

        mAbout = findViewById(R.id.edit_info_about_text);
        mJobTitle = findViewById(R.id.edit_info_work_text);
        mAge = findViewById(R.id.edit_info_age_text);
        mGenderRadioGroup = findViewById(R.id.edit_info_gender_group);
        mMale = findViewById(R.id.edit_info_gender_radio_male);
        mFemale = findViewById(R.id.edit_info_gender_radio_female);



        mMainPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL",mUri);
                        reference.updateChildren(map);

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
