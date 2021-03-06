package com.ideaxen.hr.ideasms.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ideaxen.hr.ideasms.R;
import com.ideaxen.hr.ideasms.adapter.MessagesRecyclerViewAdapter;
import com.ideaxen.hr.ideasms.dbHelper.DbOperations;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.clockUtilities.DataParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessagesRecyclerView extends AppCompatActivity {

    private RecyclerView messageRecyclerView;
    Toolbar toolbar;
    List<SmsModel> smsModels;
    DbOperations dbOperations;
    DataParser dataParser;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_recycler_view);

        toolbar = findViewById(R.id.messages_tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Message History");

        messageRecyclerView = findViewById(R.id.messagesRecyclerListViewId);
        mobile = getIntent().getStringExtra(Constants.SelectedMobileNo);
        loadMessagesInRecyclerView();
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
                loadMessagesInRecyclerView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMessagesInRecyclerView() {
        dbOperations = new DbOperations(this);
        dataParser = new DataParser();
        smsModels = new ArrayList<>();
        smsModels.clear();

        // read messages table data
        Cursor cursor = dbOperations.fetchMobileMessages(mobile);
        smsModels = dataParser.parseData(cursor);

        messageRecyclerView.setHasFixedSize(true);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(MessagesRecyclerView.this));
        MessagesRecyclerViewAdapter messagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter(MessagesRecyclerView.this, smsModels);
        messageRecyclerView.setAdapter(messagesRecyclerViewAdapter);


    }

    // alert dialog
    public void ConfirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Delete all messages?");

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
                        Toast.makeText(MessagesRecyclerView.this, "Canceled", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // delete history table
    private void deleteHistory() {
        dbOperations = new DbOperations(this);
        dbOperations.deleteMobileMassages(Constants.HISTORY_TABLE, mobile);
        Toast.makeText(this, "Message histories Deleted Successfully!", Toast.LENGTH_LONG).show();
        loadMessagesInRecyclerView();
    }

}
