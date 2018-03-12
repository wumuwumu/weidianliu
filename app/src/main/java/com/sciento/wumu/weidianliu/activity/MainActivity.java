package com.sciento.wumu.weidianliu.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.ListScanCallback;
import com.clj.fastble.utils.HexUtil;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.sciento.wumu.weidianliu.R;
import com.sciento.wumu.weidianliu.adapter.DeviceListAdapter;
import com.sciento.wumu.weidianliu.utils.ProgressDialogUtils;
import com.sciento.wumu.weidianliu.utils.RequestPermissonUtil;
import com.sciento.wumu.weidianliu.view.LongClickButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends BaseActivity implements CompoundButton.OnClickListener, LongClickButton.LongClickRepeatListener,
        View.OnTouchListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";

    //animator
    final ValueAnimator animator = ValueAnimator.ofInt(0, 200);

    //隐藏动画
    final ValueAnimator[] animator_hide = {
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),
            ValueAnimator.ofInt(0, 20),

    };
    final ValueAnimator task = ValueAnimator.ofInt(0, 200);
    private final int MSG_GET_DATE = 0;
    private final int MSG_QUEUE_DATE = 2;
    private final int MSG_SHOW_DATE = 1;
    private final int MSG_UPDATE_DEVICE_NAME = 3;
    private final int SEND_DELAY = 200;

    @BindView(R.id.ll_drawer)
    LinearLayout llDrawer;
    @BindView(R.id.dl_all)
    DrawerLayout dlAll;
    @BindView(R.id.btn_chinese)
    Button btnChinese;
    @BindView(R.id.btn_english)
    Button btnEnglish;
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
    @BindView(R.id.img_battery)
    ImageView imgBattery;
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;
    //显示值
    int[] mEveryRank = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int mAllRank = 100;
    //timer
    int[] mTimerShow = {1, 0, 0};

    List<BluetoothDevice> deviceList = new ArrayList<>();
    BluetoothDevice connectDevice;

    Queue<Byte> BleQueue = new LinkedList<Byte>();
    //mode
    int modeSelect = 1;

    //all switch
    boolean start_en = false;
    boolean startPressIs = false;

    int mallRankCopy = 0;
    int mstart = 0;
    int mstop = 0;
    int mratio = 0;
    int alltime = 0;
    //get back
    long mIntervelTime = 0;
    @BindView(R.id.btn_find_bledevice)
    Button btnFindBledevice;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.lv_bledevice_list)
    ListView lvBledeviceList;
    byte[] mbytecopy = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    List<CircleProgressBar> circleProgressBarList = new ArrayList<>();
    @BindView(R.id.btn_about)
    Button btnAbout;
    //    private String lockName = "BlakBat";
    private String lockName = "BlakBat";
    private boolean isConnected = false;
    //主线程进行数据的处理
    private Handler HandlerBluMain = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_DATE:
                    Bundle queueyBundle = msg.getData();
                    byte[] queuebyte = queueyBundle.getByteArray("datearray");
                    for (int i = 0; i < queuebyte.length; i++) {
                        BleQueue.offer(queuebyte[i]);
                    }
                    if (BleQueue.size() >= 13) {
                        int fe = BleQueue.peek();
                        while (fe != -2 && !BleQueue.isEmpty()) {
                            BleQueue.poll();
                            fe = BleQueue.peek();
                        }
                        if (BleQueue.size() >= 13) {
                            byte[] mgetByte = new byte[13];
                            for (int j = 0; j < 13; j++) {
                                mgetByte[j] = BleQueue.poll();
                            }
                            analyseData(mgetByte);
                        }

                    }
