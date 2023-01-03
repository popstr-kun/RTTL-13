package com.example.rttl_13;

import java.util.Locale;

public class Msg {
    public static final int TYPE_RECEIVED = 0; //表示这是一条收到的消息
    public static final int TYPE_SEND = 1;     //表示这是一条发出的消息
    private String content;
    private int type;
    private Locale language;
    private String name = "即時翻譯輸出";

    Msg(String content, int type ){
        this.content = content;
        this.type = type;
    }

    Msg(String content, int type ,Locale language){
        this.content = content;
        this.type = type;
        this.language = language;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
    public String getString(){return content;}
    public Locale getLocale(){return language;}

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
