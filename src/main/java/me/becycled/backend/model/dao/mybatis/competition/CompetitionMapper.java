package me.becycled.backend.model.dao.mybatis.competition;

import me.becycled.backend.model.entity.competition.Competition;
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
public interface CompetitionMapper {

    @Insert(
        "INSERT INTO competitions (owner_user_id, community_id, private, start_date, route_id, sport_type, venue_geo_data, duration, description) " +
            "VALUES (" +
            "#{ownerUserId}," +
            "#{communityId}," +
            "#{isPrivate}," +
            "#{startDate}," +
            "#{routeId}," +
            "#{sportType}::SPORT_TYPE, " +
            "#{venueGeoData}, " +
            "#{duration}, " +
            "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Competition user);

    @Update(
        "UPDATE competitions SET "
            + "owner_user_id=#{ownerUserId}, "
            + "community_id=#{communityId}, "
            + "private=#{isPrivate}, "
            + "start_date=#{startDate}, "
            + "sport_type=#{sportType}::SPORT_TYPE, "
            + "venue_geo_data=#{venueGeoData}, "
            + "duration=#{duration}, "
            + "description=#{description}, "
            + "created_at=#{createdAt} "
            + "WHERE id=#{id}")
    int update(Competition workout);

    @Delete("DELETE FROM competitions WHERE id=#{id}")
    int delete(Integer id);

    @Results(id = "competitionResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "owner_user_id", property = "ownerUserId"),
        @Result(column = "community_id", property = "communityId"),
        @Result(column = "private", property = "isPrivate"),
        @Result(column = "start_date", property = "startDate"),
        @Result(column = "route_id", property = "routeId"),
        @Result(column = "sport_type", property = "sportType"),
        @Result(column = "id", property = "userIds", javaType = List.class, many = @Many(select = "getCompetitionMembers")),
        @Result(column = "venue_geo_data", property = "venueGeoData"),
        @Result(column = "duration", property = "duration"),
        @Result(column = "description", property = "description"),
        @Result(column = "created_at", property = "createdAt")
    })
    @Select("SELECT * FROM competitions WHERE id=#{id}")
    Competition getById(Integer id);

    @Select("SELECT * FROM competitions WHERE community_id IN (SELECT id FROM communities WHERE nickname=#{nickname})")
    @ResultMap("competitionResult")
    List<Competition> getByCommunityNickname(String nickname);

    @Select("SELECT * FROM competitions WHERE id IN (SELECT competition_id FROM competition_members WHERE user_id=#{memberUserId})")
    @ResultMap("competitionResult")
    List<Competition> getByMemberUserId(Integer memberUserId);

    @Select("SELECT * FROM competitions")
    @ResultMap("competitionResult")
    List<Competition> getAll();

    @Select("SELECT user_id FROM competition_members WHERE competition_id=#{competitionId}")
    List<Integer> getCompetitionMembers(Integer competitionId);

    // see XML
    int insertCompetitionMembers(@Param("competitionId") Integer competitionId, @Param("userIds") List<Integer> userIds);

    @Delete("DELETE FROM competition_members WHERE competition_id=#{competitionId}")
    int deleteCompetitionMembers(Integer competitionId);
}
