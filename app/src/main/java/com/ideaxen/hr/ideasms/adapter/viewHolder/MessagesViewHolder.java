package com.ideaxen.hr.ideasms.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ideaxen.hr.ideasms.R;

public class MessagesViewHolder extends RecyclerView.ViewHolder {
    public ImageView iconView;
    public TextView messageAsTitleView;
    public TextView dateView;

    public MessagesViewHolder(@NonNull View itemView) {
        super(itemView);

        iconView = itemView.findViewById(R.id.icon_sms_resend);
        messageAsTitleView = itemView.findViewById(R.id.messageTextView);
        dateView = itemView.findViewById(R.id.timeTextView);
    }
}
