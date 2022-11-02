package com.example.familyplanning;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Pushbots Library
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.getstartedButton);
        btn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Fp_methods.class)));
    }
}