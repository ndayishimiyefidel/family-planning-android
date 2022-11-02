package com.example.familyplanning;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText textEmail;
    private Button resetButton;
    private ImageView img;
    private FirebaseAuth userAuthReset;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        img=findViewById(R.id.backImg);
        img.setOnClickListener(view -> onBackPressed());

        textEmail=findViewById(R.id.textEmail);
        resetButton=findViewById(R.id.resetButton);
        progress_bar=findViewById(R.id.progress_bar);
        userAuthReset=FirebaseAuth.getInstance();


        //onclick listener

        resetButton.setOnClickListener(view -> {
            String email=textEmail.getText().toString().trim();
            if(email.isEmpty()){
                textEmail.setError("Email Address is required");
                textEmail.requestFocus();
                return;
            }
            if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
                textEmail.setError("please provide valid email address");
                textEmail.requestFocus();
                return;
            }
            progress_bar.setVisibility(view.VISIBLE);
            userAuthReset.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, new StringBuilder().append("Please check your email reset password link."),Toast.LENGTH_LONG).show();
                    textEmail.setText("");
                }
                else{
                    textEmail.setText("");
                    Snackbar snackbar = Snackbar.make(view, "Whoops!,Failed to send email reset password link, try again!", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();

                }
                progress_bar.setVisibility(view.GONE);

            });

        });
    }
}