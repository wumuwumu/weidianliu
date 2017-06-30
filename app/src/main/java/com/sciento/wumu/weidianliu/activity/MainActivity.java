package com.sciento.wumu.weidianliu.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.sciento.wumu.weidianliu.R;
import com.sciento.wumu.weidianliu.utils.RequestPermissonUtil;
import com.sciento.wumu.weidianliu.view.LongClickButton;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnClickListener, LongClickButton.LongClickRepeatListener,
        View.OnTouchListener {


    @BindView(R.id.tv_alltime_remain)
    TextView tvAlltimeRemain;
    @BindView(R.id.tv_resttime_remain)
    TextView tvResttimeRemain;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_arm_minus)
    LongClickButton btnArmMinus;
    @BindView(R.id.cirpro_arm)
    CircleProgressBar cirproArm;
    @BindView(R.id.btn_arm_plus)
    LongClickButton btnArmPlus;
    @BindView(R.id.llo_arm_left)
    LinearLayout lloArmLeft;
    @BindView(R.id.btn_shoulder_minus)
    LongClickButton btnShoulderMinus;
    @BindView(R.id.cirpro_shoudler)
    CircleProgressBar cirproShoudler;
    @BindView(R.id.btn_shoulder_plus)
    LongClickButton btnShoulderPlus;
    @BindView(R.id.llo_shoudler_left)
    LinearLayout lloShoudlerLeft;
    @BindView(R.id.btn_chest_minus)
    LongClickButton btnChestMinus;
    @BindView(R.id.cirpro_chest)
    CircleProgressBar cirproChest;
    @BindView(R.id.btn_chest_plus)
    LongClickButton btnChestPlus;
    @BindView(R.id.btn_abdomen_minus)
    LongClickButton btnAbdomenMinus;
    @BindView(R.id.cirpro_abdomen)
    CircleProgressBar cirproAbdomen;
    @BindView(R.id.btn_abdomen_plus)
    LongClickButton btnAbdomenPlus;
    @BindView(R.id.btn_back_minus)
    LongClickButton btnBackMinus;
    @BindView(R.id.cirpro_back)
    CircleProgressBar cirproBack;
    @BindView(R.id.btn_back_plus)
    LongClickButton btnBackPlus;
    @BindView(R.id.all_left)
    LinearLayout allLeft;
    @BindView(R.id.btn_waist_minus)
    LongClickButton btnWaistMinus;
    @BindView(R.id.cirpro_waist)
    CircleProgressBar cirproWaist;
    @BindView(R.id.btn_waist_plus)
    LongClickButton btnWaistPlus;
    @BindView(R.id.btn_forethigh_minus)
    LongClickButton btnForethighMinus;
    @BindView(R.id.cirpro_forethigh)
    CircleProgressBar cirproForethigh;
    @BindView(R.id.btn_forethigh_plus)
    LongClickButton btnForethighPlus;
    @BindView(R.id.btn_backthigh_minus)
    LongClickButton btnBackthighMinus;
    @BindView(R.id.cirpro_backthigh)
    CircleProgressBar cirproBackthigh;
    @BindView(R.id.btn_backthigh_plus)
    LongClickButton btnBackthighPlus;
    @BindView(R.id.btn_hips_minus)
    LongClickButton btnHipsMinus;
    @BindView(R.id.cirpro_hips)
    CircleProgressBar cirproHips;
    @BindView(R.id.btn_hips_plus)
    LongClickButton btnHipsPlus;
    @BindView(R.id.btn_calfs_minus)
    LongClickButton btnCalfsMinus;
    @BindView(R.id.cirpro_calfs)
    CircleProgressBar cirproCalfs;
    @BindView(R.id.btn_calfs_plus)
    LongClickButton btnCalfsPlus;
    @BindView(R.id.all_right)
    LinearLayout allRight;
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ll_ll)
    LinearLayout llLl;
    @BindView(R.id.gif_arm)
    GifImageView gifArm;
    @BindView(R.id.gif_shoudler)
    GifImageView gifShoudler;
    @BindView(R.id.gif_chest)
    GifImageView gifChest;
    @BindView(R.id.gif_abdomen)
    GifImageView gifAbdomen;
    @BindView(R.id.gif_back)
    GifImageView gifBack;
    @BindView(R.id.gif_waist)
    GifImageView gifWaist;
    @BindView(R.id.gif_hgigh_fore)
    GifImageView gifHgighFore;
    @BindView(R.id.gif_hgigh_back)
    GifImageView gifHgighBack;
    @BindView(R.id.gif_hips)
    GifImageView gifHips;
    @BindView(R.id.gif_calf)
    GifImageView gifCalf;
    @BindView(R.id.imgbtn_all_minus)
    LongClickButton imgbtnAllMinus;
    @BindView(R.id.imgbtn_all_show)
    Button imgbtnAllShow;
    @BindView(R.id.imgbtn_all_add)
    LongClickButton imgbtnAllAdd;
    @BindView(R.id.llo_all_controller)
    LinearLayout lloAllController;
    @BindView(R.id.llo_time_text)
    LinearLayout lloTimeText;
    @BindView(R.id.btn_time_all_minus)
    LongClickButton btnTimeAllMinus;
    @BindView(R.id.btn_controler_show)
    Button btnControlerShow;
    @BindView(R.id.btn_time_all_add)
    LongClickButton btnTimeAllAdd;
    @BindView(R.id.btn_time_train_minus)
    LongClickButton btnTimeTrainMinus;
    @BindView(R.id.btn_train_show)
    Button btnTrainShow;
    @BindView(R.id.btn_time_train_add)
    LongClickButton btnTimeTrainAdd;
    @BindView(R.id.btn_time_rest_minus)
    LongClickButton btnTimeRestMinus;
    @BindView(R.id.btn_rest_show)
    Button btnRestShow;
    @BindView(R.id.btn_time_rest_add)
    LongClickButton btnTimeRestAdd;
    @BindView(R.id.llo_time)
    LinearLayout lloTime;
    @BindView(R.id.tv_aerobic)
    TextView tvAerobic;
    @BindView(R.id.switch_aerobic)
    SwitchCompat switchAerobic;
    @BindView(R.id.tv_anoxic)
    TextView tvAnoxic;
    @BindView(R.id.switch_anoxic)
    SwitchCompat switchAnoxic;
    @BindView(R.id.tv_massage)
    TextView tvMassage;
    @BindView(R.id.switch_massage)
    SwitchCompat switchMassage;
    @BindView(R.id.llo_mode)
    LinearLayout lloMode;
    @BindView(R.id.tv_blu_text)
    TextView tvBluText;
    @BindView(R.id.btn_blu)
    Button btnBlu;
    @BindView(R.id.btn_start)
    Button btnStart;



    //蓝牙连接
    public static BleManager bleManager;
    private BluetoothAdapter mBluetoothAdapter;
    private static final String TAG = "MainActivity";
    private final String lockName = "BlackBat";
    private boolean isConnected = false;

    private final int MSG_GET_DATE = 0;

    //uuid
    private static final UUID ZZR_UUID_BLE_SERVICE = UUID.fromString("0000FFF0-0000-1000-8000-00805f9b34fb");
    private static final UUID ZZR_UUID_BLE_CHAR = UUID.fromString("0000FFF2-0000-1000-8000-00805f9b34fb");
    private static final UUID ZZR_UUID_BLE_CHAR1 = UUID.fromString("0000FFF3-0000-1000-8000-00805f9b34fb");

    //显示值
    int[] mEveryRank = {50, 50, 50, 50, 50, 50, 50, 50, 50, 50};
    int mAllRank = 50;
    //timer
    int[] mTimerShow = {15, 10, 5};

    //mode
    int modeSelect = 1;

    //all switch
    boolean start_en = false;

    int mallRankCopy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RequestPermissonUtil.mayRequestLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBLEFeature();
        ButterKnife.bind(this);
        init();
        initEvent();
    }


    //是否支持蓝牙
    private void checkBLEFeature() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        bleManager = new BleManager(this);
        bleManager.enableBluetooth();
    }

    private void initEvent() {
        tvAerobic.setOnClickListener(this);
        tvAnoxic.setOnClickListener(this);
        tvMassage.setOnClickListener(this);


        //arm
        btnArmMinus.setLongClickRepeatListener(this, 50);
        btnArmMinus.setOnTouchListener(this);
        btnArmPlus.setLongClickRepeatListener(this, 50);
        btnArmPlus.setOnTouchListener(this);
        //shoudler
        btnShoulderMinus.setLongClickRepeatListener(this, 50);
        btnShoulderMinus.setOnTouchListener(this);
        btnShoulderPlus.setLongClickRepeatListener(this, 50);
        btnShoulderPlus.setOnTouchListener(this);
        //chest
        btnChestMinus.setLongClickRepeatListener(this, 50);
        btnChestMinus.setOnTouchListener(this);
        btnChestPlus.setLongClickRepeatListener(this, 50);
        btnChestPlus.setOnTouchListener(this);
        //abdomen
        btnAbdomenMinus.setLongClickRepeatListener(this, 50);
        btnAbdomenMinus.setOnTouchListener(this);
        btnAbdomenPlus.setLongClickRepeatListener(this, 50);
        btnAbdomenPlus.setOnTouchListener(this);
        //back
        btnBackMinus.setLongClickRepeatListener(this, 50);
        btnBackMinus.setOnTouchListener(this);
        btnBackPlus.setLongClickRepeatListener(this, 50);
        btnBackPlus.setOnTouchListener(this);


        //waist
        btnWaistMinus.setLongClickRepeatListener(this, 50);
        btnWaistMinus.setOnTouchListener(this);
        btnWaistPlus.setLongClickRepeatListener(this, 50);
        btnWaistPlus.setOnTouchListener(this);
        //thigh fore
        btnForethighMinus.setLongClickRepeatListener(this, 50);
        btnForethighMinus.setOnTouchListener(this);
        btnForethighPlus.setLongClickRepeatListener(this, 50);
        btnForethighPlus.setOnTouchListener(this);
        //thigh back
        btnBackthighMinus.setLongClickRepeatListener(this, 50);
        btnBackthighMinus.setOnTouchListener(this);
        btnBackthighPlus.setLongClickRepeatListener(this, 50);
        btnBackthighPlus.setOnTouchListener(this);
        //hips
        btnHipsMinus.setLongClickRepeatListener(this, 50);
        btnHipsMinus.setOnTouchListener(this);
        btnHipsPlus.setLongClickRepeatListener(this, 50);
        btnHipsPlus.setOnTouchListener(this);
        //calf
        btnCalfsMinus.setLongClickRepeatListener(this, 50);
        btnCalfsMinus.setOnTouchListener(this);
        btnCalfsPlus.setLongClickRepeatListener(this, 50);
        btnCalfsPlus.setOnTouchListener(this);
        //all
        imgbtnAllMinus.setLongClickRepeatListener(this, 50);
        imgbtnAllMinus.setOnTouchListener(this);
        imgbtnAllAdd.setLongClickRepeatListener(this, 50);
        imgbtnAllAdd.setOnTouchListener(this);


        //time all
        btnTimeAllMinus.setLongClickRepeatListener(this, 50);
        btnTimeAllMinus.setOnTouchListener(this);
        btnTimeAllAdd.setLongClickRepeatListener(this, 50);
        btnTimeAllAdd.setOnTouchListener(this);
        //time train
        btnTimeTrainMinus.setLongClickRepeatListener(this, 50);
        btnTimeTrainMinus.setOnTouchListener(this);
        btnTimeTrainAdd.setLongClickRepeatListener(this, 50);
        btnTimeTrainAdd.setOnTouchListener(this);
        //time rest
        btnTimeRestMinus.setLongClickRepeatListener(this, 50);
        btnTimeRestMinus.setOnTouchListener(this);
        btnTimeRestAdd.setLongClickRepeatListener(this, 50);
        btnTimeRestAdd.setOnTouchListener(this);


//        progressBar.setMax(70);
//        Drawable indicator = getResources().getDrawable(
//                //R.drawable.progress_indicator);
//                R.drawable.lightmini);
//        Rect bounds = new Rect(0, 0, indicator.getIntrinsicWidth() + 5,
//                indicator.getIntrinsicHeight());
//        indicator.setBounds(bounds);
//
//        progressBar.setProgressIndicator(indicator);
//        progressBar.setProgress(0);
//        progressBar.setVisibility(View.VISIBLE);
    }

    private void init() {
        cirproArm.setProgress(mEveryRank[0]);
        cirproShoudler.setProgress(mEveryRank[1]);
        cirproChest.setProgress(mEveryRank[2]);
        cirproAbdomen.setProgress(mEveryRank[3]);
        cirproBack.setProgress(mEveryRank[4]);

        //右
        cirproWaist.setProgress(mEveryRank[5]);
        cirproForethigh.setProgress(mEveryRank[6]);
        cirproBackthigh.setProgress(mEveryRank[7]);
        cirproHips.setProgress(mEveryRank[8]);
        cirproCalfs.setProgress(mEveryRank[9]);

        //all
        imgbtnAllShow.setText(mAllRank + "");

        //timer
        btnControlerShow.setText(mTimerShow[0] + "");
        btnTrainShow.setText(mTimerShow[1] + "");
        btnRestShow.setText(mTimerShow[2] + "");

        //mode
        switchAerobic.setChecked(true);

        //gif
        hideGif();

    }

    @Override
    protected void onDestroy() {

        bleManager.closeBluetoothGatt();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bleManager.enableBluetooth();
    }


    private void connectNameDevice(final String deviceName) {

        bleManager.scanNameAndConnect(deviceName, 20000, false, new BleGattCallback() {
            @Override
            public void onNotFoundDevice() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        tvBluText.setText(R.string.string_blu_dis);
                        Toast.makeText(MainActivity.this, "没有发现设备", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onFoundDevice(BluetoothDevice device) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "发现设备", Toast.LENGTH_LONG).show();
                    }
                });
            }


            @Override
            public void onConnectSuccess(final BluetoothGatt gatt, int status) {

                gatt.discoverServices();
            }


            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                //查找使用的uuid
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initConnect(lockName, gatt);
                        isConnected = true;
                        btnBlu.setBackgroundResource(R.drawable.btn_blu_press);
                        tvBluText.setText(R.string.string_blu_en);

                    }
                });

            }

            @Override
            public void onConnectFailure(BleException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnected = false;
                        btnBlu.setBackgroundResource(R.drawable.btn_blu_nor);
                        tvBluText.setText(R.string.string_blu_dis);
                        bleManager.closeBluetoothGatt();
                    }
                });
                bleManager.handleException(exception);
            }


        });
    }

    //初始化
    private void initConnect(String deviceName, BluetoothGatt gatt) {
        bleManager.getBluetoothState();
        if (gatt != null) {
            for (final BluetoothGattService service : gatt.getServices()) {
                for (final BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    ////识别uuid
                    if (characteristic.getUuid().equals(ZZR_UUID_BLE_CHAR1)) {
                        //开启接收notify
                        startNotify(service.getUuid().toString(), characteristic.getUuid().toString());

                    }
                }
            }
        }
    }

    //读蓝牙函数
    private void startNotify(String serviceUUID, final String characterUUID) {
        Log.i(TAG, "startNotify");
        boolean suc = bleManager.notify(
                serviceUUID,
                characterUUID,
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                        Log.d(TAG, "notify success： " + '\n' + String.valueOf(HexUtil.encodeHex(characteristic.getValue())));
                        byte[] m_byte = characteristic.getValue();
                        if (m_byte.length == 13) {
                            Message message = Message.obtain();
                            message.what = MSG_GET_DATE;
                            Bundle dateBundle = new Bundle();
                            dateBundle.putByteArray("datearray", m_byte);
                            message.setData(dateBundle);
                            HandlerBlu.sendMessage(message);
                        }


                    }

                    @Override
                    public void onFailure(BleException exception) {

                        bleManager.handleException(exception);
                    }
                });

    }

    //写蓝牙函数
    private void startWrite(String serviceUUID, final String characterUUID, String writeData) {

        Log.i(TAG, "startWrite");
        boolean suc = bleManager.writeDevice(
                serviceUUID,
                characterUUID,
                HexUtil.hexStringToBytes(writeData),
                new BleCharacterCallback() {
                    @Override
                    public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                        Log.d(TAG, "write success: " + '\n' + String.valueOf(HexUtil.encodeHex(characteristic.getValue())));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        bleManager.handleException(exception);
                    }
                });

        if (suc) {
            bleManager.stopListenCharacterCallback(ZZR_UUID_BLE_CHAR1.toString());
            startNotify(serviceUUID.toString(), ZZR_UUID_BLE_CHAR1.toString());
        }
    }

    private Handler HandlerBlu = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_DATE:
                    Bundle myBundle = msg.getData();
                    byte[] mbyte = myBundle.getByteArray("datearray");
                    tvAlltimeRemain.setText("总剩余时间：" + mbyte[3]+"min");

                    if (mbyte[4] > 0) {
                        progressBar.setProgress((int) (70 - (int)(mbyte[4] / (float) mTimerShow[1] * 70.0f)));
                        tvResttimeRemain.setText("运动剩余时间：" + mbyte[4]+"s");
                    } else {
                        progressBar.setProgress((int) (mbyte[5] / (float) mTimerShow[2] * 70.0f));
                        tvResttimeRemain.setText("休息剩余时间" + mbyte[5]+"s");
                    }
                    //Toast.makeText(MainActivity.this,""+mbyte[2],Toast.LENGTH_SHORT).show();
            }
        }
    };


    @OnClick({R.id.btn_blu,
            R.id.btn_start,
            R.id.cirpro_arm,
            R.id.cirpro_shoudler,
            R.id.cirpro_chest,
            R.id.cirpro_abdomen,
            R.id.cirpro_back,
            R.id.cirpro_waist,
            R.id.cirpro_forethigh,
            R.id.cirpro_backthigh,
            R.id.cirpro_hips,
            R.id.cirpro_calfs,

//            R.id.gif_arm,
//            R.id.gif_shoudler,
//            R.id.gif_chest,
//            R.id.gif_abdomen,
//            R.id.gif_back,
//            R.id.gif_waist,
//            R.id.gif_hgigh_fore,
//            R.id.gif_hgigh_back,
//            R.id.gif_hips,
//            R.id.gif_calf,
//            R.id.btn_arm_minus,
            //R.id.btn_arm_plus,
//            R.id.btn_shoulder_minus, R.id.btn_shoulder_plus,
//            R.id.btn_chest_minus, R.id.btn_chest_plus,
//            R.id.btn_abdomen_minus, R.id.btn_abdomen_plus,
//            R.id.btn_back_minus, R.id.btn_back_plus,
//            R.id.btn_waist_minus, R.id.btn_waist_plus,
//            R.id.btn_backthigh_minus, R.id.btn_backthigh_plus,
//            R.id.btn_forethigh_minus, R.id.btn_forethigh_plus,
//            R.id.btn_hips_minus, R.id.btn_hips_plus,
//            R.id.btn_calfs_minus, R.id.btn_calfs_plus,
//            R.id.imgbtn_all_minus, R.id.imgbtn_all_add,
//            R.id.btn_time_all_minus, R.id.btn_time_all_add,
//            R.id.btn_time_train_minus, R.id.btn_time_train_add,
//            R.id.btn_time_rest_minus, R.id.btn_time_rest_add
    })
    public void
    OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_blu:
                if (!isConnected) {
                    String strble = tvBluText.getText().toString().trim();
                    if (strble.equals("未连接")) {
                        connectNameDevice(lockName);
                        tvBluText.setText(R.string.string_blue_ing);
                    }
                }
                break;
            case R.id.btn_start:
                if (!start_en) {
                    //sendCurrentSignal();
                    sendTimer();
                    btnStart.setBackgroundResource(R.drawable.btn_start_en);
                    start_en = !start_en;
                } else {
                    startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                            "FE"
                                    + "01"
                                    + "00"
                                    + String.format("%02x", mTimerShow[0])
                                    + String.format("%02x", mTimerShow[1])
                                    + String.format("%02x", mTimerShow[2])
                                    + "0" + modeSelect
                                    + "00"
                                    + "00"
                                    + "00"
                                    + "00"
                                    + "00"
                                    + "EF"
                    );
                    sendTimer();
                    btnStart.setBackgroundResource(R.drawable.btn_start_dis);
                    start_en = !start_en;
                }
                break;


            case R.id.cirpro_arm:
                hideGif();
                gifArm.setVisibility(View.VISIBLE);
                break;

            case R.id.cirpro_shoudler:
                hideGif();
                gifShoudler.setVisibility(View.VISIBLE);
                break;
            case R.id.cirpro_chest:
                hideGif();
                gifChest.setVisibility(View.VISIBLE);
                break;
            case R.id.cirpro_abdomen:
                hideGif();
                gifAbdomen.setVisibility(View.VISIBLE);
                break;
            case R.id.cirpro_back:
                hideGif();
                gifBack.setVisibility(View.VISIBLE);
                break;
            case R.id.cirpro_waist:
                hideGif();
                gifWaist.setVisibility(View.VISIBLE);
                break;
            case R.id.cirpro_forethigh:
                hideGif();
                gifHgighFore.setVisibility(View.VISIBLE);
                break;
            case R.id.cirpro_backthigh:
                hideGif();
                gifHgighBack.setVisibility(View.VISIBLE);
                break;
            case R.id.cirpro_hips:
                hideGif();
                gifHips.setVisibility(View.VISIBLE);
                break;
            case R.id.cirpro_calfs:
                hideGif();
                gifCalf.setVisibility(View.VISIBLE);
                break;
