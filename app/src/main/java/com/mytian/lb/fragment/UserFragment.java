package com.mytian.lb.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.DateUtil;
import com.core.util.StringUtil;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.R;
import com.mytian.lb.activity.AddFollowActivity;
import com.mytian.lb.adapter.UserAdapter;
import com.mytian.lb.bean.follow.FollowListResult;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.bean.user.UpdateParentParam;
import com.mytian.lb.enums.WomanOrManEnum;
import com.mytian.lb.event.PushUserEventType;
import com.mytian.lb.event.SettingEventType;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.helper.AnimatorUtils;
import com.mytian.lb.manager.FollowManager;
import com.mytian.lb.manager.UserManager;
import com.mytian.lb.mview.ClipRevealFrame;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.OnClick;

public class UserFragment extends AbsFragment implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.name_value)
    EditText nameValue;
    @Bind(R.id.phone_value)
    TextView phoneValue;
    @Bind(R.id.birthday_value)
    TextView birthdayValue;
    @Bind(R.id.integral_value)
    TextView integralValue;
    @Bind(R.id.layout_setting)
    ClipRevealFrame layoutSetting;
    @Bind(R.id.change_bt)
    Button change_bt;
    @Bind(R.id.woman_bt)
    Button woman_bt;
    @Bind(R.id.man_bt)
    Button man_bt;
    @Bind(R.id.gender_layout)
    RelativeLayout gender_layout;

    @BindColor(R.color.theme)
    int accentColor;
    private Calendar birthdayDate;

    private static boolean isSettingShow;

    @Bind(R.id.listview)
    PullToRefreshListView listview;
    @Bind(R.id.ll_listEmpty)
    LinearLayout llListEmpty;

    private ListView mActualListView;
    private UserAdapter mAdapter;

    private ArrayList<FollowUser> arrayList;
    private FollowManager manager = new FollowManager();
    private int currentPager = 1;
    private View settingAchor;

    //user
    private static boolean isOpenUser;//只执行一次动画
    @Bind(R.id.user_phone)
    TextView user_phone;
    @Bind(R.id.user_icon)
    RoundedImageView user_icon;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.layout_user)
    LinearLayout layout_user;
    @BindDimen(R.dimen.actionbar_user_height)
    float EDITEXT_OFFER;
    private final OvershootInterpolator mInterpolator = new OvershootInterpolator();

    private int user_gender = -1;

    private void initListView() {

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getListData(INIT_LIST);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getListData(LOAD_DATA);
            }
        });

        View headView = mInflater.inflate(R.layout.layout_user_new_messge, null);

        mActualListView = listview.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(mActualListView);

        mAdapter = new UserAdapter((AbsActivity) getActivity(), arrayList);

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);

        animationAdapter.setAbsListView(mActualListView);

        mActualListView.setAdapter(animationAdapter);

        mActualListView.addHeaderView(headView);

        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddFollowActivity();
            }
        });

    }

    private void getListData(int state) {
        if (state == INIT_LIST) {
            currentPager = 1;
            listview.setMode(PullToRefreshBase.Mode.BOTH);
            arrayList = null;
        }
        manager.followList(getActivity(), "" + currentPager, "1", activityHandler, state);
    }

    private void toAddFollowActivity() {
        Intent intent = new Intent(getActivity(), AddFollowActivity.class);
        startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_user;
    }

    @Override
    public void EInit() {
        isSettingShow = false;
        isOpenUser = false;
        birthdayDate = Calendar.getInstance();
        initListView();
        setUserInfo();
        getListData(INIT_LIST);
    }

    private void setUserInfo() {
        String name = App.getInstance().userResult.getParent().getAlias();
        boolean isName = StringUtil.isBlank(name);
        if (isName) {
            nameValue.setHint("请输入您的昵称");
        } else {
            nameValue.setHint(name);
        }
        name = !isName ? name : "你猜.";
        String phone = App.getInstance().userResult.getParent().getPhone();
        phone = StringUtil.isNotBlank(phone) ? phone : "...";
        String head = App.getInstance().userResult.getParent().getHeadThumb();
        user_name.setText(name);
        user_phone.setText(phone);
        phoneValue.setText(phone);
        long bir = App.getInstance().userResult.getParent().getBirthday();
        if (bir > 0) {
            String birthday = DateUtil.ConverToString(bir, DateUtil.YYYY_MM_DD);
            birthdayValue.setHint(birthday);
        } else {
            birthdayValue.setHint("选下咯");
        }
        user_icon.setVisibility(View.VISIBLE);
        Glide.with(App.getInstance()).load(R.mipmap.head_user_woman).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().placeholder(R.mipmap.default_head).into(user_icon);
    }

    public void onEvent(SettingEventType event) {
        settingAchor = event.mView;
        toggleShowSetting(event.mView);
    }

    public void onEvent(PushUserEventType event) {
        if (event == null) {
            return;
        }
        if (mAdapter == null) {
            return;
        }
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        int size = arrayList.size();
        if (size > 0) {
            arrayList.add(0, event.user);
        } else {
            arrayList.add(event.user);
        }
        mAdapter.refresh(arrayList);
        setEmpty();
    }

    @OnClick(R.id.exit_bt)
    void exitAccount() {
        App.getInstance().changeAccount(true);
    }

    @OnClick(R.id.change_bt)
    void onChangeInfo() {
        String name = nameValue.getText().toString();
        String nameHint = nameValue.getHint().toString();
        String birthday = birthdayValue.getText().toString();
        String birthdayHint = birthdayValue.getHint().toString();
        if (StringUtil.isBlank(name) && StringUtil.isBlank(nameHint)) {
            AnimationHelper.getInstance().viewAnimationQuiver(nameValue);
            return;
        }
        if (StringUtil.isBlank(birthday) && StringUtil.isBlank(birthdayHint)) {
            AnimationHelper.getInstance().viewAnimationQuiver(birthdayValue);
            return;
        }

        int sex = -1;
        if (woman_bt.getVisibility() == View.VISIBLE && man_bt.getVisibility() == View.VISIBLE) {
            sex = -1;
        } else if (woman_bt.getVisibility() == View.VISIBLE) {
            sex = WomanOrManEnum.WOMAN.getCode();
        } else if (man_bt.getVisibility() == View.VISIBLE) {
            sex = WomanOrManEnum.MAN.getCode();
        }

        if (sex == -1) {
            CommonUtil.showToast("暂不在泰国上线.");
            AnimationHelper.getInstance().viewAnimationQuiver(gender_layout);
            return;
        }
        user_gender = sex;
        dialogShow();
        UserManager manager = new UserManager();
        UpdateParentParam param = new UpdateParentParam();
        param.setSex(sex);
        if (StringUtil.isNotBlank(name)) {
            param.setAlias(name);
        }
        if (StringUtil.isNotBlank(birthday)) {
            param.setBirthday(birthdayDate.getTimeInMillis());
        }
        manager.updateParent(mContext, param, activityHandler, UPDATE_PARENT);
    }

    @OnClick({R.id.gender_layout, R.id.woman_bt, R.id.man_bt})
    void selectSex(View view) {
        int id = view.getId();
        if (id == R.id.gender_layout) {
            List<Animator> animList = new ArrayList<>();
            animList.addAll(selectSexView(1));
            AnimatorSet animSet = new AnimatorSet();
            animSet.playSequentially(animList);
            animSet.start();
        } else if (id == R.id.woman_bt) {
            Animator revealAnim = createViewScale0(man_bt);
            revealAnim.start();
        } else if (id == R.id.man_bt) {
            Animator revealAnim = createViewScale0(woman_bt);
            revealAnim.start();
        }
    }

    /**
     * 设置界面
     */
    public void toggleShowSetting(View v) {
        int x = layoutSetting.getLeft() + layoutSetting.getRight();
        int y = 0;
        float radiusOf = 1f * v.getWidth() / 2f;
        float radiusFromToRoot = (float) Math.hypot(
                Math.max(x, layoutSetting.getWidth() - x),
                Math.max(y, layoutSetting.getHeight() - y));

        if (!isSettingShow) {
            isSettingShow = true;
            showMenu(x, y, radiusOf, radiusFromToRoot);
        } else {
            isSettingShow = false;
            hideMenu(x, y, radiusFromToRoot, radiusOf);
        }
    }

    private void sendActionBarAnim() {
        if (!isOpenUser) {
            isOpenUser = true;
            activityHandler.sendEmptyMessageDelayed(ANIMATION, 1000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sendActionBarAnim();
    }

    /**
     * actionbar user info animation
     */
    private void userAnimation(boolean is) {
        ValueAnimator animation = ValueAnimator.ofFloat(is ? 0 : EDITEXT_OFFER, is ? EDITEXT_OFFER : 0);
        if (is) {
            animation.setInterpolator(mInterpolator);
            animation.setDuration(450);
        } else {
            animation.setDuration(300);
        }
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout_user.getLayoutParams();
                lp.height = (int) value;
                layout_user.setLayoutParams(lp);
            }

        });
        animation.start();
    }

    private void showMenu(int cx, int cy, float startRadius, float endRadius) {
        List<Animator> animList = new ArrayList<>();

        layoutSetting.setVisibility(View.VISIBLE);
        Animator revealAnim = createCircularReveal(layoutSetting, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(500);
        animList.add(revealAnim);

        revealAnim = createViewScale1(change_bt);
        animList.add(revealAnim);

        int sex = App.getInstance().userResult.getParent().getSex();

        if (WomanOrManEnum.WOMAN.getCode() == sex) {
            revealAnim = createViewScale1(woman_bt);
            animList.add(revealAnim);
        } else {
            revealAnim = createViewScale1(man_bt);
            animList.add(revealAnim);
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
    }

    private void hideMenu(int cx, int cy, float startRadius, float endRadius) {
        List<Animator> animList = new ArrayList<>();

        Animator revealAnim = createCircularReveal(layoutSetting, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(500);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layoutSetting.setVisibility(View.INVISIBLE);
            }
        });
        animList.add(revealAnim);

        revealAnim = createViewScale0(change_bt);
        animList.add(revealAnim);

        animList.addAll(selectSexView(0));

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
    }

    /**
     * 设置 性别选择按钮 显示 隐藏
     * <p/>
     *
     * @param state state 0 = 隐藏 , 1 = 显示
     * @return
     */
    private List<Animator> selectSexView(int state) {
        List<Animator> animList = new ArrayList<>();
        Animator revealAnim;
        if (state == 0) {
            if (woman_bt.getVisibility() == View.VISIBLE) {
                revealAnim = createViewScale0(woman_bt);
                animList.add(revealAnim);
            }
            if (man_bt.getVisibility() == View.VISIBLE) {
                revealAnim = createViewScale0(man_bt);
                animList.add(revealAnim);
            }
        } else if (state == 1) {
            if (woman_bt.getVisibility() == View.INVISIBLE) {
                revealAnim = createViewScale1(woman_bt);
                animList.add(revealAnim);
            }
            if (man_bt.getVisibility() == View.INVISIBLE) {
                revealAnim = createViewScale1(man_bt);
                animList.add(revealAnim);
            }
        }
        return animList;
    }

    private Animator createViewScale1(final View view) {
        Animator revealAnim = ObjectAnimator.ofPropertyValuesHolder(
                view,
                AnimatorUtils.scaleX(0f, 1f),
                AnimatorUtils.scaleY(0f, 1f)
        );
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });
        revealAnim.setDuration(100);
        revealAnim.setInterpolator(new DecelerateInterpolator());
        return revealAnim;
    }

    private Animator createViewScale0(final View view) {
        Animator revealAnim = ObjectAnimator.ofPropertyValuesHolder(
                view,
                AnimatorUtils.scaleX(1f, 0f),
                AnimatorUtils.scaleY(1f, 0f)
        );
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        revealAnim.setDuration(100);
        revealAnim.setInterpolator(new DecelerateInterpolator());
        return revealAnim;
    }

    private Animator createCircularReveal(final ClipRevealFrame view, int x, int y, float startRadius,
                                          float endRadius) {
        final Animator reveal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            reveal = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        } else {
            view.setClipOutLines(true);
            view.setClipCenter(x, y);
            reveal = ObjectAnimator.ofFloat(view, "ClipRadius", startRadius, endRadius);
            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setClipOutLines(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return reveal;
    }

    private static final int INIT_LIST = 0x01;//初始化数据处理
    private static final int LOAD_DATA = 0x02;//加载数据处理
    private static final int UPDATE_PARENT = 0x04;//信息补全
    private static final int COUNT_MAX = 15;//加载数据最大值
    private static final int ANIMATION = 0x03;//user layout
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case INIT_LIST:
                case LOAD_DATA:
                    loadData((CommonResponse) msg.obj);
                    break;
                case UPDATE_PARENT:
                    loadUpdate((CommonResponse) msg.obj);
                    break;
                case ANIMATION:
                    userAnimation(true);
                    break;
                default:
                    break;
            }
        }
    };

    private void loadUpdate(CommonResponse resposne) {
        dialogDismiss();
        CommonUtil.showToast(resposne.getMsg());
        if (resposne.isSuccess()) {
            String name = nameValue.getText().toString();
            String birthday = birthdayValue.getText().toString();
            nameValue.clearFocus();
            if (StringUtil.isNotBlank(name)) {
                nameValue.setText("");
                nameValue.setHint(name);
                user_name.setText(name);
                App.getInstance().userResult.getParent().setAlias(name);
            }
            if (StringUtil.isNotBlank(birthday)) {
                birthdayValue.setText("");
                birthdayValue.setHint(birthday);
                App.getInstance().userResult.getParent().setBirthday(Long.parseLong(birthday));
            }
            if (user_gender != -1) {
                App.getInstance().userResult.getParent().setSex(user_gender);
            }
        }
    }

    private void loadData(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            FollowListResult result = (FollowListResult) resposne.getData();
            ArrayList<FollowUser> list = result.getList();
            int size = list == null ? 0 : list.size();
            if (arrayList == null) {
                arrayList = new ArrayList<>();
            }
            if (size > 0) {
                arrayList.addAll(list);
                mAdapter.refresh(arrayList);
            }
            if (size >= COUNT_MAX) {
                currentPager++;
            } else {
                listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        } else {
            //避免第一次应用启动时 创建fragment加载数据多次提示
        }
        listview.onRefreshComplete();
        setEmpty();
    }

    private void setEmpty() {
        if (arrayList == null || arrayList.size() <= 0) {
            llListEmpty.setVisibility(View.VISIBLE);
        } else {
            llListEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * 时间选择
     */
    @OnClick(R.id.birthday_value)
    void showDateDialog() {
        Calendar now = Calendar.getInstance();
        Date time = now.getTime();
        time.setTime(time.getTime());
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        now.setTime(time);
        dpd.setMaxDate(now);
        dpd.setAccentColor(accentColor);
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

    }

    @Override
    public boolean onBackPressed() {
        if (isSettingShow) {
            toggleShowSetting(settingAchor);
            return true;
        }
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        birthdayDate.set(year, monthOfYear, dayOfMonth);
        String dateStr = DateUtil.ConverToString(birthdayDate.getTime());
        birthdayValue.setText(dateStr);
    }
}
