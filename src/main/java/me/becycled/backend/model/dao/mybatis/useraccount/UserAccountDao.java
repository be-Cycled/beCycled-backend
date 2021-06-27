package me.becycled.backend.model.dao.mybatis.useraccount;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.user.UserAccount;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author I1yi4
 */
public class UserAccountDao extends BaseMyBatisDao {

    public UserAccountDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public UserAccount create(final UserAccount entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserAccountMapper mapper = session.getMapper(UserAccountMapper.class);

            mapper.create(entity);
            return entity;
        }
    }

    public UserAccount getByUserId(final Integer userId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserAccountMapper mapper = session.getMapper(UserAccountMapper.class);

            return mapper.getByUserId(userId);
        }
    }
}
