package com.example.familyplanning;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Pushbots Library
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.getstartedButton);
        Log.d("MyLog","DEVICE TOKEN  is:"+ FirebaseInstanceId.getInstance().getToken());

        btn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Fp_methods.class)));
    }
}