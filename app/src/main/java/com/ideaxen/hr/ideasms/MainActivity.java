package com.ideaxen.hr.ideasms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.ideaxen.hr.ideasms.dbHelper.DbOperations;
import com.ideaxen.hr.ideasms.dbHelper.DbProvider;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.clockUtilities.DataParser;
import com.ideaxen.hr.ideasms.utility.permissionUtilities.PermissionHandler;
import com.ideaxen.hr.ideasms.utility.simCardUtilities.SimCardChooserRadioButtonAlertDialog;
import com.ideaxen.hr.ideasms.utility.simCardUtilities.SimCardSubscriptionChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ideaxen.hr.ideasms.utility.permissionUtilities.PermissionHandler.checkPermissions;

public class MainActivity extends AppCompatActivity {


    public static String PACKAGE_NAME;
    HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    Toolbar toolbar;
    EditText appNameEditText;
    TextView appNameTextView;

    List<SmsModel> smsModels;
    DbOperations dbOperations;
    DataParser dataParser;

    PermissionHandler permissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        setToolBar();

        // check SMS send permission
        checkPermissions(MainActivity.this);

        // CREATE Firebase notification channel
        createFcmChannel();
        // check the Firebase Push notification subscription status
        checkFcmSubscription();
//
        // check Selected Sim Card Subscription status to Send SMS
        checkSimSubscription();
    }
    //    --- End onCreate method---

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


    // ------- UI Functions Starts from here ------
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

    // delete history table
    private void deleteHistory() {
        dbOperations = new DbOperations(this);
        dbOperations.deleteAll(DbProvider.HISTORY_TABLE);
        Toast.makeText(MainActivity.this, "SMS Histories are Deleted Successfully!", Toast.LENGTH_LONG).show();
        loadDataInListView();
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
                loadDataInListView();
                Toast.makeText(MainActivity.this, "Refreshed Successfully!", Toast.LENGTH_LONG).show();
                break;
            case R.id.appNameButtonId:
                requestForAppName();
                break;
            case R.id.simCardRadioGroupButtonId:
                SimCardChooserRadioButtonAlertDialog simCardChooserRadioDialog = new SimCardChooserRadioButtonAlertDialog(MainActivity.this);
                simCardChooserRadioDialog.registerSimCard();
//                registerSimCard();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
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
        String appNameInStorage = getAppName();
        if (appNameInStorage.equals(Constants.APP_NAME_NOT_REGISTERED)) {
            requestForAppName();
        }
    }

    // check SIM card subscription status
    public void checkSimSubscription() {
        SimCardSubscriptionChecker simCardSubscriptionChecker = new SimCardSubscriptionChecker();
        simCardSubscriptionChecker.checkSimSubscription(MainActivity.this);
    }


