package com.ideaxen.hr.ideasms.adapter.viewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ideaxen.hr.ideasms.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    public   ImageView iconView;
    public   TextView titleView;
    public   TextView subTitleView;
    public   TextView dateView;


    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        iconView = itemView.findViewById(R.id.icon_sms_not_send);
        titleView = itemView.findViewById(R.id.mobileNoTextView);
        subTitleView = itemView.findViewById(R.id.userNameTextView);
        dateView = itemView.findViewById(R.id.dateTimeTextView);
    }
}
