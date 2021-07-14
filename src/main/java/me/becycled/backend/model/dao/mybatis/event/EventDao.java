package me.becycled.backend.model.dao.mybatis.event;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.event.Event;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author I1yi4
 */
public class EventDao extends BaseMyBatisDao {

    public EventDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public Event create(final Event entity) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            final EventMapper mapper = session.getMapper(EventMapper.class);
            mapper.create(entity);

            if (!entity.getUserIds().isEmpty()) {
                mapper.insertEventMembers(entity.getId(), entity.getUserIds());
            }

            session.commit();
            return mapper.getById(entity.getId());
        }
    }

    public Event update(final Event entity) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            final EventMapper mapper = session.getMapper(EventMapper.class);
            mapper.update(entity);

            mapper.deleteEventMembers(entity.getId());
            if (!entity.getUserIds().isEmpty()) {
                mapper.insertEventMembers(entity.getId(), entity.getUserIds());
            }

            session.commit();
            return mapper.getById(entity.getId());
        }
    }

    public int delete(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final EventMapper mapper = session.getMapper(EventMapper.class);

            return mapper.delete(id);
        }
    }

    public Event getById(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final EventMapper mapper = session.getMapper(EventMapper.class);

            return mapper.getById(id);
        }
    }

    public List<Event> getByCommunityNickname(final String nickname) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final EventMapper mapper = session.getMapper(EventMapper.class);

            return mapper.getByCommunityNickname(nickname);
        }
    }

    public List<Event> getByMemberUserId(final Integer memberUserId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final EventMapper mapper = session.getMapper(EventMapper.class);

            return mapper.getByMemberUserId(memberUserId);
        }
    }

    public List<Event> getAffiche() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final EventMapper mapper = session.getMapper(EventMapper.class);

            return mapper.getAffiche();
        }
    }

    public List<Event> getFeed() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final EventMapper mapper = session.getMapper(EventMapper.class);

            return mapper.getFeed();
        }
    }

    public List<Event> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final EventMapper mapper = session.getMapper(EventMapper.class);

            return mapper.getAll();
        }
    }
}
