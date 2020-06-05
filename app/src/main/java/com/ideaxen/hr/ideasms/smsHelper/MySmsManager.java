//package com.ideaxen.hr.ideasms.smsHelper;
//
//import android.content.Context;
//
//import com.ideaxen.hr.ideasms.httpHelper.ServerDataManager;
//
//
//public class MySmsManager {
//    private Context context;
//
//    public MySmsManager(Context context) {
//        this.context = context;
//    }
//
//    public void makeSmsReady(String url) {
//        System.out.println("Make ready url: " + url);
//        try {
//            ServerDataManager serverDataManager = new ServerDataManager(context);
//            serverDataManager.execute(url);
//        } catch (Exception e) {
//            System.out.println("Get Data Error: " + e);
//        }
//    }
//}
