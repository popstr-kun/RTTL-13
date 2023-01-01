package com.example.rttl_13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

     Button btn_rt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_rt = findViewById(R.id.button_to_return);
        btn_rt.setOnClickListener((View v)-> {
            Intent intent = new Intent(this, ADActivity.class);
            startActivity(intent);

        });
    }
}