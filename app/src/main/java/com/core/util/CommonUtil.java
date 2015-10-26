package com.core.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.bin.kndle.App;
import com.orhanobut.logger.Logger;

import junit.framework.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类.
 *
 * @author bin.teng
 */
public class CommonUtil {

    private static final String TAG = CommonUtil.class.getSimpleName();

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Toast mToast;

    /**
     * 根据生日字符串返回数字数组,分别表示年月日
     *
     * @param birthday 1983-2-22
     * @return 数字数组[1983, 2, 22]
     */
    public static int[] getBirthday(String birthday) {
        int[] date = new int[3];
        if (StringUtil.isNotEmpty(birthday)) {
            String str[] = birthday.split("-");
            date[0] = Integer.valueOf(str[0]);
            date[1] = Integer.valueOf(str[1]);
            date[2] = Integer.valueOf(str[2]);
        }
        return date;
    }

    /**
     * 得到用户所在地信息(去除那些重复的部分,例如上海上海市中国)
     *
     * @param provinceName 省会名称
     * @param cityName     城市名称
     * @param countyName   国家名称
     * @return 所在地
     */
    public static String getLocation(String provinceCode, String provinceName, String cityName, String countyName) {
        String location = "";
        if ("110000".equals(provinceCode) || "310000".equals(provinceCode) || "120000".equals(provinceCode)
                || "500000".equals(provinceCode)) {
            location = cityName + " " + countyName;
        } else {
            location = provinceName + " " + cityName;
        }
        return location;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return 分辨率值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context 上下文
     * @param pxValue 分辨率值
     * @return dp值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将正则表达式修改为“^-?[0-9]+”即可，修改为"^[0-9]+(.[0-9]*)?$"即可匹配所有数字。
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]*)?$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String convertStreamToString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8 * 1024);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            sb.delete(0, sb.length());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                return null;
            }
        }
        return sb.toString();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[] drawableToByteArray(Drawable drawable) {
        ByteArrayOutputStream output = null;
        try {
            // 取 drawable 的长宽
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);
            output = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, output);
            bitmap.recycle();
            byte[] result = output.toByteArray();
            output.close();
            return result;
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);
        return data;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            Logger.i( "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        Logger.d("readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if (offset < 0) {
            Logger.e( "readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            Logger.e( "readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            Logger.e( "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len];
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            Logger.e( "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            Logger.d("extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            Logger.d("extractThumbNail: extract beX = " + beX + ", beY = " + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Logger.i( "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x"
                    + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                Logger.e( "bitmap decode failed");
                return null;
            }

            Logger.i( "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1,
                        (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
                Logger.i( "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            Logger.e( "decode bitmap failed: " + e.getMessage());
            options = null;
        }

        return null;
    }

    private static void showToast(Context context, String msg, int time) {
        mToast = Toast.makeText(context, msg, time);
//		toast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void showToast(String msg) {
        showToast(App.getInstance(), msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resId) {
        showToast(App.getInstance(), App.getInstance().getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    /**
     * 获取包名
     *
     * @param c
     * @return
     * @author andy.fang
     * @createDate 2014-4-21
     */
    public static String getPackageName(Context c) {
        String str = "";
        PackageManager manager = c.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
            str = info.packageName; // 包名
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 判断应用是否在前台显示
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 返回true表示在前台显示
     */
    public static boolean isAppOnForeground(Context context, String packageName) {
        if (packageName == null || context == null) {
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个应用是否正在运行
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 正在运行返回true, 否则返回false
     */
    public static boolean isAppOnRunning(Context context, String packageName) {
        if (packageName == null || context == null) {
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得APP的版本名称
     *
     * @param c 上下文
     * @return 返回版本名称(例如: 3.2.1)
     */
    public static String getAppVersionName(Context c) {
        try {
            PackageManager manager = c.getPackageManager();
            PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void delivery2Handler(Handler handler, int what, Object obj) {
        if (handler == null)
            return;
        Message msg = handler.obtainMessage(what);
        msg.obj = obj;
        handler.sendMessage(msg);
    }

}
