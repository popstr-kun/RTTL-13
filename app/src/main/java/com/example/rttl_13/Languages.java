package com.example.rttl_13;

import java.util.Locale;

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
        inputLocale   = language;
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

        temp  = inputLanguage;
        temp2 = inputLocale;
        inputLanguage = outputLanguage;
        inputLocale   = speechLanguage;
        outputLanguage = temp;
        speechLanguage = temp2;
    }
}
