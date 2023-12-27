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

public class LoginActivity extends AppCompatActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private FirebaseAuth auth;
    private DatabaseReference data;
    private GoogleSignInClient googleSignInClient;
    private String email, password;

    private TextView tvNoAccount;
    private Button btnLoginFacebook, btnLoginGoogle, btnLogin;
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // check wether The user is already logged in, if logged,redirect to the main activity.
            goToMainActivity();
        }

        tvNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SignUpActivity.getIntent(LoginActivity.this);
                startActivity(intent);
                finish();
            }
        });
        //login from email and password
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if (!(email.isEmpty() || password.isEmpty())) {
                    login(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Fill the information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent signIntent=googleSignInClient.getSignInIntent();
                launcher.launch(signIntent);
            }
        });
    }

    //launcher for login from google
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
                                                Toast.makeText(LoginActivity.this, "Successfully Sign-in with Google", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Google Sign-in Failed", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        } else {

                            Toast.makeText(LoginActivity.this, "Log in fail", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onActivityResult: this task else ra=un");
                            ;
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "login fail", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onActivityResult: laogin fail");
                        ;
                    }

                }
            });

    private void init() {

        tvNoAccount = findViewById(R.id.tv_noaccount);
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLoginFacebook = findViewById(R.id.btn_login_facebook);
        btnLoginGoogle = findViewById(R.id.btn_login_google);
        btnLogin = findViewById(R.id.bnt_login);

        //initialize firebase and database

        auth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance().getReference();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        //initialize firebase database
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


    }

    private void login(String email, String password) { //method for login with email and password
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if login is successful
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                goToMainActivity();
                            }
                            else{
                                // Login failed, handle the error here
                                // You can check the exception to determine the cause of the failure
                                Exception exception = task.getException();
                                if (exception != null) {
                                    Log.e("Login", "Failed with exception: " + exception.getMessage());
                                }
                            }
                        }
                    }
                });
    }

    private void goToMainActivity() {

        Intent intent = MainActivity.getIntent(getApplicationContext());
        startActivity(intent);
        finish();
    }
}