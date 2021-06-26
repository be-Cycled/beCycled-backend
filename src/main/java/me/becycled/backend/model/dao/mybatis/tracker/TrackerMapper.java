package me.becycled.backend.model.dao.mybatis.tracker;

import me.becycled.backend.model.entity.telemetry.Tracker;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author binakot
 */
public interface TrackerMapper {

    @Insert(
        "INSERT INTO trackers (user_id, imei) " +
            "VALUES (#{userId}, #{imei})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Tracker entity);

    @Results(id = "trackerResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "user_id", property = "userId"),
        @Result(column = "imei", property = "imei")
    })
    @Select("SELECT * FROM trackers WHERE id=#{id}")
    Tracker getById(Integer id);

    @Select("SELECT * FROM trackers WHERE imei=#{imei}")
    @ResultMap("trackerResult")
    Tracker getByImei(String imei);

    @Select("SELECT * FROM trackers")
    @ResultMap("trackerResult")
    List<Tracker> getAll();

    @Update("UPDATE trackers SET user_id=#{userId}, imei=#{imei} WHERE id=#{id}")
    int update(Tracker tracker);

    @Delete("DELETE FROM trackers WHERE id=#{id}")
    int delete(Integer id);
}
