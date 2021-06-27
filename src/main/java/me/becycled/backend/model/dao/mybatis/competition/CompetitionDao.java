package me.becycled.backend.model.dao.mybatis.competition;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.competition.Competition;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author I1yi4
 */
public class CompetitionDao extends BaseMyBatisDao {

    public CompetitionDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public Competition create(final Competition entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CompetitionMapper mapper = session.getMapper(CompetitionMapper.class);

            mapper.create(entity);
            return mapper.getById(entity.getId());
        }
    }

    public Competition update(final Competition entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CompetitionMapper mapper = session.getMapper(CompetitionMapper.class);

            mapper.update(entity);
            return mapper.getById(entity.getId());
        }
    }

    public Competition getById(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CompetitionMapper mapper = session.getMapper(CompetitionMapper.class);

            return mapper.getById(id);
        }
    }

    public List<Competition> getByCommunityNickname(final String nickname) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CompetitionMapper mapper = session.getMapper(CompetitionMapper.class);

            return mapper.getByCommunityNickname(nickname);
        }
    }

    public List<Competition> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final CompetitionMapper mapper = session.getMapper(CompetitionMapper.class);

            return mapper.getAll();
        }
    }
}
