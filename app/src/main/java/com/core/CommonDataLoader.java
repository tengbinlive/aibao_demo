package com.core;

import android.content.Context;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.core.enums.CodeEnum;
import com.core.util.CommonUtil;
import com.mytian.lb.App;

/**
 * 通用数据加载类.
 * <p/>
 * 说明: 调用了Velloy的RequestQueue和ImageLoader进行Http通信.
 *
 * @author bin.teng
 */
public class CommonDataLoader {

    /**
     * 该类的实例
     */
    private static CommonDataLoader mInstance = null;

    /**
     * Volley的RequestQueue对象
     */
    private RequestQueue mRequestQueue;

    /**
     * Volley的ImageLoader对象
     */
    private ImageLoader mImageLoader;

    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }

    /**
     * 获得CommonDataLoader的一个实例
     *
     * @param context App的上下文
     * @return
     */
    public static CommonDataLoader getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CommonDataLoader(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * 构造函数(私有)
     *
     * @param context App的上下文
     */
    private CommonDataLoader(Context context) {
        mRequestQueue = Volley.newRequestQueue(context, new OkHttpStack());
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
    }

    /**
     * 取消请求.
     *
     * @param tag
     */
    public void cancelRequest(Object tag) {
        if (mRequestQueue != null) mRequestQueue.cancelAll(tag);
    }

    /**
     * 取消所有请求.
     */
    public void cancelAllRequest() {
        if (mRequestQueue != null) mRequestQueue.cancelAll(new RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    public static void begin(CommonCallback callback) {
        if (callback != null) {
            callback.begin();
        }
    }

    public static void loginRequired(CommonCallback callback) {
        if (callback != null) {
            callback.end();
            callback.resp(new CommonResponse(CodeEnum.LOGIN_REQUIRED));
        }
    }

    public static void callback(CommonCallback callback, CommonResponse response) {
        if (callback != null) {
            callback.end();
            callback.resp(response);
        }
    }

    /**
     * 进行通讯并加载数据.
     *
     * @param request 通用请求对象
     */
    public void load(CommonRequest request) {
        if (!App.isNetworkAvailable()) {
            Handler handler = request.getHandler();
            Integer what = request.getHandlerMsgCode();
            CommonCallback cb = request.getCallback();
            if (handler != null && what != null) {
                CommonResponse response = new CommonResponse(CodeEnum.NETWORK_EXCEPTION);
                CommonUtil.delivery2Handler(handler, what, response);
            } else if (cb != null) {
                callback(cb, new CommonResponse(CodeEnum.CONNECT_UNAVAILABLE));
            } else {
                CommonUtil.showToast("糟糕，网络不可用");
            }
        } else if (!request.isValid()) {
            CommonUtil.showToast("缺少参数");
        } else {
            mRequestQueue.add(request);
        }
    }

}
