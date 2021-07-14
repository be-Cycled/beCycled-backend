package me.becycled.backend.model.dao.mybatis.tracker;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.telemetry.Tracker;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author binakot
 */
public final class TrackerDao extends BaseMyBatisDao {

    public TrackerDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public Tracker create(final Tracker entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TrackerMapper mapper = session.getMapper(TrackerMapper.class);

            mapper.create(entity);
            return mapper.getById(entity.getId());
        }
    }

    public Tracker getById(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TrackerMapper mapper = session.getMapper(TrackerMapper.class);

            return mapper.getById(id);
        }
    }

    public Tracker getByImei(final String imei) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TrackerMapper mapper = session.getMapper(TrackerMapper.class);

            return mapper.getByImei(imei);
        }
    }

    public List<Tracker> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TrackerMapper mapper = session.getMapper(TrackerMapper.class);

            return mapper.getAll();
        }
    }

    public Tracker update(final Tracker entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TrackerMapper mapper = session.getMapper(TrackerMapper.class);
            mapper.update(entity);
            return mapper.getById(entity.getId());
        }
    }

    public int delete(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TrackerMapper mapper = session.getMapper(TrackerMapper.class);

            return mapper.delete(id);
        }
    }
}
