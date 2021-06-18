/*
 * Copyright © 2021 ООО "Первая Мониторинговая Компания".
 * All rights reserved.
 */

package me.becycled.backend.model.utils.mybatis.typehandler;

import me.becycled.backend.model.entity.route.SportType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author I1yi4
 */
public class SportTypeListTypeHandler implements TypeHandler<List<SportType>> {

    @Override
    public void setParameter(final PreparedStatement ps, final int i, final List<SportType> parameter, final JdbcType jdbcType) throws SQLException {
        ps.setArray(i, ps.getConnection().createArrayOf("SPORT_TYPE", parameter == null ? null : parameter.toArray()));
    }

    @Override
    public List<SportType> getResult(final ResultSet rs, final String columnName) throws SQLException {
        final Array array = rs.getArray(columnName);
        if (array == null) {
            return null;
        }
        return Arrays.stream((String[]) array.getArray()).map(SportType::valueOf).collect(Collectors.toList());
    }

    @Override
    public List<SportType> getResult(final ResultSet rs, final int columnIndex) throws SQLException {
        final Array array = rs.getArray(columnIndex);
        if (array == null) {
            return null;
        }
        return Arrays.stream((String[]) array.getArray()).map(SportType::valueOf).collect(Collectors.toList());
    }

    @Override
    public List<SportType> getResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        final Array array = cs.getArray(columnIndex);
        if (array == null) {
            return null;
        }
        return Arrays.stream((String[]) array.getArray()).map(SportType::valueOf).collect(Collectors.toList());
    }
}
