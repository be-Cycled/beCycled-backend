package me.becycled.backend.model.dao.mybatis.workout;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.workout.Workout;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author I1yi4
 */
public class WorkoutDao extends BaseMyBatisDao {

    public WorkoutDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public Workout create(final Workout entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);

            mapper.create(entity);
            return mapper.getById(entity.getId());
        }
    }

    public Workout getById(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);

            return mapper.getById(id);
        }
    }

    public List<Workout> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);

            return mapper.getAll();
        }
    }

    public Workout update(final Workout entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);
            mapper.update(entity);
            return mapper.getById(entity.getId());
        }
    }
}
