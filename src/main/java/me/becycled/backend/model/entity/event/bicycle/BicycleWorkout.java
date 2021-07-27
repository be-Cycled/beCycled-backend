package me.becycled.backend.model.entity.event.bicycle;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import me.becycled.backend.model.entity.event.EventType;
import me.becycled.backend.model.entity.event.Workout;
import me.becycled.backend.model.utils.JsonUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * @author binakot
 */
@ApiModel(description = "Тренировка по велоспорту")
public final class BicycleWorkout extends Workout {

    @JsonView(JsonUtils.Views.AttributeColumn.class)
    @ApiModelProperty(notes = "Тип велосипеда", required = true, position = 12)
    private BicycleType bicycleType;

    public BicycleWorkout() {
        this.eventType = EventType.BICYCLE_WORKOUT;
    }

    private BicycleWorkout(final String jsonAttributes) throws IOException {
        JsonUtils.getJsonMapper().readerForUpdating(this).readValue(jsonAttributes);
    }

    //region GETTERS & SETTERS

    public BicycleType getBicycleType() {
        return bicycleType;
    }

    public void setBicycleType(final BicycleType bicycleType) {
        this.bicycleType = bicycleType;
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
        if (!super.equals(o)) {
            return false;
        }
        final BicycleWorkout that = (BicycleWorkout) o;
        return bicycleType == that.bicycleType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bicycleType);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .appendSuper(super.toString())
            .append("bicycleType", bicycleType)
            .toString();
    }
}
