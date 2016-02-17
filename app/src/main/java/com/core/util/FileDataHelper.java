package com.core.util;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.mytian.lb.Constant;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class FileDataHelper {

    private static final String TAG = FileDataHelper.class.getSimpleName();

    private FileDataHelper() {

    }

    /**
     * 得到一个指定名称格式的图片名字
     *
     * @return
     */
    public static String getImgName(String suffix) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyyMMdd_HHmmsss");
        StringBuilder result = new StringBuilder("IMG_");
        result.append(sdf.format(new Date()) + (int) (Math.random() * 100)).append(suffix);
        return result.toString();
    }

    /**
     * 初始化APP在存储器中的目录,如不存在则创建.
     */
    public static boolean initDirectory() {
        boolean result = false;
        String root = null;
        File rootFile = null;

        try {
            // 获得APP根目录(SDCard或者内部存储器)
            if (hasSdcard()) {
                rootFile = Environment.getExternalStorageDirectory();
                if (rootFile.exists()) {
                    root = rootFile.toString();
                    Logger.i("sdcard is using!");
                } else {
                    Logger.i("sdcard not use!");
                }
            }

            // 如果没有找到SDCard
            if (root == null) {
                rootFile = new File(Constant.Dir.ROOT_DIR);
                if (rootFile.exists()) {
                    root = rootFile.toString();
                    Logger.i("/flase is using!");
                } else {
                    Logger.i("/flase not use!");
                }
            }

            if (rootFile != null) {
                File rootDir = new File(rootFile + Constant.Dir.ROOT_DIR);
                File downloadDir = new File(rootFile + Constant.Dir.DOWNLOAD_DIR);
                File imageDir = new File(rootFile + Constant.Dir.IMAGE_DIR);
                File cacheDir = new File(rootFile + Constant.Dir.CACHE_DIR);

                if (!rootDir.exists()) {
                    rootDir.mkdirs();
                    Logger.i(rootDir + " is not exist, created succeed!");
                }
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                    Logger.i(downloadDir + " is not exist, created succeed!");
                }
                if (!imageDir.exists()) {
                    imageDir.mkdirs();
                    Logger.i(imageDir + " is not exist, created succeed!");
                }
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                    Logger.i(cacheDir + " is not exist, created succeed!");
                }
                result = true;
            } else {
                Logger.e("rootFile is null, created failed");
            }
        } catch (Exception e) {
            Logger.e("create directory Exception, " + e);
        }
        return result;
    }

    public static String getFilePath(String path) {
        if (hasSdcard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
        } else {
            return Constant.Dir.ROOT_DIR + path;
        }
    }

    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    /**
     * 删除文件夹目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteFiles(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        return flag;
    }


    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }
}
