package com.ideaxen.hr.ideasms.models;

public class SmsModel {

    private int id, send;
    private String strId;


    private String mobileNo;
    private String userName;
    private String message;
    private String date;


    public SmsModel(int id, String mobile, String userName, String message, String date, int send) {
        this.id = id;
        this.mobileNo = mobile;
        this.userName = userName;
        this.message = message;
        this.date = date;
        this.send = send;
    }


    public SmsModel(String strId, String mobileNo, String userName, String message, String date, int send) {
        this.strId = strId;
        this.mobileNo = mobileNo;
        this.userName = userName;
        this.message = message;
        this.date = date;
        this.send = send;
    }

    public SmsModel(String mobileNo, String userName, String message) {
        this.mobileNo = mobileNo;
        this.userName = userName;
        this.message = message;
    }

    public SmsModel(String strId, String mobileNo, String userName, String message, int send) {
        this.strId = strId;
        this.mobileNo = mobileNo;
        this.userName = userName;
        this.message = message;
        this.send = send;
    }

    public SmsModel() {
    }

    public int getId() {
        return id;
    }

    public String getStrId() {
        return strId;
    }

    public int getSend() {
        return send;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}
