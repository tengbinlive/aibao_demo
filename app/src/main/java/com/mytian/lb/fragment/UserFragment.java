package com.mytian.lb.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.DateUtil;
import com.core.util.FileDataHelper;
import com.core.util.StringUtil;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.activity.AddFollowActivity;
import com.mytian.lb.activity.AuthClipPictureActivity;
import com.mytian.lb.adapter.UserAdapter;
import com.mytian.lb.bean.follow.FollowListResult;
import com.mytian.lb.bean.follow.FollowUser;
import com.mytian.lb.bean.user.UpdateParentParam;
import com.mytian.lb.enums.WomanOrManEnum;
import com.mytian.lb.event.PushStateEventType;
import com.mytian.lb.event.PushUserEventType;
import com.mytian.lb.event.SettingEventType;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.helper.AnimatorUtils;
import com.mytian.lb.helper.Utils;
import com.mytian.lb.manager.FollowManager;
import com.mytian.lb.manager.UserManager;
import com.mytian.lb.mview.BottomView;
import com.mytian.lb.mview.ClipRevealFrame;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.rey.material.widget.RadioButton;
import android.widget.CompoundButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
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
    RadioButton woman_bt;
    @Bind(R.id.man_bt)
    RadioButton man_bt;

    @BindColor(R.color.theme)
    int accentColor;
    private Calendar birthdayDate;

    public static boolean isSettingShow;

    @Bind(R.id.listview)
    PullToRefreshListView listview;
    @Bind(R.id.ll_listEmpty)
    View llListEmpty;

    private ListView mActualListView;
    private UserAdapter mAdapter;

    private ArrayMap<String, FollowUser> arrayList;
    private FollowManager manager = new FollowManager();
    private int currentPager = 1;
    private View settingAchor;

    /**
     * 图片地址
     */
    private Uri imageUri;
    private static final String STR_PATH = "path";
    private static final String STR_CLIPRATIO = "clipRatio";
    private double clipRatio = 1.0;
    private BottomView mBottomView;
    /**
     * 本地图片选取标志
     */
    private static final int FLAG_CHOOSE_IMG = 0x11;
    /**
     * 截取结束标志
     */
    private static final int FLAG_MODIFY_FINISH = 0x7;
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 0x17;

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

    private Bitmap headIcon;

    private int isUpdateSuccess;

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
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    woman_bt.setChecked(woman_bt == buttonView);
                    man_bt.setChecked(man_bt == buttonView);
                }
            }
        };

        woman_bt.setOnCheckedChangeListener(listener);
        man_bt.setOnCheckedChangeListener(listener);
        initListView();
        setUserInfo();
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSettingShow) {
                    settingAchor = view;
                    toggleShowSetting(view);
                } else {
                    selectPict();
                }
            }
        });
        getListData(INIT_LIST);
    }

    private void setUserInfo() {
        if (App.getInstance().userResult == null) {
            return;
        }
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
        Glide.with(App.getInstance()).load(head).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().placeholder(R.mipmap.default_head).into(user_icon);
    }

    public void onEvent(SettingEventType event) {
        settingAchor = event.mView;
        toggleShowSetting(event.mView);
    }

    /**
     * 线上状态更新
     *
     * @param event
     */
    public void onEvent(PushStateEventType event) {
        String babyUid = event.babyUid;
        if (arrayList != null && arrayList.containsKey(babyUid)) {
            FollowUser followUser = arrayList.get(babyUid);
            followUser.setIs_online(event.is_online);
            arrayList.put(babyUid, followUser);
            mAdapter.refresh(babyUid, followUser);
        }
    }

    public void onEvent(PushUserEventType event) {
        if (event == null) {
            return;
        }
        if (mAdapter == null) {
            return;
        }
        if (arrayList == null) {
            arrayList = new ArrayMap<>();
        }
        arrayList.put(event.user.getUid(), event.user);
        mAdapter.refresh(arrayList);
        setEmpty();
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

        int sex = App.getInstance().userResult.getParent().getSex();
        if (woman_bt.isChecked()) {
            sex = WomanOrManEnum.WOMAN.getCode();
        } else if (man_bt.isChecked()) {
            sex = WomanOrManEnum.MAN.getCode();
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
        if (null != headIcon) {
            byte[] bmData = Utils.Bitmap2Bytes(headIcon);
            String pict = Utils.byte2hex(bmData);
            param.setHeadThumb(pict);
        }
        manager.updateParent(mContext, param, activityHandler, UPDATE_PARENT);
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
            resetData();
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
                ViewGroup.LayoutParams lp = layout_user.getLayoutParams();
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
            woman_bt.setChecked(true);
            man_bt.setChecked(false);
        } else {
            man_bt.setChecked(true);
            woman_bt.setChecked(false);
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

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
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

    private void resetData() {
        if (null != headIcon) {
            FileDataHelper.deleteDirectory(FileDataHelper.getFilePath(Constant.Dir.IMAGE_TEMP));
            headIcon = null;
        }
        if (isUpdateSuccess == 2) {
            String head = App.getInstance().userResult.getParent().getHeadThumb();
            Glide.with(App.getInstance()).load(head).asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop().placeholder(R.mipmap.default_head).into(user_icon);
        }
        isUpdateSuccess = 0;
    }

    private void loadUpdate(CommonResponse resposne) {
        dialogDismiss();
        CommonUtil.showToast(resposne.getMsg());
        if (resposne.isSuccess()) {
            isUpdateSuccess = 1;
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
                App.getInstance().userResult.getParent().setBirthday(birthdayDate.getTimeInMillis());
            }
            App.getInstance().userResult.getParent().setSex(user_gender);
        }
    }

    private void loadData(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            FollowListResult result = (FollowListResult) resposne.getData();
            ArrayList<FollowUser> list = result.getList();
            int size = list == null ? 0 : list.size();
            if (arrayList == null) {
                arrayList = new ArrayMap<>();
            }
            if (size > 0) {
                for (FollowUser followUser : list) {
                    arrayList.put(followUser.getUid(), followUser);
                }
            }
            if (size >= COUNT_MAX) {
                currentPager++;
            } else {
                listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            mAdapter.refresh(arrayList);
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

    private void selectPict() {
        mBottomView = new BottomView(getActivity(), R.style.BottomViewTheme_Defalut, R.layout.bottom_view);
        mBottomView.setAnimation(R.style.BottomToTopAnim);
        TextView bt1 = (TextView) mBottomView.getView().findViewById(R.id.bottom_tv_1);
        TextView bt2 = (TextView) mBottomView.getView().findViewById(R.id.bottom_tv_2);
        TextView bt3 = (TextView) mBottomView.getView().findViewById(R.id.bottom_tv_3);
        TextView cancel = (TextView) mBottomView.getView().findViewById(R.id.bottom_tv_cancel);
        View divider1 = mBottomView.getView().findViewById(R.id.divider1);
        bt1.setVisibility(View.GONE);
        divider1.setVisibility(View.GONE);
        bt2.setText("拍照");
        bt3.setText("从相册获取");
        PictButtonOnClickListener listener = new PictButtonOnClickListener();
        bt2.setOnClickListener(listener);
        bt3.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        mBottomView.showBottomView(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //结果码不等于取消时候
        FragmentActivity activity = getActivity();
        if (resultCode != activity.RESULT_CANCELED) {
            if (requestCode == FLAG_CHOOSE_IMG && resultCode == activity.RESULT_OK) {
                processGalleryIMG(data);
            } else if (requestCode == FLAG_CHOOSE_CAMERA && resultCode == activity.RESULT_OK) {
                processCamera();
            } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == activity.RESULT_OK) {
                if (data != null) {
                    String headPaht = data.getStringExtra(STR_PATH);
                    headIcon = BitmapFactory.decodeFile(headPaht);
                    user_icon.setImageBitmap(headIcon);
                    isUpdateSuccess = 2;
                }
            }
        }
    }

    class PictButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bottom_tv_2:
                    getImageFromCamera();
                    mBottomView.dismissBottomView();
                    break;
                case R.id.bottom_tv_3:
                    getImageFromGallery();
                    mBottomView.dismissBottomView();
                    break;
                case R.id.bottom_tv_cancel:
                    mBottomView.dismissBottomView();
                    break;
            }
        }
    }

    /**
     * 相册
     */
    private void getImageFromGallery() {
        imageStyle();
        Intent intentFromGallery = new Intent();
        intentFromGallery.setAction(Intent.ACTION_PICK);
        intentFromGallery.setType("image/*");
        startActivityForResult(intentFromGallery, FLAG_CHOOSE_IMG);
    }

    /**
     * 相机获取图片
     */
    private void getImageFromCamera() {
        imageStyle();
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (FileDataHelper.hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        startActivityForResult(intentFromCapture, FLAG_CHOOSE_CAMERA);
    }

    private void imageStyle() {
        // 照片命名
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String origFileName = "osc_" + timeStamp + ".jpg";
        String path = FileDataHelper.getFilePath(Constant.Dir.IMAGE_TEMP);
        imageUri = Uri.fromFile(new File(path, origFileName));
    }

    private void processGalleryIMG(Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                Cursor cursor = getActivity().getContentResolver().query(uri,
                        new String[]{MediaStore.Images.Media.DATA},
                        null, null, null);
                if (null == cursor) {
                    CommonUtil.showToast("图片没找到哦");
                    return;
                }
                cursor.moveToFirst();
                String path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();

                Intent intent = new Intent(getActivity(), AuthClipPictureActivity.class);
                intent.putExtra(STR_PATH, path);
                intent.putExtra(STR_CLIPRATIO, clipRatio);
                startActivityForResult(intent, FLAG_MODIFY_FINISH);
            } else {
                Intent intent = new Intent(getActivity(), AuthClipPictureActivity.class);
                intent.putExtra(STR_PATH, uri.getPath());
                intent.putExtra(STR_CLIPRATIO, clipRatio);
                startActivityForResult(intent, FLAG_MODIFY_FINISH);
            }
        }
    }

    private void processCamera() {
        Intent intent = new Intent(getActivity(), AuthClipPictureActivity.class);
        intent.putExtra(STR_PATH, imageUri.getPath());
        intent.putExtra(STR_CLIPRATIO, clipRatio);
        startActivityForResult(intent, FLAG_MODIFY_FINISH);
    }

}
