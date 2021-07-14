package me.becycled.backend.model.dao.mybatis.route;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.route.Route;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author I1yi4
 */
public final class RouteDao extends BaseMyBatisDao {

    public RouteDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public Route create(final Route entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final RouteMapper mapper = session.getMapper(RouteMapper.class);

            mapper.create(entity);
            return mapper.getById(entity.getId());
        }
    }

    public Route update(final Route entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final RouteMapper mapper = session.getMapper(RouteMapper.class);

            mapper.update(entity);
            return mapper.getById(entity.getId());
        }
    }

    public Route getById(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final RouteMapper mapper = session.getMapper(RouteMapper.class);

            return mapper.getById(id);
        }
    }

    public List<Route> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final RouteMapper mapper = session.getMapper(RouteMapper.class);

            return mapper.getAll();
        }
    }
}
