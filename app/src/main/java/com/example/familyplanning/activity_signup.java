package com.example.familyplanning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class activity_signup extends AppCompatActivity {
    private ImageView img;
    private EditText fullName, textNumber,textPhone,Email,textPwd,cfPwd,textDob;
    private CheckBox terms;
    private RadioGroup radioGroup;
    private Button btnCreate;
    private ProgressBar progress_bar;
    private DatabaseReference reff;
    private FirebaseAuth userAuth;
    private String token;
    private final String regrex="^07[2,3,8,9]{1}\\d{7}$";
    private final String flnReg="/^[a-zA-Z\\s]+$/";
//    private final String regrexId="^1[2,3,8,9]{1}\\d{7}$";


    Users users;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        //back space
        img=findViewById(R.id.backImg);
        img.setOnClickListener(view -> onBackPressed());
        //get variables from xml
        fullName=findViewById(R.id.fullName);
        textNumber=findViewById(R.id.textidNumber);
        textPhone=findViewById(R.id.textPhoneNumber);
        Email=findViewById(R.id.Email);
        textPwd=findViewById(R.id.textPassword);
        cfPwd=findViewById(R.id.textcfPassword);
        textDob=findViewById(R.id.textDob);
        //get button
        btnCreate=findViewById(R.id.btnCreate);
        //progress bar
        progress_bar=findViewById(R.id.progress_bar);

        //check box

        terms=findViewById(R.id.terms);

        Log.d("MyLog","DEVICE TOKEN  is"+ FirebaseInstanceId.getInstance().getToken());
        token=FirebaseInstanceId.getInstance().getToken();
        System.out.println("device token is"+token);


//        textdob.setOnClickListener(arg0 -> {
//
//            final Calendar calendar = Calendar.getInstance();
//            int yy = calendar.get(Calendar.YEAR);
//            int mm = calendar.get(Calendar.MONTH);
//            int dd = calendar.get(Calendar.DAY_OF_MONTH);
//            DatePickerDialog datePicker = new DatePickerDialog(activity_signup.this, (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
//                String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1)
//                        + "/" + String.valueOf(year);
//                textdob.setText(date);
//            }, yy, mm, dd);
//            datePicker.show();
//        });

        //radio button group
        radioGroup=findViewById(R.id.rbtnGender);
        RadioButton genderBtn=(RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        final String[] gender = {genderBtn.getText().toString()};
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.rbtnMale:
                    gender[0] = "Male";
                    break;
                case R.id.rbtnFemale:
                    gender[0] = "Female";
                    break;
            }
        });

        //firebase authentication
        userAuth=FirebaseAuth.getInstance();
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = df.format(date);
        System.out.println("current date is"+currentDate);

        btnCreate.setOnClickListener(view -> {
            String fuln=fullName.getText().toString().trim();
            String email=Email.getText().toString().trim();
            String password=textPwd.getText().toString().trim();
            String dob=textDob.getText().toString().trim();
            String idno=textNumber.getText().toString().trim();
            String iphone=textPhone.getText().toString().trim();
            String cp=cfPwd.getText().toString().trim();
            System.out.println("your dob is"+dob);



            //simple validation

            if (fuln.isEmpty()) {
               fullName.setError("Full name is required");
               fullName.requestFocus();

                return;
            }
            if(!fuln.matches(flnReg)){
                fullName.setError("Names should not contain only letter!");
                fullName.requestFocus();
                return;
            }
            if (idno.isEmpty()) {
                textNumber.setError("Identity Number is required");
                textNumber.requestFocus();
                return;
            }
            if(idno.length()<16){
                textNumber.setError("Identity Number must be at least 16 digits");
                textNumber.requestFocus();
                return;
            }
            if(iphone.isEmpty()){
                textPhone.setError("Phone is required");
                textPhone.requestFocus();
                return;
            }
            if(iphone.length()<10){
                textPhone.setError("Phone number must be at least 10 digits");
                textPhone.requestFocus();
                return;
            }
            if(!iphone.matches(regrex)){
                textPhone.setError("Phone number must have format:07[2,3,8,9]xxxxxxxx");
                textPhone.requestFocus();
                return;
            }

            if (dob.isEmpty()) {
                textDob.setError("DOB name is required");
                textDob.requestFocus();
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate start = LocalDate.parse(currentDate,formatter);
            LocalDate end = LocalDate.parse(dob,formatter);
            System.out.println("years:"+ChronoUnit.YEARS.between(end, start));
            long number_of_years=ChronoUnit.YEARS.between(end, start);
            if(number_of_years<18)
            {
                textDob.setError("You're under 18, you don't allowed family planning method");
                textDob.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                Email.setError("Email is required");
                Email.requestFocus();
                return;
            }
            if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
                Email.setError("please provide valid email");
                Email.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                textPwd.setError("password  is required");
                textPwd.requestFocus();
                return;
            }
            if(password.length()<6){
                textPwd.setError("password  must be at least 6 character");
                textPwd.requestFocus();
                return;
            }
            if(cp.isEmpty()){
                cfPwd.setError("Confirm password is required");
                cfPwd.requestFocus();
                return;
            }
            if(!(password.equals(cp)))
            {
               Toast.makeText(this,"Two password must be the same",Toast.LENGTH_LONG).show();
            }
            if(token.equals(""))
            {
                Snackbar snackbar = Snackbar.make(view, "Whoops,Error No Device Token or Registration Id generated,Check your internet connection and Try again!", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }



            progress_bar.setVisibility(view.VISIBLE);
            reff = FirebaseDatabase.getInstance().getReference().child("UserAccounts");

            reff.orderByChild("idno").equalTo(idno).addValueEventListener(new ValueEventListener(){

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //id number exists in Database
                                fullName.setText("");
                                textNumber.setText("");
                                textPhone.setText("");
                                Email.setText("");
                                textPwd.setText("");
                                cfPwd.setText("");
                                textDob.setText("");
                                Snackbar snackbar = Snackbar.make(view, "Whoops,Error id number Already exist", Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(Color.RED);
                                snackbar.show();
                                progress_bar.setVisibility(view.GONE);
                            } else {

                                //account does not exist
                                userAuth.createUserWithEmailAndPassword(email,password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    Users user=new Users(fuln,idno,iphone,email,password,dob,gender[0],token);
                                                    //firebase database connection
                                                    reff= FirebaseDatabase.getInstance().getReference("UserAccounts");
                                                    reff.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .setValue(user).addOnCompleteListener(task1 -> {
                                                                if(task1.isSuccessful()){

                                                                    // Toast.makeText(activity_signup.this, new StringBuilder().append("Thank you for creating account").append(fullName).toString(),Toast.LENGTH_LONG).show();
                                                                    Snackbar snackbar = Snackbar.make(view, "Thank you for creating account!", Snackbar.LENGTH_LONG);
                                                                    snackbar.setBackgroundTint(Color.GREEN);
                                                                    snackbar.show();
                                                                    startActivity(new Intent(getApplicationContext(), activity_login.class));
                                                                }
                                                                else{
                                                                    fullName.setText("");
                                                                    textNumber.setText("");
                                                                    textPhone.setText("");
                                                                    Email.setText("");
                                                                    textPwd.setText("");
                                                                    cfPwd.setText("");
                                                                    textDob.setText("");
                                                                    //Toast.makeText(activity_signup.this, new StringBuilder().append("Whoops,Error while creating account,try again "),Toast.LENGTH_LONG).show();
                                                                    Snackbar snackbar = Snackbar.make(view, "Whoops,Error while creating account,try again", Snackbar.LENGTH_LONG);
                                                                    snackbar.setBackgroundTint(Color.RED);
                                                                    snackbar.show();
                                                                    progress_bar.setVisibility(view.GONE);
                                                                }
                                                            });
                                                }

                                            }
                                        });
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        });


    }
    public void backTossingIn(View view) {
        onBackPressed();
    }
}