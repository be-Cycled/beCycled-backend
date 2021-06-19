package me.becycled.backend.model.dao.mybatis.post;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.post.Post;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.Instant;
import java.util.List;

/**
 * @author Suren Kalaychyan
 */
public final class PostDao extends BaseMyBatisDao {

    public PostDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public Post create(final Post entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final PostMapper mapper = session.getMapper(PostMapper.class);

            mapper.create(entity);
            return mapper.getById(entity.getId());
        }
    }

    public Post getById(final Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final PostMapper mapper = session.getMapper(PostMapper.class);

            return mapper.getById(id);
        }
    }

    public List<Post> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final PostMapper mapper = session.getMapper(PostMapper.class);

            return mapper.getAll();
        }
    }

    public Post update(final Post entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final PostMapper mapper = session.getMapper(PostMapper.class);
            entity.setUpdatedAt(Instant.now());
            mapper.update(entity);
            return mapper.getById(entity.getId());
        }
    }
}
