package com.ideaxen.hr.ideasms.utility.sharedPreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ideaxen.hr.ideasms.utility.Constants;

public class AppNameHandler {
    private Context context;

    public AppNameHandler(Context context) {
        this.context = context;
    }

    // save the app name in sharedPreferences variable MY_APP_NAME.
    public void saveAppName(String appName) {
        appName = appName.replace(" ", "").toLowerCase();

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sharedPreferences.edit();
        spEdit.putString(Constants.MY_APP_NAME, appName);
        spEdit.apply();
        String appNameInStorage = getAppName();
        subscribeToTopicForFCM(appNameInStorage);
    }


    // get subscribe app name from shared preference storage
    public String getAppName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);

        // check the sharedPreferences variable MY_APP_NAME  has stored any value or not
        if (sharedPreferences.contains(Constants.MY_APP_NAME)) {
            return sharedPreferences.getString(Constants.MY_APP_NAME, Constants.APP_NAME_NOT_REGISTERED);
        } else {
            return Constants.APP_NAME_NOT_REGISTERED;
        }
    }


    // register FCM Notification topic as App Name
    private void subscribeToTopicForFCM(String appName) {
        if ((!appName.equals(Constants.APP_NAME_NOT_REGISTERED)) && (!appName.isEmpty())) {
            FirebaseMessaging.getInstance().subscribeToTopic(appName);
            Toast.makeText(context, "Successfully subscribe to " + appName.toUpperCase(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Unfortunately, you failed to subscribe", Toast.LENGTH_LONG).show();
        }
    }


    // Unregister FCM Notification topic as App Name
    public void unSubscribeToTopicForFCM(String appName) {
        if ((!appName.equals(Constants.APP_NAME_NOT_REGISTERED)) && (!appName.isEmpty())) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(appName);
            Toast.makeText(context, "Successfully Unsubscribe the " + appName, Toast.LENGTH_LONG).show();
        }
    }

}
