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
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);
            mapper.create(entity);

            if (!entity.getUserIds().isEmpty()) {
                mapper.insertWorkoutMembers(entity.getId(), entity.getUserIds());
            }

            session.commit();
            return mapper.getById(entity.getId());
        }
    }

    public Workout update(final Workout entity) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);
            mapper.update(entity);

            mapper.deleteWorkoutMembers(entity.getId());
            if (!entity.getUserIds().isEmpty()) {
                mapper.insertWorkoutMembers(entity.getId(), entity.getUserIds());
            }

            session.commit();
            return mapper.getById(entity.getId());
        }
    }

    public int delete(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);

            return mapper.delete(id);
        }
    }

    public Workout getById(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);

            return mapper.getById(id);
        }
    }

    public List<Workout> getByCommunityNickname(final String nickname) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);

            return mapper.getByCommunityNickname(nickname);
        }
    }

    public List<Workout> getByMemberUserId(final Integer memberUserId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);

            return mapper.getByMemberUserId(memberUserId);
        }
    }

    public List<Workout> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final WorkoutMapper mapper = session.getMapper(WorkoutMapper.class);

            return mapper.getAll();
        }
    }
}
