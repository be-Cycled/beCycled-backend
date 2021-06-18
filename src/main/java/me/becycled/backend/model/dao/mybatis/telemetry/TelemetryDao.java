package me.becycled.backend.model.dao.mybatis.telemetry;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.telemetry.Telemetry;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.Instant;
import java.util.List;

/**
 * @author binakot
 */
public class TelemetryDao extends BaseMyBatisDao {

    public TelemetryDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public void create(final Telemetry entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TelemetryMapper mapper = session.getMapper(TelemetryMapper.class);

            mapper.create(entity);
        }
    }

    public void create(final List<Telemetry> entities) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TelemetryMapper mapper = session.getMapper(TelemetryMapper.class);

            entities.forEach(mapper::create);
        }
    }

    public Telemetry getLastByTrackerId(final Integer trackerId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TelemetryMapper mapper = session.getMapper(TelemetryMapper.class);

            return mapper.getLastByTrackerId(trackerId);
        }
    }

    public List<Telemetry> getRangeByTrackerId(final Integer trackerId, final Instant from, final Instant to) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TelemetryMapper mapper = session.getMapper(TelemetryMapper.class);

            return mapper.getRangeByTrackerId(trackerId, from, to);
        }
    }

    public List<Telemetry> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final TelemetryMapper mapper = session.getMapper(TelemetryMapper.class);

            return mapper.getAll();
        }
    }
}
