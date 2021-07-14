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
@ApiModel(description = "Файл изображения")
public final class Image {

    @ApiModelProperty(notes = "Имя файла", required = true, position = 0)
    private String fileName;
    @ApiModelProperty(notes = "Данные файла", required = true, position = 1)
    private byte[] data;

    //region GETTERS & SETTERS

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
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
        return Objects.equals(fileName, image.fileName)
            && Arrays.equals(data, image.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fileName);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("fileName", fileName)
            .append("data", data)
            .toString();
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public static Image build(final String fileName, final byte[] data) {
        final Image image = new Image();
        image.setFileName(fileName);
        image.setData(data);
        return image;
    }
}
