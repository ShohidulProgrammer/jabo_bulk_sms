package com.ideaxen.hr.ideasms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ListView historyListView;
    private String[] mobiles;
    private String[] users;
    private Date date;
    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mobiles = getResources().getStringArray(R.array.mobiles);
        icon = findViewById(R.id.icon_sms_not_send);
        historyListView = findViewById(R.id.listViewId);

        date = Calendar.getInstance().getTime();

        CustomAdapter adapter = new CustomAdapter(this,mobiles,users,date);
        historyListView.setAdapter(adapter);

        // item click listener
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                String mobile = mobiles[i];
                Toast.makeText(MainActivity.this,mobile, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
