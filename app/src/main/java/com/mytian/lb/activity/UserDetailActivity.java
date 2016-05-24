package com.mytian.lb.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.adapter.MainViewPagerAdapter;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.event.AgreementStateEventType;
import com.mytian.lb.event.PushStateEventType;
import com.mytian.lb.event.UpdateBabyAliasEventType;
import com.mytian.lb.fragment.AgreementFragment;
import com.mytian.lb.fragment.HabitFragment;
import com.mytian.lb.helper.SharedPreferencesHelper;
import com.mytian.lb.manager.UserManager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;

/**
 * 用户详情界面
 */
public class UserDetailActivity extends AbsActivity {

    public final static String USER = "USER";
    public final static int AGREEMENT = 0;
    public final static int HABIT = AGREEMENT + 1;

    @BindView(R.id.user_icon)
    RoundedImageView userIcon;
    @BindView(R.id.user_remark)
    TextView userRemark;
    @BindView(R.id.user_state)
    TextView userState;
    @BindView(R.id.user_phone)
    TextView userPhone;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.viewpager_tab)
    SmartTabLayout viewpagerTab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindColor(R.color.pink)
    int pinkColor;
    @BindColor(R.color.textcolor_match)
    int textMatchColor;

    private FollowUser cureentParent;
    private String remarkName;
    private String headURL = "";


    @Override
    public void initActionBar() {
        setToolbarRightStrID(R.string.remark);
        setToolbarRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemarkView();
            }
        });
    }

    private void setUserInfo(FollowUser userInfo) {
        if (null != userInfo) {
            cureentParent = userInfo;
            setUserRemark();
            headURL = cureentParent.getHead_thumb();
            String phone = cureentParent.getPhone();
            userPhone.setText(phone);
            setUserState(userInfo.getIs_online(), userInfo.getAppointing(), userInfo.getAppointer());
        }
        Glide.with(mContext).load(headURL)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.default_head)
                .centerCrop().into(userIcon);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Object> imgs = new ArrayList<>();
                imgs.add(headURL);
                String remark = userRemark.getText().toString();
                ShowPictureActivity.toShowPicture(UserDetailActivity.this, imgs, remark);
            }
        });
    }

    private void setUserRemark() {
        String remark = cureentParent.getBaby_alias();
        String name = cureentParent.getAlias();
        if (StringUtil.isBlank(remark)) {
            userRemark.setText(name);
            userName.setVisibility(View.GONE);
        } else {
            userName.setVisibility(View.VISIBLE);
            userRemark.setText(remark);
            userName.setText(getString(R.string.hint_name) + "：" + name);
        }
    }

    /**
     * 设置用户状态
     *
     * @param isOnline
     */
    private void setUserState(String isOnline, String appointing, String appointer) {
        if (FollowUser.OFFLINE.equals(isOnline)) {
            String state = getString(R.string.offline);
            userState.setTextColor(textMatchColor);
            userState.setText(state);
        } else {
            setIsAgreementState(appointing, appointer);
        }
    }

    /**
     * 设置约定状态
     *
     * @param appointing
     * @param appointer
     */
    private void setIsAgreementState(String appointing, String appointer) {
        String state = "";
        String userUid = App.getInstance().getUserResult().getParent().getUid();
        String sharedKey = AgreementFragment.SHAREDPREFERENCES_TIME + cureentParent.getUid() + userUid;
        String content = SharedPreferencesHelper.getString(mContext, sharedKey, "");
        if (StringUtil.isNotBlank(content) || StringUtil.isBlank(appointer)) {
            userState.setVisibility(View.GONE);
            return;
        }
        userState.setVisibility(View.VISIBLE);
        if (AgreementStateEventType.AGREEMENT_ING.equals(appointing)) {
            state = String.format(mContext.getString(R.string.and_agreement), appointer);
            userState.setTextColor(pinkColor);
        }
        userState.setText(state);
    }

    /**
     * 约定状态更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AgreementStateEventType event) {
        String babyUid = event.babyUid;
        String time = event.appoint_time;
        String appoint_time = App.getInstance().getAppoint_time();
        boolean isTime = StringUtil.isNotBlank(appoint_time) && time.equals(appoint_time);
        if (cureentParent.getUid().equals(babyUid) && isTime) {
            String appointing = event.appointing;
            cureentParent.setAppointing(appointing);
            cureentParent.setAppointer(event.appointer);
            setUserState(cureentParent.getIs_online(), appointing, event.appointer);
        }
    }

    /**
     * 线上状态更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PushStateEventType event) {
        String babyUid = event.babyUid;
        if (babyUid.equals(cureentParent.getUid())) {
            cureentParent.setIs_online(event.is_online);
            setUserState(event.is_online, cureentParent.getAppointing(), cureentParent.getAppointer());
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_user_detail;
    }

    @Override
    public void EInit() {
        cureentParent = (FollowUser) getIntent().getSerializableExtra(USER);
        setUserInfo(cureentParent);
        initViewPager();
    }

    // 初始化资源
    private void initViewPager() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, cureentParent);
        ArrayList fragments = new ArrayList<>();
        AgreementFragment agreementFragment = new AgreementFragment();
        HabitFragment habitFragment = new HabitFragment();
        agreementFragment.setArguments(bundle);
        habitFragment.setArguments(bundle);
        fragments.add(agreementFragment);
        fragments.add(habitFragment);
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewpagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                LinearLayout custom_ly = (LinearLayout) mInflater.inflate(R.layout.tab_user_icon, container, false);
                switch (position) {
                    case AGREEMENT:
                        ((TextView) custom_ly.findViewById(R.id.menu_title)).setText(R.string.agreement);
                        break;
                    case HABIT:
                        ((TextView) custom_ly.findViewById(R.id.menu_title)).setText(R.string.habit);
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return custom_ly;
            }
        });
        viewpagerTab.setViewPager(viewPager);
    }

    private void showRemarkView() {
        dialogDismiss();
        LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.dialog_remark, null);
        final EditText remark = (EditText) convertView.findViewById(R.id.desc_et);
        Button change_ok = (Button) convertView.findViewById(R.id.change_ok);
        String remarkStr = cureentParent.getBaby_alias();
        remark.setHint(R.string.remark);
        remark.setText(remarkStr);
        if (StringUtil.isNotBlank(remarkStr)) {
            remark.setSelection(remarkStr.length());
        }
        change_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remarkName = remark.getText().toString();
                dialogShow(R.string.update_remark);
                UserManager manager = new UserManager();
                manager.updateRemarkName(mContext, cureentParent.getUid(), remarkName, mHandler, UPDATE_REMARKNAME);
            }
        });
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(true) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, this); // .setCustomView(View
        dialogBuilder.show();
    }

    private static final int UPDATE_REMARKNAME = 0x01;//更新备注

    @Override
    public void handlerCallBack(Message msg) {
        super.handlerCallBack(msg);
        int what = msg.what;
        switch (what) {
            case UPDATE_REMARKNAME:
                loadData((CommonResponse) msg.obj);
                break;
            default:
                break;
        }
    }

    /**
     * 数据返回
     *
     * @param resposne
     */
    private void loadData(CommonResponse resposne) {
        dialogDismiss();
        CommonUtil.showToast(resposne.getMsg());
        if (resposne.isSuccess()) {
            cureentParent.setBaby_alias(remarkName);
            setUserRemark();
            EventBus.getDefault().post(new UpdateBabyAliasEventType(cureentParent.getUid(), remarkName));
        }
    }

}
