package com.example.switchlanguage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*跳轉頁面--------------------------------------------------------*/
        btn = findViewById(R.id.switchButton);
        btn.setOnClickListener((View v)->{
            Intent intent = new Intent(this,translateActivity.class);
            startActivity(intent);
        });
        /*接收回傳值--------------------------------------------------------*/
        Intent intent = this.getIntent();
        int keyinput = intent.getIntExtra("name",0);
        System.out.println(keyinput);
        /*--------------------------------------------------------*/
    }

}