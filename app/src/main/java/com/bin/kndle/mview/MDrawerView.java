package com.bin.kndle.mview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bin.kndle.R;


public class MDrawerView extends LinearLayout {

    public static final int PLATE = 0;
    public static final int MSG = PLATE + 1;
    public static final int ORDER = MSG + 1;
    public static final int COUPON = ORDER + 1;
    public static final int SHARE = COUPON + 1;
    public static final int SETTING = SHARE + 1;
    public static final int WALLET = SETTING + 1;

    private LayoutInflater mInflater;

    private LinearLayout mPlate, mWallet, mMsg, mOrder, mCoupon, mShare, mSetting;

    private TextView mWalletTv, mPlateTv, mMsgTv, mMsTitlegTv, mOrderTv, mCouponTv, mShareTv, mSettingTv;

    private CircleNetworkImageView mHead;

    private TextView mName;

    private Context mContext;

    public interface OnButtonClick {
        void onClick(int type);
    }

    private OnButtonClick onButtonClick;

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }


    public MDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.layout_drawer, this);

        mHead = (CircleNetworkImageView) findViewById(R.id.menu_user_icon);
        mName = (TextView) findViewById(R.id.menu_user_name);

        mPlate = (LinearLayout) findViewById(R.id.me_plate);
        mWallet = (LinearLayout) findViewById(R.id.me_wallet);
        mMsg = (LinearLayout) findViewById(R.id.me_msg);
        mOrder = (LinearLayout) findViewById(R.id.me_order);
        mCoupon = (LinearLayout) findViewById(R.id.me_coupon);
        mShare = (LinearLayout) findViewById(R.id.me_share);
        mSetting = (LinearLayout) findViewById(R.id.me_setting);

        mPlateTv = (TextView) findViewById(R.id.me_plate_value);
        mWalletTv = (TextView) findViewById(R.id.me_wallet_value);
        mMsTitlegTv = (TextView) findViewById(R.id.me_msg_title);
        mMsgTv = (TextView) findViewById(R.id.me_msg_value);
        mOrderTv = (TextView) findViewById(R.id.me_order_value);
        mCouponTv = (TextView) findViewById(R.id.me_coupon_value);
        mShareTv = (TextView) findViewById(R.id.me_share_value);
        initOnClick();
    }

    public void setUserInfo() {

    }

    private void initOnClick() {
        mHead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mPlate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != onButtonClick) {
                    onButtonClick.onClick(PLATE);
                }
            }
        });
        mMsg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != onButtonClick) {
                    onButtonClick.onClick(MSG);
                }
            }
        });
        mOrder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != onButtonClick) {
                    onButtonClick.onClick(ORDER);
                }

            }
        });
        mCoupon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != onButtonClick) {
                    onButtonClick.onClick(COUPON);
                }
            }
        });
        mShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != onButtonClick) {
                    onButtonClick.onClick(SHARE);
                }
            }
        });
        mSetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != onButtonClick) {
                    onButtonClick.onClick(SETTING);
                }
            }
        });
        mWallet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != onButtonClick) {
                    onButtonClick.onClick(WALLET);
                }
            }
        });
    }

}
