package com.example.familyplanning;

import static android.app.ProgressDialog.show;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileFragment extends Fragment {
    private EditText fullName, textNumber,textPhone,Email,textPwd,textdob;
    private CheckBox terms;
    private RadioGroup radioGroup;
    private Button btnUpdate;
    private DatabaseReference reff;
    private FirebaseUser userAuth;
    private String UserId;
    private RadioButton rbtnMale;
    private RadioButton rbtnFemale;
    private String _NAME,_EMAIL,_GENDER,_NID,_PHONE,_DOB,_PASSWORD;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.profile_fragment, container, false);
        fullName=view.findViewById(R.id.fullName);
        textNumber=view.findViewById(R.id.textidNumber);
        textPhone=view.findViewById(R.id.textPhoneNumber);
        Email=view.findViewById(R.id.Email);
        textPwd=view.findViewById(R.id.textPassword);
        textdob=view.findViewById(R.id.textdob);
        rbtnMale=view.findViewById(R.id.rbtnMale);
        rbtnFemale=view.findViewById(R.id.rbtnFemale);
        System.out.println("gener:"+rbtnFemale);
        //check box
        terms=view.findViewById(R.id.terms);
        //date picker
        textdob.setOnClickListener(arg0 -> {

            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) (view1,year, monthOfYear, dayOfMonth) -> {
                String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                        + "-" + String.valueOf(year);
                textdob.setText(date);
            }, yy, mm, dd);
            datePicker.show();
        });
        //radio button group
//        radioGroup=view.findViewById(R.id.rbtnGender);
//        RadioButton genderBtn=(RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
//        final String[] genders = {genderBtn.getText().toString()};
//        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
//            switch (i){
//                case R.id.rbtnMale:
//                    genders[0] = "Male";
//                    break;
//                case R.id.rbtnFemale:
//                    genders[0] = "Female";
//                    break;
//            }
//        });
        //get button
        btnUpdate=view.findViewById(R.id.btnUpdate);
        userAuth=FirebaseAuth.getInstance().getCurrentUser();

        reff= FirebaseDatabase.getInstance().getReference("UserAccounts");
        reff.keepSynced(true);
        UserId= userAuth.getUid();
        //fetch and display profile information
        reff.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile=snapshot.getValue(Users.class);
                if(userProfile!=null){
                    _NAME=userProfile.getFuln();
                    _EMAIL=userProfile.getEmail();
                    _DOB=userProfile.getDob();
                    _NID= userProfile.getIdno();
                    _PHONE=userProfile.getIphone();
                    _GENDER=userProfile.getGender();
                    _PASSWORD=userProfile.getPassword();


                    //set text to edit text
                    fullName.setText(_NAME);
                    textNumber.setText(_NID);
                    textPhone.setText(_PHONE);
                    Email.setText(_EMAIL);
                    textdob.setText(_DOB);
                    textPwd.setText(_PASSWORD);
                    //check which gender logged in
                    System.out.println("your gender is"+_GENDER);
                    if(_GENDER.equals("Male")){
                        rbtnMale.setChecked(true);
                    }
                    else{
                        rbtnFemale.setChecked(true);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //update profile information
        btnUpdate.setOnClickListener(view1 -> {
          if(isNameChanged()||isPasswordChanged()||isPhoneChanged()||isNidChanged()||isDobChanged()||isGenderChanged()){
              Snackbar snackbar = Snackbar.make(view1, "Your profile information have been updated successfully!", Snackbar.LENGTH_LONG);
              snackbar.setBackgroundTint(Color.BLUE);
              snackbar.show();
          }
          else{
              Snackbar snackbar = Snackbar.make(view1, "Your profile is the same!", Snackbar.LENGTH_LONG);
              snackbar.setBackgroundTint(Color.GREEN);
              snackbar.show();
          }
      });
        return view;
    }
    private boolean isNidChanged() {
        if(!_NID.equals(textNumber.getText().toString())){
            reff.child(UserId).child("idno").setValue(textNumber.getText().toString());
            return  true;
        }
        else{
            return false;
        }

    }
    private boolean isPhoneChanged() {
        if(!_PHONE.equals(textPhone.getText().toString())){
            reff.child(UserId).child("iphone").setValue(textPhone.getText().toString());
            return  true;

        }
        else{
            return false;
        }
    }
    private boolean isPasswordChanged() {
        if(!_PASSWORD.equals(textPwd.getText().toString())){
            reff.child(UserId).child("password").setValue(textPwd.getText().toString());
            return  true;

        }
        else{
            return false;
        }
    }
    private boolean isNameChanged() {
        if(!_NAME.equals(fullName.getText().toString())){
            reff.child(UserId).child("fuln").setValue(fullName.getText().toString());
            return  true;

        }
        else{
            return false;
        }
    }
    private boolean isDobChanged() {
        if(!_DOB.equals(textdob.getText().toString())){
            reff.child(UserId).child("dob").setValue(textdob.getText().toString());
            return  true;

        }
        else{
            return false;
        }
    }
    private boolean isGenderChanged() {
        if(!_GENDER.equals(rbtnFemale.getText().toString())){
            reff.child(UserId).child("gender").setValue(textdob.getText().toString());
            return  true;

        }
        else if(!_GENDER.equals(rbtnMale.getText().toString())){
            reff.child(UserId).child("gender").setValue(textdob.getText().toString());
            return  true;
        }
        else{
            return false;
        }
    }

}