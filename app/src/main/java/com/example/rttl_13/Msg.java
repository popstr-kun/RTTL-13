package com.example.rttl_13;

public class Msg {
    public static final int TYPE_RECEIVED = 0; //表示这是一条收到的消息
    public static final int TYPE_SEND = 1;     //表示这是一条发出的消息
    private String content;
    private int type;

    public Msg(String content, int type){
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
