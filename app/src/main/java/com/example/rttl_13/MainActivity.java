package com.example.rttl_13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author SHEN_KUN
 */
public class MainActivity extends AppCompatActivity {

    /*語言列表---------------------------------------------------------------------------------*/
    private String[] countryName = new String[]{
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
    private Locale[] languageis = new Locale[]{
            Locale.TRADITIONAL_CHINESE,
            Locale.SIMPLIFIED_CHINESE,
            Locale.FRANCE,
            Locale.GERMANY,
            Locale.ITALY,
            Locale.JAPAN,
            Locale.KOREA,
            Locale.UK,
            Locale.US,
            Locale.CANADA,
            Locale.CANADA_FRENCH
    };
    /*---------------------------------------------------------------------------------------*/

    private static int keyInput = 0,keyOutput =8;

    private TextToSpeech textToSpeech;
    private String translateTextGlobal;

    Languages language = new Languages(languageis[keyInput],languageis[keyOutput]);

    private List<Msg>     msgList = new ArrayList<>();
    private RecyclerView  msgRecyclerView;
    private EditText      inputText;

    private MsgAdapter    msgAdapter;


    TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyB1t4w5AJ3A2fOOacSWbYjj7peFyIXoYyg").build();
    com.google.cloud.translate.Translate translate = options.getService();

    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        super.onStop();
        // 不管是否正在朗读TTS都被打断
        textToSpeech.stop();
        // 关闭，释放资源
        textToSpeech.shutdown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setTitle("即時翻譯系統");

        ADActivity adActivity = new ADActivity(getApplicationContext());
        AdView adView = findViewById(R.id.adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adActivity.loadRewardedInterstitialAd(getApplicationContext());


        ImageView imageSpeak = findViewById(R.id.image_speak);
        Button btnInput      = findViewById(R.id.btn_input);
        Button btnOutput     = findViewById(R.id.btn_output);
        ImageView imageSwap  = findViewById(R.id.image_swap);
        msgRecyclerView      = findViewById(R.id.msg_recycler_view);
        inputText            = findViewById(R.id.input_text);
        Button btnMsgSend    = findViewById(R.id.send);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        msgAdapter           = new MsgAdapter(msgList = getData());

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(msgAdapter);

        internetCheck();

        /*跳轉頁面(輸入按鈕)--------------------------------------------------*/
        btnInput.setOnClickListener((View v)->{
            int switchKey = 0;

            Intent intent = new Intent(this, TranslatLanguageForm.class);
            intent.putExtra("switchKey", switchKey); //可放所有基本類別
            startActivity(intent);
        });
        /*-----------------------------------------------------------------*/

        /*跳轉頁面(輸出按鈕)--------------------------------------------------*/
        btnOutput.setOnClickListener((View v)->{
            int switchKey = 1;
            Intent intent = new Intent(this, TranslatLanguageForm.class);
            intent.putExtra("switchKey", switchKey); //可放所有基本類別
            startActivity(intent);
        });
        /*-----------------------------------------------------------------*/

        /*接收回傳值---------------------------------------------------------*/
        Bundle bundleKey = this.getIntent().getExtras();
        if (bundleKey != null) {
            int languageKeyintput = bundleKey.getInt("languageKey");
            int switchKeyintput = bundleKey.getInt("switchKey");
            /*回傳值--------------------------------------------------------------------
            switchKeyintput：表示是輸入按鈕的參數(0) or 輸出按鈕的參數(1)
            languageKeyintput：表示語言對應的編號
            EX: 輸入按鈕按下且選擇德文   ->    switchKeyintput==0 、 languageKeyintput==3
                輸出按鈕按下且選擇日文   ->    switchKeyintput==1 、 languageKeyintput==6
            -------------------------------------------------------------------------*/
            if(switchKeyintput == 0){
                keyInput = languageKeyintput;
                language.setInputLanguage(languageis[keyInput]);
            }else if(switchKeyintput == 1){
                keyOutput = languageKeyintput;
                language.setOutputLanguage(languageis[keyOutput]);
            }

            System.out.println("switchKeyintput(0為輸入，1為輸出)： " + switchKeyintput);
            System.out.println("languageKeyintput：" + languageKeyintput + ", 語言："+ countryName[languageKeyintput] + ", 國家：" + countryName[languageKeyintput]);
        }
        /*-----------------------------------------------------------------*/


        //Text to Speech
        textToSpeech = new TextToSpeech(MainActivity.this, i -> {
            if(i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(language.getSpeechLanguage());
            }
            else {
                Log.e("error","Text to Speech 初始化失敗");
                Toast.makeText(getApplicationContext(),"Text to Speech 初始化失敗",Toast.LENGTH_SHORT).show();
            }
        });

        btnInput.setText(String.format("輸入\n%s",countryName[keyInput]));
        btnOutput.setText(String.format("輸出\n%s",countryName[keyOutput]));


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

        btnMsgSend.setOnClickListener(v ->  {
            String content = inputText.getText().toString();

            //Text to Speech
            textToSpeech = new TextToSpeech(MainActivity.this, i -> {
                if(i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(language.getSpeechLanguage());
                }
                else {
                    Log.e("error","Text to Speech 初始化失敗");
                    Toast.makeText(getApplicationContext(),"Text to Speech 初始化失敗",Toast.LENGTH_SHORT).show();
                }
            });

            if(!"".equals(content)) {
                msgList.add(new Msg(content,Msg.TYPE_SEND));

                msgAdapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
                //清空输入框中的内容
                inputText.setText("");
                runTranslation(translate,content,language.getOutputLanguage());
            }
        });

        imageSwap.setOnClickListener(v -> {
            language.ioLanguageSwap();
            //Text to Speech
            textToSpeech = new TextToSpeech(MainActivity.this, i -> {
                if(i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(language.getSpeechLanguage());
                }
                else {
                    Log.e("error","Text to Speech 初始化失敗");
                    Toast.makeText(getApplicationContext(),"Text to Speech 初始化失敗",Toast.LENGTH_SHORT).show();
                }
            });
            int i;
            i=keyInput;
            keyInput = keyOutput;
            keyOutput = i;
            btnInput.setText(String.format("輸入\n%s",countryName[keyInput]));
            btnOutput.setText(String.format("輸出\n%s",countryName[keyOutput]));

        });

        msgRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(oldBottom > bottom){
                    msgRecyclerView.post(()->{
                        if(msgAdapter.getItemCount() > 0 ){
                            msgRecyclerView.scrollToPosition(msgAdapter.getItemCount() - 1);
                        }
                    });
                }
            }
        });


        msgAdapter.setOnItemClickListener(new MsgAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int msg) {

                textToSpeech = new TextToSpeech(MainActivity.this, i -> {
                    if(i != TextToSpeech.ERROR) {
                        textToSpeech.setLanguage(language.getSpeechLanguage());
                        Toast.makeText(getApplicationContext(), "重新朗讀", Toast.LENGTH_SHORT).show();
                        textToSpeech.speak(msgList.get(msg).getString(),TextToSpeech.QUEUE_FLUSH,null);
                        System.out.println(msgList.get(msg).getString());
                    }
                    else {
                        Log.e("error","Text to Speech 初始化失敗");
                        Toast.makeText(getApplicationContext(),"Text to Speech 初始化失敗",Toast.LENGTH_SHORT).show();
                    }
                });


            }
            @Override
            public void onItemLongClick(int msg) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                msgList.add(new Msg(result.get(0),Msg.TYPE_SEND));
                msgAdapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
                runTranslation(translate,result.get(0),language.getOutputLanguage());
            }
        }
    }

    private final Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                msgList.add(new Msg(String.format("%s",translateTextGlobal),Msg.TYPE_RECEIVED));
                msgAdapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
            }
            return false;
        }
    });

    //必須使用
    private void runTranslation(com.google.cloud.translate.Translate translate, String text, String targetLanguage){
        new Thread(() -> {
            Translation translation = translate.translate(text, com.google.cloud.translate.Translate.TranslateOption.targetLanguage(targetLanguage));
            String translatedText = translation.getTranslatedText();
            System.out.println(translatedText);
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
        }).start();
    }

    private List<Msg> getData(){
        List<Msg> list = new ArrayList<>();
        list.add(new Msg("哈囉您好~歡迎使用即時翻譯系統\n\n本系統提供各國語音&語言翻譯",Msg.TYPE_RECEIVED));
        return list;
    }

    private void internetCheck(){
        // 判斷有無連接網路
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info == null || !info.isConnected() )
        {
            Toast.makeText(getApplicationContext(),"無網路連接，請連接後重試", Toast.LENGTH_SHORT).show();
            return;
        }
    }


}

