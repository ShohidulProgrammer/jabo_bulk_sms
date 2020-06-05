package com.ideaxen.hr.ideasms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ideaxen.hr.ideasms.R;
import com.ideaxen.hr.ideasms.adapter.viewHolder.MessagesViewHolder;
import com.ideaxen.hr.ideasms.models.SmsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesViewHolder> {

    Context context;
    List<SmsModel> smsModels;

    private static final String T = "MessagesRecyclerViewAdapter";

    public MessagesRecyclerViewAdapter(Context context, List<SmsModel> smsModels) {
        this.context = context;
        this.smsModels = smsModels;
    }

    public Object getItem(int position) {
        return smsModels.get(position);
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_details,parent,false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        SmsModel smsModel = (SmsModel) getItem(position);
        System.out.println("sms Model mobile no: " + smsModels.get(position).getMobileNo());

        if (smsModel.getSend() == 1){
            holder.iconView.setImageIcon(null);
        }
        holder.messageAsTitleView.setText(smsModel.getMessage());
        holder.dateView.setText(smsModel.getDate());

        // set date with formatter
        try {
            String date = smsModel.getDate();
            String pattern = "EEE, dd-MMM-yyyy 'at' hh:mm a";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date parseDate  = sdf.parse(date);
            String finalDate = sdf.format(parseDate);

            // set date time in recycler list view
            holder.dateView.setText(finalDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return smsModels.size();
    }
}
