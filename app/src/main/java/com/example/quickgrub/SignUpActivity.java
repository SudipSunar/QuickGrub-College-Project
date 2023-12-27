package com.example.quickgrub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quickgrub.DataModell.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference data;

    private GoogleSignInClient googleSignInClient;
    private String userName, email, password;
    private Button btnCreate, btnFacebook, btnGoogle;
    private EditText etName, etEmail, etPassword;

    public static Intent getIntent(Context context) {

        return new Intent(context, SignUpActivity.class);
    }

    private TextView tvHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = etName.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if (!(userName.isEmpty() || email.isEmpty() || password.isEmpty())) {
                    createAccount(email, password);
                }
            }
        });


        tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LoginActivity.getIntent(SignUpActivity.this);
                startActivity(intent);
                finish();
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIntent = googleSignInClient.getSignInIntent();
                launcher.launch(signIntent);

            }
        });
    }

    //make launcher for sign in with google
    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                        Log.d("TAG", "onActivityResult: this part run");

                        if (task.isSuccessful()) {

                            Log.d("TAG", "onActivityResult: this part run2");
                            GoogleSignInAccount account = task.getResult();
                            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                            auth.signInWithCredential(credential)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> authtask) {
                                            if (authtask.isSuccessful()) {
                                                Log.d("TAG", "onActivityResult: this part run3");

                                                goToMainActivity();
                                                Toast.makeText(SignUpActivity.this, "Successfully Sign-in with Google", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Google Sign-in Failed", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        } else {

                            Toast.makeText(SignUpActivity.this, "Log in fail", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onActivityResult: this task else ra=un");
                            ;
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "login fail", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onActivityResult: laogin fail");
                        ;
                    }

                }
            });

    private void init() {

        etName = findViewById(R.id.et_sign_name);
        etEmail = findViewById(R.id.et_sign_email);
        etPassword = findViewById(R.id.et_sign_password);
        btnGoogle = findViewById(R.id.btn_google);
        btnFacebook = findViewById(R.id.btn_facebook);
        btnCreate = findViewById(R.id.btn_create_account);
        tvHaveAccount = findViewById(R.id.tv_haveaccount);

        //initialize firebase and database
        auth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance().getReference();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        //initialize firebase database
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


    }


    //method for sign up or create account with email and password
    private void createAccount(String email, String password) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Sign_up Complete", Toast.LENGTH_SHORT).show();
                    Intent intent = LoginActivity.getIntent(getApplicationContext());
                    startActivity(intent);
                    finish();
                    saveUserData();
                }
            }
        });


    }

    private void saveUserData() {   //method to save user in database

        //retrieve data from input field
        userName = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        UserModel userModel = new UserModel(userName, email, password);

        FirebaseUser currentUser = auth.getCurrentUser();

        //save data to firebase database

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child("user").child(userId).setValue(userModel);
        }
    }

    private void goToMainActivity() {
        Intent intent = MainActivity.getIntent(getApplicationContext());
        startActivity(intent);
        finish();
    }
}