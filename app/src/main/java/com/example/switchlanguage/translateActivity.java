package com.example.switchlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class translateActivity extends AppCompatActivity {
    private ListView listView;
    private String[] language_name = new String[]{"英文", "繁體中文", "簡體中文", "德文", "法文","義大利","日文"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        listView = findViewById(R.id.listView);

        findViews();
        setAdapter();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*-------------------------------------------------------------------------------------
                String msg = String.format("%d",position);
                setToast(translateActivity.this, msg);
                -------------------------------------------------------------------------------------*/
                Intent intent = new Intent();
                intent.setClass(translateActivity.this, MainActivity.class);
                intent.putExtra("name", position); //可放所有基本類別
                startActivity(intent);
                /*-------------------------------------------------------------------------------------*/
            }
        });
    }

    public void setToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    private void setAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, language_name);
        listView.setAdapter(adapter);
    }

    private void findViews() {
        listView = (ListView) findViewById(R.id.listView);
    }

}