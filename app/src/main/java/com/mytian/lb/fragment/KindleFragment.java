package com.mytian.lb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import butterknife.BindView;

public class KindleFragment extends AbsFragment {

    @BindView(R.id.webview)
    XWalkView xWalkView;

    @BindView(R.id.progress)
    ProgressWheel progress;

    @Override
    public int getContentView() {
        return R.layout.fragment_kindle;
    }

    @Override
    public void EInit() {
        super.EInit();
        webviewSetInit("http://mrdoob.com/lab/javascript/threejs/css3d/periodictable/");
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (xWalkView != null) {
            xWalkView.onNewIntent(intent);
        }
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
    public void onActivityResult(int requestCode, int resultCode,
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
    public boolean onBackPressed() {
        // Go backward
        if (xWalkView != null && xWalkView.getNavigationHistory().canGoBack()) {
            xWalkView.getNavigationHistory().navigate(
                    XWalkNavigationHistory.Direction.BACKWARD, 1);
            return true;
        } else {
            return false;
        }
    }


}
