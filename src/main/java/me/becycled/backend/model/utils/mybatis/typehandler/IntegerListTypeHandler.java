package me.becycled.backend.model.utils.mybatis.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author I1yi4
 */
public class IntegerListTypeHandler extends BaseTypeHandler<List<Integer>> {

    @Override
    public void setNonNullParameter(final PreparedStatement ps, final int i, final List<Integer> parameter, final JdbcType jdbcType) throws SQLException {
        final Array array = ps.getConnection().createArrayOf("integer", parameter.toArray());
        ps.setArray(i, array);
    }

    @Override
    public List<Integer> getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return toList(rs.getArray(columnName));
    }

    @Override
    public List<Integer> getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return toList(rs.getArray(columnIndex));
    }

    @Override
    public List<Integer> getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return toList(cs.getArray(columnIndex));
    }

    private List<Integer> toList(final Array pgArray) throws SQLException {
        if (pgArray == null) {
            return Collections.emptyList();
        }
        return Arrays.stream((Integer[]) pgArray.getArray()).collect(Collectors.toList());
    }
}
