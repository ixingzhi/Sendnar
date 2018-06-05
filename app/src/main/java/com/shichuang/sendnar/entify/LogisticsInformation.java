package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public class LogisticsInformation {

   private String message;
   private String nu;
   private String com;
   private String status;
   private List<LogisticsInformationModel> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LogisticsInformationModel> getData() {
        return data;
    }

    public void setData(List<LogisticsInformationModel> data) {
        this.data = data;
    }

    public static class LogisticsInformationModel{
       private String time;
       private String context;

       public String getTime() {
           return time;
       }

       public void setTime(String time) {
           this.time = time;
       }

       public String getContext() {
           return context;
       }

       public void setContext(String context) {
           this.context = context;
       }
   }

}
