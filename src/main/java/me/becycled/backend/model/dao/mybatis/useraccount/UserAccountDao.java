package me.becycled.backend.model.dao.mybatis.useraccount;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.dao.mybatis.user.UserMapper;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author I1yi4
 */
public final class UserAccountDao extends BaseMyBatisDao {

    public UserAccountDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public UserAccount getByUserId(final Integer userId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserAccountMapper mapper = session.getMapper(UserAccountMapper.class);

            return mapper.getByUserId(userId);
        }
    }

    public UserAccount create(final User user, final UserAccount userAccount) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserAccountMapper userAccountMapper = session.getMapper(UserAccountMapper.class);
            final UserMapper userMapper = session.getMapper(UserMapper.class);

            userAccountMapper.create(user, userAccount);

            final User createdUser = userMapper.getByLogin(user.getLogin());
            return userAccountMapper.getByUserId(createdUser.getId());
        }
    }

    public UserAccount update(final UserAccount entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserAccountMapper mapper = session.getMapper(UserAccountMapper.class);

            mapper.update(entity);
            return entity;
        }
    }
}
