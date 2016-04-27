package com.core;

/**
 * 通用回调接口.
 *
 * @author bin.teng
 */
public interface CommonCallback {
    /**
     * 开始操作
     */
    void begin();

    /**
     * 结束操作
     */
    void end();

    /**
     * 数据回调
     */
    void resp(CommonResponse response);

}
