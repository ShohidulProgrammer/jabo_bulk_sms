package com.ideaxen.hr.ideasms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ideaxen.hr.ideasms.R;
import com.ideaxen.hr.ideasms.adapter.viewHolder.MessagesViewHolder;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.smsUtilities.SmsSender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesViewHolder> {

    private Context context;
    private List<SmsModel> smsModels;

    public MessagesRecyclerViewAdapter(Context context, List<SmsModel> smsModels) {
        this.context = context;
        this.smsModels = smsModels;
    }

    private Object getItem(int position) {
        return smsModels.get(position);
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_details, parent, false);
        return new MessagesViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull final MessagesViewHolder holder, int position) {
        final SmsModel smsModel = (SmsModel) getItem(position);

        if (smsModel.getSend() == 1) {
//            holder.iconView.setImageIcon(null);
            holder.iconView.setImageResource(R.drawable.ic_check_green_24dp);
            holder.iconView.setEnabled(false);
        }
        holder.messageAsTitleView.setText(smsModel.getMessage());
        holder.dateView.setText(smsModel.getDate());

        // set date with formatter
        try {
            String date = smsModel.getDate();
            String pattern = "EEE, dd-MMM-yyyy 'at' hh:mm a";
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat(pattern);
            Date parseDate = sdf.parse(date);

            // set date time in recycler list view
            if (parseDate !=null){
                String finalDate = sdf.format(parseDate);
                holder.dateView.setText(finalDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // on click listener
        holder.iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsModel.getId();
                reSendSms(smsModel);
            }
        });
    }

    // ReSend SMS which was failed to send
    private void reSendSms(SmsModel smsModel) {
        String id = "" + smsModel.getId();
        String mobile = smsModel.getMobileNo();
        String user = smsModel.getUserName();
        String message = smsModel.getMessage();
        int isSend = smsModel.getSend();

        // resend sms
        SmsSender smsSender = new SmsSender(context);
        smsSender.sendSms(id, mobile, user, message, isSend);
    }

    @Override
    public int getItemCount() {
        return smsModels.size();
    }
}
