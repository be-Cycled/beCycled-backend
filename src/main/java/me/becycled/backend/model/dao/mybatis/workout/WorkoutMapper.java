package me.becycled.backend.model.dao.mybatis.workout;

import me.becycled.backend.model.entity.workout.Workout;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author I1yi4
 */
public interface WorkoutMapper {

    @Insert(
        "INSERT INTO workouts (owner_user_id, community_id, private, start_date, route_id, sport_type, venue, duration, description) " +
            "VALUES (" +
            "#{ownerUserId}," +
            "#{communityId}," +
            "#{isPrivate}," +
            "#{startDate}," +
            "#{routeId}," +
            "#{sportType}::SPORT_TYPE, " +
            "#{venue}, " +
            "#{duration}, " +
            "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Workout user);

    @Update(
        "UPDATE workouts SET "
            + "owner_user_id=#{ownerUserId}, "
            + "community_id=#{communityId}, "
            + "private=#{isPrivate}, "
            + "start_date=#{startDate}, "
            + "sport_type=#{sportType}::SPORT_TYPE, "
            + "description=#{description}, "
            + "venue=#{venue}, "
            + "duration=#{duration}, "
            + "created_at=#{createdAt} "
            + "WHERE id=#{id}")
    int update(Workout workout);

    @Delete("DELETE FROM workouts WHERE id=#{id}")
    int delete(Integer id);

    @Results(id = "workoutResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "owner_user_id", property = "ownerUserId"),
        @Result(column = "community_id", property = "communityId"),
        @Result(column = "private", property = "isPrivate"),
        @Result(column = "start_date", property = "startDate"),
        @Result(column = "route_id", property = "routeId"),
        @Result(column = "sport_type", property = "sportType"),
        @Result(column = "id", property = "userIds", javaType = List.class, many = @Many(select = "getWorkoutMembers")),
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

    @Select("SELECT * FROM workouts WHERE id IN (SELECT workout_id FROM workout_members WHERE user_id=#{memberUserId})")
    @ResultMap("workoutResult")
    List<Workout> getByMemberUserId(Integer memberUserId);

    @Select("SELECT * FROM workouts")
    @ResultMap("workoutResult")
    List<Workout> getAll();

    @Select("SELECT user_id FROM workout_members WHERE workout_id=#{workoutId}")
    List<Integer> getWorkoutMembers(Integer workoutId);

    // see XML
    int insertWorkoutMembers(@Param("workoutId") Integer workoutId, @Param("userIds") List<Integer> userIds);

    @Delete("DELETE FROM workout_members WHERE workout_id=#{workoutId}")
    int deleteWorkoutMembers(Integer workoutId);
}
