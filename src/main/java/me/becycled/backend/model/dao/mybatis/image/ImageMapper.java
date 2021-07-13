package me.becycled.backend.model.dao.mybatis.image;

import me.becycled.backend.model.entity.image.Image;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author I1yi4
 */
public interface ImageMapper {

    @Insert(
        "INSERT INTO images (file_name, data) " +
            "VALUES (" +
            "#{fileName}," +
            "#{data})")
    int create(Image image);

    @Results(id = "imageResult", value = {
        @Result(id = true, column = "file_name", property = "fileName"),
        @Result(column = "data", property = "data")
    })
    @Select("SELECT * FROM images WHERE file_name=#{fileName}")
    Image getById(String id);

    @Select("SELECT * FROM images")
    @ResultMap("imageResult")
    List<Image> getAll();
}
