package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/5/21.
 */

public class HonoraryContribution {
    private UserInfo charitable;
    @SerializedName("piclist")
    private List<Badge> picList;

    public UserInfo getCharitable() {
        return charitable;
    }

    public void setCharitable(UserInfo charitable) {
        this.charitable = charitable;
    }

    public List<Badge> getPicList() {
        return picList;
    }

    public void setPicList(List<Badge> picList) {
        this.picList = picList;
    }

    public static class UserInfo {
        private int id;
        @SerializedName("head_portrait")
        private String headPortrait;
        private String charitable;  // 慈善度
        @SerializedName("level_name")
        private String levelName;  // 级别名

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHeadPortrait() {
            return headPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait;
        }

        public String getCharitable() {
            return charitable;
        }

        public void setCharitable(String charitable) {
            this.charitable = charitable;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }
    }

    public static class Badge {
        private String pic;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
