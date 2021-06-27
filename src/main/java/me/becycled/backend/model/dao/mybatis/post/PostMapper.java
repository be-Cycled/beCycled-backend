package me.becycled.backend.model.dao.mybatis.post;

import me.becycled.backend.model.entity.post.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Suren Kalaychyan
 */
public interface PostMapper {

    @Insert(
        "INSERT INTO posts (user_id, title, content, poster) " +
            "VALUES (" +
            "#{userId}," +
            "#{title}," +
            "#{content}," +
            "#{poster})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Post post);

    @Update(
        "UPDATE posts SET " +
            "title=#{title}, " +
            "content=#{content}, " +
            "poster=#{poster}, " +
            "updated_at=#{updatedAt} " +
            "WHERE id=#{id}")
    int update(Post post);

    @Results(id = "postResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "title", property = "title"),
        @Result(column = "content", property = "content"),
        @Result(column = "poster", property = "poster"),
        @Result(column = "created_at", property = "createdAt"),
        @Result(column = "updated_at", property = "updatedAt")
    })
    @Select("SELECT * FROM posts WHERE id=#{id}")
    Post getById(Integer id);

    @Select("SELECT * FROM posts")
    @ResultMap("postResult")
    List<Post> getAll();
}