//    private void registerSimCard() {
//        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
//
//        final String[] simOperators = getSimOperatorName().toArray(new String[0]);
//
//        if (simOperators[0].equals(Constants.SIM_READ_PERMISSION_CANCELED)) {
//            alertDialog.setTitle("You must be Permit your SIM card Read Permission");
//
//            // Yes button
//            alertDialog.setPositiveButton("OK",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            checkPermissions(MainActivity.this);
//                        }
//                    });
//        } else {
//            alertDialog.setTitle("For Sending SMS Choose SIM card");
//
//            // select initial sim card as sim -1
//            final int checkedItem = -1;
//            alertDialog.setSingleChoiceItems(simOperators, checkedItem, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(MainActivity.this, simOperators[which] + " Sim Selected", Toast.LENGTH_LONG).show();
//
//                    if (which >= 0 && which < simOperators.length) {
//                        // save selected sim serial number in Shared Preference Storage
//                        String iccId = getSimCardIccId(which);
//                        System.out.println("My ICC ID: " + iccId);
//                        saveSelectedSimCardInSPS(iccId);
//
//                        iccId = getSimCardIccIdInSPStore();
//                        Toast.makeText(MainActivity.this, "Saved SIM Serial number: " + iccId, Toast.LENGTH_LONG).show();
//                        System.out.println("Saved SIM Serial number: " + iccId);
//
//                        dialog.cancel();
//                    }
//                }
//            });
//        }
//        AlertDialog alert = alertDialog.create();
//        alert.setCancelable(false);
//        alert.show();
//    }
//
//    // save selected SIM card serial number as icc id in sharedPreferences variable MY_SELECTED_SIM_CARD_ICC_ID.
//    public void saveSelectedSimCardInSPS(String selectedSimCardIccId) {
//        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
//        SharedPreferences.Editor spEdit = sharedPreferences.edit();
//
//        // Save SIM card serial number as icc id
//        spEdit.putString(Constants.MY_SELECTED_SIM_CARD_ICC_ID, selectedSimCardIccId);
//        spEdit.apply();
//    }
//
//
//    // get subscribed SIM card info from shared preference storage
//    public String getSimCardIccIdInSPStore() {
//        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
//
//        // check the sharedPreferences variable MY_SELECTED_SIM_CARD_ICC_ID
//        // has stored any value or not
//        if (sharedPreferences.contains(Constants.MY_SELECTED_SIM_CARD_ICC_ID)) {
//            System.out.println("MY_SELECTED_SIM_CARD_ICC_ID: "
//                    + sharedPreferences.getString(Constants.MY_SELECTED_SIM_CARD_ICC_ID, Constants.SIM_CARD_NOT_SELECTED_YET));
//
//            return sharedPreferences.getString(Constants.MY_SELECTED_SIM_CARD_ICC_ID, Constants.SIM_CARD_NOT_SELECTED_YET);
//        } else {
//            return Constants.SIM_CARD_NOT_SELECTED_YET;
//        }
//    }


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
        if (!appNameInStorage.equals(Constants.APP_NAME_NOT_REGISTERED)) {
            appNameTextView.setText(appNameInStorage.toUpperCase());
        } else {
            appNameTextView.setText(Constants.APP_NAME_NOT_REGISTERED);
        }

        // Yes button
        alertDialogBuilder.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // unregister previous app name
                        String previousRegisteredAppName = getAppName();
                        unSubscribeToTopicForFCM(previousRegisteredAppName);

                        // Register the AppName as FCM TOPIC for individual group push notification
                        String appName = appNameEditText.getText().toString();
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

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sharedPreferences.edit();
        spEdit.putString(Constants.MY_APP_NAME, appName);
        spEdit.apply();
        String appNameInStorage = getAppName();
        subscribeToTopicForFCM(appNameInStorage);
    }

    // get subscribe app name from shared preference storage
    public String getAppName() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);

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
            Toast.makeText(MainActivity.this, "Successfully subscribe to " + appName.toUpperCase(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Unfortunately, you failed to subscribe", Toast.LENGTH_LONG).show();
        }
    }

    // Unregister FCM Notification topic as App Name
    private void unSubscribeToTopicForFCM(String appName) {
        if ((!appName.equals(Constants.APP_NAME_NOT_REGISTERED)) && (!appName.isEmpty())) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(appName);
            Toast.makeText(MainActivity.this, "Successfully Unsubscribe the " + appName, Toast.LENGTH_LONG).show();
        }
    }
    // ------- Subscription Functions Ends ------

    // ------- Check Permission Functions Starts from here ------
    // show sms permission result status

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionHandler == null) {
            permissionHandler = new PermissionHandler();
        }
        permissionHandler.onRequestPermissionResult(MainActivity.this,requestCode, permissions,grantResults);
    }


//    private ArrayList<String> getSimOperatorName() {
//        ArrayList<String> operators = new ArrayList<>();
//        SimCardReader simCardReader = new SimCardReader(MainActivity.this);
//        List<SimInfo> simInfoList = simCardReader.getSIMInfo();
//
//        for (int i = 0; i < simInfoList.size(); i++) {
//            operators.add(simInfoList.get(i).getOperatorName());
//        }
//        return operators;
//    }
//
//
//    // get sim card unique id
//    private String getSimCardIccId(int selectedSimCardSlot) {
//        SimCardReader simCardReader = new SimCardReader(MainActivity.this);
//        List<SimInfo> simInfoList = simCardReader.getSIMInfo();
//        return simInfoList.get(selectedSimCardSlot).getIcc_id();
//    }
    // ------- Check Permissions Functions Ends ------
}
