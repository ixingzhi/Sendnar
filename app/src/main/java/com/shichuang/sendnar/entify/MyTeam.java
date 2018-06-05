package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public class MyTeam {
    private String userPic;
    private String ice;  // 冻结
    @SerializedName("Put_forward")
    private String putForward;  // 可提现
    @SerializedName("Consumer_Profit")
    private String consumerProfit;  // 获得消费者收益
    @SerializedName("Partner_Profit")
    private String partnerProfit;  // 获得合伙人收益
    private String useridentity;  // 身份
    @SerializedName("hhrlist")
    private List<MemberInfo> partnerRankingList;
    @SerializedName("xfzlist")
    private List<MemberInfo> consumerRankingList;

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public String getPutForward() {
        return putForward;
    }

    public void setPutForward(String putForward) {
        this.putForward = putForward;
    }

    public String getConsumerProfit() {
        return consumerProfit;
    }

    public void setConsumerProfit(String consumerProfit) {
        this.consumerProfit = consumerProfit;
    }

    public String getPartnerProfit() {
        return partnerProfit;
    }

    public void setPartnerProfit(String partnerProfit) {
        this.partnerProfit = partnerProfit;
    }

    public String getUseridentity() {
        return useridentity;
    }

    public void setUseridentity(String useridentity) {
        this.useridentity = useridentity;
    }

    public List<MemberInfo> getPartnerRankingList() {
        return partnerRankingList;
    }

    public void setPartnerRankingList(List<MemberInfo> partnerRankingList) {
        this.partnerRankingList = partnerRankingList;
    }

    public List<MemberInfo> getConsumerRankingList() {
        return consumerRankingList;
    }

    public void setConsumerRankingList(List<MemberInfo> consumerRankingList) {
        this.consumerRankingList = consumerRankingList;
    }

    public static class MemberInfo{
        @SerializedName("head_portrait")
        private String headPortrait;

        public String getHeadPortrait() {
            return headPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait;
        }
    }
}
