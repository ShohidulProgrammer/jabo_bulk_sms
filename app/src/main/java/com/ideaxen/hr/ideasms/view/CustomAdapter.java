package com.ideaxen.hr.ideasms.view;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ideaxen.hr.ideasms.R;

import java.util.Date;
 public class CustomAdapter extends BaseAdapter {

    String[] mobiles;
    String[] user;
    Date date;
    Context context;
    Image icon;
    private  LayoutInflater inflater;

    public CustomAdapter(Context context, String[] mobile, String[] user, Date date){

        this.context = context;
        this.mobiles = mobile;
        this.user = user;
        this.date = date;
    }

    @Override
    public int getCount() {
        return mobiles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null){
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent,false);
        }

        ImageView iconView = convertView.findViewById(R.id.icon_sms_not_send);
        TextView titleView = convertView.findViewById(R.id.mobileNoTextView);
        TextView subTitleView = convertView.findViewById(R.id.userNameTextView);
        TextView dateView = convertView.findViewById(R.id.dateTimeTextView);

        titleView.setText(mobiles[i]);

        return convertView;
    }
}
