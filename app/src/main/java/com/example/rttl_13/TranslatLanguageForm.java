package com.example.rttl_13;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TranslatLanguageForm extends AppCompatActivity {
    private ListView listView;
    private String[] language_name = new String[]{
            "台灣",
            "支那",
            "法國",
            "德國",
            "義大利",
            "日本",
            "韓國",
            "英國",
            "美國",
            "加拿大-英文",
            "加拿大-法文"
    };

    @Override
    public void finish() {
        super.finish();
        Log.e("finish","activity finish");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        this.setTitle("語言選擇");

        listView = findViewById(R.id.listView);

        /*接收輸入or輸出按鈕參數-----------------------------------------------------------------------------*/
        Intent intent_language_switch = this.getIntent();
        int languageswitchkey = intent_language_switch.getIntExtra("switchKey",0);
        /*-----------------------------------------------------------------------------------------------*/

        findViews();
        setAdapter();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*-------------------------------------------------------------------------------------
                String msg = String.format("%d",position);
                setToast(translateActivity.this, msg);
                -------------------------------------------------------------------------------------*/
                runVibrate(50);
                Intent intent = new Intent();
                intent.setClass(TranslatLanguageForm.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("languageKey", position); //可放所有基本類別
                bundle.putInt("switchKey", languageswitchkey);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
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

    private void runVibrate(int vibratorTime){
        Vibrator myVibrator = (Vibrator) getApplication()//取得震動
                .getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(vibratorTime);
    }
}