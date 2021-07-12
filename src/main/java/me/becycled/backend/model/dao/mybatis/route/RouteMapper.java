package me.becycled.backend.model.dao.mybatis.route;

import me.becycled.backend.model.entity.route.Route;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author I1yi4
 */
public interface RouteMapper {

    @Insert(
        "INSERT INTO routes (user_id, name, route_geo_data, route_preview, sport_types, disposable, description) " +
            "VALUES (" +
            "#{userId}," +
            "#{name}," +
            "#{routeGeoData}," +
            "#{routePreview}," +
            "#{sportTypes, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler}," +
            "#{disposable}," +
            "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Route route);

    @Update(
        "UPDATE routes SET " +
            "name=#{name}, " +
            "route_geo_data=#{routeGeoData}, " +
            "route_preview=#{routePreview}, " +
            "sport_types=#{sportTypes, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler}, " +
            "disposable=#{disposable}, " +
            "description=#{description} " +
            "WHERE id=#{id}")
    int update(Route route);

    @Results(id = "routeResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "name", property = "name"),
        @Result(column = "route_geo_data", property = "routeGeoData"),
        @Result(column = "route_preview", property = "routePreview"),
        @Result(column = "sport_types", property = "sportTypes", typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler.class),
        @Result(column = "disposable", property = "disposable"),
        @Result(column = "description", property = "description"),
        @Result(column = "popularity", property = "popularity"),
        @Result(column = "created_at", property = "createdAt")
    })
    @Select("SELECT * FROM routes WHERE id=#{id}")
    Route getById(Integer id);

    @Select("SELECT * FROM routes")
    @ResultMap("routeResult")
    List<Route> getAll();
}
