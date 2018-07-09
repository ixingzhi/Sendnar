package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiedongdong on 2018/6/20.
 */

public class KeyWord {
    @SerializedName("hot_words")
    private String word;

    public KeyWord(String word){
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
