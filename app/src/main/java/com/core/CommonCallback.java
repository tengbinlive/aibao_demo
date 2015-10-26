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
    public void begin();

    /**
     * 结束操作
     */
    public void end();

    /**
     * 数据回调
     */
    public void resp(CommonResponse response);

}
