package com.example.familyplanning;

import static java.lang.System.exit;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class NotificationsFragment extends Fragment {
private TextView textP,textC,txtdValue,textEnddate,textremValue,Overdue;
    public NotificationsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.notifications_fragment, container, false);

        textP=v.findViewById(R.id.textP);
        textC=v.findViewById(R.id.textC);
        txtdValue=v.findViewById(R.id.txtdValue);
        textEnddate=v.findViewById(R.id.textEnddate);
        textremValue=v.findViewById(R.id.textremValue);
        Overdue=v.findViewById(R.id.Overdue);

        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
        assert userAuth != null;
        String userId = userAuth.getUid();
//
        Query query = FirebaseDatabase.getInstance().getReference("Members")
                .child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener(){
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                SelectMethod method=snapshot.getValue(SelectMethod.class);
//                assert method != null;
                    String methodname= Objects.requireNonNull(method).getUfpmethod();
                    String period=method.getDuration();
                    String startdate=method.getUstartdate();
                    String enddate= method.getUenddate();
                    String getstatus=method.getStatus();
                    textP.setText(methodname);
                    txtdValue.setText(period);
                    textC.setText(startdate);
                    textEnddate.setText(enddate);
                    switch (getstatus) {
                        case "Active":
                            Date date = Calendar.getInstance().getTime();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                            String currentDate = df.format(date);
                            if (!startdate.equals("") && !enddate.equals("")) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                                LocalDate start = LocalDate.parse(currentDate, formatter);
                                LocalDate end = LocalDate.parse(enddate, formatter);
                                System.out.println(ChronoUnit.DAYS.between(start, end));
                                long number_of_days = ChronoUnit.DAYS.between(start, end);
                                if (number_of_days > 0) {
                                    long years = number_of_days / 365;
                                    long months = (number_of_days - years * 365) / 30;
                                    long days = (number_of_days - years * 365 - months * 30);
                                    textremValue.setText("" + years + " Year(s) " + months + " Month(s)" + days + " Days");

                                } else {


                                    Overdue.setText("Your family planning have expired,Go to nearest health center to renew your method!Thank you for using our app");

                                }
                            } else {
                                Overdue.setText("Go to nearest health center to confirm your method!Thank you for using our app");
                            }
                            break;
                        case "Inactive":
                            Overdue.setText("Your method have been cancelled,choose other or go nearest health center for help.Thank you");

                            break;
                        case "Pending":
                            Overdue.setText("Go to nearest health center to confirm your method!Thank you for using our app");

                            break;
                    }


                }
                else{
                    Overdue.setText("No method selected!Please go to method menu to choose it.Thank you");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                exit(0);

            }
        });
        return v;
    }
}