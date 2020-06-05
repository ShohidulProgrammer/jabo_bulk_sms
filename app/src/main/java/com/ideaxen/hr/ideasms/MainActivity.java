package com.ideaxen.hr.ideasms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ideaxen.hr.ideasms.adapter.HistoryRecyclerViewAdapter;
import com.ideaxen.hr.ideasms.dbOperation.DbOperations;
import com.ideaxen.hr.ideasms.dbOperation.DbProvider;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.DataParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    Toolbar toolbar;
    EditText appNameEditText;
    TextView appNameTextView;

    List<SmsModel> smsModels;
    DbOperations dbOperations;
    DataParser dataParser;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String MY_APP_NAME = "AppName";
    private static final String NOT_REGISTERED = "You are\nNot Registered";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolBar();
        // check SMS send permission
        checkForSmsPermission();
//        checkSmsSendingPermission();
        // CREATE Firebase notification channel
        createFcmChannel();
        // check the Firebase Push notification subscription status
        checkFcmSubscription();
    }

    private void createFcmChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("pushNotificationChannel", "ideaSMSNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }


    // set toolbar TITLE and ACTION BUTTON
    private void setToolBar() {
        toolbar = findViewById(R.id.historyToolBarId);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMS History");
    }

    // load sms history List data
    public void loadDataInListView() {
        RecyclerView historyRecyclerView;
        dbOperations = new DbOperations(this);
        dataParser = new DataParser();
        smsModels = new ArrayList<>();

        // read history table data
        smsModels.clear();
        Cursor cursor = dbOperations.getAllHistoryData(DbProvider.HISTORY_TABLE);
        smsModels = dataParser.parseData(cursor);

        // custom adapter for sms history list view
        historyRecyclerView = findViewById(R.id.historyRecyclerListViewId);
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(MainActivity.this, smsModels);
        historyRecyclerView.setAdapter(historyRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteButtonId:
                ConfirmDelete();
                break;
            case R.id.refreshButtonId:
                loadDataInListView();
                Toast.makeText(MainActivity.this, "Refreshed Successfully!", Toast.LENGTH_LONG).show();
                break;
            case R.id.appNameButtonId:
                requestForAppName();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    // alert dialog for confirmation to Delete history table
    public void ConfirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to Delete all SMS histories");

        // Yes button
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete the history table
                        deleteHistory();
                        dialog.cancel();
                    }
                });

        // cancel button
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Canceled!", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // check firbase notification subscription status
    public void checkFcmSubscription() {
        String appNameInStorage = getAppName();
        if (appNameInStorage.equals(NOT_REGISTERED)) {
            requestForAppName();
        }
    }

    // alert dialog for input App Name
    public void requestForAppName() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.register_app_name, null);
        appNameEditText = view.findViewById(R.id.appNameEditTextId);
        appNameTextView = view.findViewById(R.id.appNameTextViewId);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        String appNameInStorage = getAppName();
        if (!appNameInStorage.equals(NOT_REGISTERED)) {
            appNameTextView.setText(appNameInStorage.toUpperCase());
        } else {
            appNameTextView.setText(NOT_REGISTERED);
        }

        // Yes button
        alertDialogBuilder.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Register the AppName as FCM TOPIC for individual group push notification
                        String appName = appNameEditText.getText().toString();
                        String previousRegisteredAppName = getAppName();
                        unSubscribeToTopicForFCM(previousRegisteredAppName);
                        saveAppName(appName);
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        // set Continue/Yes button enabled or disabled
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        appNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edit text is empty or white space
                if (s.toString().trim().length() == 0) {
                    // Disable ok button
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Enable the button if Something into the edit text except white space.
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    // save the app name in sharedPreferences variable MY_APP_NAME.
    public void saveAppName(String appName) {
        appName = appName.replace(" ", "").toLowerCase();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sharedPreferences.edit();
        spEdit.putString(MY_APP_NAME, appName);
        spEdit.apply();
        String appNameInStorage = getAppName();
        subscribeToTopicForFCM(appNameInStorage);
    }

    // get subscribe app name from shared preference storage
    public String getAppName() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // check the sharedPreferences variable MY_APP_NAME  has stored any value or not
        if (sharedPreferences.contains(MY_APP_NAME)) {
            return sharedPreferences.getString(MY_APP_NAME, NOT_REGISTERED);
        } else {
            return NOT_REGISTERED;
        }
    }

    // register FCM Notification topic as App Name
    private void subscribeToTopicForFCM(String appName) {
        if ((!appName.equals(NOT_REGISTERED)) && (!appName.isEmpty())) {
            FirebaseMessaging.getInstance().subscribeToTopic(appName);
            Toast.makeText(MainActivity.this, "Successfully subscribe to " + appName.toUpperCase(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Unfortunately, you failed to subscribe", Toast.LENGTH_LONG).show();
        }
    }

    // Unregister FCM Notification topic as App Name
    private void unSubscribeToTopicForFCM(String appName) {
        if ((!appName.equals(NOT_REGISTERED)) && (!appName.isEmpty())) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(appName);
            Toast.makeText(MainActivity.this, "Successfully Unsubscribe the " + appName, Toast.LENGTH_LONG).show();
        }
    }


    // delete history table
    private void deleteHistory() {
        dbOperations = new DbOperations(this);
        dbOperations.deleteAll(DbProvider.HISTORY_TABLE);
        Toast.makeText(MainActivity.this, "SMS Histories are Deleted Successfully!", Toast.LENGTH_LONG).show();
        loadDataInListView();
    }

    // show sms permission result status
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // other 'case' lines to check for other
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            /*
                 permission denied, boo! Disable the
                 functionality that depends on this permission.
                */
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission granted
                Toast.makeText(getApplicationContext(), "SMS Sending Permission Successfully Granted.",
                        Toast.LENGTH_LONG).show();
            } else Toast.makeText(getApplicationContext(),
                    "SMS Sending Permission Disabled.\nPlease! Check Your Permission Settings.", Toast.LENGTH_LONG).show();
            // permissions this app might request.
        }
    }

    // check SMS send permission
    private void checkForSmsPermission() {
        // if permission not granted
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted.
            // Use requestPermissions().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
//        else {
////            // Permission already granted.
////            requestPermissions(
////                    new String[]{Manifest.permission.SEND_SMS},
////                    0);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recycler View reloading
        loadDataInListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbOperations.close();
    }
}
