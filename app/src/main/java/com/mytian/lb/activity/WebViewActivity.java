package com.mytian.lb.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.core.util.StringUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.mytian.lb.helper.MWebChromeClient;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;

/**
 * web 界面
 */
public class WebViewActivity extends AbsActivity {

    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.progress)
    ProgressWheel progress;

    private MWebChromeClient mWebChromeClient;

    private Bundle mSavedInstanceState;

    private String title;

    public final static String URL = "URL";
    public final static String TITLE = "TITLE";

    @Override
    public void EInit() {
        super.EInit();
        String url = getIntent().getStringExtra(URL);
        title = getIntent().getStringExtra(TITLE);
        String actionbarTitlte = StringUtil.isBlank(title) ? getString(R.string.app_name) : title;
        setToolbarIntermediateStr(actionbarTitlte);
        webviewSetInit(url);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_webview;
    }


    //webview 属性 设置
    private void webviewSetInit(String url) {
        WebSettings websettings = webview.getSettings();
        websettings.setBuiltInZoomControls(false);
        websettings.setSupportZoom(false);
        websettings.setDisplayZoomControls(false);
        websettings.setUseWideViewPort(true);
        websettings.setDefaultTextEncodingName("UTF-8");
        // for localStorage
        websettings.setDomStorageEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        websettings.setAppCachePath(appCachePath);
        websettings.setAllowFileAccess(true);
        websettings.setAppCacheEnabled(true);

        //先不要自动加载图片，等页面finish后再发起图片加载
        //同时在WebView的WebViewClient实例中的onPageFinished()方法添加如下代码：#001
        if (Build.VERSION.SDK_INT >= 19) {
            websettings.setLoadsImagesAutomatically(true);
        } else {
            websettings.setLoadsImagesAutomatically(false);
        }

        //加速临时关闭，过渡期后再开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        websettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.setWebViewClient(mWebViewClient);
        mWebChromeClient = new MWebChromeClient(this);
        webview.setWebChromeClient(mWebChromeClient);
        webview.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        if (null != mSavedInstanceState) {
            webview.restoreState(mSavedInstanceState);
        } else {
            webview.loadUrl(url);
        }
    }


    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //#001
            if (!webview.getSettings().getLoadsImagesAutomatically()) {
                webview.getSettings().setLoadsImagesAutomatically(true);
            }
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    };

    /**
     * 返回文件选择
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        mWebChromeClient.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (webview != null) {
            webview.saveState(outState);
        }
    }

    @Override
    public void onDestroy() {
        if (webview != null) {
            webview.stopLoading();
            webview.removeAllViews();
            webview.destroy();
            webview = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webview != null && webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
