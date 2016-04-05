package com.mytian.lb.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.core.CommonResponse;
import com.core.util.CommonUtil;
import com.core.util.DateUtil;
import com.core.util.FileDataHelper;
import com.core.util.StringUtil;
import com.dao.Parent;
import com.dao.ParentDao;
import com.gitonway.lee.niftymodaldialogeffects.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.NiftyDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mytian.lb.AbsFragment;
import com.mytian.lb.App;
import com.mytian.lb.BuildConfig;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.activity.ResetPassWordActivity;
import com.mytian.lb.bean.user.UpdateParentParam;
import com.mytian.lb.bean.user.UpdateParentPortraitResult;
import com.mytian.lb.bean.user.UserResult;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.helper.AnimatorUtils;
import com.mytian.lb.manager.AppManager;
import com.mytian.lb.manager.UserManager;
import com.mytian.lb.mview.BottomView;
import com.mytian.lb.push.PushHelper;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.RadioButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.OnClick;

/**
 * 用户设置界面
 */
public class UserFragment extends AbsFragment implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.name_value)
    TextView nameValue;
    @Bind(R.id.phone_value)
    TextView phoneValue;
    @Bind(R.id.birthday_value)
    TextView birthdayValue;
    @Bind(R.id.woman_bt)
    RadioButton woman_bt;
    @Bind(R.id.man_bt)
    RadioButton man_bt;

    @Bind(R.id.update_tv)
    TextView updatetv;

    @Bind(R.id.cancel_bt)
    Button cancel_bt;
    @Bind(R.id.change_bt)
    Button change_bt;

    @BindColor(R.color.theme)
    int accentColor;
    @BindColor(R.color.textcolor_theme)
    int textcolor_theme;
    private Calendar birthdayDate;

    /**
     * 图片地址
     */
    private Uri imageUri;
    private BottomView mBottomView;
    /**
     * 本地图片选取标志
     */
    private static final int FLAG_CHOOSE_IMG = 0x11;
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 0x12;

    @Bind(R.id.user_icon)
    RoundedImageView user_icon;

    @Bind(R.id.isdebug)
    CheckBox isdebug;

    private int user_gender = -1;

    private int isUpdateSuccess;

    private Uri headUri;

    private boolean isChangeButton;

    @Override
    public int getContentView() {
        return R.layout.fragment_user;
    }

    @Override
    public void EInit() {
        setUserInfo();
        birthdayDate = Calendar.getInstance();
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setButtonState(true);
                    woman_bt.setChecked(woman_bt == buttonView);
                    man_bt.setChecked(man_bt == buttonView);
                }
            }
        };
        woman_bt.setOnCheckedChangeListener(listener);
        man_bt.setOnCheckedChangeListener(listener);
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPict();
            }
        });
        String updateStr = mContext.getString(R.string.update) + "      " + BuildConfig.VERSION_NAME + "  for android  " + BuildConfig.BUILD_TYPE;
        if (!BuildConfig.BUILD_TYPE.equals("release")) {
            isdebug.setVisibility(View.VISIBLE);
            isdebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    PushHelper.getInstance().setIsToast(b);
                }
            });
        }
        updatetv.setText(updateStr);
    }

    private void setUserInfo() {
        Parent parent = App.getInstance().getUserResult().getParent();
        String name = parent.getAlias();
        if (StringUtil.isNotBlank(name)) {
            nameValue.setText("");
            nameValue.setHint(name);
        }
        String phone = parent.getPhone();
        String head = parent.getHeadThumb();
        if (StringUtil.isNotBlank(phone)) {
            phoneValue.setText(phone);
        }
        long bir = parent.getBirthday();
        if (bir > 0) {
            String birthday = DateUtil.ConverToString(bir, DateUtil.YYYY_MM_DD);
            birthdayValue.setText("");
            birthdayValue.setHint(birthday);
        } else {
            birthdayValue.setHint(R.string.no_setting);
        }

        int sex = parent.getSex();
        if (UserResult.WOMAN == sex) {
            woman_bt.setChecked(true);
            man_bt.setChecked(false);
        } else {
            woman_bt.setChecked(false);
            man_bt.setChecked(true);
        }

        user_icon.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(head).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().placeholder(R.mipmap.default_head).into(user_icon);
    }

    private void cancleButtonState() {
        isChangeButton = false;
        setButtonState(false);
        isChangeButton = false;
    }

    /**
     * 设置 确定 & 取消按钮 显示状态
     *
     * @param is true 显示，false 隐藏
     */
    private void setButtonState(boolean is) {
        if (isChangeButton) {
            return;
        }
        isChangeButton = true;
        ArrayList<Animator> animList = new ArrayList<>();
        Animator revealAnim;
        if (is) {
            revealAnim = createViewScale1(change_bt);
            animList.add(revealAnim);
            revealAnim = createViewScale1(cancel_bt);
            animList.add(revealAnim);
        } else {
            revealAnim = createViewScale0(cancel_bt);
            animList.add(revealAnim);
            revealAnim = createViewScale0(change_bt);
            animList.add(revealAnim);
        }
        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
    }

    @OnClick(R.id.about_tv)
    void about() {
        Uri uri = Uri.parse("http://tengbin.me/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.change_bt)
    void onChangeInfo() {
        cancleButtonState();
        CharSequence charSequence = nameValue.getText();
        String name = "";
        if (charSequence != null) {
            name = charSequence.toString();
        }
        String birthday = "";
        charSequence = birthdayValue.getText();
        if (charSequence != null) {
            birthday = charSequence.toString();
        }
        String nameHint = "";
        charSequence = nameValue.getHint();
        if (charSequence != null) {
            nameHint = charSequence.toString();
        }

        boolean isName = StringUtil.isNotBlank(name);
        boolean isBirthday = StringUtil.isNotBlank(birthday);
        boolean isHeadPath = headUri != null;

        if (StringUtil.isBlank(name) && StringUtil.isBlank(nameHint)) {
            AnimationHelper.getInstance().viewAnimationQuiver(nameValue);
            return;
        }

        int sex_src = App.getInstance().getUserResult().getParent().getSex();
        int sex = sex_src;
        if (woman_bt.isChecked()) {
            sex = UserResult.WOMAN;
        } else if (man_bt.isChecked()) {
            sex = UserResult.MAN;
        }

        boolean isSex = sex_src != sex;

        if (!isName && !isBirthday && !isHeadPath && !isSex) {
            CommonUtil.showToast(R.string.no_change);
            return;
        }

        user_gender = sex;
        dialogShow();
        UserManager manager = new UserManager();
        UpdateParentParam param = new UpdateParentParam();
        param.setSex(sex);
        if (isName) {
            param.setAlias(name);
        }
        if (isBirthday) {
            param.setBirthday(birthdayDate.getTimeInMillis());
        }
        if (isHeadPath) {
            manager.updateParentPortrait(mContext, new File(headUri.getPath()), activityHandler, UPDATE_PARENTPORTRAIT);
        }
        manager.updateParent(mContext, param, activityHandler, UPDATE_PARENT);
    }

    @OnClick(R.id.cancel_bt)
    void onCancelInfo() {
        isChangeButton = false;
        setButtonState(false);
        setUserInfo();
        isChangeButton = false;
        deleteImageFile();
    }

    private static final int UPDATE_PARENT = 0x04;//信息补全
    private static final int UPDATE_PARENTPORTRAIT = 0x05;//更新头像
    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case UPDATE_PARENT:
                    loadUpdate((CommonResponse) msg.obj);
                    break;
                case UPDATE_PARENTPORTRAIT:
                    loadUpdateParentPortrait((CommonResponse) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private void resetData() {
        deleteImageFile();
        if (isUpdateSuccess == 2) {
            String head = App.getInstance().getUserResult().getParent().getHeadThumb();
            Glide.with(mContext).load(head).asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop().placeholder(R.mipmap.default_head).into(user_icon);
        }
        isUpdateSuccess = 0;
    }

    /**
     * 删除上传头像 裁剪遗留图片
     */
    private void deleteImageFile() {
        if (headUri != null) {
            FileDataHelper.deleteDirectory(FileDataHelper.getFilePath(Constant.Dir.IMAGE_TEMP));
            headUri = null;
        }
    }

    private void loadUpdate(CommonResponse resposne) {
        dialogDismiss();
        CommonUtil.showToast(resposne.getMsg());
        if (resposne.isSuccess()) {
            Parent parent = App.getInstance().getUserResult().getParent();
            String name = nameValue.getText().toString();
            String birthday = birthdayValue.getText().toString();
            if (StringUtil.isNotBlank(name)) {
                nameValue.setText("");
                nameValue.setHint(name);
                parent.setAlias(name);
            }
            if (StringUtil.isNotBlank(birthday)) {
                birthdayValue.setText("");
                birthdayValue.setHint(birthday);
                parent.setBirthday(birthdayDate.getTimeInMillis());
            }
            parent.setSex(user_gender);
            App.getInstance().getUserResult().setParent(parent);
            ParentDao dao = App.getDaoSession().getParentDao();
            dao.deleteAll();
            dao.insertInTx(parent);
        }
    }

    private void loadUpdateParentPortrait(CommonResponse resposne) {
        if (resposne.isSuccess()) {
            UpdateParentPortraitResult result = (UpdateParentPortraitResult) resposne.getData();
            isUpdateSuccess = 1;
            Parent parent = App.getInstance().getUserResult().getParent();
            parent.setHeadThumb(result.getHeadPortraitUrl());
            App.getInstance().getUserResult().setParent(parent);
            ParentDao dao = App.getDaoSession().getParentDao();
            dao.deleteAll();
            dao.insertInTx(parent);
        } else {
            CommonUtil.showToast(R.string.failure_head_update);
        }
        resetData();
    }

    @OnClick(R.id.update_tv)
    void toUpdateApp() {
        dialogShow(R.string.update_get);
        AppManager.getInstance().updateVersion(dialogBuilder);
    }

    @OnClick(R.id.reset_password_tv)
    void toResetPassword() {
        Intent intent = new Intent(getActivity(), ResetPassWordActivity.class);
        String phone = App.getInstance().getUserResult().getParent().getPhone();
        if (StringUtil.isNotBlank(phone) && StringUtil.checkMobile(phone)) {
            intent.putExtra("phone", phone);
        }
        startActivity(intent);
    }

    @OnClick(R.id.exit_tv)
    void exitAccount() {
        App.getInstance().changeAccount(true);
    }

    /**
     * 时间选择
     */
    @OnClick(R.id.birthday_layout)
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

    /**
     * 昵称输入
     */
    @OnClick(R.id.name_layout)
    void showNameDialog() {
        dialogDismiss();
        LinearLayout convertView = (LinearLayout) mInflater.inflate(R.layout.dialog_remark, null);
        final TextView title = (TextView) convertView.findViewById(R.id.title);
        final EditText nameEt = (EditText) convertView.findViewById(R.id.desc_et);
        Button change_ok = (Button) convertView.findViewById(R.id.change_ok);
        title.setText(R.string.setting_name);
        Parent parent = App.getInstance().getUserResult().getParent();
        String alias = parent.getAlias();
        nameEt.setHint(R.string.hint_name);
        nameEt.setText(alias);
        if (StringUtil.isNotBlank(alias)) {
            nameEt.setSelection(alias.length());
        }
        change_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = nameEt.getText().toString();
                if (StringUtil.isBlank(nameStr)) {
                    CommonUtil.showToast(R.string.no_name);
                    return;
                }
                dialogDismiss();
                nameValue.setText(nameStr);
                setButtonState(true);
            }
        });
        dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        dialogBuilder.withDuration(700) // def
                .isCancelableOnTouchOutside(true) // def | isCancelable(true)
                .withEffect(Effectstype.Fadein) // def Effectstype.Slidetop
                .setCustomView(convertView, getActivity()); // .setCustomView(View
        dialogBuilder.show();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        birthdayDate.set(year, monthOfYear, dayOfMonth);
        String dateStr = DateUtil.ConverToString(birthdayDate.getTime());
        birthdayValue.setText(dateStr);
        setButtonState(true);
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
        bt2.setText(R.string.photograph);
        bt3.setText(R.string.being_from_album);
        PictButtonOnClickListener listener = new PictButtonOnClickListener();
        bt2.setOnClickListener(listener);
        bt3.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        mBottomView.showBottomView(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FLAG_CHOOSE_CAMERA) {
                startCropActivity(imageUri);
            } else if (requestCode == FLAG_CHOOSE_IMG) {
                processCropIMG(data);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        headUri = UCrop.getOutput(result);
        if (headUri != null) {
            user_icon.setImageURI(headUri);
            isUpdateSuccess = 2;
            setButtonState(true);
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            CommonUtil.showToast(cropError.getMessage());
        } else {
            CommonUtil.showToast(R.string.toast_unexpected_error);
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
            intentFromCapture.putExtra("return-data", true);
        }
        startActivityForResult(intentFromCapture, FLAG_CHOOSE_CAMERA);
    }

    private void imageStyle() {
        // 照片命名
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String origFileName = "osc_" + timeStamp + ".jpg";
        String path = FileDataHelper.getFilePath(Constant.Dir.IMAGE_TEMP);
        File tempFile = new File(path);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        File imageFile = new File(path, origFileName);
        imageUri = Uri.fromFile(imageFile);
    }

    private void processCropIMG(Intent data) {
        if (data != null) {
            final Uri selectedUri = data.getData();
            if (selectedUri != null) {
                startCropActivity(selectedUri);
            } else {
                CommonUtil.showToast(R.string.toast_cannot_retrieve_selected_image);
            }
        }
    }

    /**
     * 进入图片裁剪
     *
     * @param uri
     */
    private void startCropActivity(@NonNull Uri uri) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setToolbarColor(accentColor);
        options.setStatusBarColor(accentColor);
        options.setActiveWidgetColor(accentColor);
        options.setToolbarTitleTextColor(textcolor_theme);
        UCrop.of(uri, imageUri).withAspectRatio(1, 1).withOptions(options)
                .start(getContext(), this);
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

}
