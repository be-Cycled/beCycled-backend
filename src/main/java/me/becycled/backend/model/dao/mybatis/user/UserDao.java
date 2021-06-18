package me.becycled.backend.model.dao.mybatis.user;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author I1yi4
 */
public class UserDao extends BaseMyBatisDao {

    public UserDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public User create(final User entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserMapper mapper = session.getMapper(UserMapper.class);
            final int id = mapper.create(entity);

            return mapper.getById(id);
        }
    }

    public User getById(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserMapper mapper = session.getMapper(UserMapper.class);

            return mapper.getById(id);
        }
    }

    public User getByLogin(final String login) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserMapper mapper = session.getMapper(UserMapper.class);

            return mapper.getByLogin(login);
        }
    }

    public User getByEmail(final String login) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserMapper mapper = session.getMapper(UserMapper.class);

            return mapper.getByEmail(login);
        }
    }

    public List<User> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserMapper mapper = session.getMapper(UserMapper.class);

            return mapper.getAll();
        }
    }

    public User update(final User entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.update(entity);
            return mapper.getById(entity.getId());
        }
    }
}

