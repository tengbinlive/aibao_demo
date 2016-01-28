package com.mytian.lb.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
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
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.activity.AuthClipPictureActivity;
import com.mytian.lb.activity.ResetPassWordActivity;
import com.mytian.lb.bean.user.UpdateParentParam;
import com.mytian.lb.bean.user.UpdateParentPortraitResult;
import com.mytian.lb.enums.WomanOrManEnum;
import com.mytian.lb.helper.AnimationHelper;
import com.mytian.lb.manager.AppManager;
import com.mytian.lb.manager.UserManager;
import com.mytian.lb.mview.BottomView;
import com.rey.material.widget.RadioButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
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

    @BindColor(R.color.theme)
    int accentColor;
    private Calendar birthdayDate;

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

    @Bind(R.id.user_icon)
    RoundedImageView user_icon;

    private int user_gender = -1;

    private int isUpdateSuccess;

    private String headPath;

    @Override
    public int getContentView() {
        return R.layout.fragment_user;
    }

    @Override
    public void EInit() {
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
        setUserInfo();
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPict();
            }
        });
    }

    private void setUserInfo() {
        Parent parent = App.getInstance().getUserResult().getParent();
        String name = parent.getAlias();
        if (StringUtil.isNotBlank(name)) {
            nameValue.setHint(name);
        }
        String phone = parent.getPhone();
        phone = StringUtil.isNotBlank(phone) ? phone : "...";
        String head = parent.getHeadThumb();
        phoneValue.setText(phone);
        long bir = parent.getBirthday();
        if (bir > 0) {
            String birthday = DateUtil.ConverToString(bir, DateUtil.YYYY_MM_DD);
            birthdayValue.setHint(birthday);
        }
        user_icon.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(head).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().placeholder(R.mipmap.default_head).into(user_icon);
    }

    @OnClick(R.id.change_bt)
    void onChangeInfo() {

        String name = nameValue.getText().toString();
        String nameHint = nameValue.getHint().toString();
        String birthday = birthdayValue.getText().toString();

        boolean isName = StringUtil.isNotBlank(name);
        boolean isBirthday = StringUtil.isNotBlank(birthday);
        boolean isHeadPath = StringUtil.isNotBlank(headPath);

        if (StringUtil.isBlank(name) && StringUtil.isBlank(nameHint)) {
            AnimationHelper.getInstance().viewAnimationQuiver(nameValue);
            return;
        }

        int sex_src = App.getInstance().getUserResult().getParent().getSex();
        int sex = sex_src;
        if (woman_bt.isChecked()) {
            sex = WomanOrManEnum.WOMAN.getCode();
        } else if (man_bt.isChecked()) {
            sex = WomanOrManEnum.MAN.getCode();
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
            manager.updateParentPortrait(mContext, new File(headPath), activityHandler, UPDATE_PARENTPORTRAIT);
        }
        manager.updateParent(mContext, param, activityHandler, UPDATE_PARENT);
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
        if (StringUtil.isNotBlank(headPath)) {
            FileDataHelper.deleteDirectory(FileDataHelper.getFilePath(Constant.Dir.IMAGE_TEMP));
            headPath = null;
        }
        if (isUpdateSuccess == 2) {
            String head = App.getInstance().getUserResult().getParent().getHeadThumb();
            Glide.with(mContext).load(head).asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop().placeholder(R.mipmap.default_head).into(user_icon);
        }
        isUpdateSuccess = 0;
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
        AppManager manager = new AppManager();
        manager.updateVersion();
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
        TextView change_ok = (TextView) convertView.findViewById(R.id.change_ok);
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
        if (requestCode == FLAG_CHOOSE_CAMERA) {
            processCamera();
        } else if (requestCode == FLAG_CHOOSE_IMG && resultCode == Activity.RESULT_OK) {
            processGalleryIMG(data);
        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                headPath = data.getStringExtra(STR_PATH);
                if (StringUtil.isNotBlank(headPath)) {
                    Bitmap headIcon = BitmapFactory.decodeFile(headPath);
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
