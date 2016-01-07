package com.mytian.lb.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.util.StringUtil;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.mytian.lb.adapter.MainViewPagerAdapter;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.event.PushStateEventType;
import com.mytian.lb.fragment.AgreementFragment;
import com.mytian.lb.fragment.HabitFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

import butterknife.Bind;

public class UserDetailActivity extends AbsActivity {

    public final static String USER = "USER";
    public final static int AGREEMENT = 0;
    public final static int HABIT = AGREEMENT + 1;

    @Bind(R.id.user_icon)
    RoundedImageView userIcon;
    @Bind(R.id.user_remark)
    TextView userRemark;
    @Bind(R.id.user_state)
    TextView userState;
    @Bind(R.id.user_phone)
    TextView userPhone;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.viewpager_tab)
    SmartTabLayout viewpagerTab;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private FollowUser cureentParent;

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
        String head = "";
        if (null != userInfo) {
            cureentParent = userInfo;
            String remark = cureentParent.getAlias();
            String name = cureentParent.getAlias();
            head = cureentParent.getHead_thumb();
            String phone = cureentParent.getPhone();
            if(StringUtil.isBlank(remark)){
                userRemark.setText(name);
                userName.setVisibility(View.GONE);
            }else {
                userName.setVisibility(View.VISIBLE);
                userRemark.setText(remark);
                userName.setText("昵称：" + name);
            }
            userPhone.setText(phone);
            setState(userInfo.getIs_online());
        }
        Glide.with(mContext).load(head)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.default_head)
                .centerCrop().into(userIcon);
    }

    private void setState(String isOnline){
        String state = "";
        if (FollowUser.OFFLINE.equals(isOnline)) {
            state = "离线";
        }
        userState.setText(state);
    }

    /**
     * 线上状态更新
     *
     * @param event
     */
    public void onEvent(PushStateEventType event) {
        String babyUid = event.babyUid;
        if (babyUid.equals(cureentParent.getUid())) {
            setState(event.is_online);
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
        final EditText name = (EditText) convertView.findViewById(R.id.desc_et);
        TextView change_ok = (TextView) convertView.findViewById(R.id.change_ok);
        name.setHint(userRemark.getText());
        change_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDismiss();
                userRemark.setText(name.getText());
            }
        });
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(true) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, this); // .setCustomView(View
        dialogBuilder.show();
    }

}
