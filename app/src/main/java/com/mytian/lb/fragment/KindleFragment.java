package com.mytian.lb.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.mytian.lb.AbsFragment;
import com.mytian.lb.R;
import com.mytian.lb.mview.MarkDownView;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;

public class KindleFragment extends AbsFragment {

    @Bind(R.id.markdownview)
    MarkDownView markDownView;

    @Bind(R.id.progress)
    ProgressWheel progress;

    @Override
    public boolean onBackPressed() {
        if (markDownView.canGoBack()) {
            markDownView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_kindle;
    }

    @Override
    public void EInit() {
        super.EInit();
        markDownView.setEloadProgress(new MarkDownView.EloadProgress() {
            @Override
            public void start() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void end() {
                progress.setVisibility(View.GONE);
            }
        });
        activityHandler.sendEmptyMessage(1001);
    }

    private Handler activityHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {
                markDownView.setStringSource("#aibao\n" +
                        "\n" +
                        "[download](http://fir.im/5xv4)");
            }
        }
    };

}
