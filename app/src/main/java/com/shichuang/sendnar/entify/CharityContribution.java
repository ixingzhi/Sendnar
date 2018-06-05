package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/5/11.
 */

public class CharityContribution {

    private List<CharityContributionModel> rows;
    private int recordCount;

    public List<CharityContributionModel> getRows() {
        return rows;
    }

    public void setRows(List<CharityContributionModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }
    public static class CharityContributionModel{
        private int id;
        private String nickname;
        @SerializedName("head_portrait")
        private String headPortrait;
        @SerializedName("SUM")
        private String sum;
        private String shouyi;
        @SerializedName("shouyi_yue")
        private String shouyiMonth;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadPortrait() {
            return headPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getShouyi() {
            return shouyi;
        }

        public void setShouyi(String shouyi) {
            this.shouyi = shouyi;
        }

        public String getShouyiMonth() {
            return shouyiMonth;
        }

        public void setShouyiMonth(String shouyiMonth) {
            this.shouyiMonth = shouyiMonth;
        }
    }
}
