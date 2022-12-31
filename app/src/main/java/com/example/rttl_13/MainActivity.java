package com.example.rttl_13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private String translateTextGlobal;
    Languages language = new Languages(Locale.ENGLISH,Locale.TAIWAN);

    private List<Msg>           msgList = new ArrayList<>();
    private RecyclerView        msgRecyclerView;
    private EditText            inputText;
    private Button              send;
    private LinearLayoutManager layoutManager;
    private MsgAdapter          adapter;

    TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyB1t4w5AJ3A2fOOacSWbYjj7peFyIXoYyg").build();
    Translate translate = options.getService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        ImageView imageSpeak = findViewById(R.id.image_speak);
        Button btnInput      = findViewById(R.id.btn_input);
        Button btnOutput     = findViewById(R.id.btn_output);
        ImageView imageSwap  = findViewById(R.id.image_swap);
        msgRecyclerView      = findViewById(R.id.msg_recycler_view);
        inputText            = findViewById(R.id.input_text);
        send                 = findViewById(R.id.send);
        layoutManager        = new LinearLayoutManager(this);
        adapter              = new MsgAdapter(msgList = getData());

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

        btnInput.setText(String.format("輸入\n%s",language.getInputLanguage()));
        btnOutput.setText(String.format("結果\n%s",language.getOutputLanguage()));

        // 判斷有無連接網路
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info == null || !info.isConnected() )
        {
            Toast.makeText(getApplicationContext(),"無網路連接，請連接後重試", Toast.LENGTH_SHORT).show();
            return;
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

        imageSpeak.setOnClickListener(v ->  {
                //Speech to Text
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language.getInputLanguage());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說～");
                try{
                    startActivityForResult(intent,200);
                }catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"Intent problem", Toast.LENGTH_SHORT).show();
                }

        });

        send.setOnClickListener(v ->  {
            String content = inputText.getText().toString();

            if(!content.equals("")) {
                msgList.add(new Msg(content,Msg.TYPE_SEND));
                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
                inputText.setText("");//清空输入框中的内容
                runTranslation(translate,content,language.getOutputLanguage());
            }
        });

        imageSwap.setOnClickListener(v -> {
            language.ioLanguageSwap();
            btnInput.setText(String.format("輸入\n%s",language.getInputLanguage()));
            btnOutput.setText(String.format("結果\n%s",language.getOutputLanguage()));
        });

//        btnRepeat.setOnClickListener(v -> {
//            if(repeatText != null){
//                textToSpeech.speak(repeatText,TextToSpeech.QUEUE_FLUSH,null);
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                msgList.add(new Msg(result.get(0),Msg.TYPE_SEND));
                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);

                runTranslation(translate,result.get(0),language.getOutputLanguage());
            }
        }
    }

    private final Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                msgList.add(new Msg(translateTextGlobal,Msg.TYPE_RECEIVED));
                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
            }
            return false;
        }
    });


    //必須使用
    private void runTranslation(Translate translate,String text,String targetLanguage){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage(targetLanguage));
                String translatedText = translation.getTranslatedText();
                System.out.println(translatedText);
                //outputText.setText(String.format("結果(%s): %s ",language.getOutputLanguage(),translatedText));
                try {
                    Thread.sleep(100);
                    if(translatedText != null){
                        translateTextGlobal = translatedText;
                        textToSpeech.speak(translatedText,TextToSpeech.QUEUE_FLUSH,null);
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);

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

    private List<Msg> getData(){
        List<Msg> list = new ArrayList<>();
        list.add(new Msg("哈囉~歡迎使用即時翻譯系統",Msg.TYPE_RECEIVED));
        return list;
    }

}

