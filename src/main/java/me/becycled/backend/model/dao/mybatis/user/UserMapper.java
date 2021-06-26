package me.becycled.backend.model.dao.mybatis.user;

import me.becycled.backend.model.entity.user.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author I1yi4
 */
public interface UserMapper {

    @Insert(
        "INSERT INTO users ("
            + "   login, first_name, last_name, email, "
            + "   phone, about, avatar) "
            + "VALUES ("
            + "   #{login}, #{firstName}, #{lastName}, #{email}, "
            + "   #{phone}, #{about}, #{avatar})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int create(User user);

    @Results(id = "userResult", value = {
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "login", property = "login"),
        @Result(column = "first_name", property = "firstName"),
        @Result(column = "last_name", property = "lastName"),
        @Result(column = "email", property = "email"),
        @Result(column = "phone", property = "phone"),
        @Result(column = "about", property = "about"),
        @Result(column = "avatar", property = "avatar"),
        @Result(column = "created_at", property = "createdAt")
    })
    @Select("SELECT * FROM users WHERE id=#{id}")
    User getById(Integer id);

    @Select("SELECT * FROM users WHERE login=#{login}")
    @ResultMap("userResult")
    User getByLogin(String login);

    @Select("SELECT * FROM users WHERE email=#{email}")
    @ResultMap("userResult")
    User getByEmail(String email);

    @Select("SELECT * FROM users")
    @ResultMap("userResult")
    List<User> getAll();

    @Update(
        "UPDATE users SET login=#{login}, first_name=#{firstName}, last_name=#{lastName}, email=#{email}, phone=#{phone}, about=#{about}, avatar=#{avatar}"
            + "WHERE id=#{id}")
    int update(User user);
}
