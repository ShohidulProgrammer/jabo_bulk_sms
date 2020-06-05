package com.ideaxen.hr.ideasms.utility.notification;
import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class PushNotificationService extends FirebaseMessagingService {
    private static final String TAG = "FCM";
    public static int NOTIFICATION_ID = 1;
    String url, body;
    private Context context;

    public PushNotificationService() {
        Log.d(TAG, " Notification has been received");
    }



    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "\n\n\nRefreshed token: " + token);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "on Message Received From: " + remoteMessage.getFrom());


        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            url = remoteMessage.getData().get("url");
//            url = (url == null ? "" : url);

            // get json data from server
//            System.out.println(JsonData.getJSON(url));
//            String response = JsonData.getJSON(url);


//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body:  " + remoteMessage.getNotification().getBody());
//        }

//        body = remoteMessage.getData().get("body");
//        body = (body == null ? "" : body);
        }


    }
}
