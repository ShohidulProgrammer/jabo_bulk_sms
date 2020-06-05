package com.ideaxen.hr.ideasms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ideaxen.hr.ideasms.adapter.HistoryRecyclerViewAdapter;
import com.ideaxen.hr.ideasms.dbOperation.DbOperations;
import com.ideaxen.hr.ideasms.dbOperation.DbProvider;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.notification.FCMBroadcastReceiver;
import com.ideaxen.hr.ideasms.notification.PushNotificationService;
import com.ideaxen.hr.ideasms.smsHelper.SmsBroadcastReceiver;
import com.ideaxen.hr.ideasms.smsHelper.SmsSender;
import com.ideaxen.hr.ideasms.utility.DataParser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    Toolbar toolbar;
    AlertDialog.Builder builder;

    List<SmsModel> smsModels;
    DbOperations dbOperations;
    DataParser dataParser;

    private static final String TAG = "FCM";
//    public static final String ACTION_NOTIFICATION_RECEIVED = "com.google.android.c2dm.intent.RECEIVE";
    PushNotificationService notificationService;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
//    private FCMBroadcastReceiver fcmBroadcastReceiver = new FCMBroadcastReceiver();
    private SmsBroadcastReceiver smsBroadcastReceiver = new SmsBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolBar();
        // check SMS send permission
        checkSmsSendingPermission();
        // register broadcast receiver
        registerBroadcastReceiver();

        // CREATE Firebase notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("pushNotificationChannel", "ideaxenSMSNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
//            Log.d(TAG, "on channel created then notification Received From: " );
        }

        //  List view UI load sms history from history table data in local DB
        loadDataInListView();
    }

    // set toolbar TITLE and ACTION BUTTON
    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.historyToolBarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SMS History");
    }

    // load sms history List data
    public void loadDataInListView() {
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
        HistoryRecyclerViewAdapter historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(MainActivity.this,smsModels);
        historyRecyclerView.setAdapter(historyRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_buttons,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String msg = "";
        switch (item.getItemId()){
            case R.id.deleteButtonId:
                openAlertDialog();

            break;
            case R.id.refreshButtonId:
                msg = "Refresh Button Pressed";
                loadDataInListView();
            break;

        }
        System.out.println("Message: "+msg);
        return super.onOptionsItemSelected(item);
    }

    // alert dialog
    public void openAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to Delete all history");

        // Yes button
        alertDialogBuilder.setPositiveButton("yes",
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
                        Toast.makeText(MainActivity.this,"Canceled",Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // delete history table
    private void deleteHistory() {
        dbOperations = new DbOperations(this);
        dbOperations.deleteAll(DbProvider.HISTORY_TABLE);
        Toast.makeText(MainActivity.this,"History table has been Deleted Successfully!",Toast.LENGTH_LONG).show();
        loadDataInListView();
    }

    // check sms sending request permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission granted
                    System.out.println("sms sending permission was granted");
                    Toast.makeText(getApplicationContext(), "SMS sending permission granted.",
                            Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("sms sending permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(),
                            "SMS sending permission disabled, \nplease check your permission settings.", Toast.LENGTH_LONG).show();
                    return;
                }
            } // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    // check SMS send permission
    public void checkSmsSendingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(
                    Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.SEND_SMS)) {
                    System.out.println("Request for permission");


                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {

                    // No explanation needed, we can request the permission.
                    requestPermissions(
                            new String[]{Manifest.permission.SEND_SMS},
                            0);

                    // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
    }

    private void registerBroadcastReceiver() {
        // access notification
//        notificationService = new PushNotificationService();

        // set intent broadcast receiver
//        IntentFilter notificationIntentFilter = new IntentFilter(ACTION_NOTIFICATION_RECEIVED);
//        IntentFilter sentIntentFilter = new IntentFilter(SmsSender.ACTION_SMS_SENT);
//        IntentFilter deliveredIntentFilter = new IntentFilter(SmsSender.ACTION_SMS_DELIVERED);

        // register broadcast receiver
//        this.registerReceiver(fcmBroadcastReceiver, notificationIntentFilter);
//        this.registerReceiver(smsBroadcastReceiver, sentIntentFilter);
//        this.registerReceiver(smsBroadcastReceiver, deliveredIntentFilter);
    }
    // unregister the broadcast receiver
    @Override
    protected void onDestroy() {
//        this.unregisterReceiver(fcmBroadcastReceiver);
//        this.unregisterReceiver(smsBroadcastReceiver);
        super.onDestroy();
    }
}
