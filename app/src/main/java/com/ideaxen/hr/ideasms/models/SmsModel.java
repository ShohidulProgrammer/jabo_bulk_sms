package com.ideaxen.hr.ideasms.models;

public class SmsModel {

    private int id, send;
    private String mobileNo, userName, message, date;


    public SmsModel(int id, String mobile, String userName, String message, String date, int send) {
        this.setId(id);
        this.setMobileNo(mobile);
        this.setUserName(userName);
        this.setMessage(message);
        this.setDate(date);
        this.setSend(send);

        this.id = id;
        this.mobileNo = mobile;
        this.userName = userName;
        this.message = message;
        this.date = date;
        this.send = send;
    }

    public SmsModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }

//    public String toString() {
//        return "id: " + getId() + "\n" +
//                "mobileNo: " + getMobileNo() + "\n" +
//                "userName: " + getUserName() + "\n" +
//                "message: " + getMessage() + "\n" +
//                "date: " + getDate() + "\n" +
//                "send : " + isSend();
//    }

}
