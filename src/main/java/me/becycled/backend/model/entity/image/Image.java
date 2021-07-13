package me.becycled.backend.model.entity.image;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author I1yi4
 */
@ApiModel(description = "Картинка")
public class Image {

    @ApiModelProperty(notes = "Уникальный идентификатор UUID", required = true, position = 0)
    private String id;
    @ApiModelProperty(notes = "Байты картинки", required = true, position = 1)
    private byte[] data;

    private Image() {
        // private default
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public Image(final byte[] data) {
        this.data = data;
    }

    //region GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Image image = (Image) o;
        return Objects.equals(id, image.id) && Arrays.equals(data, image.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("data", data)
            .toString();
    }
}
