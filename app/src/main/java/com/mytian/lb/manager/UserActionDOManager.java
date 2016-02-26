package com.mytian.lb.manager;

import android.content.Context;

import com.dao.UserAction;
import com.dao.UserActionDao;
import com.mytian.lb.App;
import com.mytian.lb.R;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.dao.query.WhereCondition;

/**
 * 本地动作数据.
 *
 * @author bin.teng
 */
public class UserActionDOManager {

    private static UserActionDOManager instance;

    private ArrayList<UserAction> arrayListAgreement;

    private ArrayList<UserAction> arrayListHabit;

    private final static String TYPE_AGREEMENT = "AGREEMENT"; //约定
    private final static String TYPE_HABIT = "HABIT"; //习惯

    public ArrayList<UserAction> getArrayListAgreement() {
        return arrayListAgreement;
    }

    public ArrayList<UserAction> getArrayListHabit() {
        return arrayListHabit;
    }

    public static UserActionDOManager getInstance() {
        if (instance == null) {
            instance = new UserActionDOManager();
        }
        return instance;
    }

    public void init(Context context) {
        // 从数据库中载入信息
        UserActionDao dao = App.getDaoSession().getUserActionDao();
        if (dao.count() <= 0) {
            initLocalData(context);
        }
        classification(dao);
    }

    private void classification(UserActionDao dao) {
        WhereCondition wcAgr = UserActionDao.Properties.Type.eq(TYPE_AGREEMENT);
        WhereCondition wcHab = UserActionDao.Properties.Type.eq(TYPE_HABIT);
        arrayListAgreement = (ArrayList<UserAction>) dao.queryBuilder().where(wcAgr).list();
        arrayListHabit = (ArrayList<UserAction>) dao.queryBuilder().where(wcHab).list();
    }

    private void initLocalData(Context context) {
        String [] argeementTitle = context.getResources().getStringArray(R.array.argeement);
        String [] habitTitle = context.getResources().getStringArray(R.array.habit);
        int startIndex = 2000;
        ArrayList<UserAction> arrayList = new ArrayList<>();
        int length = argeementTitle.length;
        /**
         * 约定
         */
        for (int i = 0; i < length; i++) {
            UserAction bean = new UserAction();
            int appointid = startIndex + (i + 1);
            bean.setDate(new Date(15 * 60 * 1000));
            bean.setType(TYPE_AGREEMENT);
            bean.setAppointId("" + appointid);
            bean.setTitle(argeementTitle[i]);
            bean.setIcon("icon_" + appointid);
            bean.setIcon_disabled("icon_" + appointid + "_disabled");
            arrayList.add(bean);
        }
        /**
         * 习惯
         */
        startIndex = 1000;
        length = habitTitle.length;
        for (int i = 0; i < length; i++) {
            UserAction bean = new UserAction();
            int appointid = startIndex + (i + 1);
            bean.setType(TYPE_HABIT);
            bean.setDate(new Date(15 * 60 * 1000));
            bean.setAppointId("" + appointid);
            bean.setTitle(habitTitle[i]);
            bean.setIcon("icon_" + appointid);
            bean.setIcon_disabled("icon_" + appointid + "_disabled");
            arrayList.add(bean);
        }
        UserActionDao dao = App.getDaoSession().getUserActionDao();
        dao.deleteAll();
        dao.insertInTx(arrayList);
    }

}
