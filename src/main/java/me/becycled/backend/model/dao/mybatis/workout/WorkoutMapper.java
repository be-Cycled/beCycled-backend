package me.becycled.backend.model.dao.mybatis.workout;

import me.becycled.backend.model.entity.workout.Workout;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author I1yi4
 */
public interface WorkoutMapper {

    @Insert(
        "INSERT INTO workouts (owner_user_id, community_id, private, start_date, route_id, sport_type, user_ids, venue, duration, description) "
            + "VALUES (" +
            "#{ownerUserId}," +
            "#{communityId}," +
            "#{isPrivate}," +
            "#{startDate}," +
            "#{routeId}," +
            "#{sportType}::SPORT_TYPE, " +
            "#{userIds, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.IntegerListTypeHandler}," +
            "#{venue}, " +
            "#{duration}, " +
            "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Workout user);


    @Results(id = "workoutResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "owner_user_id", property = "ownerUserId"),
        @Result(column = "community_id", property = "communityId"),
        @Result(column = "private", property = "isPrivate"),
        @Result(column = "start_date", property = "startDate"),
        @Result(column = "route_id", property = "routeId"),
        @Result(column = "sport_type", property = "sportType"),
        @Result(column = "user_ids", property = "userIds", typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.IntegerListTypeHandler.class),
        @Result(column = "venue", property = "venue"),
        @Result(column = "duration", property = "duration"),
        @Result(column = "description", property = "description"),
        @Result(column = "created_at", property = "createdAt")
    })
    @Select("SELECT * FROM workouts WHERE id=#{id}")
    Workout getById(Integer id);

    @Select("SELECT * FROM workouts WHERE community_id IN (SELECT id FROM communities WHERE nickname=#{nickname})")
    @ResultMap("workoutResult")
    List<Workout> getByCommunityNickname(String nickname);

    @Select("SELECT * FROM workouts")
    @ResultMap("workoutResult")
    List<Workout> getAll();

    @Update(
        "UPDATE workouts SET "
            + "owner_user_id=#{ownerUserId}, "
            + "community_id=#{communityId}, "
            + "private=#{isPrivate}, "
            + "start_date=#{startDate}, "
            + "sport_type=#{sportType}::SPORT_TYPE, "
            + "user_ids=#{userIds, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.IntegerListTypeHandler}, "
            + "description=#{description}, "
            + "venue=#{venue}, "
            + "duration=#{duration}, "
            + "created_at=#{createdAt} "
            + "WHERE id=#{id}")
    int update(Workout workout);
}
