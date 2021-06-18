package me.becycled.backend.model.dao.mybatis;

import me.becycled.backend.model.dao.mybatis.user.UserDao;
import me.becycled.backend.model.dao.mybatis.useraccount.UserAccountDao;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author I1yi4
 */
public class DaoFactory {

    private final UserDao userDao;
    private final UserAccountDao userAccountDao;

    public DaoFactory(final SqlSessionFactory sqlSessionFactory) {
        userDao = new UserDao(sqlSessionFactory);
        userAccountDao = new UserAccountDao(sqlSessionFactory);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public UserAccountDao getUserAccountDao() {
        return userAccountDao;
    }
}
