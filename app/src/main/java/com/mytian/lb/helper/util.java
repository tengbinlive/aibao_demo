package com.mytian.lb.helper;

import android.content.Context;

/**
 * Created by Administrator on 2015/10/30.
 */
public class util {

    public static int getResource(Context ctx, String imageName,String defType) {
        int resId = ctx.getResources().getIdentifier(imageName, defType, ctx.getPackageName());
        return resId;
    }
}