package com.mytian.lb.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.core.util.FileDataHelper;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.Constant;
import com.mytian.lb.R;
import com.mytian.lb.mview.ClipView;

import net.bither.util.NativeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 图片裁剪
 *
 * @author Administrator
 * @date 2015-1-12
 */
public class AuthClipPictureActivity extends AbsActivity implements OnTouchListener,
        OnClickListener {
    private ImageView srcPic;
    /**
     * 保存按钮
     */
    private Button saveLayout;
    /**
     * 取消
     */
    private Button line_cancle;
    private ClipView clipview;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    /**
     * 动作标志：无
     */
    private static final int NONE = 0;
    /**
     * 动作标志：拖动
     */
    private static final int DRAG = 1;
    /**
     * 动作标志：缩放
     */
    private static final int ZOOM = 2;
    /**
     * 初始化动作标志
     */
    private int mode = NONE;

    /**
     * 记录起始坐标
     */
    private PointF start = new PointF();
    /**
     * 记录缩放时两指中间点坐标
     */
    private PointF mid = new PointF();
    private float oldDist = 1f;

    private Bitmap bitmap;
    private String mPath;
    public int screenWidth = 0;
    public int screenHeight = 0;

    /**
     * 裁剪图片地址
     */
    public String filename;

    public static String mFilePath = FileDataHelper.getFilePath(Constant.Dir.IMAGE_TEMP);

    @Override
    public void EInit() {
        super.EInit();
        getWindowWH();
        mPath = getIntent().getStringExtra("path");
        bitmap = createBitmap(mPath, screenWidth, screenHeight);
        if (bitmap == null) {
            finish();
            return;
        }
        srcPic = (ImageView) this.findViewById(R.id.src_pic);
        final LinearLayout button_layout = (LinearLayout) this.findViewById(R.id.button_layout);

        srcPic.setOnTouchListener(this);

        ViewTreeObserver observer = srcPic.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                srcPic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                initClipView(srcPic.getTop(), button_layout.getTop(), bitmap);
            }
        });
        saveLayout = (Button) this.findViewById(R.id.sure);
        line_cancle = (Button) this.findViewById(R.id.line_cancle);
        line_cancle.setOnClickListener(this);
        saveLayout.setOnClickListener(this);
        File f = new File(mFilePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        filename = mFilePath + "/" + timeStamp + "clip.jpg";
    }

    @Override
    public int getContentView() {
        return R.layout.activity_clip_image;
    }

    @Override
    public void initActionBar() {
        setToolbarIntermediateStrID(R.string.auth_clip_pic);
    }

    /**
     * 获取屏幕的高和宽
     */
    private void getWindowWH() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    public Bitmap createBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
            opts.inJustDecodeBounds = false;
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 初始化截图区域，并将源图按裁剪框比例缩放
     *
     * @param top
     * @param bitmap
     */
    private void initClipView(int top, int bottom, final Bitmap bitmap) {
        clipview = new ClipView(AuthClipPictureActivity.this);
        clipview.setClipRatio(getIntent().getDoubleExtra("clipRatio", 0.5));
        clipview.setClipWidth(screenWidth);
        clipview.setCustomTopBarHeight(top);
        clipview.setCustomBottomBarHeight(bottom);
        clipview.addOnDrawCompleteListener(new ClipView.OnDrawListenerComplete() {

            public void onDrawCompelete() {
                clipview.removeOnDrawCompleteListener();
                int clipHeight = clipview.getClipHeight();
                int clipWidth = clipview.getClipWidth() + 50;
                int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
                int midY = clipview.getClipTopMargin() + (clipHeight / 2);

                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();

                // 按裁剪框求缩放比例
                float scale = (clipWidth * 1.0f) / imageWidth;
                if (imageWidth > imageHeight) {
                    scale = (clipHeight * 1.0f) / imageHeight;
                }
                // 起始中心点
                float imageMidX = imageWidth / 2;
                float imageMidY = clipview.getCustomTopBarHeight()
                        + imageHeight * scale / 2;
                srcPic.setScaleType(ScaleType.MATRIX);

                // 缩放
                matrix.postScale(scale, scale);
                // 平移
                matrix.postTranslate(0, midY - imageMidY);

                srcPic.setImageMatrix(matrix);
                srcPic.setImageBitmap(bitmap);
            }
        });

        this.addContentView(clipview, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                // 设置开始点位置
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        view.setImageMatrix(matrix);
        return true;
    }

    /**
     * 多点触控时，计算最先放下的两指距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 多点触控时，计算最先放下的两指中心坐标
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_cancle:
                finish();
                break;
            case R.id.sure:
                getBitmap();
                Intent intent = new Intent();
                intent.putExtra("path", filename);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 获取裁剪框内截图
     *
     * @return
     */
    private Bitmap getBitmap() {

        // 获取截屏
        View view = this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//
        Bitmap finalBitmap = Bitmap.createBitmap(view.getDrawingCache(),
                clipview.getClipLeftMargin(), clipview.getClipTopMargin(),
                clipview.getClipWidth(),
                clipview.getClipHeight());
//
        savaBitmap(finalBitmap);

        // 释放资源
        view.destroyDrawingCache();

        return finalBitmap;
    }

    /**
     * 保存bitmap对象到本地
     *
     * @param bitmap
     */
    public void savaBitmap(Bitmap bitmap) {
        File f = new File(filename);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NativeUtil.compressBitmap(bitmap, NativeUtil.DEFAULT_QUALITY,
                f.getAbsolutePath(), true);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
