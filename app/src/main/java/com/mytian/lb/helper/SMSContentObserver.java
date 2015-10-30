package com.mytian.lb.helper;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.AlertView.Style;
import com.bigkoo.alertview.OnItemClickListener;
import com.mytian.lb.activity.RegisterActivity;
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

    Cursor c;
    @Override
    public void onChange(boolean selfChange) {
        if (!isStart) {
            isStart = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Uri uri = Uri.parse("content://sms/");
                         c = mContext.getContentResolver().query(uri, null, null, null, "date desc");
                        if (c != null) {
                            c.moveToPosition(0);
                            String smsBody = c.getString(c.getColumnIndex("body"));
                            setSms(smsBody);
                            c.close();
                        }
                    }catch (Exception e){
                        handler.sendEmptyMessage(SMS);
                    }finally {
                        if (null!=c) {
                            c.close();
                        }
                    }

                }
            }, 500);
        }

    }

    private final static int SMS = 1;

    private AlertView alertView;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SMS:
                    if(alertView==null){
                        alertView = new AlertView("温馨提示","请您允许自动获取验证码，" +
                                "\n或是填写验证，谢谢."
                                ,null,new String[]{"确定"},null,mContext, Style.Alert,
                                new OnItemClickListener(){
                                    @Override
                                    public void onItemClick(Object o, int i) {
                                        alertView.dismiss();
                                    }
                                });
                    }
                    alertView.show();
                    break;
            }
        }
    };


    private final static String SMS_MARK = "麦田映像";
    private final static String SMS_MARK_START = "您的验证码是";
    private final static String SMS_MARK_END = "，请";

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