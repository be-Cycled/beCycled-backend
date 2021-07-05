package me.becycled.backend.model.dao.mybatis.useraccount;

import me.becycled.backend.model.entity.user.UserAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author I1yi4
 */
interface UserAccountMapper {

    @Insert(
        "INSERT INTO user_accounts (user_id ,password, last_auth_time) " +
            "VALUES (#{userId}, #{password}, #{lastAuthTime})")
    int create(UserAccount userAccount);

    @Update(
        "UPDATE user_accounts SET " +
            "password=#{password} " +
            "WHERE user_id=#{userId}")
    int update(UserAccount userAccount);

    @Results(id = "userAccountResult", value = {
        @Result(column = "user_id", property = "userId"),
        @Result(column = "password", property = "password"),
        @Result(column = "last_auth_time", property = "lastAuthTime")
    })
    @Select("SELECT * FROM user_accounts WHERE user_id=#{userId}")
    UserAccount getByUserId(Integer userId);
}
