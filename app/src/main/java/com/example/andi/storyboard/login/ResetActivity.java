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

import com.example.andi.storyboard.MainActivity;
import com.example.andi.storyboard.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {

    private EditText resetEmail;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        resetEmail = (EditText) findViewById(R.id.reset_email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnReset = (Button) findViewById(R.id.reset_send_email);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        btnReset.setOnClickListener(new View.OnClickListener() {
            String email = resetEmail.getText().toString().trim();
            @Override
            public void onClick(View v) {
                if (email != null && !email.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);

                    auth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Reset email sent to " + email, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button backButton = (Button) findViewById(R.id.reset_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}