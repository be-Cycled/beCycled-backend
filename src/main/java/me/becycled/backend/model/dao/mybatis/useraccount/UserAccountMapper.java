package me.becycled.backend.model.dao.mybatis.useraccount;

import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.model.entity.user.UserAccount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author I1yi4
 */
public interface UserAccountMapper {

    // see XML
    int create(@Param("user") User user, @Param("userAccount") UserAccount userAccount);

    @Update(
        "UPDATE user_accounts SET " +
            "password=#{password}, " +
            "last_auth_time=#{lastAuthTime} " +
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
