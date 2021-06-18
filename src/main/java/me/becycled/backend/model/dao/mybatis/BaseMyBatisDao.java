package me.becycled.backend.model.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author I1yi4
 */
public class BaseMyBatisDao {

    protected final SqlSessionFactory sqlSessionFactory;

    public BaseMyBatisDao(final SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
}
