package me.becycled.backend.service.email;

import com.sun.istack.NotNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author I1yi4
 */
public class Attachment {

    @NotNull
    @ApiModelProperty(notes = "Название файла", required = true, position = 0)
    private String name = StringUtils.EMPTY;

    @NotNull
    @ApiModelProperty(notes = "Содержимое файла", required = true, position = 1)
    private byte[] data = new byte[0];

    Attachment() {
        // default
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public Attachment(final String name, final byte[] data) {
        this.name = name;
        this.data = data;
    }

    //region GETTERS & SETTERS

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @SuppressWarnings("PMD.MethodReturnsInternalArray")
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getData() {
        return data;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setData(final byte[] data) {
        this.data = data;
    }

    //endregion GETTERS & SETTERS

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attachment)) {
            return false;
        }
        final Attachment that = (Attachment) o;
        return Objects.equals(name, that.name) &&
            Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("name", name)
            .append("data", data)
            .toString();
    }
}
