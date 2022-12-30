package com.example.rttl_13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private String repeatText;
    Languages language = new Languages(Locale.TAIWAN,Locale.JAPAN);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView speak = findViewById(R.id.speak);
        Button btnRepeat = findViewById(R.id.btn_repeat);
        Button btnSwap = findViewById(R.id.btn_swap);

        TextView inputText = findViewById(R.id.InputText);
        inputText.setText(String.format("輸入(%s):",language.getInputLanguage()));
        TextView outputText = findViewById(R.id.OutputText);
        outputText.setText(String.format("結果(%s):",language.getOutputLanguage()));

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Speech to Text
                // 判斷有無連接網路
                ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                if( info == null || !info.isConnected() )
                {
                    Toast.makeText(getApplicationContext(),"無網路連接，請連接後重試", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language.getInputLanguage());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說～");
                try{
                    startActivityForResult(intent,200);
                }catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"Intent problem", Toast.LENGTH_SHORT).show();
                }

                //Text to Speech
                textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i != TextToSpeech.ERROR) {
                            textToSpeech.setLanguage(language.getSpeechLanguage());
                        }
                    }
                });
            }
        });

        btnRepeat.setOnClickListener(v -> {
            if(repeatText != null){
                textToSpeech.speak(repeatText,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        btnSwap.setOnClickListener(v ->{
            language.ioLanguageSwap();
            inputText.setText(String.format("輸入(%s):",language.getInputLanguage()));
            outputText.setText(String.format("結果(%s):",language.getOutputLanguage()));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView inputText = findViewById(R.id.InputText);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyB1t4w5AJ3A2fOOacSWbYjj7peFyIXoYyg").build();
                Translate translate = options.getService();
                inputText.setText(String.format("輸入(%s): %s",language.getInputLanguage(), result.get(0)));
                runTranslation(translate,result.get(0),language.getOutputLanguage());
            }
        }
    }



    //必須使用
    private void runTranslation(Translate translate,String text,String targetLanguage){
        TextView outputText = findViewById(R.id.OutputText);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage(targetLanguage));
                String translatedText = translation.getTranslatedText();
                System.out.println(translatedText);
                outputText.setText(String.format("結果(%s): %s ",language.getOutputLanguage(),translatedText));
                try {
                    Thread.sleep(100);
                    if(translatedText != null){
                        repeatText = translatedText;
                        textToSpeech.speak(translatedText,TextToSpeech.QUEUE_FLUSH,null);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        textToSpeech.stop();
    }

}

class Languages{
    private String inputLanguage;
    private String outputLanguage;
    private Locale speechLanguage;

    private Locale inputLocale;

    Languages(Locale input,Locale output){
        setInputLanguage(input);
        setOutputLanguage(output);
    }

    void setInputLanguage(Locale language){
        inputLanguage = language.toString();
        inputLocale = inputLocale;
    }
    void setOutputLanguage(Locale language){
        outputLanguage = language.toString();
        speechLanguage = language;
    }
    String getInputLanguage(){
        return inputLanguage;
    }
    String getOutputLanguage(){
        return outputLanguage;
    }
    Locale getSpeechLanguage(){
        return speechLanguage;
    }

    void ioLanguageSwap(){
        String temp ;
        Locale temp2 ;

        temp = inputLanguage;
        temp2 = inputLocale;
        inputLanguage = outputLanguage;
        inputLocale = speechLanguage;
        outputLanguage = temp;
        speechLanguage = temp2;

    }
}