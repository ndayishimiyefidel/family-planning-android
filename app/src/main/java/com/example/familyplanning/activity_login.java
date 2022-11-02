package com.example.familyplanning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class activity_login extends AppCompatActivity {
private Button signButton;
private EditText textEmail,textPassword;
private FirebaseAuth userAuthSignin;
private ProgressBar progress_bar;
private TextView passwordForgot;
private ImageView circleImg;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        circleImg=findViewById(R.id.circleImg);
        Glide.with(this).load(circleImg).circleCrop();

        //forget password
        passwordForgot=findViewById(R.id.passwordForgot);
        passwordForgot.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class)));

        //initial progress
        progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
        //initialize user input
        textEmail=findViewById(R.id.textEmail);
        textPassword=findViewById(R.id.textPassword);
        signButton=findViewById(R.id.signButton);
        //firebase auth
        userAuthSignin=FirebaseAuth.getInstance();
        //on clicked sign button
        signButton.setOnClickListener(view -> {
            String email=textEmail.getText().toString().trim();
            String password=textPassword.getText().toString().trim();
            //simple validation
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
            if (password.isEmpty()) {
                textPassword.setError("password  is required");
                textPassword.requestFocus();
                return;
            }
            if(password.length()<4){
                textPassword.setError("password  must be at least 6 character");
                textPassword.requestFocus();

                return;
            }
            progress_bar.setVisibility(view.VISIBLE);
            userAuthSignin.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        FirebaseUser us=FirebaseAuth.getInstance().getCurrentUser();
                                        if(us.isEmailVerified()){

                                            Snackbar snackbar = Snackbar.make(view, "Login successful!", Snackbar.LENGTH_LONG);
                                            snackbar.setBackgroundTint(Color.GREEN);
                                            snackbar.show();
                                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                        }
                                        else{
                                            textEmail.setText("");
                                            textPassword.setText("");
                                            Toast.makeText(activity_login.this, new StringBuilder().append("Please check your email to  verify it."),Toast.LENGTH_LONG).show();
//
                                            us.sendEmailVerification();

                                            progress_bar.setVisibility(view.GONE);
                                        }

                                    }
                                    else{
                                        textEmail.setText("");
                                        textPassword.setText("");
                                        //Toast.makeText(activity_login.this, new StringBuilder().append("Whoops!,something went wrong,could not login"),Toast.LENGTH_LONG).show();
                                        Snackbar snackbar = Snackbar.make(view, "Whoops!,something went wrong,could not login,check your credentials", Snackbar.LENGTH_LONG);
                                        snackbar.setBackgroundTint(Color.RED);
                                        snackbar.show();

                                        progress_bar.setVisibility(view.GONE);
                                    }

                                }
                            });
        });
    }
    public void createAccount(View view) {
        startActivity(new Intent(getApplicationContext(), activity_signup.class));
    }
}