package com.bin.kndle.enums;


import com.bin.kndle.R;

/**
 * tab菜单枚举类.
 *
 * @author
 */
public enum BottomMenu {

    //    主界面
    //test1
    INVESTMENT(R.string.test_text,R.string.test_text, R.mipmap.main_investment_press, R.mipmap.main_investment_normal, 0xfff12e40, 0xff808080),
    //test2
    RECOMMEND(R.string.test_text,R.string.test_text, R.mipmap.main_recommend_press, R.mipmap.main_recommend_normal, 0xfff12e40, 0xff808080),
    //test3
    GUNG(R.string.test_text,R.string.test_text, R.mipmap.main_gung_press, R.mipmap.main_gung_normal, 0xfff12e40, 0xff808080),
    //test4
    ME(R.string.test_text, R.string.test_text,R.mipmap.main_me_press, R.mipmap.main_me_normal, 0xfff12e40, 0xff808080);

    private int title;
    private int title_press;
    private int resid_press;
    private int resid_normal;
    private int title_colos_press;
    private int title_colos_normal;

    BottomMenu(int title,int title_press ,int resid_press, int resid_normal, int title_colos_press, int title_colos_normal) {
        this.title = title;
        this.title_press = title_press;
        this.resid_press = resid_press;
        this.resid_normal = resid_normal;
        this.title_colos_press = title_colos_press;
        this.title_colos_normal = title_colos_normal;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getTitle_press() {
        return title_press;
    }

    public void setTitle_press(int title_press) {
        this.title_press = title_press;
    }

    public int getResid_press() {
        return resid_press;
    }

    public void setResid_press(int resid_press) {
        this.resid_press = resid_press;
    }

    public int getResid_normal() {
        return resid_normal;
    }

    public void setResid_normal(int resid_normal) {
        this.resid_normal = resid_normal;
    }

    public int getTitle_colos_press() {
        return title_colos_press;
    }

    public void setTitle_colos_press(int title_colos_press) {
        this.title_colos_press = title_colos_press;
    }

    public int getTitle_colos_normal() {
        return title_colos_normal;
    }

    public void setTitle_colos_normal(int title_colos_normal) {
        this.title_colos_normal = title_colos_normal;
    }

    @Override
    public String toString() {
        return "BottomMenu{" +
                "title=" + title +
                ", title_press=" + title_press +
                ", resid_press=" + resid_press +
                ", resid_normal=" + resid_normal +
                ", title_colos_press=" + title_colos_press +
                ", title_colos_normal=" + title_colos_normal +
                '}';
    }
}