//                    analyseData(queuebyte);
                    break;
                //保存蓝牙连接的名字
                case MSG_UPDATE_DEVICE_NAME:
                    //获取一个文件名为test、权限为private的xml文件的SharedPreferences对象
                    SharedPreferences sharedPreferences = getSharedPreferences("weidianliu", MODE_PRIVATE);

                    //得到SharedPreferences.Editor对象，并保存数据到该对象中
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("devicename", lockName);

                    //保存key-value对到文件中
                    editor.commit();
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private void analyseData(byte[] mgetByte) {

        if (mgetByte[12] == -17) {
            if (!start_en && mgetByte[6] == 1) {
//                startPressIs = false;
                start_en = true;
                //sendCurrentSignal();


            } else if (start_en && mgetByte[6] == 2) {
                //停止发送
//
            }

            //更新电量
            updateBattery(mgetByte[2]);

        }
    }

    private void updateBattery(byte mbattery) {
        if (mbattery >= 0 && mbattery <= 100) {
            if (mbattery > 69)
                imgBattery.setImageResource(R.drawable.im_dianliang100);
            else if (mbattery > 59)
                imgBattery.setImageResource(R.drawable.im_dianliang80);
            else if (mbattery > 39)
                imgBattery.setImageResource(R.drawable.im_dianliang60);
            else if (mbattery > 19)
                imgBattery.setImageResource(R.drawable.im_dianliang40);
            else if (mbattery > 9)
                imgBattery.setImageResource(R.drawable.im_dianliang20);
            else if (mbattery >= 0)
                imgBattery.setImageResource(R.drawable.im_dianliang10);

        } else {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        ButterKnife.bind(this);
        init();
        initEvent();
//        thread.start();
    }

    private void initEvent() {
        tvAerobic.setOnClickListener(this);
        tvAnoxic.setOnClickListener(this);
        tvMassage.setOnClickListener(this);


        //长按清零时间
        cirproArm.setOnLongClickListener(this);
        cirproChest.setOnLongClickListener(this);
        cirproShoudler.setOnLongClickListener(this);
        cirproCalfs.setOnLongClickListener(this);
        cirproHips.setOnLongClickListener(this);
        cirproAbdomen.setOnLongClickListener(this);
        cirproBack.setOnLongClickListener(this);
        cirproBackthigh.setOnLongClickListener(this);
        cirproForethigh.setOnLongClickListener(this);
        cirproWaist.setOnLongClickListener(this);

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


        //hide
        animator_hide[0].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnArmMinus.setVisibility(View.INVISIBLE);
                btnArmPlus.setVisibility(View.INVISIBLE);
                gifArm.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator_hide[1].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnShoulderMinus.setVisibility(View.INVISIBLE);
                btnShoulderPlus.setVisibility(View.INVISIBLE);
                gifShoudler.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[2].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnChestMinus.setVisibility(View.INVISIBLE);
                btnChestPlus.setVisibility(View.INVISIBLE);
                gifChest.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[3].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnAbdomenMinus.setVisibility(View.INVISIBLE);
                btnAbdomenPlus.setVisibility(View.INVISIBLE);
                gifAbdomen.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[4].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnBackMinus.setVisibility(View.INVISIBLE);
                btnBackPlus.setVisibility(View.INVISIBLE);
                gifBack.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator_hide[5].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnWaistMinus.setVisibility(View.INVISIBLE);
                btnWaistPlus.setVisibility(View.INVISIBLE);
                gifWaist.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[6].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnForethighPlus.setVisibility(View.INVISIBLE);
                btnForethighMinus.setVisibility(View.INVISIBLE);
                gifHgighFore.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[7].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnBackthighMinus.setVisibility(View.INVISIBLE);
                btnBackthighPlus.setVisibility(View.INVISIBLE);
                gifHgighBack.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[8].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnHipsMinus.setVisibility(View.INVISIBLE);
                btnHipsPlus.setVisibility(View.INVISIBLE);
                gifHips.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[9].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnCalfsMinus.setVisibility(View.INVISIBLE);
                btnCalfsPlus.setVisibility(View.INVISIBLE);
                gifCalf.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[10].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imgbtnAllMinus.setVisibility(View.INVISIBLE);
                imgbtnAllAdd.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[11].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnTimeAllAdd.setVisibility(View.INVISIBLE);
                btnTimeAllMinus.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[12].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnTimeTrainMinus.setVisibility(View.INVISIBLE);
                btnTimeTrainAdd.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_hide[13].addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnTimeRestMinus.setVisibility(View.INVISIBLE);
                btnTimeRestAdd.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

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

        lvBledeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (start_en) {
                    return;
                }
                if (isConnected) {
                    if (lockName == deviceList.get(position).getName()) {
                        return;
                    } else {
                        bleManager.closeBluetoothGatt();
                        isConnected = false;
                        btnBlu.setBackgroundResource(R.drawable.btn_blu_nor);
                        tvBluText.setText(getString(R.string.string_blu_dis));
                    }

                }

                dlAll.closeDrawer(llDrawer);
                lockName = deviceList.get(position).getName();
                connectDevice = deviceList.get(position);
                HandlerBluMain.sendEmptyMessage(MSG_UPDATE_DEVICE_NAME);

                String strble = tvBluText.getText().toString().trim();
                if (strble.equals(getString(R.string.string_blu_dis))) {
                    tvBluText.setText(R.string.string_blue_ing);
                    connectNameDevice(lockName);
                }
            }
        });


    }

    private void init() {

        circleProgressBarList.add(0, cirproArm);
        circleProgressBarList.add(1, cirproShoudler);
        circleProgressBarList.add(2, cirproChest);
        circleProgressBarList.add(3, cirproAbdomen);
        circleProgressBarList.add(4, cirproBack);
        circleProgressBarList.add(5, cirproWaist);
        circleProgressBarList.add(6, cirproForethigh);
        circleProgressBarList.add(7, cirproBackthigh);
        circleProgressBarList.add(8, cirproHips);
        circleProgressBarList.add(9, cirproCalfs);


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

        //hide
        btnArmPlus.setVisibility(View.INVISIBLE);
        btnArmMinus.setVisibility(View.INVISIBLE);

        btnShoulderMinus.setVisibility(View.INVISIBLE);
        btnShoulderPlus.setVisibility(View.INVISIBLE);

        btnChestMinus.setVisibility(View.INVISIBLE);
        btnChestPlus.setVisibility(View.INVISIBLE);

        btnAbdomenMinus.setVisibility(View.INVISIBLE);
        btnAbdomenPlus.setVisibility(View.INVISIBLE);

        btnBackMinus.setVisibility(View.INVISIBLE);
        btnBackPlus.setVisibility(View.INVISIBLE);

        btnWaistMinus.setVisibility(View.INVISIBLE);
        btnWaistPlus.setVisibility(View.INVISIBLE);

        btnForethighMinus.setVisibility(View.INVISIBLE);
        btnForethighPlus.setVisibility(View.INVISIBLE);

        btnBackthighMinus.setVisibility(View.INVISIBLE);
        btnBackthighPlus.setVisibility(View.INVISIBLE);

        btnHipsMinus.setVisibility(View.INVISIBLE);
        btnHipsPlus.setVisibility(View.INVISIBLE);

        btnCalfsMinus.setVisibility(View.INVISIBLE);
        btnCalfsPlus.setVisibility(View.INVISIBLE);

        imgbtnAllMinus.setVisibility(View.INVISIBLE);
        imgbtnAllAdd.setVisibility(View.INVISIBLE);

        btnTimeAllMinus.setVisibility(View.INVISIBLE);
        btnTimeAllAdd.setVisibility(View.INVISIBLE);

        btnTimeRestMinus.setVisibility(View.INVISIBLE);
        btnTimeRestAdd.setVisibility(View.INVISIBLE);

        btnTimeTrainMinus.setVisibility(View.INVISIBLE);
        btnTimeTrainAdd.setVisibility(View.INVISIBLE);

        for (int i = 0; i < animator_hide.length; i++) {
            animator_hide[i].setDuration(5000);
            animator_hide[i].setRepeatCount(1);
        }

    }

    /**
     * 切换语言
     *
     * @param language
     */

    private void switchLanguage(String language) {

        //设置应用语言类型

        Resources resources = getResources();

        Configuration config = resources.getConfiguration();

        DisplayMetrics dm = resources.getDisplayMetrics();

        if (language.equals("zh_simple")) {

            config.locale = Locale.SIMPLIFIED_CHINESE;
            btnChinese.setBackgroundColor(getResources().getColor(R.color.color_btn));

        } else if (language.equals("en")) {

            config.locale = Locale.ENGLISH;
            btnEnglish.setBackgroundColor(getResources().getColor(R.color.color_btn));
        } else {

            config.locale = Locale.getDefault();

        }

        resources.updateConfiguration(config, dm);


    }

    @Override
    protected void onDestroy() {

        bleManager.closeBluetoothGatt();
        animator.cancel();
        HandlerBluMain.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("weidianliu", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "zh_simple");
        switchLanguage(language);
        bleManager.enableBluetooth();
        if (isConnected) {
            btnBlu.setBackgroundResource(R.drawable.btn_blu_press);
            tvBluText.setText(R.string.string_blu_en);
        } else {
            tvBluText.setText(R.string.string_blu_dis);
            btnBlu.setBackgroundResource(R.drawable.btn_blu_nor);
        }
        //thread.start();
    }

    private void connectNameDevice(final String deviceName) {

        bleManager.scanNameAndConnect(deviceName, 20000, false, new BleGattCallback() {
            @Override
            public void onNotFoundDevice() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        tvBluText.setText(R.string.string_blu_dis);
                        Toast.makeText(MainActivity.this, getString(R.string.str_no_find_bledevice), Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onFoundDevice(BluetoothDevice device) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, getString(R.string.str_no_find_bledevice), Toast.LENGTH_LONG).show();
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
                        connectDevice = gatt.getDevice();
                        showDeviceList(deviceList);


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
                        lockName = "";
                        showDeviceList(deviceList);
                        animator.cancel();
                    }
                });
                bleManager.handleException(exception);
            }


        });
    }

    //蓝牙初始化
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

                        Message message = Message.obtain();
                        message.what = MSG_SHOW_DATE;
                        Bundle dateBundle = new Bundle();
                        dateBundle.putByteArray("datearray", m_byte);
                        message.setData(dateBundle);
                        HandlerBluMain.sendMessage(message);


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

    /**
     * 搜索周围蓝牙设备
     */
    private void scanDevice() {
        if (bleManager.isInScanning())
            return;

        ProgressDialogUtils.getInstance().show(MainActivity.this, R.string.str_find_bledevice);

        boolean en = bleManager.scanDevice(new ListScanCallback(15000) {


            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                super.onLeScan(device, rssi, scanRecord);
            }

            @Override
            public void onScanTimeout() {
                super.onScanTimeout();
            }

            @Override
            public void onDeviceFound(final BluetoothDevice[] devices) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDialogUtils.getInstance().dismiss();
//                        Toast.makeText(MainActivity.this,devices.length+"",Toast.LENGTH_SHORT).show();
                        deviceList = new ArrayList<BluetoothDevice>(Arrays.asList(devices));

                        if (isConnected) {

                            deviceList.add(connectDevice);
                        }
                        showDeviceList(deviceList);

                    }
                });
            }
        });
    }

    /**
     * 显示蓝牙设备列表
     */
    private void showDeviceList(List<BluetoothDevice> deviceList) {
//        List<String> nameList = new ArrayList<>();
//        for(int i =0 ;i<deviceList.size();i++){
//            nameList.add(i,deviceList.get(i).getName());
//        }
        DeviceListAdapter deviceListAdapter = new DeviceListAdapter(MainActivity.this, deviceList, lockName);
        lvBledeviceList.setAdapter(deviceListAdapter);
//        deviceListAdapter.notifyDataSetChanged();

    }

    //动画开始
    private void startAnimal() {
        mratio = (int) (200 * 2f / (mTimerShow[1] + mTimerShow[2]));
        mstart = (int) (200.0f * mTimerShow[1] / (mTimerShow[1] + mTimerShow[2]));
        mstop = 200 - mstart;
        //animator.end();
        animator.cancel();
        alltime = mTimerShow[0] * 60;
        tvAlltimeRemain.setText(getString(R.string.str_all_remain_time) + mTimerShow[0] + "min");
        animator.setDuration((mTimerShow[1] + mTimerShow[2]) * 1000);
        animator.setRepeatCount((int) (mTimerShow[0] * 60.0 / (mTimerShow[1] + mTimerShow[2])));
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int integer = (int) animator.getAnimatedValue();
                if (integer < mstart) {
                    progressBar.setProgress((int) (integer * 70.0f / mstart));
                    tvResttimeRemain.setText(getString(R.string.str_train_remain_time) + (mTimerShow[1] - (int) (integer / 200f * (mTimerShow[1] + mTimerShow[2]))) + "s");
                    if (integer % mratio == 0) {
                        sendOpen();
                    }

                } else {
                    progressBar.setProgress((int) ((200 - integer) * 70.0f / mstop));
                    tvResttimeRemain.setText(getString(R.string.str_rest_remain_time) + ((int) ((200 - integer) / 200f * (mTimerShow[1] + mTimerShow[2]))) + "s");
                    if (integer % mratio == 0 || integer == mstart) {
                        sendClose();
                    }
                }
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            int i = 0;

            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnStart.setBackgroundResource(R.drawable.btn_start_dis);
                tvAlltimeRemain.setText(getString(R.string.str_all_remain_time) + "0min");
                tvCountDown.setVisibility(View.INVISIBLE);
                start_en = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                btnStart.setBackgroundResource(R.drawable.btn_start_dis);
                tvAlltimeRemain.setText(getString(R.string.str_all_remain_time) + "0min");
                tvCountDown.setVisibility(View.INVISIBLE);
                sendCloseTimer();
                start_en = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                i++;
                tvAlltimeRemain.setText(getString(R.string.str_train_remain_time)
                        + ((int) (mTimerShow[0] * 60f - i * (mTimerShow[1] + mTimerShow[2])) / 60 + 1)
                        + "min");
                sendOpen();

            }
        });
        animator.start();
        mIntervelTime = 0;

        btnStart.setBackgroundResource(R.drawable.btn_start_en);

    }


    private void stopAnimal() {
        animator.cancel();
        start_en = false;
    }


    //按钮的点击事件
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
            R.id.imgbtn_all_show,
            R.id.btn_controler_show,
            R.id.btn_rest_show,
            R.id.btn_train_show,
            R.id.btn_find_bledevice,
            R.id.btn_chinese,
            R.id.btn_english,
            R.id.btn_about,
    })
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_blu:
                if (!isConnected) {
                    SharedPreferences sharedPreferences = getSharedPreferences("weidianliu", Context.MODE_PRIVATE);
                    lockName = sharedPreferences.getString("devicename", lockName);
                    String strble = tvBluText.getText().toString().trim();
                    if (strble.equals(getString(R.string.string_blu_dis))) {
                        connectNameDevice(lockName);
                        tvBluText.setText(R.string.string_blue_ing);
                    }
                }
                break;
            case R.id.btn_find_bledevice:
                scanDevice();
                break;
            case R.id.btn_chinese:
                bleManager.closeBluetoothGatt();
                isConnected = false;
                //获取一个文件名为test、权限为private的xml文件的SharedPreferences对象
                SharedPreferences sharedPreferenceschinese = getSharedPreferences("weidianliu", MODE_PRIVATE);

                //得到SharedPreferences.Editor对象，并保存数据到该对象中
                SharedPreferences.Editor editorChinese = sharedPreferenceschinese.edit();
                editorChinese.putString("language", "zh_simple");

                //保存key-value对到文件中
                editorChinese.commit();
                switchLanguage("zh_simple");
                //更新语言后，destroy当前页面，重新绘制

                finish();

                Intent itchinese = new Intent(MainActivity.this, MainActivity.class);

                //清空任务栈确保当前打开activit为前台任务栈栈顶

                itchinese.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(itchinese);
                break;
            case R.id.btn_english:
                bleManager.closeBluetoothGatt();
                isConnected = false;
                //获取一个文件名为test、权限为private的xml文件的SharedPreferences对象
                SharedPreferences sharedPreferencesenglish = getSharedPreferences("weidianliu", MODE_PRIVATE);

                //得到SharedPreferences.Editor对象，并保存数据到该对象中
                SharedPreferences.Editor editorenglish = sharedPreferencesenglish.edit();
                editorenglish.putString("language", "en");

                //保存key-value对到文件中
                editorenglish.commit();
                switchLanguage("en");
                //更新语言后，destroy当前页面，重新绘制

                finish();

                Intent itenglish = new Intent(MainActivity.this, MainActivity.class);

                //清空任务栈确保当前打开activit为前台任务栈栈顶

                itenglish.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(itenglish);
                break;
            case R.id.btn_about:
                final View dialogView = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.item_about, null);
                Dialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                        .setTitle(getString(R.string.about))//设置标题
                        .setView(dialogView)
                        .setMessage("？")//设置提示内容
                        //确定按钮
