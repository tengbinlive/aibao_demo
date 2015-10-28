package com.bin.kndle.helper;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.bin.kndle.activity.RegisterActivity;
import com.core.util.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

public class SMSContentObserver extends ContentObserver {
    private Context mContext;
    private Handler mHandler;
    private Timer timer;
    private boolean isStart = false;

    public SMSContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
        timer = new Timer();
    }

    @Override
    public void onChange(boolean selfChange) {
        if (!isStart) {
            isStart = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Uri uri = Uri.parse("content://sms/");
                    Cursor c = mContext.getContentResolver().query(uri, null, null, null, "date desc");
                    if (c != null) {
                        c.moveToPosition(0);
                        String smsBody = c.getString(c.getColumnIndex("body"));
                        setSms(smsBody);
                        c.close();
                    }
                }
            }, 500);
        }

    }

    private final static String SMS_MARK = "Test";
    private final static String SMS_MARK_START = "您的验证码是";
    private final static String SMS_MARK_END = "，";

    private void setSms(String smsBody) {
        if (StringUtil.isNotBlank(smsBody)) {
            int index = smsBody.indexOf(SMS_MARK);
            if (index != -1) {
                index = smsBody.indexOf(SMS_MARK_START);
                int end = smsBody.indexOf(SMS_MARK_END);
                int length = SMS_MARK_START.length();
                String securityCde = smsBody.substring(index + length, end);
                Message message = new Message();
                message.what = RegisterActivity.LOAD_AUTHCODE_FILL;
                message.obj = securityCde;
                mHandler.sendMessage(message);
            }
        }
    }

}