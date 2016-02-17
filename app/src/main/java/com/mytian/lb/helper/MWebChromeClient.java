package com.mytian.lb.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.TextView;
import android.widget.Toast;

import com.core.util.FileDataHelper;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.mview.BottomView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MWebChromeClient extends WebChromeClient {
    private Activity mContext;
    private ValueCallback<Uri> mUploadMessage;
    public final static int FLAG_CHOOSE_CAMERA = 2;
    public final static int FLAG_CHOOSE_IMG = 3;
    private BottomView mBottomView;

    /**
     * 图片地址
     */
    private Uri imageUri;

    public MWebChromeClient(Activity context) {
        super();
        mContext = context;
    }

    /*****************
     * android中使用WebView来打开本机的文件选择器
     *************************/
    // js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
    // Android > 4.1.1 调用这个方法
    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType, String capture) {
        mUploadMessage = uploadMsg;
        showAddImgPopup();
    }

    // 3.0 + 调用这个方法
    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType) {
        mUploadMessage = uploadMsg;
        showAddImgPopup();
    }

    // Android < 3.0 调用这个方法
    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        showAddImgPopup();
    }

    /**************
     * end
     ***************/

    private void showAddImgPopup() {
        if (mBottomView == null) {
            mBottomView = new BottomView(mContext, R.style.BottomViewTheme_Defalut, R.layout.bottom_view);
            mBottomView.setAnimation(R.style.BottomToTopAnim);
            mBottomView.setCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mUploadMessage.onReceiveValue(null);
                    mBottomView.dismissBottomView();
                }
            });
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
        }
        mBottomView.showBottomView(true);

    }

    class PictButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bottom_tv_2:
                    doCamera();
                    mBottomView.dismissBottomView();
                    break;
                case R.id.bottom_tv_3:
                    doGallery();
                    mBottomView.dismissBottomView();
                    break;
                case R.id.bottom_tv_cancel:
                    mUploadMessage.onReceiveValue(null);
                    mBottomView.dismissBottomView();
                    break;
            }
        }
    }

    /**
     * 从相机获取图片
     */
    public void doCamera() {
        boolean isSD = FileDataHelper.hasSdcard();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (!isSD) {
            Toast.makeText(mContext, "没有找到sd卡，您的美照可能无法存储", Toast.LENGTH_SHORT).show();
        }
        imageStyle();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", true);
        mContext.startActivityForResult(
                Intent.createChooser(intent, "完成操作需要使用"),
                FLAG_CHOOSE_CAMERA);
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

    /**
     * 从相册获取图片
     */
    public void doGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        mContext.startActivityForResult(
                Intent.createChooser(intent, "完成操作需要使用"),
                FLAG_CHOOSE_IMG);
    }

    /**
     * 返回文件选择
     */
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        if (requestCode == MWebChromeClient.FLAG_CHOOSE_CAMERA && resultCode == mContext.RESULT_OK) {
            if (null == mUploadMessage)
                return;
            Uri result = imageUri;
            mUploadMessage.onReceiveValue(result);
        } else if (requestCode == MWebChromeClient.FLAG_CHOOSE_IMG && resultCode == mContext.RESULT_OK) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
        } else if (resultCode == mContext.RESULT_CANCELED) {
            if (null == mUploadMessage)
                return;
            mUploadMessage.onReceiveValue(null);
        }
    }

}