//                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
                        .create();//创建对话框
                dialog.show();//显示对话框
                break;
            case R.id.btn_start:
                if (mTimerShow[1] == 0 && mTimerShow[2] == 0) {
                    break;
                }

                if (isConnected == false) {
                    return;
                }


                if (!start_en) {
                    sendCurrentSignal();
                    tvCountDown.setText("3S");
                    tvCountDown.setVisibility(View.VISIBLE);
                    task.setRepeatCount(3);
                    task.setDuration(1000);

                    task.addListener(new Animator.AnimatorListener() {
                        int count = 3;

                        @Override
                        public void onAnimationStart(Animator animation) {
                            count = 3;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            sendDouOpen();
                            startAnimal();
                            start_en = true;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                            count--;
                            if (count < 0) {
                                tvCountDown.setVisibility(View.INVISIBLE);
                            }
                            tvCountDown.setText(count + "S");

//                        if(mbytecopy[6] ==0)
//                        {

                        }
                    });
                    task.start();

                } else {
                    stopAnimal();
//                    sendCloseTimer();

//                    task.cancel();
//                    sendDouClose();

                    tvCountDown.setVisibility(View.INVISIBLE);
                    start_en = false;
                }
////                重复发送，等待回复

//                startPressIs = true;
//                task.start();
                break;
            case R.id.cirpro_arm:

                if (btnArmMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnArmMinus.setVisibility(View.VISIBLE);
                    btnArmPlus.setVisibility(View.VISIBLE);
                    gifArm.setVisibility(View.VISIBLE);
                    animator_hide[0].start();
                }

                break;

            case R.id.cirpro_shoudler:
                if (btnShoulderMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnShoulderMinus.setVisibility(View.VISIBLE);
                    btnShoulderPlus.setVisibility(View.VISIBLE);
                    gifShoudler.setVisibility(View.VISIBLE);
                    animator_hide[1].start();
                }
                break;
            case R.id.cirpro_chest:
                if (btnChestMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnChestMinus.setVisibility(View.VISIBLE);
                    btnChestPlus.setVisibility(View.VISIBLE);
                    gifChest.setVisibility(View.VISIBLE);
                    animator_hide[2].start();
                }
                break;
            case R.id.cirpro_abdomen:
                if (btnAbdomenMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnAbdomenMinus.setVisibility(View.VISIBLE);
                    btnAbdomenPlus.setVisibility(View.VISIBLE);
                    gifAbdomen.setVisibility(View.VISIBLE);
                    animator_hide[3].start();
                }
                break;
            case R.id.cirpro_back:
                if (btnBackMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnBackMinus.setVisibility(View.VISIBLE);
                    btnBackPlus.setVisibility(View.VISIBLE);
                    gifBack.setVisibility(View.VISIBLE);
                    animator_hide[4].start();
                }
                break;
            case R.id.cirpro_waist:
                if (btnWaistMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnWaistMinus.setVisibility(View.VISIBLE);
                    btnWaistPlus.setVisibility(View.VISIBLE);
                    gifWaist.setVisibility(View.VISIBLE);
                    animator_hide[5].start();
                }
                break;
            case R.id.cirpro_forethigh:
                if (btnForethighMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnForethighMinus.setVisibility(View.VISIBLE);
                    btnForethighPlus.setVisibility(View.VISIBLE);
                    gifHgighFore.setVisibility(View.VISIBLE);
                    animator_hide[6].start();
                }
                break;
            case R.id.cirpro_backthigh:
                if (btnBackthighMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnBackthighMinus.setVisibility(View.VISIBLE);
                    btnBackthighPlus.setVisibility(View.VISIBLE);
                    gifHgighBack.setVisibility(View.VISIBLE);
                    animator_hide[7].start();
                }
                break;
            case R.id.cirpro_hips:

                if (btnHipsMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnHipsPlus.setVisibility(View.VISIBLE);
                    btnHipsMinus.setVisibility(View.VISIBLE);
                    gifHips.setVisibility(View.VISIBLE);
                    animator_hide[8].start();
                }
                break;
            case R.id.cirpro_calfs:

                if (btnCalfsMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnCalfsMinus.setVisibility(View.VISIBLE);
                    btnCalfsPlus.setVisibility(View.VISIBLE);
                    gifCalf.setVisibility(View.VISIBLE);
                    animator_hide[9].start();
                }
                break;
            case R.id.imgbtn_all_show:
                if (imgbtnAllMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    imgbtnAllMinus.setVisibility(View.VISIBLE);
                    imgbtnAllAdd.setVisibility(View.VISIBLE);
                    animator_hide[10].start();
                }
                break;
            case R.id.btn_controler_show:
                if (btnTimeAllMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnTimeAllMinus.setVisibility(View.VISIBLE);
                    btnTimeAllAdd.setVisibility(View.VISIBLE);
                    animator_hide[11].start();
                }
                break;
            case R.id.btn_train_show:
                if (btnTimeTrainMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnTimeTrainMinus.setVisibility(View.VISIBLE);
                    btnTimeTrainAdd.setVisibility(View.VISIBLE);
                    animator_hide[12].start();
                }
                break;
            case R.id.btn_rest_show:
                if (btnTimeRestMinus.getVisibility() != View.VISIBLE) {
                    hideAllBtn();
                    hideGif();
                    //animator_hide[0].cancel();
                    btnTimeRestMinus.setVisibility(View.VISIBLE);
                    btnTimeRestAdd.setVisibility(View.VISIBLE);
                    animator_hide[13].start();
                }
                break;
        }
    }

    //隐藏那些显示的按钮
    void hideGif() {
        gifArm.setVisibility(View.INVISIBLE);
        gifShoudler.setVisibility(View.INVISIBLE);
        gifChest.setVisibility(View.INVISIBLE);
        gifAbdomen.setVisibility(View.INVISIBLE);
        gifBack.setVisibility(View.INVISIBLE);
        gifWaist.setVisibility(View.INVISIBLE);
        gifHgighFore.setVisibility(View.INVISIBLE);
        gifHgighBack.setVisibility(View.INVISIBLE);
        gifHips.setVisibility(View.INVISIBLE);
        gifCalf.setVisibility(View.INVISIBLE);

    }

    //修改模式
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_aerobic:
                //initSwitch();
                switchAerobic.setChecked(true);
                switchAnoxic.setChecked(false);
                switchMassage.setChecked(false);
                modeSelect = 1;
                sendOpenTimer();
                break;
            case R.id.tv_anoxic:
                //initSwitch();
                switchAnoxic.setChecked(true);
                switchAerobic.setChecked(false);

                switchMassage.setChecked(false);
                modeSelect = 2;
                sendOpenTimer();
                break;
            case R.id.tv_massage:
                //initSwitch();
                switchMassage.setChecked(true);
                switchAerobic.setChecked(false);
                switchAnoxic.setChecked(false);
                modeSelect = 3;
                sendOpenTimer();
                break;
        }

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.cirpro_arm:
                return updateRangeBtnValue(0, 0);
            case R.id.cirpro_shoudler:
                return updateRangeBtnValue(1, 0);
            case R.id.cirpro_chest:
                return updateRangeBtnValue(2, 0);
            case R.id.cirpro_abdomen:
                return updateRangeBtnValue(3, 0);
            case R.id.cirpro_back:
                return updateRangeBtnValue(4, 0);
            case R.id.cirpro_waist:
                return updateRangeBtnValue(5, 0);
            case R.id.cirpro_forethigh:
                return updateRangeBtnValue(6, 0);
            case R.id.cirpro_backthigh:
                return updateRangeBtnValue(7, 0);
            case R.id.cirpro_hips:
                return updateRangeBtnValue(8, 0);
            case R.id.cirpro_calfs:
                return updateRangeBtnValue(9, 0);

        }
        return false;
    }

    //清理并发送数据
    private boolean updateRangeBtnValue(int btn, int value) {
        mEveryRank[btn] = 0;
        circleProgressBarList.get(btn).setProgress(0);
        sendCurrentSignal();
        return true;
    }

    //初始化模式
    private void initSwitch() {
        switchAerobic.setChecked(false);
        switchAnoxic.setChecked(false);
        switchMassage.setChecked(false);
    }

    //发送的等级百分比所有的状态
    private void sendCurrentSignal() {
        sendSignal();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                sendSignal();
            }
        }, SEND_DELAY);
    }

    private void sendSignal() {
        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                "FE"
                        + "02"
                        + String.format("%02x", mEveryRank[0] * mAllRank / 100)
                        + String.format("%02x", mEveryRank[1] * mAllRank / 100)
                        + String.format("%02x", mEveryRank[2] * mAllRank / 100)
                        + String.format("%02x", mEveryRank[3] * mAllRank / 100)
                        + String.format("%02x", mEveryRank[4] * mAllRank / 100)
                        + String.format("%02x", mEveryRank[5] * mAllRank / 100)
                        + String.format("%02x", mEveryRank[6] * mAllRank / 100)
                        + String.format("%02x", mEveryRank[7] * mAllRank / 100)
                        + String.format("%02x", mEveryRank[8] * mAllRank / 100)
                        + String.format("%02x", mEveryRank[9] * mAllRank / 100)
                        + "EF"
        );
    }

    //发送定时
    private void sendCloseTimer() {
        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                "FE"
                        + "01"
//                        + "0" + (mAllRank == 0 ? 0 : 1)
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
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                        "FE"
                                + "01"
//                        + "0" + (mAllRank == 0 ? 0 : 1)
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
            }
        }, SEND_DELAY);
