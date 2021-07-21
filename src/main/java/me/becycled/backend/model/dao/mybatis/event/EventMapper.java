package me.becycled.backend.model.dao.mybatis.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.becycled.backend.model.entity.event.Event;
import me.becycled.backend.model.utils.JsonUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author I1yi4
 */
public interface EventMapper {

    @InsertProvider(type = EventSqlBuilder.class, method = "buildCreateSql")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Event event);

    @InsertProvider(type = EventSqlBuilder.class, method = "buildUpdateSql")
    int update(Event event);

    @Delete("DELETE FROM events WHERE id=#{id}")
    int delete(Integer id);

    @Select("SELECT * FROM events WHERE id=#{id}")
    @ResultMap("eventResult")
    Event getById(Integer id);

    @Select("SELECT * FROM events WHERE community_id IN (SELECT id FROM communities WHERE nickname=#{nickname})")
    @ResultMap("eventResult")
    List<Event> getByCommunityNickname(String nickname);

    @Select("SELECT * FROM events WHERE id IN (SELECT event_id FROM event_members WHERE user_id=#{memberUserId})")
    @ResultMap("eventResult")
    List<Event> getByMemberUserId(Integer memberUserId);

    @Select("SELECT user_id FROM event_members WHERE event_id=#{eventId}")
    List<Integer> getEventMembers(Integer eventId);

    @Select("SELECT * FROM events " +
        "WHERE now() < start_date + duration * interval '1 second' " +
        "ORDER BY start_date ASC")
    @ResultMap("eventResult")
    List<Event> getAffiche();

    @Select("SELECT * FROM events " +
        "WHERE now() >= start_date + duration * interval '1 second' " +
        "ORDER BY start_date DESC")
    @ResultMap("eventResult")
    List<Event> getFeed();

    @Select("SELECT * FROM events")
    @ResultMap("eventResult")
    List<Event> getAll();

    // see XML
    int insertEventMembers(@Param("eventId") Integer eventId,
                           @Param("userIds") List<Integer> userIds);

    @Delete("DELETE FROM event_members WHERE event_id=#{eventId}")
    int deleteEventMembers(Integer eventId);

    /**
     * @author I1yi4
     */
    class EventSqlBuilder {
        private static final String TABLE_NAME = "events";

        @SuppressWarnings("PMD.DoubleBraceInitialization")
        public String buildCreateSql(final Event event) throws JsonProcessingException {
            return new SQL() {{
                INSERT_INTO(TABLE_NAME);

                INTO_COLUMNS("owner_user_id, community_id, event_type, start_date, duration, description, url, route_id, venue_geo_data, attributes");
                INTO_VALUES("#{ownerUserId}");
                INTO_VALUES("#{communityId}");
                INTO_VALUES("#{eventType}::EVENT_TYPE");
                INTO_VALUES("#{startDate}");
                INTO_VALUES("#{duration}");
                INTO_VALUES("#{description}");
                INTO_VALUES("#{url}");
                INTO_VALUES("#{routeId}");
                INTO_VALUES("#{venueGeoData}");
                INTO_VALUES("'" + JsonUtils.getJsonMapper().writerWithView(JsonUtils.Views.AttributeColumn.class).writeValueAsString(event) + "'::JSONB");

            }}.toString();
        }

        @SuppressWarnings("PMD.DoubleBraceInitialization")
        public String buildUpdateSql(final Event event) throws JsonProcessingException {
            return new SQL() {{
                UPDATE(TABLE_NAME);

                SET("start_date = #{startDate}");
                SET("duration = #{duration}");
                SET("description = #{description}");
                SET("url = #{url}");
                SET("route_id = #{routeId}");
                SET("venue_geo_data = #{venueGeoData}");
                SET("attributes = '" + JsonUtils.getJsonMapper().writerWithView(JsonUtils.Views.AttributeColumn.class).writeValueAsString(event) + "'::JSONB");

                WHERE("id = #{id}");

            }}.toString();
        }
    }
}
