package com.ideaxen.hr.ideasms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
<<<<<<< HEAD
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

=======
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
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
import com.ideaxen.hr.ideasms.adapter.HistoryRecyclerViewAdapter;
import com.ideaxen.hr.ideasms.dbHelper.DbOperations;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.clockUtilities.DataParser;
import com.ideaxen.hr.ideasms.utility.permissionUtilities.PermissionHandler;
import com.ideaxen.hr.ideasms.utility.sharedPreferenceManager.AppNameHandler;
import com.ideaxen.hr.ideasms.utility.simCardUtilities.SimCardChooserRadioDialog;
=======
import com.google.firebase.messaging.FirebaseMessaging;
import com.ideaxen.hr.ideasms.adapter.HistoryRecyclerViewAdapter;
import com.ideaxen.hr.ideasms.dbOperation.DbOperations;
import com.ideaxen.hr.ideasms.dbOperation.DbProvider;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.DataParser;
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

<<<<<<< HEAD
import static com.ideaxen.hr.ideasms.utility.permissionUtilities.PermissionHandler.checkPermissions;

public class MainActivity extends AppCompatActivity {
    public static String MY_PACKAGE_NAME;
=======
public class MainActivity extends AppCompatActivity {


    private static final String TAG = "Max_SMS";
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
    HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    Toolbar toolbar;
    EditText appNameEditText;
    TextView appNameTextView;

    List<SmsModel> smsModels;
    DbOperations dbOperations;
    DataParser dataParser;

<<<<<<< HEAD
    PermissionHandler permissionHandler;
    AppNameHandler appNameHandler;
=======
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
//    private static final int MY_PERMISSIONS_REQUEST_SEND_Group_SMS = 0;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String MY_APP_NAME = "AppName";
    private static final String NOT_REGISTERED = "You are\nNot Registered";

>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD

        // get package name for global use
        MY_PACKAGE_NAME = getApplicationContext().getPackageName();
        appNameHandler = new AppNameHandler(MainActivity.this);

        // set page TITLE and ACTION BUTTON
        setToolBar();
        // CREATE Firebase notification channel
        createFcmChannel();
//        check the Firebase Push notification subscription status
        checkFcmSubscription();
        // check permission for SMS sending and SIM card reading
       checkPermissions(MainActivity.this);

       airplaneModeOn();
    }
    //    --- End onCreate method---

    @Override
    protected void onResume() {
        super.onResume();
        // Recycler View reloading
        loadSmsHistoryDataInListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbOperations.close();
    }

    // ------- UI Functions implementation Starts from here ------
    // load sms history recycler view List data
    public void loadSmsHistoryDataInListView() {
=======
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
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
        RecyclerView historyRecyclerView;
        dbOperations = new DbOperations(this);
        dataParser = new DataParser();
        smsModels = new ArrayList<>();

        // read history table data
<<<<<<< HEAD
        smsModels.clear(); // clear not necessary
        Cursor cursor = dbOperations.getAllHistoryData(Constants.HISTORY_TABLE);
=======
        smsModels.clear();
        Cursor cursor = dbOperations.getAllHistoryData(DbProvider.HISTORY_TABLE);
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
        smsModels = dataParser.parseData(cursor);

        // custom adapter for sms history list view
        historyRecyclerView = findViewById(R.id.historyRecyclerListViewId);
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(MainActivity.this, smsModels);
        historyRecyclerView.setAdapter(historyRecyclerViewAdapter);
    }

<<<<<<< HEAD
    // delete history table
    private void deleteHistory() {
        if (dbOperations == null) {
            dbOperations = new DbOperations(this);
        }

        dbOperations.deleteAll(Constants.HISTORY_TABLE);
        Toast.makeText(MainActivity.this, "SMS histories deleted successfully", Toast.LENGTH_LONG).show();
        loadSmsHistoryDataInListView();
=======
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
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
    }

    // alert dialog for confirmation to Delete history table
    public void ConfirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
<<<<<<< HEAD
        alertDialogBuilder.setMessage("Delete all SMS histories?");
=======
        alertDialogBuilder.setMessage("Are you sure, You wanted to Delete all SMS histories");
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485

