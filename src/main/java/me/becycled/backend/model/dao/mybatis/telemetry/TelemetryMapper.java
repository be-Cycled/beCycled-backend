package me.becycled.backend.model.dao.mybatis.telemetry;

import me.becycled.backend.model.entity.telemetry.Telemetry;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.time.Instant;
import java.util.List;

/**
 * @author binakot
 */
public interface TelemetryMapper {

    @Insert(
        "INSERT INTO telemetries (tracker_id, fix_time, latitude, longitude, altitude, speed, course) " +
            "VALUES (#{trackerId}, #{fixTime}, #{latitude}, #{longitude}, #{altitude}, #{speed}, #{course})")
    int create(Telemetry entity);

    @Results(id = "telemetryResult", value = {
        @Result(column = "tracker_id", property = "trackerId"),
        @Result(column = "fix_time", property = "fixTime"),
        @Result(column = "server_time", property = "serverTime"),
        @Result(column = "latitude", property = "latitude"),
        @Result(column = "longitude", property = "longitude"),
        @Result(column = "altitude", property = "altitude"),
        @Result(column = "speed", property = "speed"),
        @Result(column = "course", property = "course")
    })
    @Select("SELECT * FROM telemetries WHERE tracker_id=#{trackerId} ORDER BY fix_time DESC LIMIT 1")
    Telemetry getLastByTrackerId(Integer trackerId);

    @Select("SELECT * FROM telemetries " +
        "WHERE tracker_id=#{trackerId} AND fix_time > #{from} AND fix_time < #{to}" +
        "ORDER BY fix_time DESC")
    @ResultMap("telemetryResult")
    List<Telemetry> getRangeByTrackerId(Integer trackerId, Instant from, Instant to);

    @Select("SELECT * FROM telemetries ORDER BY tracker_id ASC, fix_time DESC")
    @ResultMap("telemetryResult")
    List<Telemetry> getAll();
}