//            case R.id.btn_arm_minus:
//                if (mEveryRank[0] <= 0) break;
//                mEveryRank[0]--;
//                cirproArm.setProgress(mEveryRank[0]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_arm_plus:
//                if (mEveryRank[0] >= 100) break;
//                mEveryRank[0]++;
//                cirproArm.setProgress(mEveryRank[0]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_shoulder_minus:
//                if (mEveryRank[1] <= 0) break;
//                mEveryRank[1]--;
//                cirproShoudler.setProgress(mEveryRank[1]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_shoulder_plus:
//                if (mEveryRank[1] >= 100) break;
//                mEveryRank[1]++;
//                cirproShoudler.setProgress(mEveryRank[1]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_chest_minus:
//                if (mEveryRank[2] <= 0) break;
//                mEveryRank[2]--;
//                cirproChest.setProgress(mEveryRank[2]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_chest_plus:
//                if (mEveryRank[2] >= 100) break;
//                mEveryRank[2]++;
//                cirproChest.setProgress(mEveryRank[2]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_abdomen_minus:
//                if (mEveryRank[3] <= 0) break;
//                mEveryRank[3]--;
//                cirproAbdomen.setProgress(mEveryRank[3]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_abdomen_plus:
//                if (mEveryRank[3] >= 100) break;
//                mEveryRank[3]++;
//                cirproAbdomen.setProgress(mEveryRank[3]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_back_minus:
//                if (mEveryRank[4] <= 0) break;
//                mEveryRank[4]--;
//                cirproBack.setProgress(mEveryRank[4]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_back_plus:
//                if (mEveryRank[4] >= 100) break;
//                mEveryRank[4]++;
//                cirproBack.setProgress(mEveryRank[4]);
//                sendCurrentSignal();
//                break;
//
//
//            case R.id.btn_waist_minus:
//                if (mEveryRank[5] <= 0) break;
//                mEveryRank[5]--;
//                cirproWaist.setProgress(mEveryRank[5]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_waist_plus:
//                if (mEveryRank[5] >= 100) break;
//                mEveryRank[5]++;
//                cirproWaist.setProgress(mEveryRank[5]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_forethigh_minus:
//                if (mEveryRank[6] <= 0) break;
//                mEveryRank[6]--;
//                cirproForethigh.setProgress(mEveryRank[6]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_forethigh_plus:
//                if (mEveryRank[6] >= 100) break;
//                mEveryRank[6]++;
//                cirproForethigh.setProgress(mEveryRank[6]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_backthigh_minus:
//                if (mEveryRank[7] <= 0) break;
//                mEveryRank[7]--;
//                cirproBackthigh.setProgress(mEveryRank[7]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_backthigh_plus:
//                if (mEveryRank[7] >= 100) break;
//                mEveryRank[7]++;
//                cirproBackthigh.setProgress(mEveryRank[7]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_hips_minus:
//                if (mEveryRank[8] <= 0) break;
//                mEveryRank[8]--;
//                cirproHips.setProgress(mEveryRank[8]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_hips_plus:
//                if (mEveryRank[8] >= 100) break;
//                mEveryRank[8]++;
//                cirproHips.setProgress(mEveryRank[8]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_calfs_minus:
//                if (mEveryRank[9] <= 0) break;
//                mEveryRank[9]--;
//                cirproCalfs.setProgress(mEveryRank[9]);
//                sendCurrentSignal();
//                break;
//            case R.id.btn_calfs_plus:
//                if (mEveryRank[9] >= 100) break;
//                mEveryRank[9]++;
//                cirproCalfs.setProgress(mEveryRank[9]);
//                sendCurrentSignal();
//                break;
//
//
//            case R.id.imgbtn_all_minus:
//                if (mAllRank <= 0) break;
//                mAllRank--;
//                imgbtnAllShow.setText(mAllRank + "");
//                sendCurrentSignal();
//                break;
//            case R.id.imgbtn_all_add:
//                if (mAllRank >= 100) break;
//                mAllRank++;
//                imgbtnAllShow.setText(mAllRank + "");
//                sendCurrentSignal();
//                break;
//
//            case R.id.btn_time_all_minus:
//                if (mTimerShow[0] <= 5) break;
//                mTimerShow[0]--;
//                btnControlerShow.setText(mTimerShow[0] + "");
//                sendTimer();
//                break;
//            case R.id.btn_time_all_add:
//                if (mTimerShow[0] >= 60) break;
//                mTimerShow[0]++;
//                btnControlerShow.setText(mTimerShow[0] + "");
//                sendTimer();
//                break;
//            case R.id.btn_time_train_minus:
//                if (mTimerShow[1] <= 0) break;
//                mTimerShow[1]--;
//                btnTrainShow.setText(mTimerShow[1] + "");
//                sendTimer();
//                break;
//            case R.id.btn_time_train_add:
//                if (mTimerShow[1] >= 60) break;
//                mTimerShow[1]++;
//                btnTrainShow.setText(mTimerShow[1] + "");
//                sendTimer();
//                break;
//            case R.id.btn_time_rest_minus:
//                if (mTimerShow[2] <= 0) break;
//                mTimerShow[2]--;
//                btnRestShow.setText(mTimerShow[2] + "");
//                sendTimer();
//                break;
//            case R.id.btn_time_rest_add:
//                if (mTimerShow[2] >= 70) break;
//                mTimerShow[2]++;
//                btnRestShow.setText(mTimerShow[2] + "");
//                progressBar.setProgress(mTimerShow[2]);
//                sendTimer();
//                break;
        }
    }

    void hideGif(){
        gifArm.setVisibility(View.GONE);
        gifShoudler.setVisibility(View.GONE);
        gifChest.setVisibility(View.GONE);
        gifAbdomen.setVisibility(View.GONE);
        gifBack.setVisibility(View.GONE);
        gifWaist.setVisibility(View.GONE);
        gifHgighFore.setVisibility(View.GONE);
        gifHgighBack.setVisibility(View.GONE);
        gifHips.setVisibility(View.GONE);
        gifCalf.setVisibility(View.GONE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_aerobic:
                //initSwitch();
                switchAerobic.setChecked(true);
                switchAnoxic.setChecked(false);
                switchMassage.setChecked(false);
                modeSelect = 1;
                sendTimer();
                break;
            case R.id.tv_anoxic:
                //initSwitch();
                switchAnoxic.setChecked(true);
                switchAerobic.setChecked(false);

                switchMassage.setChecked(false);
                modeSelect = 2;
                sendTimer();
                break;
            case R.id.tv_massage:
                //initSwitch();
                switchMassage.setChecked(true);
                switchAerobic.setChecked(false);
                switchAnoxic.setChecked(false);
                modeSelect = 3;
                sendTimer();
                break;
        }

    }

    private void initSwitch() {
        switchAerobic.setChecked(false);
        switchAnoxic.setChecked(false);
        switchMassage.setChecked(false);
    }

    private void sendCurrentSignal() {
        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                "FE"
                        + "02"
                        + String.format("%02x", mEveryRank[0])
                        + String.format("%02x", mEveryRank[1])
                        + String.format("%02x", mEveryRank[2])
                        + String.format("%02x", mEveryRank[3])
                        + String.format("%02x", mEveryRank[4])
                        + String.format("%02x", mEveryRank[5])
                        + String.format("%02x", mEveryRank[6])
                        + String.format("%02x", mEveryRank[7])
                        + String.format("%02x", mEveryRank[8])
                        + String.format("%02x", mEveryRank[9])
                        + "EF"
        );
    }

    private void sendTimer() {
        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                "FE"
                        + "01"
                        + "0" + (mAllRank == 0 ? 0 : 1)
                        + String.format("%02x", mTimerShow[0])
                        + String.format("%02x", mTimerShow[1])
                        + String.format("%02x", mTimerShow[2])
                        + "0" + modeSelect
                        + "00"
                        + "00"
                        + "00"
                        + "00"
                        + "00"
                        + "EF"
        );
    }


    @Override
    public void repeatAction(View view) {
        updateControlData(view);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int active = event.getAction();
        switch (active) {
            case MotionEvent.ACTION_DOWN:
                updateControlData(v);
                break;
            case MotionEvent.ACTION_UP:
                if(start_en)
                {
                    sendInfor(v);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return false;
    }


    void sendInfor(View v) {
        switch (v.getId()) {
            case R.id.btn_arm_minus:
                sendCurrentSignal();
                break;
            case R.id.btn_arm_plus:
                sendCurrentSignal();
                break;
            case R.id.btn_shoulder_minus:
                sendCurrentSignal();
                break;
            case R.id.btn_shoulder_plus:
                sendCurrentSignal();
                break;
            case R.id.btn_chest_minus:
                sendCurrentSignal();
                break;
            case R.id.btn_chest_plus:
                sendCurrentSignal();
                break;
            case R.id.btn_abdomen_minus:
                sendCurrentSignal();
                break;
            case R.id.btn_abdomen_plus:
                sendCurrentSignal();
                break;
            case R.id.btn_back_minus:
                sendCurrentSignal();
                break;
            case R.id.btn_back_plus:
                sendCurrentSignal();
                break;


            case R.id.btn_waist_minus:
                sendCurrentSignal();
                break;
            case R.id.btn_waist_plus:
                sendCurrentSignal();
                break;
            case R.id.btn_forethigh_minus:
                sendCurrentSignal();
                break;
            case R.id.btn_forethigh_plus:
                sendCurrentSignal();
                break;
            case R.id.btn_backthigh_minus:
                sendCurrentSignal();
                break;
            case R.id.btn_backthigh_plus:
                sendCurrentSignal();
                break;
            case R.id.btn_hips_minus:
                sendCurrentSignal();
                break;
            case R.id.btn_hips_plus:
                sendCurrentSignal();
                break;
            case R.id.btn_calfs_minus:
                sendCurrentSignal();
                break;
            case R.id.btn_calfs_plus:
                sendCurrentSignal();
                break;


            case R.id.imgbtn_all_minus:
                sendCurrentSignal();
                sendTimer();
                break;
            case R.id.imgbtn_all_add:
                sendCurrentSignal();
                sendTimer();
                break;

            case R.id.btn_time_all_minus:
                sendTimer();

                break;
            case R.id.btn_time_all_add:
                sendTimer();
                break;
            case R.id.btn_time_train_minus:
                sendTimer();
                break;
            case R.id.btn_time_train_add:
                sendTimer();
                break;
            case R.id.btn_time_rest_minus:
                sendTimer();
                break;
            case R.id.btn_time_rest_add:
                sendTimer();
                break;
        }
    }

    void updateControlData(View v) {
        switch (v.getId()) {
            case R.id.btn_arm_minus:
                if (mEveryRank[0] <= 0) break;
                mEveryRank[0]--;
                cirproArm.setProgress(mEveryRank[0]);

                break;
            case R.id.btn_arm_plus:
                if (mEveryRank[0] >= 100) break;
                mEveryRank[0]++;
                cirproArm.setProgress(mEveryRank[0]);

                break;
            case R.id.btn_shoulder_minus:
                if (mEveryRank[1] <= 0) break;
                mEveryRank[1]--;
                cirproShoudler.setProgress(mEveryRank[1]);

                break;
            case R.id.btn_shoulder_plus:
                if (mEveryRank[1] >= 100) break;
                mEveryRank[1]++;
                cirproShoudler.setProgress(mEveryRank[1]);

                break;
            case R.id.btn_chest_minus:
                if (mEveryRank[2] <= 0) break;
                mEveryRank[2]--;
                cirproChest.setProgress(mEveryRank[2]);

                break;
            case R.id.btn_chest_plus:
                if (mEveryRank[2] >= 100) break;
                mEveryRank[2]++;
                cirproChest.setProgress(mEveryRank[2]);

                break;
            case R.id.btn_abdomen_minus:
                if (mEveryRank[3] <= 0) break;
                mEveryRank[3]--;
                cirproAbdomen.setProgress(mEveryRank[3]);

                break;
            case R.id.btn_abdomen_plus:
                if (mEveryRank[3] >= 100) break;
                mEveryRank[3]++;
                cirproAbdomen.setProgress(mEveryRank[3]);

                break;
            case R.id.btn_back_minus:
                if (mEveryRank[4] <= 0) break;
                mEveryRank[4]--;
                cirproBack.setProgress(mEveryRank[4]);

                break;
            case R.id.btn_back_plus:
                if (mEveryRank[4] >= 100) break;
                mEveryRank[4]++;
                cirproBack.setProgress(mEveryRank[4]);

                break;


            case R.id.btn_waist_minus:
                if (mEveryRank[5] <= 0) break;
                mEveryRank[5]--;
                cirproWaist.setProgress(mEveryRank[5]);

                break;
            case R.id.btn_waist_plus:
                if (mEveryRank[5] >= 100) break;
                mEveryRank[5]++;
                cirproWaist.setProgress(mEveryRank[5]);

                break;
            case R.id.btn_forethigh_minus:
                if (mEveryRank[6] <= 0) break;
                mEveryRank[6]--;
                cirproForethigh.setProgress(mEveryRank[6]);

                break;
            case R.id.btn_forethigh_plus:
                if (mEveryRank[6] >= 100) break;
                mEveryRank[6]++;
                cirproForethigh.setProgress(mEveryRank[6]);
                break;
            case R.id.btn_backthigh_minus:
                if (mEveryRank[7] <= 0) break;
                mEveryRank[7]--;
                cirproBackthigh.setProgress(mEveryRank[7]);

                break;
            case R.id.btn_backthigh_plus:
                if (mEveryRank[7] >= 100) break;
                mEveryRank[7]++;
                cirproBackthigh.setProgress(mEveryRank[7]);

                break;
            case R.id.btn_hips_minus:
                if (mEveryRank[8] <= 0) break;
                mEveryRank[8]--;
                cirproHips.setProgress(mEveryRank[8]);

                break;
            case R.id.btn_hips_plus:
                if (mEveryRank[8] >= 100) break;
                mEveryRank[8]++;
                cirproHips.setProgress(mEveryRank[8]);

                break;
            case R.id.btn_calfs_minus:
                if (mEveryRank[9] <= 0) break;
                mEveryRank[9]--;
                cirproCalfs.setProgress(mEveryRank[9]);

                break;
            case R.id.btn_calfs_plus:
                if (mEveryRank[9] >= 100) break;
                mEveryRank[9]++;
                cirproCalfs.setProgress(mEveryRank[9]);

                break;


            case R.id.imgbtn_all_minus:
                if (mAllRank <= 0) break;
                mAllRank--;
                imgbtnAllShow.setText(mAllRank + "");

                break;
            case R.id.imgbtn_all_add:
                if (mAllRank >= 100) break;
                mAllRank++;
                imgbtnAllShow.setText(mAllRank + "");

                break;

            case R.id.btn_time_all_minus:
                if (mTimerShow[0] <= 5) break;
                mTimerShow[0]--;
                btnControlerShow.setText(mTimerShow[0] + "");

                break;
            case R.id.btn_time_all_add:
                if (mTimerShow[0] >= 60) break;
                mTimerShow[0]++;
                btnControlerShow.setText(mTimerShow[0] + "");

                break;
            case R.id.btn_time_train_minus:
                if (mTimerShow[1] <= 0) break;
                mTimerShow[1]--;
                btnTrainShow.setText(mTimerShow[1] + "");

                break;
            case R.id.btn_time_train_add:
                if (mTimerShow[1] >= 60) break;
                mTimerShow[1]++;
                btnTrainShow.setText(mTimerShow[1] + "");

                break;
            case R.id.btn_time_rest_minus:
                if (mTimerShow[2] <= 0) break;
                mTimerShow[2]--;
                btnRestShow.setText(mTimerShow[2] + "");

                break;
            case R.id.btn_time_rest_add:
                if (mTimerShow[2] >= 70) break;
                mTimerShow[2]++;
                btnRestShow.setText(mTimerShow[2] + "");
                //progressBar.setProgress(mTimerShow[2]);

                break;
        }
    }
}
