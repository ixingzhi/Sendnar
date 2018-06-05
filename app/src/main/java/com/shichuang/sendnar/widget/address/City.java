package com.shichuang.sendnar.widget.address;

import java.util.List;

/**
 * Created by xiedongdong on 2017/10/23.
 */
public class City implements CityInterface {

    public String name;
    public String code;
    public List<City1> citys;

    @Override
    public String getCityName() {
        return name;
    }

    @Override
    public String getCityCode() {
        return code;
    }

    public static class City1 implements CityInterface {
        public String name;
        public String code;
        public String provincecode;
        public List<Area> areas;

        @Override
        public String getCityName() {
            return name;
        }

        @Override
        public String getCityCode() {
            return code;
        }

        public static class Area implements CityInterface {
            public String citycode;
            public String name;
            public String code;

            @Override
            public String getCityName() {
                return name;
            }

            @Override
            public String getCityCode() {
                return code;
            }
        }

    }
}
