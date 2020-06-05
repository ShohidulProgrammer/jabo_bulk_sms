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
<<<<<<< HEAD
import com.ideaxen.hr.ideasms.dbHelper.DbOperations;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.clockUtilities.DataParser;
=======
import com.ideaxen.hr.ideasms.dbOperation.DbOperations;
import com.ideaxen.hr.ideasms.dbOperation.DbProvider;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.DataParser;
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485

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
<<<<<<< HEAD
        Objects.requireNonNull(getSupportActionBar()).setTitle("Message History");

        messageRecyclerView = findViewById(R.id.messagesRecyclerListViewId);
        mobile = getIntent().getStringExtra(Constants.SelectedMobileNo);
        loadMessagesInRecyclerView();
=======
        Objects.requireNonNull(getSupportActionBar()).setTitle("Messages History");

        messageRecyclerView = findViewById(R.id.messagesRecyclerListViewId);
        mobile = getIntent().getStringExtra("MOBILE");
        loadMessagesInRecyclerView();

>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
<<<<<<< HEAD
=======
        String msg = "";
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
        switch (item.getItemId()) {
            case R.id.deleteButtonId:
                ConfirmDelete();
                break;
            case R.id.refreshButtonId:
<<<<<<< HEAD
                loadMessagesInRecyclerView();
                break;
        }
=======
                msg = "Refresh Button Pressed";
                loadMessagesInRecyclerView();
                break;
        }
        System.out.println("Message: " + msg);
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
        return super.onOptionsItemSelected(item);
    }

    private void loadMessagesInRecyclerView() {
        dbOperations = new DbOperations(this);
        dataParser = new DataParser();
        smsModels = new ArrayList<>();
<<<<<<< HEAD
        smsModels.clear();

        // read messages table data
=======

        // read messages table data
//        String mobile = getIntent().getStringExtra("MOBILE");
        smsModels.clear();
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
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
<<<<<<< HEAD
        alertDialogBuilder.setMessage("Delete all messages?");
=======
        alertDialogBuilder.setMessage("Are you sure, You wanted to Delete all messages");
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485

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
<<<<<<< HEAD
        dbOperations.deleteMobileMassages(Constants.HISTORY_TABLE, mobile);
        Toast.makeText(this, "Message histories Deleted Successfully!", Toast.LENGTH_LONG).show();
=======
        dbOperations.deleteMobileMassages(DbProvider.HISTORY_TABLE, mobile);
        Toast.makeText(this, "Messages has been Successfully Deleted!", Toast.LENGTH_LONG).show();
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
        loadMessagesInRecyclerView();
    }

}
