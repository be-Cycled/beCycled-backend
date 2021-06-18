package me.becycled.backend.model.dao.mybatis.route;

import me.becycled.backend.model.entity.route.Route;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author I1yi4
 */
public interface RouteMapper {

    @Insert(
        "INSERT INTO routes (user_id, name, route_info, sport_types, disposable, description) "
            + "VALUES ("
            + "#{userId},"
            + "#{name},"
            + "#{routeInfo},"
            + "#{sportTypes, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler},"
            + "#{disposable},"
            + "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Route route);

    @Results(id = "routeResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "name", property = "name"),
        @Result(column = "route_info", property = "routeInfo"),
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

    @Update(
        "UPDATE routes SET "
            + "name=#{name}, "
            + "route_info=#{routeInfo}, "
            + "sport_types=#{sportTypes, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler}, "
            + "disposable=#{disposable}, "
            + "description=#{description} "
            + "WHERE id=#{id}")
    int update(Route route);
}
