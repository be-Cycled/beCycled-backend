package me.becycled.backend.model.dao.mybatis.routephoto;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.route.RoutePhoto;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author I1yi4
 */
public class RoutePhotoDao extends BaseMyBatisDao {

    public RoutePhotoDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public RoutePhoto create(final RoutePhoto entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final RoutePhotoMapper mapper = session.getMapper(RoutePhotoMapper.class);

            mapper.create(entity);
            return mapper.getById(entity.getId());
        }
    }

    public RoutePhoto getById(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final RoutePhotoMapper mapper = session.getMapper(RoutePhotoMapper.class);

            return mapper.getById(id);
        }
    }

    public List<RoutePhoto> getByRouteId(final Integer routeId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final RoutePhotoMapper mapper = session.getMapper(RoutePhotoMapper.class);

            return mapper.getByRouteId(routeId);
        }
    }

    public List<RoutePhoto> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final RoutePhotoMapper mapper = session.getMapper(RoutePhotoMapper.class);

            return mapper.getAll();
        }
    }

    public int delete(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final RoutePhotoMapper mapper = session.getMapper(RoutePhotoMapper.class);

            return mapper.delete(id);
        }
    }
}
