package com.mytian.lb;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mytian.lb.event.AnyEventType;
import com.mytian.lb.imp.EInitFragmentDate;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public abstract class AbsFragment extends Fragment implements EInitFragmentDate {

    protected final String TAG = AbsFragment.class.getSimpleName();

    public static final int RECODE_RELEASE = 4;//发布资讯返回代码

    public final static String KEY = "KEY";

    public NiftyDialogBuilder dialogBuilder;

    public LayoutInflater mInflater;

    public Context mContext;

    public abstract boolean onBackPressed();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public void onEvent(AnyEventType event) {
        //接收消息
    }

    @Override
    public void onDestroy() {
        dialogDismiss();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void dialogShow() {
        dialogDismiss();
        dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(R.layout.loading_view, getActivity()); // .setCustomView(View
        activityHandler.sendEmptyMessage(DIALOGSHOW);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = this.getContext();
        mInflater = inflater;
        View rootView = mInflater.inflate(getContentView(), container, false);
        ButterKnife.bind(this, rootView);
        EInit();
        return rootView;
    }

    public abstract int getContentView();

    @Override
    public void EInit() {

    }

    @Override
    public void EDestroy() {

    }

    @Override
    public void EResetInit() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EDestroy();
        ButterKnife.unbind(this);
    }

    public void dialogShow(String title) {
        dialogDismiss();
        LinearLayout convertView = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.loading_view, null);
        TextView dialog_confirm_content = (TextView) convertView.findViewById(R.id.dialog_confirm_content);
        dialog_confirm_content.setText(title);
        dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, getActivity()); // .setCustomView(View
        activityHandler.sendEmptyMessage(DIALOGSHOW);

    }

    public void dialogDismiss() {
        if (null != dialogBuilder && dialogBuilder.isShowing()) {
            activityHandler.sendEmptyMessage(DIALOGDISMISS);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    private final static int DIALOGSHOW = 1;
    private final static int DIALOGDISMISS = 0;

    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DIALOGSHOW:
                    dialogBuilder.show();
                    break;
                case DIALOGDISMISS:
                    dialogBuilder.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

}