        // Yes button
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete the history table
                        deleteHistory();
<<<<<<< HEAD
//                        dialog.cancel();
=======
                        dialog.cancel();
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
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

<<<<<<< HEAD
    // set toolbar TITLE and ACTION BUTTON
    private void setToolBar() {
        toolbar = findViewById(R.id.historyToolBarId);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMS History");
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
                loadSmsHistoryDataInListView();
                Toast.makeText(MainActivity.this, "Refreshed Successfully!", Toast.LENGTH_LONG).show();
                break;
            case R.id.appNameButtonId:
                requestForAppName(true,"Save");
                break;
            case R.id.simCardRadioGroupButtonId:
                SimCardChooserRadioDialog simCardChooserRadioDialog = new SimCardChooserRadioDialog(MainActivity.this);
                simCardChooserRadioDialog.registerSimCardRadioDialog();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    // request an alert dialog app name editor for getting subscribed App Name from user input
    public void requestForAppName(boolean previouslyRegistered, String okBtnText) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.register_app_name, null);
        appNameEditText = view.findViewById(R.id.appNameEditTextId);
        appNameTextView = view.findViewById(R.id.appNameTextViewId);

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        String appNameInStorage = appNameHandler.getAppName();
        if (!appNameInStorage.equals(Constants.APP_NAME_NOT_REGISTERED)) {
            appNameTextView.setText(appNameInStorage.toUpperCase());
        } else {
            appNameTextView.setText(Constants.APP_NAME_NOT_REGISTERED);
        }

        // Yes button
        alertDialogBuilder.setPositiveButton(okBtnText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // unregister previous app name
                        String previousRegisteredAppName = appNameHandler.getAppName();
                        appNameHandler.unSubscribeToTopicForFCM(previousRegisteredAppName);

                        // Register the AppName as FCM TOPIC for individual group push notification
                        String appName = appNameEditText.getText().toString();
                        appNameHandler.saveAppName(appName);
                    }
                });

        if (previouslyRegistered) {
            // Yes button
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
        }

=======
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
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
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

<<<<<<< HEAD
    // ------- UI Functions Ends here ------

    // ------- Subscription Functions Starts from here ------
    private void createFcmChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("pushNotificationChannel", "ideaSMSNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    // check firebase notification subscription status
    public void checkFcmSubscription() {
        String appNameInStorage = appNameHandler.getAppName();
        if (appNameInStorage.equals(Constants.APP_NAME_NOT_REGISTERED)) {
            requestForAppName(false,"Continue");
        }
    }
    // ------- Subscription Functions ENDs here ------


    // ------- Check Permission Functions Starts from here ------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionHandler == null) {
            permissionHandler = new PermissionHandler();
        }
        permissionHandler.onRequestPermissionResult(MainActivity.this,requestCode, permissions,grantResults);
    }
    // ------- Check Permissions Functions Ends ------


    public void airplaneModeOn() {
        try {

            boolean isEnabled = Settings.System.getInt(getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0) == 1;
            Settings.System.putInt(getContentResolver(),Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", !isEnabled);
            sendBroadcast(intent);
            Log.d("writeMode","\n\n\n\n\n ACTION_AIRPLANE_MODE_CHANGED \n\n\n\n");
        } catch (Exception e) {
            System.out.println("\n\n\n\n\nexception:" + e.toString()+"\n\n\n\n");
            Log.d("writeMode","\n\n\n\n\nexception:" + e.toString()+"\n\n\n\n");
            Toast.makeText(this, "exception:" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

//    public void makeAirplaneModeOn(){
//        Settings.System.putInt(getContentResolver(),
//                Settings.System.AIRPLANE_MODE_ON, 1);  //  turn airplane mode on
//    }
=======
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
        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, Manifest.permission_group.SMS) != PackageManager.PERMISSION_GRANTED)) {

            // Permission not yet granted.
            // Use requestPermissions().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS,Manifest.permission_group.SMS},
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
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
}
