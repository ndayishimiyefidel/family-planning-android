package com.example.familyplanning;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SingleMethodActivity extends AppCompatActivity {
    private Button btnContinue;
    private FirebaseAuth userAuth;
    private DatabaseReference reff;
    private String currentUserId;
    private String  mPeriod;
    private String mName;
    private  String name;
    private  String email;
    private  String nid;
    private  String phone;
    private String gender;
    private String token;
    private String sector,cell,dob;
    private  CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_activity);
        ImageView backImg = findViewById(R.id.backImg);
        //on back pressed
        backImg.setOnClickListener(view -> onBackPressed());

        //initializing data from previous activity
         mName=getIntent().getStringExtra("methodname");
        String mCategory = getIntent().getStringExtra("category");
        mPeriod=getIntent().getStringExtra("period");
        String mAdvantages=getIntent().getStringExtra("advantages");
        String mDisadv=getIntent().getStringExtra("disadvantages");
        String mUsage=getIntent().getStringExtra("usage");
        String mImgurl=getIntent().getStringExtra("imgurl");
        //get find text from activity
        TextView textTitle = findViewById(R.id.textTitle);
        TextView textP = findViewById(R.id.textP);
        TextView textUsageDesc = findViewById(R.id.textUsageDesc);
        TextView textC = findViewById(R.id.textC);
        TextView textAdvDesc = findViewById(R.id.textAdvDesc);
        TextView textDisadvDesc = findViewById(R.id.textDisadvDesc);
        checkBox = findViewById(R.id.checkBox);
        ImageView imageView = findViewById(R.id.singleImg);
        //finally, assign those value to text view
        textTitle.setText(mName);
        textUsageDesc.setText(mUsage);
        textAdvDesc.setText(mAdvantages);
        textDisadvDesc.setText(mDisadv);
        textP.setText(mPeriod);
        textC.setText(mCategory);
        Glide.with(this).load(mImgurl).circleCrop().into(imageView);
        userAuth=FirebaseAuth.getInstance();
        btnContinue=findViewById(R.id.continueBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=userAuth.getCurrentUser();

        if (user != null) {
            reff= FirebaseDatabase.getInstance().getReference("UserAccounts");
            reff.keepSynced(true);
            currentUserId=user.getUid();
            String st="Active";
            String searchKey=currentUserId+" "+st;

            System.out.println("search key:"+searchKey);
            String[] str=searchKey.split(" ");
            String searchKey1=str[0];
            String searchKey2=str[1];
            System.out.println("search key1:"+searchKey1);
            System.out.println("search key2:"+searchKey2);
            reff.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users userProfile=snapshot.getValue(Users.class);
                    if(userProfile!=null){
                         name=userProfile.getFuln();
                         email=userProfile.getEmail();
                         dob=userProfile.getDob();
                         nid= userProfile.getIdno();
                         phone=userProfile.getIphone();
                         sector=userProfile.getSector();
                         cell=userProfile.getCell();
                         gender=userProfile.getGender();
                         token=userProfile.getToken();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            btnContinue.setOnClickListener(view -> {
                //check whether user is male or female
                System.out.println("your gender is "+gender);
                if (!checkBox.isChecked()) {
                    Snackbar snackbar = Snackbar.make(view, "Whoops,Error you must agree terms and conditions", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                    return;
                }
                if(gender.equals("Male") && mPeriod.equals("Permanent")){
                    reff = FirebaseDatabase.getInstance().getReference().child("Members");
                    reff.keepSynced(true);
                    reff.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot DataSnapshot1 :snapshot.getChildren()){
                                if(DataSnapshot1.child("uid").exists()&&DataSnapshot1.child("status").exists()){
                                    if(Objects.requireNonNull(DataSnapshot1.child("uid").getValue()).toString().equals(searchKey1)&& Objects.requireNonNull(DataSnapshot1.child("status").getValue()).toString().equals(searchKey2)) {
                                        //Do What You Want To Do.
                                        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

                                            NotificationChannel channel=new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
                                            NotificationManager manager=getSystemService(NotificationManager.class);
                                            manager.createNotificationChannel(channel);
                                        }
                                        NotificationCompat.Builder builder=new NotificationCompat.Builder(SingleMethodActivity.this,"n")
                                                .setContentText("family planning")
                                                .setSmallIcon(R.drawable.ic_notifications)
                                                .setAutoCancel(true)
                                                .setContentText(getString(R.string.help_center));
                                        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(SingleMethodActivity.this);
                                        managerCompat.notify(999,builder.build());
                                        Toast.makeText(SingleMethodActivity.this, new StringBuilder().append(getString(R.string.help_center)), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

                                    }
                                    else{
                                        //choose method
                                        reff = FirebaseDatabase.getInstance().getReference("Members");
                                        reff.keepSynced(true);

                                        SelectMethod member = new SelectMethod(name,email,nid,phone,mName,"","",mPeriod,"Pending",sector,cell,dob,token,gender);
                                        reff.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(member).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {


                                                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

                                                    NotificationChannel channel=new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
                                                    NotificationManager manager=getSystemService(NotificationManager.class);
                                                    manager.createNotificationChannel(channel);
                                                }
                                                NotificationCompat.Builder builder=new NotificationCompat.Builder(SingleMethodActivity.this,"n")
                                                        .setContentText("family planning")
                                                        .setSmallIcon(R.drawable.ic_notifications)
                                                        .setAutoCancel(true)
                                                        .setContentText("Thank your for choosing  go to nearest healthy center for confirming");
                                                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(SingleMethodActivity.this);
                                                managerCompat.notify(999,builder.build());
                                                Toast.makeText(SingleMethodActivity.this, new StringBuilder().append("Thank you for choosing"), Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));



                                            } else {

                                                Toast.makeText(SingleMethodActivity.this, new StringBuilder().append("Whoops,Error while To choose method "), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(gender.equals("Female")){
                    reff = FirebaseDatabase.getInstance().getReference().child("Members");
                    reff.keepSynced(true);
                    reff.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

                                    NotificationChannel channel=new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
                                    NotificationManager manager=getSystemService(NotificationManager.class);
                                    manager.createNotificationChannel(channel);
                                }
                                NotificationCompat.Builder builder=new NotificationCompat.Builder(SingleMethodActivity.this,"n")
                                        .setContentText("family planning")
                                        .setSmallIcon(R.drawable.ic_notifications)
                                        .setAutoCancel(true)
                                        .setContentText(getString(R.string.help_center));
                                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(SingleMethodActivity.this);
                                managerCompat.notify(999,builder.build());
                                Toast.makeText(SingleMethodActivity.this, new StringBuilder().append(getString(R.string.help_center)), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

                            } else {
                                reff = FirebaseDatabase.getInstance().getReference("Members");
                                reff.keepSynced(true);

                                SelectMethod member = new SelectMethod(name,email,nid,phone,mName,"","",mPeriod,"Pending",sector,cell,dob,token,gender);
                                reff.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(member).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {


                                        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

                                            NotificationChannel channel=new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
                                            NotificationManager manager=getSystemService(NotificationManager.class);
                                            manager.createNotificationChannel(channel);
                                        }
                                        NotificationCompat.Builder builder=new NotificationCompat.Builder(SingleMethodActivity.this,"n")
                                                .setContentText("family planning")
                                                .setSmallIcon(R.drawable.ic_notifications)
                                                .setAutoCancel(true)
                                                .setContentText("Thank your for choosing  go to nearest healthy center for confirming");
                                        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(SingleMethodActivity.this);
                                        managerCompat.notify(999,builder.build());
                                        Toast.makeText(SingleMethodActivity.this, new StringBuilder().append("Thank you for choosing"), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));



                                    } else {

                                        Toast.makeText(SingleMethodActivity.this, new StringBuilder().append("Whoops,Error while To choose method "), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
                else{
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

                        NotificationChannel channel=new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager manager=getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(channel);
                    }
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(SingleMethodActivity.this,"n")
                            .setContentText("family planning")
                            .setSmallIcon(R.drawable.ic_notifications)
                            .setAutoCancel(true)
                            .setContentText(getString(R.string.menonly));
                    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(SingleMethodActivity.this);
                    managerCompat.notify(999,builder.build());
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

                }

            });
        } else {
            //
            btnContinue.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), activity_login.class)));
        }
    }
}