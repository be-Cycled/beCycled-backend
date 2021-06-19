package me.becycled.backend.model.dao.mybatis.workout;

import me.becycled.backend.model.entity.workout.Workout;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author I1yi4
 */
public interface WorkoutMapper {

    @Insert(
        "INSERT INTO workouts (owner_user_id, community_id, private, start_date, route_id, sport_types, user_ids, description) "
            + "VALUES (" +
            "#{ownerUserId}," +
            "#{communityId}," +
            "#{privateWorkout}," +
            "#{startDate}," +
            "#{routeId}," +
            "#{sportTypes, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler}, " +
            "#{userIds, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.IntegerListTypeHandler}," +
            "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Workout user);


    @Results(id = "workoutResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "owner_user_id", property = "ownerUserId"),
        @Result(column = "community_id", property = "communityId"),
        @Result(column = "private", property = "privateWorkout"),
        @Result(column = "start_date", property = "startDate"),
        @Result(column = "route_id", property = "routeId"),
        @Result(column = "sport_types", property = "sportTypes", typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler.class),
        @Result(column = "user_ids", property = "userIds", typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.IntegerListTypeHandler.class),
        @Result(column = "description", property = "description"),
        @Result(column = "created_at", property = "createdAt")
    })
    @Select("SELECT * FROM workouts WHERE id=#{id}")
    Workout getById(Integer id);

    @Select("SELECT * FROM workouts")
    @ResultMap("workoutResult")
    List<Workout> getAll();

    @Update(
        "UPDATE workouts SET "
            + "owner_user_id=#{ownerUserId}, "
            + "community_id=#{communityId}, "
            + "private=#{privateWorkout}, "
            + "start_date=#{startDate}, "
            + "sport_types=#{sportTypes, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler}, "
            + "user_ids=#{userIds, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.IntegerListTypeHandler}, "
            + "description=#{description}, "
            + "created_at=#{createdAt} "
            + "WHERE id=#{id}")
    int update(Workout workout);
}
