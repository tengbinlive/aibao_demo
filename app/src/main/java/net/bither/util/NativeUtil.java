/*
 * Copyright 2014 http://Bither.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bither.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 图片压缩
 */
public class NativeUtil {

    public static int DEFAULT_QUALITY = 55;

    public static void compressBitmap(Bitmap bit, String fileName,
                                      boolean optimize) {
        compressBitmap(bit, DEFAULT_QUALITY, fileName, optimize);
    }

    public static void compressBitmap(Bitmap bit, int quality, String fileName,
                                      boolean optimize) {
        Bitmap result = null;
        int bean = 1;//缩小倍数
        result = Bitmap.createBitmap(bit.getWidth() / bean, bit.getHeight() / bean,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bit.getWidth() / bean, bit.getHeight() / bean);//
        canvas.drawBitmap(bit, null, rect, null);
        saveBitmap(result, quality, fileName, optimize);
        result.recycle();
    }

    private static void saveBitmap(Bitmap bit, int quality, String fileName,
                                   boolean optimize) {

        compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality,
                fileName.getBytes(), optimize);

    }

    private static native String compressBitmap(Bitmap bit, int w, int h,
                                                int quality, byte[] fileNameBytes, boolean optimize);

    static {
        System.loadLibrary("jpegbither");
        System.loadLibrary("bitherjni");
    }

}