//        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
//                "FE"
//                        + "01"
////                        + "0" + (mAllRank == 0 ? 0 : 1)
//                        + "00"
//                        + String.format("%02x", mTimerShow[0])
//                        + String.format("%02x", mTimerShow[1])
//                        + String.format("%02x", mTimerShow[2])
//                        + "0" + modeSelect
//                        + "00"
//                        + "00"
//                        + "00"
//                        + "00"
//                        + "00"
//                        + "EF"
//        );
    }

    //发送定时
    private void sendOpenTimer() {
        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                "FE"
                        + "01"
//                        + "0" + (mAllRank == 0 ? 0 : 1)
                        + "01"
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
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                        "FE"
                                + "01"
//                        + "0" + (mAllRank == 0 ? 0 : 1)
                                + "01"
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
        }, SEND_DELAY);
//        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
//                "FE"
//                        + "01"
////                        + "0" + (mAllRank == 0 ? 0 : 1)
//                        + "00"
//                        + String.format("%02x", mTimerShow[0])
//                        + String.format("%02x", mTimerShow[1])
//                        + String.format("%02x", mTimerShow[2])
//                        + "0" + modeSelect
//                        + "00"
//                        + "00"
//                        + "00"
//                        + "00"
//                        + "00"
//                        + "EF"
//        );
    }

    private void sendTimer() {
        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                "FE"
                        + "01"
                        + "0" + (start_en ? 1 : 0)
//                        + "01"
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
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                        "FE"
                                + "01"
                                + "0" + (start_en ? 1 : 0)
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
        }, SEND_DELAY);
