package com.sciento.wumu.weidianliu.activity;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.sciento.wumu.weidianliu.R;
import com.sciento.wumu.weidianliu.utils.RequestPermissonUtil;

import java.util.UUID;

/**
 * Created by wumu on 10/29/17.
 */

public class BaseActivity extends AppCompatActivity {

    //uuid
    protected static final UUID ZZR_UUID_BLE_SERVICE = UUID.fromString("0000FFF0-0000-1000-8000-00805f9b34fb");
    protected static final UUID ZZR_UUID_BLE_CHAR = UUID.fromString("0000FFF1-0000-1000-8000-00805f9b34fb");
    protected static final UUID ZZR_UUID_BLE_CHAR1 = UUID.fromString("0000FFF4-0000-1000-8000-00805f9b34fb");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        RequestPermissonUtil.mayRequestLocation(this);
        super.onCreate(savedInstanceState);

    }





}
