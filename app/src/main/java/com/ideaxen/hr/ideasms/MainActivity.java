package com.ideaxen.hr.ideasms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.ideaxen.hr.ideasms.adapter.HistoryRecyclerViewAdapter;
import com.ideaxen.hr.ideasms.dbHelper.DbOperations;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.clockUtilities.DataParser;
import com.ideaxen.hr.ideasms.utility.permissionUtilities.PermissionHandler;
import com.ideaxen.hr.ideasms.utility.sharedPreferenceManager.AppNameHandler;
import com.ideaxen.hr.ideasms.utility.simCardUtilities.SimCardChooserRadioDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ideaxen.hr.ideasms.utility.permissionUtilities.PermissionHandler.checkPermissions;

public class MainActivity extends AppCompatActivity {
    public static String MY_PACKAGE_NAME;
    HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    Toolbar toolbar;
    EditText appNameEditText;
    TextView appNameTextView;

    List<SmsModel> smsModels;
    DbOperations dbOperations;
    DataParser dataParser;

    PermissionHandler permissionHandler;
    AppNameHandler appNameHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        RecyclerView historyRecyclerView;
        dbOperations = new DbOperations(this);
        dataParser = new DataParser();
        smsModels = new ArrayList<>();

        // read history table data
        smsModels.clear(); // clear not necessary
        Cursor cursor = dbOperations.getAllHistoryData(Constants.HISTORY_TABLE);
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
        if (dbOperations == null) {
            dbOperations = new DbOperations(this);
        }

        dbOperations.deleteAll(Constants.HISTORY_TABLE);
        Toast.makeText(MainActivity.this, "SMS histories deleted successfully", Toast.LENGTH_LONG).show();
        loadSmsHistoryDataInListView();
    }

    // alert dialog for confirmation to Delete history table
    public void ConfirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Delete all SMS histories?");

        // Yes button
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete the history table
                        deleteHistory();
//                        dialog.cancel();
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
}
