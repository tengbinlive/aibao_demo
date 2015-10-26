package com.core.util;

import android.os.Environment;

import com.bin.kndle.Constant;

import java.io.File;

public final class FileDataHelper {

    private static final String TAG = FileDataHelper.class.getSimpleName();

    private FileDataHelper() {

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
                    Log.i(TAG, "sdcard is using!");
                } else {
                    Log.i(TAG, "sdcard not use!");
                }
            }

            // 如果没有找到SDCard
            if (root == null) {
                rootFile = new File("/touyanshe");
                if (rootFile.exists()) {
                    root = rootFile.toString();
                    Log.i(TAG, "/flase is using!");
                } else {
                    Log.i(TAG, "/flase not use!");
                }
            }

            if (rootFile != null) {
                File rootDir = new File(rootFile + Constant.Dir.ROOT_DIR);
                File downloadDir = new File(rootFile + Constant.Dir.DOWNLOAD_DIR);
                File imageDir = new File(rootFile + Constant.Dir.IMAGE_DIR);
                File cacheDir = new File(rootFile + Constant.Dir.CACHE_DIR);

                if (!rootDir.exists()) {
                    rootDir.mkdirs();
                    Log.i(TAG, rootDir + " is not exist, created succeed!");
                }
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                    Log.i(TAG, downloadDir + " is not exist, created succeed!");
                }
                if (!imageDir.exists()) {
                    imageDir.mkdirs();
                    Log.i(TAG, imageDir + " is not exist, created succeed!");
                }
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                    Log.i(TAG, cacheDir + " is not exist, created succeed!");
                }
                result = true;
            } else {
                Log.e(TAG, "rootFile is null, created failed");
            }
        } catch (Exception e) {
            Log.e(TAG, "create directory Exception, " + e);
        }
        return result;
    }

    public static String getFilePath(String path) {
        if (hasSdcard()) {
            return Environment
                    .getExternalStorageDirectory().getAbsolutePath() + path;
        } else {
            return "/touyanshe" + path;
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
