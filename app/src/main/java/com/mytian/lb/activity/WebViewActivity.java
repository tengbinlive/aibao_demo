package com.mytian.lb.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.core.util.StringUtil;
import com.mytian.lb.AbsActivity;
import com.mytian.lb.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import butterknife.BindView;

/**
 * web 界面
 */
public class WebViewActivity extends AbsActivity {

    @BindView(R.id.webview)
    XWalkView xWalkView;
    @BindView(R.id.progress)
    ProgressWheel progress;

    public final static String URL = "URL";
    public final static String TITLE = "TITLE";

    @Override
    public void EInit() {
        super.EInit();
        String url = getIntent().getStringExtra(URL);
        String title = getIntent().getStringExtra(TITLE);
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
        xWalkView.setUIClient(new XWalkUIClient(xWalkView) {
            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                super.onPageLoadStarted(view, url);
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                super.onPageLoadStopped(view, url, status);
                progress.setVisibility(View.GONE);
            }
        });
        xWalkView.load(url, null);
    }


    /**
     * 返回文件选择
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (xWalkView != null) {
            xWalkView.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (xWalkView != null) {
            xWalkView.onDestroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (xWalkView != null) {
            xWalkView.pauseTimers();
            xWalkView.onHide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (xWalkView != null) {
            xWalkView.resumeTimers();
            xWalkView.onShow();
        }
    }

    @Override
    public void onBackPressed() {

        // Go backward
        if (xWalkView != null && xWalkView.getNavigationHistory().canGoBack()) {
            xWalkView.getNavigationHistory().navigate(
                    XWalkNavigationHistory.Direction.BACKWARD, 1);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (xWalkView != null) {
            xWalkView.onNewIntent(intent);
        }
    }

}
