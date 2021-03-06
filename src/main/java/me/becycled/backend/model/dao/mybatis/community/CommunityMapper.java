package me.becycled.backend.model.dao.mybatis.community;

import me.becycled.backend.model.entity.community.Community;
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
public interface CommunityMapper {
    @Insert(
        "INSERT INTO communities (owner_user_id, name, nickname, avatar, community_type, sport_types, url, description) "
            + "VALUES ("
            + "#{ownerUserId},"
            + "#{name},"
            + "#{nickname},"
            + "#{avatar},"
            + "#{communityType}::COMMUNITY_TYPE,"
            + "#{sportTypes, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler},"
            + "#{url},"
            + "#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(Community community);

    @Update(
        "UPDATE communities SET "
            + "name=#{name}, "
            + "nickname=#{nickname}, "
            + "avatar=#{avatar}, "
            + "community_type=#{communityType}::COMMUNITY_TYPE, "
            + "sport_types=#{sportTypes, typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler}, "
            + "url=#{url}, "
            + "description=#{description} "
            + "WHERE id=#{id}")
    int update(Community community);

    @Delete("DELETE FROM communities WHERE id=#{id}")
    int delete(Integer id);

    @Results(id = "communityResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "owner_user_id", property = "ownerUserId"),
        @Result(column = "name", property = "name"),
        @Result(column = "nickname", property = "nickname"),
        @Result(column = "avatar", property = "avatar"),
        @Result(column = "community_type", property = "communityType"),
        @Result(column = "sport_types", property = "sportTypes", typeHandler = me.becycled.backend.model.utils.mybatis.typehandler.SportTypeListTypeHandler.class),
        @Result(column = "id", property = "userIds", javaType = List.class, many = @Many(select = "getCommunityMembers")),
        @Result(column = "url", property = "url"),
        @Result(column = "description", property = "description"),
        @Result(column = "created_at", property = "createdAt")
    })
    @Select("SELECT * FROM communities WHERE id=#{id}")
    Community getById(Integer id);

    @Select("SELECT * FROM communities WHERE nickname=#{nickname}")
    @ResultMap("communityResult")
    Community getByNickname(String nickname);

    @Select("SELECT * FROM communities WHERE owner_user_id=#{ownerUserId}")
    @ResultMap("communityResult")
    List<Community> getByOwnerUserId(Integer ownerUserId);

    @Select("SELECT * FROM communities WHERE id IN (SELECT community_id FROM community_members WHERE user_id=#{memberUserId})")
    @ResultMap("communityResult")
    List<Community> getByMemberUserId(Integer memberUserId);

    @Select("SELECT * FROM communities")
    @ResultMap("communityResult")
    List<Community> getAll();

    @Select("SELECT user_id FROM community_members WHERE community_id=#{communityId}")
    List<Integer> getCommunityMembers(Integer communityId);

    // see XML
    int insertCommunityMembers(@Param("communityId") Integer communityId, @Param("userIds") List<Integer> userIds);

    @Delete("DELETE FROM community_members WHERE community_id=#{communityId}")
    int deleteCommunityMembers(Integer communityId);
}
