package com.mytian.lb.helper;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 *
 * Created by Administrator on 2015/10/30.
 */
public class Utils {

    private Utils() {
    }

    public static int getResource(Context ctx, String imageName, String defType) {
        return ctx.getResources().getIdentifier(imageName, defType, ctx.getPackageName());
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String byte2hex(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        String tmp;
        for (byte aB : b) {
            tmp = (Integer.toHexString(aB & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

}