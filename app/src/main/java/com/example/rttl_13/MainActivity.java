package com.example.rttl_13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView speak = findViewById(R.id.speak);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說～");
                try{
                    startActivityForResult(intent,200);
                }catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"Intent problem", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            TextView inputText = findViewById(R.id.InputText);
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyB1t4w5AJ3A2fOOacSWbYjj7peFyIXoYyg").build();
                Translate translate = options.getService();
                String text = "Hello, world!";
                String targetLanguage = "zh_TW"; // 目標語言
                inputText.setText("輸入:"+result.get(0));
                runTranslation(translate,result.get(0),targetLanguage);
            }
        }
    }

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg){

        }
    };

    private void runTranslation(Translate translate,String text,String targetLanguage){
        TextView outputText = findViewById(R.id.OutputText);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage(targetLanguage));
                String translatedText = translation.getTranslatedText();
                System.out.println(translatedText);
                outputText.setText("結果:"+translatedText);
            }
        }).start();

    }
}