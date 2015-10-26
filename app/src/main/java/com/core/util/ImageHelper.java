package com.core.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bin.kndle.Constant;
import com.orhanobut.logger.Logger;

public class ImageHelper {

	private static final String TAG = ImageHelper.class.getSimpleName();

	/**
	 * 获得图片的Hex值.
	 *
	 * @param path 图片原始地址
	 * @return 图片的Hex值
	 */
	public static String getImageHex(String path) {
		String hex = null;
		if (StringUtil.isNotBlank(path)) {
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			if (bitmap != null) {
				byte[] bmData = BitmapUtils.Bitmap2Bytes(bitmap);
				hex = BitmapUtils.byte2hex(bmData);
			}
		}
		return hex;
	}

	/**
	 * 获得图片的Hex值并缩放图片.
	 * 
	 * @param path 图片原始地址
	 * @param width 压缩后的宽度
	 * @param height 压缩后的额高度
	 * @return 图片的Hex值
	 */
	public static String getImageHexAndScale(String path, int width, int height) {
		String hex = null;
		Bitmap bitmap = scaleImage(path, Constant.Dir.IMAGE_TEMP, width, height);
		if (bitmap != null) {
			byte[] bmData = BitmapUtils.Bitmap2Bytes(bitmap);
			hex = BitmapUtils.byte2hex(bmData);
		}
		return hex;
	}

	/**
	 * 缩放图片.
	 * 
	 * @param sourcePath 图片原始地址
	 * @param targetPath 图片压缩后存放的地址
	 * @param width 压缩后的宽度
	 * @param height 压缩后的额高度
	 * @return Bitmap对象
	 */
	public static Bitmap scaleImage(String sourcePath, String targetPath, int width, int height) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapUtils.scaleFile(sourcePath, width, height);
			BitmapUtils.bitmapToFile(bitmap, targetPath);
		} catch (Exception e) {
			bitmap = null;
			Logger.e(e.getLocalizedMessage());
		}
		return bitmap;
	}
}
