package com.example.familyplanning;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private EditText fullName, textNumber,textPhone,Email,textPwd,textdob,textSector,textCell;
    private DatabaseReference reff;
    private String UserId;
    private RadioButton rbtnMale;
    private RadioButton rbtnFemale;
    private String _NAME,_EMAIL,_GENDER,_NID,_PHONE,_DOB,_PASSWORD,_SECTOR,_CELL;

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
        textSector=view.findViewById(R.id.textSector);
        textCell=view.findViewById(R.id.textCell);
        textPwd=view.findViewById(R.id.textPassword);
        textdob=view.findViewById(R.id.textdob);
        rbtnMale=view.findViewById(R.id.rbtnMale);
        rbtnFemale=view.findViewById(R.id.rbtnFemale);
        System.out.println("gener:"+rbtnFemale);
        //date picker
        textdob.setOnClickListener(arg0 -> {

            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(getContext(), (view1, year, monthOfYear, dayOfMonth) -> {
                String date = dayOfMonth + "-" + (monthOfYear + 1)
                        + "-" + year;
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
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        reff= FirebaseDatabase.getInstance().getReference("UserAccounts");
        reff.keepSynced(true);
        UserId= Objects.requireNonNull(userAuth).getUid();
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
                    _CELL=userProfile.getCell();
                    _SECTOR=userProfile.getSector();
                    System.out.println("your gender is "+_GENDER);


                    //set text to edit text
                    fullName.setText(_NAME);
                    textNumber.setText(_NID);
                    textPhone.setText(_PHONE);
                    Email.setText(_EMAIL);
                    textdob.setText(_DOB);
                    textPwd.setText(_PASSWORD);
                    textSector.setText(_SECTOR);
                    textCell.setText(_CELL);
                    //check which gender logged in
                    System.out.println("your gender is"+_GENDER);
                    if(_GENDER.equals("Male")){
                        rbtnMale.setChecked(true);
                    }
                    else if(_GENDER.equals("Female")){
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
          if(isNameChanged()||isPasswordChanged()||isPhoneChanged()||isNidChanged()||isDobChanged()||isGenderChanged()||isSectorChanged()||isCellChanged()){
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
    private boolean isSectorChanged() {
        if (_SECTOR.equals(textSector.getText().toString())) {
            return false;
        } else {
            reff.child(UserId).child("sector").setValue(textSector.getText().toString());
            return  true;
        }

    }
    private boolean isCellChanged() {
        if (_CELL.equals(textCell.getText().toString())) {
            return false;
        } else {
            reff.child(UserId).child("cell").setValue(textCell.getText().toString().trim());
            return  true;
        }

    }
    private boolean isNidChanged() {
        if (_NID.equals(textNumber.getText().toString())) {
            return false;
        } else {
            reff.child(UserId).child("idno").setValue(textNumber.getText().toString());
            return  true;
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