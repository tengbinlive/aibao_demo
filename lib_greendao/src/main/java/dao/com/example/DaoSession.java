package com.example;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.example.Config;
import com.example.Agreement;

import com.example.ConfigDao;
import com.example.AgreementDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig configDaoConfig;
    private final DaoConfig agreementDaoConfig;

    private final ConfigDao configDao;
    private final AgreementDao agreementDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        configDaoConfig = daoConfigMap.get(ConfigDao.class).clone();
        configDaoConfig.initIdentityScope(type);

        agreementDaoConfig = daoConfigMap.get(AgreementDao.class).clone();
        agreementDaoConfig.initIdentityScope(type);

        configDao = new ConfigDao(configDaoConfig, this);
        agreementDao = new AgreementDao(agreementDaoConfig, this);

        registerDao(Config.class, configDao);
        registerDao(Agreement.class, agreementDao);
    }
    
    public void clear() {
        configDaoConfig.getIdentityScope().clear();
        agreementDaoConfig.getIdentityScope().clear();
    }

    public ConfigDao getConfigDao() {
        return configDao;
    }

    public AgreementDao getAgreementDao() {
        return agreementDao;
    }

}