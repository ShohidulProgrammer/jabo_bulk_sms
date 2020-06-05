package com.ideaxen.hr.ideasms.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ideaxen.hr.ideasms.R;
import com.ideaxen.hr.ideasms.adapter.viewHolder.HistoryViewHolder;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.view.MessagesRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private static final String TAG = "HistoryRecyclerViewAdap";

    Context context;
    List<SmsModel> smsModels;

    public HistoryRecyclerViewAdapter(Context context, List<SmsModel> smsModels) {
        this.context = context;
        this.smsModels = smsModels;
    }

    public Object getItem(int position) {
        return smsModels.get(position);
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        final SmsModel smsModel = (SmsModel) getItem(position);
        System.out.println("sms Model mobile no: " + smsModels.get(position).getMobileNo());

//        holder.titleView.setText(smsModels.get(position).getMobileNo());
        if (smsModel.getSend() == 1) {

            holder.iconView.setImageIcon(null);
        }
        holder.titleView.setText(smsModel.getMobileNo());
        holder.subTitleView.setText(smsModel.getUserName());

        try {
            String date = smsModel.getDate();
            System.out.println("My date: "+date);
            String pattern = "EEE, dd-MMM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date parseDate  = sdf.parse(date);
            System.out.println("Parse Date: "+parseDate);
            String finalDate = sdf.format(parseDate);
            holder.dateView.setText(finalDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // on click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = smsModel.getMobileNo();
                showMessagesList(mobile);
            }
        });

    }

    @Override
    public int getItemCount() {
        return smsModels.size();
    }

    // show messages history list for a number
    private void showMessagesList(String mobile) {
        Intent intent = new Intent(context, MessagesRecyclerView.class);
        intent.putExtra("MOBILE", mobile);
        context.startActivity(intent);
    }
}
