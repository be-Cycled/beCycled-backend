package me.becycled.backend.model.dao.mybatis.community;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.community.Community;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author I1yi4
 */
public class CommunityDao extends BaseMyBatisDao {

    public CommunityDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public Community create(final Community entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CommunityMapper mapper = session.getMapper(CommunityMapper.class);

            mapper.create(entity);
            return mapper.getById(entity.getId());
        }
    }

    public Community update(final Community entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CommunityMapper mapper = session.getMapper(CommunityMapper.class);

            mapper.update(entity);
            return mapper.getById(entity.getId());
        }
    }

    public int delete(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CommunityMapper mapper = session.getMapper(CommunityMapper.class);
            return mapper.delete(id);
        }
    }

    public Community getById(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CommunityMapper mapper = session.getMapper(CommunityMapper.class);

            return mapper.getById(id);
        }
    }

    public Community getByNickname(final String nickname) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CommunityMapper mapper = session.getMapper(CommunityMapper.class);

            return mapper.getByNickname(nickname);
        }
    }

    public List<Community> getByOwnerUserId(final Integer ownerUserId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CommunityMapper mapper = session.getMapper(CommunityMapper.class);

            return mapper.getByOwnerUserId(ownerUserId);
        }
    }

    public List<Community> getByMemberUserId(final Integer memberUserId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CommunityMapper mapper = session.getMapper(CommunityMapper.class);

            return mapper.getByMemberUserId(memberUserId);
        }
    }

    public List<Community> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CommunityMapper mapper = session.getMapper(CommunityMapper.class);

            return mapper.getAll();
        }
    }
}
