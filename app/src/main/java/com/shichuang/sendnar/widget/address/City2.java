package com.shichuang.sendnar.widget.address;

/**
 * Created by Administrator on 2018/4/26.
 */

public class City2 implements CityInterface{
    private String id;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCityName() {
        return name;
    }

    @Override
    public String getCityCode() {
        return id;
    }
}
