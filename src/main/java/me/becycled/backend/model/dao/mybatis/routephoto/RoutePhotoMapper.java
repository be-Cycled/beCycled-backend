package me.becycled.backend.model.dao.mybatis.routephoto;

import me.becycled.backend.model.entity.route.RoutePhoto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author I1yi4
 */
public interface RoutePhotoMapper {

    @Insert(
        "INSERT INTO route_photos (route_id, photo) " +
            "VALUES (" +
            "#{routeId}, " +
            "#{photo})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(RoutePhoto routePhoto);

    @Results(id = "routePhotoResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "route_id", property = "routeId"),
        @Result(column = "photo", property = "photo"),
        @Result(column = "created_at", property = "createdAt")
    })
    @Select("SELECT * FROM route_photos WHERE id=#{id}")
    RoutePhoto getById(Integer id);

    @Select("SELECT * FROM route_photos WHERE route_id=#{routeId}")
    @ResultMap("routePhotoResult")
    List<RoutePhoto> getByRouteId(Integer routeId);

    @Select("SELECT * FROM route_photos")
    @ResultMap("routePhotoResult")
    List<RoutePhoto> getAll();

    @Delete("DELETE FROM route_photos WHERE id=#{id}")
    int delete(Integer id);
}
