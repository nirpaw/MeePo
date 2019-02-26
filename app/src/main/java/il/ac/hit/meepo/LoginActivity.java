package il.ac.hit.meepo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mEmail, mPassword;
    private TextView mForgotPassword;
    private Button mLoginBtn;
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference mRefTokens;

    //ProgressDialog
    private ProgressDialog mLogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mRefTokens = FirebaseDatabase.getInstance().getReference("Tokens");

        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        mLoginBtn = findViewById(R.id.login_log_btn);
        mToolbar = findViewById(R.id.login_app_toolbar);
        mForgotPassword = findViewById(R.id.login_forgot_password);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLogProgress = new ProgressDialog(this);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    mLogProgress.setTitle("Login");
                    mLogProgress.setMessage("Please wait while login in...");
                    mLogProgress.setCanceledOnTouchOutside(false);;
                    mLogProgress.show();
                    login_user(email, password);
                }

            }
        });



        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));

            }
        });


    }

    private void login_user(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mLogProgress.dismiss();
                            final String currentUserId = mAuth.getCurrentUser().getUid();
                            String deviceToken;
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    mRefTokens.child(currentUserId).child("device_token")
                                            .setValue(instanceIdResult.getToken())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(mainIntent);
                                                        finish();
                                                    }
                                                }
                                            });

                                }
                            });



                        } else {
                            mLogProgress.hide();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }
}
