package com.example.andi.storyboard.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.main.MainActivity;
import com.example.andi.storyboard.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputConfirmPassword, inputUsername;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputUsername = (EditText) findViewById(R.id.username);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = inputUsername.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirm_password = inputConfirmPassword.getText().toString().trim();


                if (TextUtils.isEmpty(username)) {
                    //Toast.makeText(getApplicationContext(), "Enter your username!", Toast.LENGTH_SHORT).show();
                    inputUsername.setError("Please enter an username");
                    return;
                }

                if (username.length() > 16) {
                    //Toast.makeText(getApplicationContext(), "Maximum length allowed is 16 characters", Toast.LENGTH_SHORT).show();
                    inputUsername.setError("Maximum length allowed is 16 characters");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    //Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    inputEmail.setError("Please enter an email");
                    return;
                }

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm_password)) {
                    //Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    inputPassword.setError("Please enter a password");
                    return;
                }
                if (!password.equals(confirm_password)) {
                    //Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    inputConfirmPassword.setError("Passwords do not match");
                    return;
                }
                if (password.length() < 6) {
                    //Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    inputPassword.setError("Password too short, enter minimum 6 characters!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    FireStoreOps.createAuthor(username, auth.getCurrentUser().getUid());

                                    //Update the user's display name/username
                                    UserProfileChangeRequest profileUpdates =
                                            new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build();

                                    auth.getCurrentUser().updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignupActivity.this, "User account successfully created!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(SignupActivity.this, "User account creation failed.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}