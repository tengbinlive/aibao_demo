package com.mytian.lb.manager;

import com.dao.Agreement;
import com.dao.AgreementDao;
import com.mytian.lb.App;
import com.mytian.lb.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * 约定.
 *
 * @author bin.teng
 */
public class AgreementDOManager {

    private static AgreementDOManager instance;

    private ArrayList<Agreement> arrayList;

    public ArrayList<Agreement> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Agreement> arrayList) {
        this.arrayList = arrayList;
    }

    public static AgreementDOManager getInstance() {
        if (instance == null) {
            instance = new AgreementDOManager();
        }
        return instance;
    }

    public void init() {
        // 从数据库中载入信息
        AgreementDao dao = App.getDaoSession().getAgreementDao();
        arrayList = (ArrayList<Agreement>) dao.loadAll();
        if (arrayList == null||arrayList.size()<=0) {
            initTestData();
        }
    }

    private void initTestData() {
        String[] argeementTitle = new String[]{"写作业", "看书", "做家务", "吃饭", "出去玩", "睡觉"};
        arrayList = new ArrayList<>();
        Agreement bean = new Agreement();
        bean.setDate(new Date(15 * 60 * 1000));
        bean.setAppointId("2001");
        bean.setTitle(argeementTitle[0]);
        bean.setIcon(R.mipmap.icon_love_hb1);
        arrayList.add(bean);
        bean = new Agreement();
        bean.setDate(new Date(15 * 60 * 1000));
        bean.setAppointId("2002");
        bean.setTitle(argeementTitle[1]);
        bean.setIcon(R.mipmap.icon_love_hb2);
        arrayList.add(bean);
        bean = new Agreement();
        bean.setDate(new Date(15 * 60 * 1000));
        bean.setAppointId("2003");
        bean.setTitle(argeementTitle[2]);
        bean.setIcon(R.mipmap.icon_love_hb3);
        arrayList.add(bean);
        bean = new Agreement();
        bean.setDate(new Date(15 * 60 * 1000));
        bean.setAppointId("2004");
        bean.setTitle(argeementTitle[3]);
        bean.setIcon(R.mipmap.icon_love_hb4);
        arrayList.add(bean);
        bean = new Agreement();
        bean.setDate(new Date(15 * 60 * 1000));
        bean.setAppointId("2005");
        bean.setTitle(argeementTitle[4]);
        bean.setIcon(R.mipmap.icon_love_hb5);
        arrayList.add(bean);
        bean = new Agreement();
        bean.setDate(new Date(15 * 60 * 1000));
        bean.setAppointId("2006");
        bean.setTitle(argeementTitle[5]);
        bean.setIcon(R.mipmap.icon_love_hb6);
        arrayList.add(bean);
        AgreementDao dao = App.getDaoSession().getAgreementDao();
        dao.deleteAll();
        dao.insertInTx(arrayList);
    }

}
