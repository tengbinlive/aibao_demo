package com.mytian.lb.demodata;

import com.mytian.lb.R;
import com.mytian.lb.bean.AgreementBean;
import com.mytian.lb.bean.follow.FollowUser;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/12.
 */
public class DemoManger {

    private static DemoManger instance;

    private String[] argeementTitle;

    private DemoUserInfo demoUserInfo_1;//小明
    private DemoUserInfo demoUserInfo_2;//小红
    private DemoUserInfo demoUserInfo_3;//韩梅梅
    private DemoUserInfo demoUserInfo_4;//李雷
    private DemoUserInfo demoUserInfo_0;//韩韩

    public static DemoManger getInstance() {
        if (instance == null) {
            instance = new DemoManger();
        }
        return instance;
    }

    private DemoManger() {
        argeementTitle = new String[]{"写作业", "看书", "做家务", "吃饭", "出去玩", "睡觉"};
        initData();
    }



    private void initData() {
        demoUserInfo_1 = new DemoUserInfo();
        FollowUser parent = new FollowUser();
        parent.setUid("10000002");
        parent.setPhone("18217612177");
        parent.setAlias("小明");
        demoUserInfo_1.setParent(parent);
        demoUserInfo_1.setId("1");
        ArrayList<AgreementBean> beans = new ArrayList<>();
        AgreementBean bean = new AgreementBean();
        bean.setTitle(argeementTitle[0]);
        bean.setIcon(R.mipmap.icon_love_hb1);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[2]);
        bean.setIcon(R.mipmap.icon_love_hb3);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[4]);
        bean.setIcon(R.mipmap.icon_love_hb5);
        beans.add(bean);
        demoUserInfo_1.setBeans(beans);

        demoUserInfo_2 = new DemoUserInfo();
        parent = new FollowUser();
        parent.setUid("10000002");
        parent.setPhone("18217611111");
        parent.setAlias("小红");
        demoUserInfo_2.setParent(parent);
        demoUserInfo_2.setId("2");
        beans = new ArrayList<>();
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[2]);
        bean.setIcon(R.mipmap.icon_love_hb3);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[1]);
        bean.setIcon(R.mipmap.icon_love_hb2);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[4]);
        bean.setIcon(R.mipmap.icon_love_hb5);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[0]);
        bean.setIcon(R.mipmap.icon_love_hb1);
        beans.add(bean);
        demoUserInfo_2.setBeans(beans);

        demoUserInfo_3 = new DemoUserInfo();
        parent = new FollowUser();
        parent.setUid("10000002");
        parent.setPhone("15217611213");
        parent.setAlias("韩梅梅");
        demoUserInfo_3.setParent(parent);
        demoUserInfo_3.setId("3");
        beans = new ArrayList<>();
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[0]);
        bean.setIcon(R.mipmap.icon_love_hb1);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[4]);
        bean.setIcon(R.mipmap.icon_love_hb5);
        beans.add(bean);
        demoUserInfo_3.setBeans(beans);

        demoUserInfo_4 = new DemoUserInfo();
        parent = new FollowUser();
        parent.setUid("10000002");
        parent.setPhone("13217611222");
        parent.setAlias("李雷");
        demoUserInfo_4.setParent(parent);
        demoUserInfo_4.setId("4");
        beans = new ArrayList<>();
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[0]);
        bean.setIcon(R.mipmap.icon_love_hb1);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[2]);
        bean.setIcon(R.mipmap.icon_love_hb3);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[1]);
        bean.setIcon(R.mipmap.icon_love_hb2);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[3]);
        bean.setIcon(R.mipmap.icon_love_hb4);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[4]);
        bean.setIcon(R.mipmap.icon_love_hb5);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[5]);
        bean.setIcon(R.mipmap.icon_love_hb6);
        beans.add(bean);
        demoUserInfo_4.setBeans(beans);

        demoUserInfo_0 = new DemoUserInfo();
        parent = new FollowUser();
        parent.setUid("10000002");
        parent.setPhone("1392121520");
        parent.setAlias("韩韩");
        demoUserInfo_0.setParent(parent);
        demoUserInfo_0.setId("0");
        beans = new ArrayList<>();
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[0]);
        bean.setIcon(R.mipmap.icon_love_hb1);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[1]);
        bean.setIcon(R.mipmap.icon_love_hb2);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[3]);
        bean.setIcon(R.mipmap.icon_love_hb4);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[2]);
        bean.setIcon(R.mipmap.icon_love_hb3);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[4]);
        bean.setIcon(R.mipmap.icon_love_hb5);
        beans.add(bean);
        bean = new AgreementBean();
        bean.setTitle(argeementTitle[5]);
        bean.setIcon(R.mipmap.icon_love_hb6);
        beans.add(bean);
        demoUserInfo_0.setBeans(beans);
    }

    /**
     * mb demo数据
     *
     * @return
     */
    public DemoUserInfo getDemoUserInfo(String index) {
        if (demoUserInfo_1.getId().equals(index)) {
            return demoUserInfo_1;
        } else if (demoUserInfo_2.getId().equals(index)) {
            return demoUserInfo_2;
        } else if (demoUserInfo_3.getId().equals(index)) {
            return demoUserInfo_3;
        } else if (demoUserInfo_4.getId().equals(index)) {
            return demoUserInfo_4;
        }else if (demoUserInfo_0.getId().equals(index)) {
            return demoUserInfo_0;
        }
        return null;
    }
}
