package me.becycled.backend.model.dao.mybatis.image;

import me.becycled.backend.model.dao.mybatis.BaseMyBatisDao;
import me.becycled.backend.model.entity.image.Image;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author I1yi4
 */
public final class ImageDao extends BaseMyBatisDao {

    public ImageDao(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public Image create(final Image entity) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final ImageMapper mapper = session.getMapper(ImageMapper.class);

            mapper.create(entity);
            return entity;
        }
    }

    public Image getByFileName(final String fileName) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final ImageMapper mapper = session.getMapper(ImageMapper.class);

            return mapper.getByFileName(fileName);
        }
    }

    public List<Image> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final ImageMapper mapper = session.getMapper(ImageMapper.class);

            return mapper.getAll();
        }
    }
}
