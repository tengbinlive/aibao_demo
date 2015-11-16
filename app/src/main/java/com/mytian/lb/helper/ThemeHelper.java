package com.mytian.lb.helper;

import android.content.Context;

import com.mytian.lb.R;

/**
 * Created by Administrator on 2015/10/30.
 */
public class ThemeHelper {

    public final static int THEME_TYPE_DEFAULT = 0;
    public final static int THEME_TYPE_PINK = 1;

    private static ThemeHelper instance;

    public MTheme currentTheme;

    private int styleTheme;

    public static ThemeHelper getInstance() {
        if (instance == null) {
            instance = new ThemeHelper();
        }
        return instance;
    }

    public void createTheme(Context context,boolean is){
        if (currentTheme == null) {
            currentTheme = new MTheme();
        }
        if(is){
            styleTheme = THEME_TYPE_DEFAULT;
            currentTheme.themeStyle = R.style.AppTheme;
            currentTheme.themeWTNStyle = R.style.AppThemeWTN;
            currentTheme.themeColos = context.getResources().getColor(R.color.theme);
            currentTheme.matchColos = context.getResources().getColor(R.color.match);
        }else{
            styleTheme = THEME_TYPE_PINK;
            currentTheme.themeStyle = R.style.AppThemePink;
            currentTheme.themeWTNStyle = R.style.AppThemeWTNPink;
            currentTheme.themeColos = context.getResources().getColor(R.color.theme_pink);
            currentTheme.matchColos = context.getResources().getColor(R.color.match_pink);
        }
    }

    public void setThemeType(Context context, boolean isTranslucent) {
        context.setTheme(isTranslucent ? currentTheme.themeStyle : currentTheme.themeWTNStyle);
    }

    public class MTheme {
        /**
         * 主色调 style 透明背景
         */
        public int themeStyle;

        /**
         * 主色调 style 不透明背景
         */
        public int themeWTNStyle;

        /**
         * 主色值
         */
        public int themeColos;

        /**
         * 配色值
         */
        public int matchColos;

    }
}
