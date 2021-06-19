package me.becycled.backend.model.dao.mybatis.competition;

import me.becycled.backend.model.entity.competition.Competition;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author I1yi4
 */
public interface CompetitionMapper {

    @Insert(
        "INSERT INTO competitions (owner_user_id, community_id, private, start_date, route_id, sport_types, user_ids, venue, duration, description) "
            + "VALUES (" +
            "#{ownerUserId}," +
            "#{communityId}," +
            "#{isPrivate}," +
            "#{startDate}," +
            "#{routeId}," +
            "#{sportTypes, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler}, " +
            "#{userIds, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.IntegerListTypeHandler}," +
            "#{venue}, " +
            "#{duration}, " +
            "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Competition user);


    @Results(id = "competitionResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "owner_user_id", property = "ownerUserId"),
        @Result(column = "community_id", property = "communityId"),
        @Result(column = "private", property = "isPrivate"),
        @Result(column = "start_date", property = "startDate"),
        @Result(column = "route_id", property = "routeId"),
        @Result(column = "sport_types", property = "sportTypes", typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler.class),
        @Result(column = "user_ids", property = "userIds", typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.IntegerListTypeHandler.class),
        @Result(column = "venue", property = "venue"),
        @Result(column = "duration", property = "duration"),
        @Result(column = "description", property = "description"),
        @Result(column = "created_at", property = "createdAt")
    })
    @Select("SELECT * FROM competitions WHERE id=#{id}")
    Competition getById(Integer id);

    @Select("SELECT * FROM competitions WHERE community_id IN (SELECT id FROM communities WHERE nickname=#{nickname})")
    @ResultMap("competitionResult")
    List<Competition> getByCommunityNickname(String nickname);

    @Select("SELECT * FROM competitions")
    @ResultMap("competitionResult")
    List<Competition> getAll();

    @Update(
        "UPDATE competitions SET "
            + "owner_user_id=#{ownerUserId}, "
            + "community_id=#{communityId}, "
            + "private=#{isPrivate}, "
            + "start_date=#{startDate}, "
            + "sport_types=#{sportTypes, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler}, "
            + "user_ids=#{userIds, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.IntegerListTypeHandler}, "
            + "venue=#{venue}, "
            + "duration=#{duration}, "
            + "description=#{description}, "
            + "created_at=#{createdAt} "
            + "WHERE id=#{id}")
    int update(Competition workout);
}