//        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
//                "FE"
//                        + "01"
////                        + "0" + (mAllRank == 0 ? 0 : 1)
//                        + "00"
//                        + String.format("%02x", mTimerShow[0])
//                        + String.format("%02x", mTimerShow[1])
//                        + String.format("%02x", mTimerShow[2])
//                        + "0" + modeSelect
//                        + "00"
//                        + "00"
//                        + "00"
//                        + "00"
//                        + "00"
//                        + "EF"
//        );
    }

    private void sendDouOpen() {
        sendOpen();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                sendOpen();
            }
        }, SEND_DELAY);
    }

    private void sendOpen() {
        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                "FE"
                        + "01"
                        + "01"
                        + String.format("%02x", mTimerShow[0])
                        + String.format("%02x", mTimerShow[1])
                        + String.format("%02x", mTimerShow[2])
                        + "0" + modeSelect
                        + "01"
                        + "00"
                        + "00"
                        + "00"
                        + "00"
                        + "EF"
        );
    }

    private void sendClose() {
        startWrite(ZZR_UUID_BLE_SERVICE.toString(), ZZR_UUID_BLE_CHAR.toString(),
                "FE"
                        + "01"
                        + "00"
                        + String.format("%02x", mTimerShow[0])
                        + String.format("%02x", mTimerShow[1])
                        + String.format("%02x", mTimerShow[2])
                        + "0" + modeSelect
                        + "02"
                        + "00"
                        + "00"
                        + "00"
                        + "00"
                        + "EF"
        );
    }

    private void sendDouClose() {
        sendClose();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                sendClose();
            }
        }, SEND_DELAY);
    }

    //长按按钮
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
                sendInfor(v);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return false;
    }

    //判断按钮的的状态，发送数据
    void sendInfor(View v) {
        switch (v.getId()) {
            case R.id.btn_arm_minus:
                //animator_hide[0].cancel();
                animator_hide[0].start();
                sendCurrentSignal();
                break;
            case R.id.btn_arm_plus:
                //animator_hide[0].cancel();
                animator_hide[0].start();
                sendCurrentSignal();
                break;
            case R.id.btn_shoulder_minus:
                //animator_hide[1].cancel();
                animator_hide[1].start();
                sendCurrentSignal();
                break;
            case R.id.btn_shoulder_plus:
                //animator_hide[1].cancel();
                animator_hide[1].start();
                sendCurrentSignal();
                break;
            case R.id.btn_chest_minus:
                //animator_hide[2].cancel();
                animator_hide[2].start();
                sendCurrentSignal();
                break;
            case R.id.btn_chest_plus:
                //animator_hide[2].cancel();
                animator_hide[2].start();
                sendCurrentSignal();
                break;
            case R.id.btn_abdomen_minus:
                //animator_hide[3].cancel();
                animator_hide[3].start();
                sendCurrentSignal();
                break;
            case R.id.btn_abdomen_plus:
                // animator_hide[3].cancel();
                animator_hide[3].start();
                sendCurrentSignal();
                break;
            case R.id.btn_back_minus:
                //animator_hide[4].cancel();
                animator_hide[4].start();
                sendCurrentSignal();
                break;
            case R.id.btn_back_plus:
                //animator_hide[4].cancel();
                animator_hide[4].start();
                sendCurrentSignal();
                break;


            case R.id.btn_waist_minus:
                // animator_hide[5].cancel();
                animator_hide[5].start();
                sendCurrentSignal();
                break;
            case R.id.btn_waist_plus:
                //animator_hide[5].cancel();
                animator_hide[5].start();
                sendCurrentSignal();
                break;
            case R.id.btn_forethigh_minus:
                // animator_hide[6].cancel();
                animator_hide[6].start();
                sendCurrentSignal();
                break;
            case R.id.btn_forethigh_plus:
                // animator_hide[6].cancel();
                animator_hide[6].start();
                sendCurrentSignal();
                break;
            case R.id.btn_backthigh_minus:
                // animator_hide[7].cancel();
                animator_hide[7].start();
                sendCurrentSignal();
                break;
            case R.id.btn_backthigh_plus:
                //animator_hide[7].cancel();
                animator_hide[7].start();
                sendCurrentSignal();
                break;
            case R.id.btn_hips_minus:
                // animator_hide[8].cancel();
                animator_hide[8].start();
                sendCurrentSignal();
                break;
            case R.id.btn_hips_plus:
                // animator_hide[8].cancel();
                animator_hide[8].start();
                sendCurrentSignal();
                break;
            case R.id.btn_calfs_minus:
                //animator_hide[9].cancel();
                animator_hide[9].start();
                sendCurrentSignal();
                break;
            case R.id.btn_calfs_plus:
                //animator_hide[9].cancel();
                animator_hide[9].start();
                sendCurrentSignal();
                break;


            case R.id.imgbtn_all_minus:
                //animator_hide[10].cancel();
                animator_hide[10].start();
                sendCurrentSignal();
                sendTimer();
                break;
            case R.id.imgbtn_all_add:
                //animator_hide[10].cancel();
                animator_hide[10].start();
                sendCurrentSignal();
                sendTimer();
                break;

            case R.id.btn_time_all_minus:
                if (start_en) break;
                // animator_hide[11].cancel();
                animator_hide[11].start();
                sendTimer();

                break;
            case R.id.btn_time_all_add:
                if (start_en) break;
                // animator_hide[11].cancel();
                animator_hide[11].start();
                sendTimer();
                break;
            case R.id.btn_time_train_minus:
                if (start_en) break;
                // animator_hide[12].cancel();
                animator_hide[12].start();
                sendTimer();
                break;
            case R.id.btn_time_train_add:
                if (start_en) break;
                // animator_hide[12].cancel();
                animator_hide[12].start();
                sendTimer();
                break;
            case R.id.btn_time_rest_minus:
                if (start_en) break;
                // animator_hide[13].cancel();
                animator_hide[13].start();
                sendTimer();
                break;
            case R.id.btn_time_rest_add:
                if (start_en) break;
                //animator_hide[13].cancel();
                animator_hide[13].start();
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
                if (start_en) break;
                if (mTimerShow[0] <= 0) break;
                mTimerShow[0]--;
                btnControlerShow.setText(mTimerShow[0] + "");

                break;
            case R.id.btn_time_all_add:
                if (start_en) break;
                if (mTimerShow[0] >= 60) break;
                mTimerShow[0]++;
                btnControlerShow.setText(mTimerShow[0] + "");

                break;
            case R.id.btn_time_train_minus:
                if (start_en) break;
                if (mTimerShow[1] <= 0) break;
                mTimerShow[1]--;
                btnTrainShow.setText(mTimerShow[1] + "");

                break;
            case R.id.btn_time_train_add:
                if (start_en) break;
                if (mTimerShow[1] >= 60) break;
                mTimerShow[1]++;
                btnTrainShow.setText(mTimerShow[1] + "");

                break;
            case R.id.btn_time_rest_minus:
                if (start_en) break;
                if (mTimerShow[2] <= 0) break;
                mTimerShow[2]--;
                btnRestShow.setText(mTimerShow[2] + "");

                break;
            case R.id.btn_time_rest_add:
                if (start_en) break;
                if (mTimerShow[2] >= 70) break;
                mTimerShow[2]++;
                btnRestShow.setText(mTimerShow[2] + "");
                //progressBar.setProgress(mTimerShow[2]);

                break;
        }
    }

    void hideAllBtn() {
        btnArmPlus.setVisibility(View.INVISIBLE);
        btnArmMinus.setVisibility(View.INVISIBLE);

        btnShoulderMinus.setVisibility(View.INVISIBLE);
        btnShoulderPlus.setVisibility(View.INVISIBLE);

        btnChestMinus.setVisibility(View.INVISIBLE);
        btnChestPlus.setVisibility(View.INVISIBLE);

        btnAbdomenMinus.setVisibility(View.INVISIBLE);
        btnAbdomenPlus.setVisibility(View.INVISIBLE);

        btnBackMinus.setVisibility(View.INVISIBLE);
        btnBackPlus.setVisibility(View.INVISIBLE);

        btnWaistMinus.setVisibility(View.INVISIBLE);
        btnWaistPlus.setVisibility(View.INVISIBLE);

        btnForethighMinus.setVisibility(View.INVISIBLE);
        btnForethighPlus.setVisibility(View.INVISIBLE);

        btnBackthighMinus.setVisibility(View.INVISIBLE);
        btnBackthighPlus.setVisibility(View.INVISIBLE);

        btnHipsMinus.setVisibility(View.INVISIBLE);
        btnHipsPlus.setVisibility(View.INVISIBLE);

        btnCalfsMinus.setVisibility(View.INVISIBLE);
        btnCalfsPlus.setVisibility(View.INVISIBLE);

        imgbtnAllMinus.setVisibility(View.INVISIBLE);
        imgbtnAllAdd.setVisibility(View.INVISIBLE);

        btnTimeAllMinus.setVisibility(View.INVISIBLE);
        btnTimeAllAdd.setVisibility(View.INVISIBLE);

        btnTimeRestMinus.setVisibility(View.INVISIBLE);
        btnTimeRestAdd.setVisibility(View.INVISIBLE);

        btnTimeTrainMinus.setVisibility(View.INVISIBLE);
        btnTimeTrainAdd.setVisibility(View.INVISIBLE);
    }



}
