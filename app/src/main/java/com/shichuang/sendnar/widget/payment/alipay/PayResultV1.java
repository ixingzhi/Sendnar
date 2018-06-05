package com.shichuang.sendnar.widget.payment.alipay;

import android.text.TextUtils;

/**
 * Created by Administrator on 2018/1/12.
 */

public class PayResultV1 {
    private String resultStatus;
    private String result;
    private String memo;

    public PayResultV1(String rawResult) {
        if(!TextUtils.isEmpty(rawResult)) {
            String[] resultParams = rawResult.split(";");
            String[] var6 = resultParams;
            int var5 = resultParams.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                String resultParam = var6[var4];
                if(resultParam.startsWith("resultStatus")) {
                    this.resultStatus = this.gatValue(resultParam, "resultStatus");
                }

                if(resultParam.startsWith("result")) {
                    this.result = this.gatValue(resultParam, "result");
                }

                if(resultParam.startsWith("memo")) {
                    this.memo = this.gatValue(resultParam, "memo");
                }
            }

        }
    }

    public String toString() {
        return "resultStatus={" + this.resultStatus + "};memo={" + this.memo + "};result={" + this.result + "}";
    }

    private String gatValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(), content.lastIndexOf("}"));
    }

    public String getResultStatus() {
        return this.resultStatus;
    }

    public String getMemo() {
        return this.memo;
    }

    public String getResult() {
        return this.result;
    }
}
