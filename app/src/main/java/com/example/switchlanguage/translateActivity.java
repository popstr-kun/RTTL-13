package com.example.switchlanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
    private EditText ed_book;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw;
    private String[][] language_name = new String[][]{{"英文", "繁體中文", "簡體中文", "德文", "法文"," 義大利","日文"},{"US","TW","","","","",""}};


    @Override
    public void onDestroy() {
        super.onDestroy();
        dbrw.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        ed_book = findViewById(R.id.ed_book);
        listView = findViewById(R.id.listView);

        findViews();
        setAdapter();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = language_name[1][position];
                setToast(translateActivity.this, msg);
            }
        });
    }

        public void setToast(Context context, String text) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }

        private void setAdapter() {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, language_name[0]);
            listView.setAdapter(adapter);
        }

        private void findViews() {
            listView = (ListView) findViewById(R.id.listView);
        }

}