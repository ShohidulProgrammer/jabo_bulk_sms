package com.ideaxen.hr.ideasms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ideaxen.hr.ideasms.R;
import com.ideaxen.hr.ideasms.adapter.viewHolder.HistoryViewHolder;
import com.ideaxen.hr.ideasms.models.SmsModel;
<<<<<<< HEAD
import com.ideaxen.hr.ideasms.utility.Constants;
=======
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
import com.ideaxen.hr.ideasms.view.MessagesRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
    private Context context;
    private List<SmsModel> smsModels;

    public HistoryRecyclerViewAdapter(Context context, List<SmsModel> smsModels) {
        this.context = context;
        this.smsModels = smsModels;
    }

    private Object getItem(int position) {
        return smsModels.get(position);
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD
        View view = LayoutInflater.from(context).inflate(R.layout.history_list_item, parent, false);
=======
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        final SmsModel smsModel = (SmsModel) getItem(position);

        // check sms sending status
        if (smsModel.getSend() == 1) {
//            holder.iconView.setImageIcon(null);
            holder.iconView.setImageResource(R.drawable.ic_check_green_24dp);
        }

        // set mobileNo and username
        holder.titleView.setText(smsModel.getMobileNo());
        holder.subTitleView.setText(smsModel.getUserName());

        // set date
        try {
            // parse date format
            String date = smsModel.getDate();
            String pattern = "EEE, dd-MMM-yyyy";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date parseDate = sdf.parse(date);

            // set date in recycler view
            if (parseDate != null) {
                holder.dateView.setText(sdf.format(parseDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // go messages list by on click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessagesList(smsModel.getMobileNo());
            }
        });
    }

    @Override
    public int getItemCount() {
        return smsModels.size();
    }

    // show all messages from an individual mobile number
    private void showMessagesList(String mobile) {
        Intent intent = new Intent(context, MessagesRecyclerView.class);
<<<<<<< HEAD
        intent.putExtra(Constants.SelectedMobileNo, mobile);
=======
        intent.putExtra("MOBILE", mobile);
>>>>>>> 43a1569ae40a16d8461f19640147cf675ad62485
        context.startActivity(intent);
    }
}
