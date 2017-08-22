package com.sciento.wumu.weidianliu.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sciento.wumu.weidianliu.R;
import com.sciento.wumu.weidianliu.activity.MainActivity;

import java.util.List;

/**
 * Created by wumu on 17-8-11.
 */

public class DeviceListAdapter extends BaseAdapter {
    Context context;
    List<BluetoothDevice> devices;
    String lockName;

    public DeviceListAdapter(Context context, List<BluetoothDevice> devices, String lockname) {
        this.context = context;
        this.devices = devices;
        lockName = lockname;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_content_device, null);
        }
        TextView deivceName = (TextView) view.findViewById(R.id.tv_device_name);
        TextView tvconnected = (TextView) view.findViewById(R.id.tv_connected);
//        RadioButton radioButton =(RadioButton)view.findViewById(R.id.rb_connected);
        deivceName.setText(devices.get(position).getName());
        if (devices.get(position).getName().equals(lockName)) {
            tvconnected.setVisibility(View.VISIBLE);
//            radioButton.setChecked(true);
        }
        return view;
    }
}
