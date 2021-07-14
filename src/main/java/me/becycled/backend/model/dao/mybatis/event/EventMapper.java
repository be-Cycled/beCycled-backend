package me.becycled.backend.model.dao.mybatis.event;

import me.becycled.backend.model.entity.event.Event;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author I1yi4
 */
public interface EventMapper {

    @InsertProvider(type = EventSqlBuilder.class, method = "buildCreateSql")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Event user);

    @InsertProvider(type = EventSqlBuilder.class, method = "buildUpdateSql")
    int update(Event event);

    @Delete("DELETE FROM events WHERE id=#{id}")
    int delete(Integer id);

    @Results(id = "eventResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "owner_user_id", property = "ownerUserId"),
        @Result(column = "community_id", property = "communityId"),
        @Result(column = "event_type", property = "eventType"),
        @Result(column = "private", property = "isPrivate"),
        @Result(column = "start_date", property = "startDate"),
        @Result(column = "route_id", property = "routeId"),
        @Result(column = "sport_type", property = "sportType"),
        @Result(column = "id", property = "userIds", javaType = List.class, many = @Many(select = "getEventMembers")),
        @Result(column = "venue_geo_data", property = "venueGeoData"),
        @Result(column = "duration", property = "duration"),
        @Result(column = "description", property = "description"),
        @Result(column = "created_at", property = "createdAt")
    })
    @Select("SELECT * FROM events WHERE id=#{id}")
    Event getById(Integer id);

    @Select("SELECT * FROM events WHERE community_id IN (SELECT id FROM communities WHERE nickname=#{nickname})")
    @ResultMap("eventResult")
    List<Event> getByCommunityNickname(String nickname);

    @Select("SELECT * FROM events WHERE id IN (SELECT event_id FROM event_members WHERE user_id=#{memberUserId})")
    @ResultMap("eventResult")
    List<Event> getByMemberUserId(Integer memberUserId);

    @Select("SELECT * FROM events WHERE now() < start_date + duration * interval '1 second'")
    @ResultMap("eventResult")
    List<Event> getAffiche();

    @Select("SELECT * FROM events WHERE now() >= start_date + duration * interval '1 second'")
    @ResultMap("eventResult")
    List<Event> getFeed();

    @Select("SELECT * FROM events")
    @ResultMap("eventResult")
    List<Event> getAll();

    @Select("SELECT user_id FROM event_members WHERE event_id=#{eventId}")
    List<Integer> getEventMembers(Integer eventId);

    // see XML
    int insertEventMembers(@Param("eventId") Integer eventId, @Param("userIds") List<Integer> userIds);

    @Delete("DELETE FROM event_members WHERE event_id=#{eventId}")
    int deleteEventMembers(Integer eventId);

    /**
     * @author I1yi4
     */
    class EventSqlBuilder {
        private static final String TABLE_NAME = "events";

        @SuppressWarnings("PMD.DoubleBraceInitialization")
        public String buildCreateSql(final Event event) {
            return new SQL() {{
                INSERT_INTO(TABLE_NAME);

                INTO_COLUMNS("owner_user_id, community_id, event_type, private, start_date, route_id, sport_type, venue_geo_data, duration, description");
                INTO_VALUES("#{ownerUserId}");
                INTO_VALUES("#{communityId}");
                INTO_VALUES("#{eventType}::EVENT_TYPE");
                INTO_VALUES("#{isPrivate}");
                INTO_VALUES("#{startDate}");
                INTO_VALUES("#{routeId}");
                INTO_VALUES("#{sportType}::SPORT_TYPE");
                INTO_VALUES("#{venueGeoData}");
                INTO_VALUES("#{duration}");
                INTO_VALUES("#{description}");

            }}.toString();
        }

        @SuppressWarnings("PMD.DoubleBraceInitialization")
        public String buildUpdateSql(final Event event) {
            return new SQL() {{
                UPDATE(TABLE_NAME);

                SET("community_id = #{communityId}");
                SET("event_type = #{eventType}::EVENT_TYPE");
                SET("private = #{isPrivate}");
                SET("start_date = #{startDate}");
                SET("sport_type = #{sportType}::SPORT_TYPE");
                SET("venue_geo_data = #{venueGeoData}");
                SET("duration = #{duration}");
                SET("description = #{description}");

                WHERE("id = #{id}");

            }}.toString();
        }
    }
}
