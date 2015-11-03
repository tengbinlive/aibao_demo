package com.mytian.lb;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mytian.lb.event.AnyEventType;
import com.mytian.lb.imp.EInitDate;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract class AbsActivity extends SwipeBackActivity implements EInitDate {

    private final static String TAG = AbsActivity.class.getSimpleName();
    public final static String KEY = "KEY";
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
    private RelativeLayout toolbar_intermediate_btn;
    private RelativeLayout toolbar_right_btn;
    private TextView toolbar_intermediate_tv;
    private ImageButton toolbar_intermediate_icon;
    private TextView toolbar_right_tv;

    private View mainLayout;

    public NiftyDialogBuilder dialogBuilder;

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

    public View getMainLayout() {
        return mainLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        App.getInstance().addActivity(this);
        int colos = getIntent().getIntExtra(STATUSBAR_COLOS, 0);
        setStatusBar(colos);
        mInflater = LayoutInflater.from(this);
        mainLayout = mInflater.inflate(getContentView(), null);
        setContentView(mainLayout);
        initAbsActionBar();
        if (null != viewTitleBar) {
            initActionBar();
        }
        ButterKnife.bind(this);

        EInit();
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
        viewTitleBar = (LinearLayout) findViewById(R.id.toolbar);
        if (null == viewTitleBar) {
            return;
        }

        menuLeft = (TextView) viewTitleBar.findViewById(R.id.toolbar_left_btn);
        toolbar_right_tv = (TextView) viewTitleBar.findViewById(R.id.toolbar_right_tv);
        toolbar_intermediate_tv = (TextView) viewTitleBar.findViewById(R.id.toolbar_intermediate_tv);
        toolbar_intermediate_btn = (RelativeLayout) viewTitleBar.findViewById(R.id.toolbar_intermediate_btn);
        toolbar_intermediate_icon = (ImageButton) viewTitleBar.findViewById(R.id.toolbar_intermediate_icon);
        toolbar_right_btn = (RelativeLayout) viewTitleBar.findViewById(R.id.toolbar_right_btn);

        menuLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    public void initAbsActionBar(ViewGroup _viewTitleBar) {
        viewTitleBar = _viewTitleBar;
        if (null == viewTitleBar) {
            return;
        }

        menuLeft = (TextView) viewTitleBar.findViewById(R.id.toolbar_left_btn);
        toolbar_right_tv = (TextView) viewTitleBar.findViewById(R.id.toolbar_right_tv);
        toolbar_intermediate_tv = (TextView) viewTitleBar.findViewById(R.id.toolbar_intermediate_tv);
        toolbar_intermediate_btn = (RelativeLayout) viewTitleBar.findViewById(R.id.toolbar_intermediate_btn);
        toolbar_intermediate_icon = (ImageButton) viewTitleBar.findViewById(R.id.toolbar_intermediate_icon);
        toolbar_right_btn = (RelativeLayout) viewTitleBar.findViewById(R.id.toolbar_right_btn);

        menuLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    /**
     * 设置actionbar 左部
     *
     * @param
     */
    public void setToolbarLeftVisbility(int visbility) {
        menuLeft.setVisibility(visbility);
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

    public void setToolbarLeftAnchor(int iconid, int anchor) {
        Drawable drawable = getResources().getDrawable(iconid);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (anchor == TOP) {
            menuLeft.setCompoundDrawables(null, drawable, null, null);
        } else if (anchor == BOTTOM) {
            menuLeft.setCompoundDrawables(null, null, null, drawable);
        } else if (anchor == LEFT) {
            menuLeft.setCompoundDrawables(drawable, null, null, null);
        } else if (anchor == RIGHT) {
            menuLeft.setCompoundDrawables(null, null, drawable, null);
        }
    }

    public void setToolbarLeft(String text) {
        menuLeft.setText(text);
    }

    public void setToolbarLeftStrID(int rId) {
        menuLeft.setText(rId);
    }

    public void setToolbarLeft(int iconid, String text) {
        setToolbarLeft(iconid);
        setToolbarLeft(text);
    }

    public void setToolbarLeft(int iconid, int rId) {
        setToolbarLeft(iconid);
        setToolbarLeftStrID(rId);
    }

    public void setToolbarLeftOnClick(View.OnClickListener onClick) {
        if (onClick != null)
            menuLeft.setOnClickListener(onClick);
    }

    /**
     * 设置actionbar 右部
     *
     * @param
     */
    public void setToolbarRightVisbility(int visbilityLayout, int visbilityTv) {
        toolbar_right_btn.setVisibility(visbilityLayout);
        toolbar_right_tv.setVisibility(visbilityTv);
    }

    public void setToolbarRight(String text) {
        toolbar_right_tv.setText(text);
    }

    /**
     * 设置标题
     *
     * @param rId
     */
    public void setToolbarRightStrID(int rId) {
        toolbar_right_tv.setText(rId);
    }

    public void setToolbarRight(int iconid) {
        Drawable drawable = null;
        if (iconid > 0) {
            drawable = getResources().getDrawable(iconid);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        toolbar_right_tv.setCompoundDrawables(drawable, null, null, null);
    }

    public void setToolbarRightAnchor(int iconid, int anchor) {
        Drawable drawable = getResources().getDrawable(iconid);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (anchor == TOP) {
            toolbar_right_tv.setCompoundDrawables(null, drawable, null, null);
        } else if (anchor == BOTTOM) {
            toolbar_right_tv.setCompoundDrawables(null, null, null, drawable);
        } else if (anchor == LEFT) {
            toolbar_right_tv.setCompoundDrawables(drawable, null, null, null);
        } else if (anchor == RIGHT) {
            toolbar_right_tv.setCompoundDrawables(null, null, drawable, null);
        }
    }

    public void setToolbarRight(int iconid, String text) {
        setToolbarRight(iconid);
        setToolbarRight(text);
    }

    public void setToolbarRight(int iconid, int rId) {
        setToolbarRight(iconid);
        setToolbarRightStrID(rId);
    }

    public void setToolbarRightOnClick(View.OnClickListener onClick) {
        if (onClick != null)
            toolbar_right_btn.setOnClickListener(onClick);
    }

    public void setToolbarRightClickable(boolean isClickable) {
        toolbar_right_btn.setClickable(isClickable);
    }

    /**
     * 设置actionbar 中部
     *
     * @param
     */
    public void setToolbarIntermediateVisbility(int visbilityLayout, int visbilityTv) {
        toolbar_intermediate_btn.setVisibility(visbilityLayout);
        toolbar_intermediate_tv.setVisibility(visbilityTv);
    }

    public void setToolbarIntermediate(String text) {
        toolbar_intermediate_tv.setText(text);
    }

    /**
     * 设置标题
     *
     * @param rId
     */
    public void setToolbarIntermediateStrID(int rId) {
        toolbar_intermediate_tv.setText(rId);
    }

    public void setToolbarIntermediate(int iconid) {
        Drawable drawable = null;
        if (iconid > 0) {
            drawable = getResources().getDrawable(iconid);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        toolbar_intermediate_tv.setCompoundDrawables(drawable, null, null, null);
    }

    public void setToolbarIntermediateIcon(int iconid, int visibility) {
        toolbar_intermediate_icon.setVisibility(visibility);
        toolbar_intermediate_icon.setImageResource(iconid);
    }

    public void setToolbarIntermediateAnchor(int iconid, int anchor) {
        Drawable drawable = getResources().getDrawable(iconid);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (anchor == TOP) {
            toolbar_intermediate_tv.setCompoundDrawables(null, drawable, null, null);
        } else if (anchor == BOTTOM) {
            toolbar_intermediate_tv.setCompoundDrawables(null, null, null, drawable);
        } else if (anchor == LEFT) {
            toolbar_intermediate_tv.setCompoundDrawables(drawable, null, null, null);
        } else if (anchor == RIGHT) {
            toolbar_intermediate_tv.setCompoundDrawables(null, null, drawable, null);
        }
    }

    public void setToolbarIntermediate(int iconid, String text) {
        setToolbarIntermediate(iconid);
        setToolbarIntermediate(text);
    }

    public void setToolbarIntermediate(int iconid, int rid) {
        setToolbarIntermediate(iconid);
        setToolbarIntermediateStrID(rid);
    }

    public void setToolbarIntermediateOnClick(View.OnClickListener onClick) {
        if (onClick != null)
            toolbar_intermediate_btn.setOnClickListener(onClick);
    }

    public abstract int getContentView();

    @Override
    public void onDestroy() {
        activityFinish = true;
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EDestroy();
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
    }

    @Override
    public void finish() {
        dialogDismiss();
        App.getInstance().deleteActivity(this);
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
        if (isBackAnim)
            overridePendingTransition(0, R.anim.push_translate_out_left);
    }


    private final  static int DIALOGSHOW = 1;
    private final  static int DIALOGDISMISS = 0;

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
