package com.example.familyplanning;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleMethodActivity extends AppCompatActivity {
    private TextView textTitle;
    private TextView textUsageDesc,textP,textC,textAdvDesc,textDisadvDesc;
    private CheckBox checkBox;
    private ImageView imageView;
    private Button btnContinue;
    private ImageView backImg;
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
    private String mCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_activity);
        backImg=findViewById(R.id.backImg);
        //on back pressed
        backImg.setOnClickListener(view -> onBackPressed());

        //initializing data from previous activity
         mName=getIntent().getStringExtra("methodname");
        mCategory=getIntent().getStringExtra("category");
        mPeriod=getIntent().getStringExtra("period");
        String mAdvantages=getIntent().getStringExtra("advantages");
        String mDisadv=getIntent().getStringExtra("disadvantages");
        String mUsage=getIntent().getStringExtra("usage");
        String mImgurl=getIntent().getStringExtra("imgurl");
        //get find text from activity
        textTitle=findViewById(R.id.textTitle);
        textP=findViewById(R.id.textP);
        textUsageDesc=findViewById(R.id.textUsageDesc);
        textC=findViewById(R.id.textC);
        textAdvDesc=findViewById(R.id.textAdvDesc);
        textDisadvDesc=findViewById(R.id.textDisadvDesc);
        checkBox=findViewById(R.id.checkBox);
        imageView=findViewById(R.id.singleImg);
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

        if(user==null){
            //
            btnContinue.setOnClickListener(view -> {
                startActivity(new Intent(getApplicationContext(), activity_login.class));

            });
        }
        else{
            reff= FirebaseDatabase.getInstance().getReference("UserAccounts");
            reff.keepSynced(true);
            currentUserId=user.getUid();
            reff.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users userProfile=snapshot.getValue(Users.class);
                    if(userProfile!=null){
                         name=userProfile.getFuln();
                         email=userProfile.getEmail();
                         String dob=userProfile.getDob();
                         nid= userProfile.getIdno();
                         phone=userProfile.getIphone();
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
                if(gender.equals("Male") && mPeriod.equals("Permanent")){

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

                                SelectMethod member = new SelectMethod(name,email,nid,phone,mName,"","",mPeriod,"Pending",token);
                                reff.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(member).addOnCompleteListener(task1 -> {
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

                                SelectMethod member = new SelectMethod(name,email,nid,phone,mName,"","",mPeriod,"Pending",token);
                                reff.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(member).addOnCompleteListener(task1 -> {
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
                }

            });
        }
    }
}