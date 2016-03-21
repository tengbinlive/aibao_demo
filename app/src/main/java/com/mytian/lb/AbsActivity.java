package com.mytian.lb;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.debug.ViewServer;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.mytian.lb.event.AnyEventType;
import com.mytian.lb.imp.EInitDate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract class AbsActivity extends SwipeBackActivity implements EInitDate {

    private final static String TAG = AbsActivity.class.getSimpleName();
    public final static int TOP = 0;
    public final static int BOTTOM = TOP + 1;
    public final static int LEFT = BOTTOM + 1;
    public final static int RIGHT = LEFT + 1;

    public final static String STATUSBAR_COLOS = "STATUSBAR_COLOS";

    public SwipeBackLayout mSwipeBackLayout;
    public boolean activityFinish;
    public LayoutInflater mInflater;

    //actionbar
    private ViewGroup viewTitleBar;
    private TextView menuLeft;
    private TextView toolbar_intermediate_tv;
    private TextView toolbar_right_tv;

    public NiftyDialogBuilder dialogBuilder;

    public Context mContext;

    private boolean isBackAnim = false;

    public boolean isBackAnim() {
        return isBackAnim;
    }

    public void setIsBackAnim(boolean isBackAnim) {
        this.isBackAnim = isBackAnim;
    }

    public ViewGroup getToolbar() {
        return viewTitleBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        App.getInstance().activityManager.pushActivity(this);
        int colos = getIntent().getIntExtra(STATUSBAR_COLOS, 0);
        setStatusBar(colos);
        mInflater = LayoutInflater.from(this);
        setContentView(getContentView());
        getWindow().setBackgroundDrawable(null);
        ButterKnife.bind(this);
        initAbsActionBar();
        if (null != viewTitleBar) {
            initActionBar();
        }
        EInit();
        if (Constant.DEBUG) {
            ViewServer.get(this).addWindow(this);
        }
    }

    /**
     * 设置statusbar全透明
     */
    private void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void initActionBar() {
    }

    private void initAbsActionBar() {
        viewTitleBar = (ViewGroup) findViewById(R.id.toolbar);
        if (null == viewTitleBar) {
            return;
        }

        menuLeft = (TextView) viewTitleBar.findViewById(R.id.toolbar_left_btn);
        toolbar_right_tv = (TextView) viewTitleBar.findViewById(R.id.toolbar_right_tv);
        toolbar_intermediate_tv = (TextView) viewTitleBar.findViewById(R.id.toolbar_intermediate_tv);

        menuLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }


    public void setToolbarLeft(int iconid) {
        Drawable drawable = null;
        if (iconid > 0) {
            drawable = getResources().getDrawable(iconid);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        menuLeft.setCompoundDrawables(drawable, null, null, null);
    }

    public void setToolbarLeftStrID(int rId) {
        menuLeft.setText(rId);
    }

    public void setToolbarLeftOnClick(View.OnClickListener onClick) {
        if (onClick != null)
            menuLeft.setOnClickListener(onClick);
    }

    /**
     * 设置actionbar 右部
     *
     * @param visbilityTv 是否显示右边描述文字
     */
    public void setToolbarRightVisbility(int visbilityTv) {
        toolbar_right_tv.setVisibility(visbilityTv);
    }

    public void setToolbarRight(int iconid) {
        Drawable drawable = null;
        if (iconid > 0) {
            drawable = getResources().getDrawable(iconid);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        toolbar_right_tv.setCompoundDrawables(null, null, drawable, null);
    }

    public void setToolbarRightOnClick(View.OnClickListener onClick) {
        if (onClick != null)
            toolbar_right_tv.setOnClickListener(onClick);
    }

    /**
     * 设置actionbar 右部标题
     *
     * @param rId 文字资源id
     */
    public void setToolbarRightStrID(int rId) {
        toolbar_right_tv.setText(rId);
    }

    /**
     * 设置标题
     *
     * @param rId 文字资源id
     */
    public void setToolbarIntermediateStrID(int rId) {
        toolbar_intermediate_tv.setText(rId);
    }

    /**
     * 设置标题
     *
     * @param value 字符串
     */
    public void setToolbarIntermediateStr(String value) {
        toolbar_intermediate_tv.setText(value);
    }

    public abstract int getContentView();

    @Override
    public void onDestroy() {
        activityFinish = true;
        super.onDestroy();
        EDestroy();
        App.getInstance().activityManager.popActivity(this);
        if (Constant.DEBUG) {
            ViewServer.get(this).removeWindow(this);
        }
    }

    @Override
    public void onPause() {
        activityFinish = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        activityFinish = false;
        super.onResume();
        App.getInstance().setCurrentActivity(this);
        if (Constant.DEBUG) {
            ViewServer.get(this).setFocusedWindow(this);
        }
    }

    @Override
    public void finish() {
        dialogDismiss();
        if (isBackAnim) {
            overridePendingTransition(0, R.anim.push_translate_out_left);
        }
        activityFinish = true;
        super.finish();
    }

    @Override
    public void EInit() {
        initSwipeBack();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(AnyEventType event) {
        //接收消息
    }

    public void dialogShow() {
        dialogDismiss();
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(R.layout.loading_view, this); // .setCustomView(View
        activityHandler.sendEmptyMessage(DIALOGSHOW);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void dialogShow(String title) {
        dialogDismiss();
        LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.loading_view, null);
        TextView dialog_confirm_content = (TextView) convertView.findViewById(R.id.dialog_confirm_content);
        dialog_confirm_content.setText(title);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, this); // .setCustomView(View
        activityHandler.sendEmptyMessage(DIALOGSHOW);

    }

    public void dialogShow(int title) {
        dialogDismiss();
        LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.loading_view, null);
        TextView dialog_confirm_content = (TextView) convertView.findViewById(R.id.dialog_confirm_content);
        dialog_confirm_content.setText(title);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, this); // .setCustomView(View
        dialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    return true;
                }
                return false;
            }
        });
        activityHandler.sendEmptyMessage(DIALOGSHOW);

    }

    public void dialogShow(int title, DialogInterface.OnCancelListener listener) {
        dialogDismiss();
        LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.loading_view, null);
        TextView dialog_confirm_content = (TextView) convertView.findViewById(R.id.dialog_confirm_content);
        dialog_confirm_content.setText(title);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        if (null != listener) {
            dialogBuilder.setOnCancelListener(listener);
        }
        dialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    return true;
                }
                return false;
            }
        });
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(false) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, this); // .setCustomView(View
        activityHandler.sendEmptyMessage(DIALOGSHOW);

    }

    public void dialogDismiss() {
        if (null != dialogBuilder && dialogBuilder.isShowing()) {
            activityHandler.sendEmptyMessage(DIALOGDISMISS);
        }
    }

    /**
     * 界面滑动
     */
    private void initSwipeBack() {
        mSwipeBackLayout = getSwipeBackLayout();
        // 滑动监听方向
        if (enableSwipe()) {
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
            mSwipeBackLayout.setEnableGesture(true);
        } else mSwipeBackLayout.setEnableGesture(false);
    }

    protected boolean enableSwipe() {
        if (Build.VERSION.SDK_INT >= 11) return true;
        return false;
    }

    @TargetApi(17)
    public boolean isFinished() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyed() || isFinishing() || activityFinish;
        } else {
            return isFinishing() || activityFinish;
        }
    }

    @Override
    public void EDestroy() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
