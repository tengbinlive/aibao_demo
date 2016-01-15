package com.mytian.lb.manager;

import com.dao.UserAction;
import com.dao.UserActionDao;
import com.mytian.lb.App;

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

    public void init() {
        // 从数据库中载入信息
        UserActionDao dao = App.getDaoSession().getUserActionDao();
        if (dao.count() <= 0) {
            initLocalData();
        }
        classification(dao);
    }

    private void classification(UserActionDao dao) {
        WhereCondition wcAgr = UserActionDao.Properties.Type.eq(TYPE_AGREEMENT);
        WhereCondition wcHab = UserActionDao.Properties.Type.eq(TYPE_HABIT);
        arrayListAgreement = (ArrayList<UserAction>) dao.queryBuilder().where(wcAgr).list();
        arrayListHabit = (ArrayList<UserAction>) dao.queryBuilder().where(wcHab).list();
    }

    private void initLocalData() {
        String[] argeementTitle = new String[]{"写作业", "看书", "做家务", "吃饭", "出去玩", "睡觉", "洗脸", "上幼儿园", "刷牙", "上厕所"};
        String[] habitTitle = new String[]{"吃饭", "洗脸", "睡觉", "刷牙", "做家务", "休息下", "写作业", "上幼儿园", "洗澡", "上厕所"};
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
