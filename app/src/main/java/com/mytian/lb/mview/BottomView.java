package com.mytian.lb.mview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class BottomView {
    private View convertView;
    private Context context;
    private int theme;
    private Dialog bv;
    private int animationStyle;
    private boolean isTop = false;

    public BottomView(Context c, int theme, int resource) {
        this.theme = theme;
        this.context = c;
        this.convertView = View.inflate(c, resource, null);
    }

    @SuppressWarnings("deprecation")
    public void showBottomView(boolean CanceledOnTouchOutside) {
        if (bv == null) {
            if (this.theme == 0)
                this.bv = new Dialog(this.context);
            else
                this.bv = new Dialog(this.context, this.theme);

            this.bv.setCanceledOnTouchOutside(CanceledOnTouchOutside);
            this.bv.getWindow().requestFeature(1);
            this.bv.setContentView(this.convertView);
            Window wm = this.bv.getWindow();
            WindowManager m = wm.getWindowManager();
            Display d = m.getDefaultDisplay();
            WindowManager.LayoutParams p = wm.getAttributes();
            p.width = d.getWidth();
            if (this.isTop)
                p.gravity = 48;
            else
                p.gravity = 80;

            if (this.animationStyle != 0) {
                wm.setWindowAnimations(this.animationStyle);
            }
            wm.setAttributes(p);
        }
        this.bv.show();
    }

    public void setTopIfNecessary() {
        this.isTop = true;
    }

    public void setAnimation(int animationStyle) {
        this.animationStyle = animationStyle;
    }

    public View getView() {
        return this.convertView;
    }

    public void setCancelListener(DialogInterface.OnCancelListener listener) {
        if (null != listener) {
            bv.setOnCancelListener(listener);
        }
    }

    public void dismissBottomView() {
        if (this.bv != null)
            this.bv.dismiss();
    }
}