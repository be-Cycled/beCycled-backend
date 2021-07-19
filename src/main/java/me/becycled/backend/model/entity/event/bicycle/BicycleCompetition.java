package me.becycled.backend.model.entity.event.bicycle;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import me.becycled.backend.model.entity.event.Competition;
import me.becycled.backend.model.entity.event.EventType;
import me.becycled.backend.model.utils.JsonUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * @author binakot
 */
@ApiModel(description = "Соревнование по велоспорту")
public final class BicycleCompetition extends Competition {

    @JsonView(JsonUtils.Views.AttributeColumn.class)
    @ApiModelProperty(notes = "Тип велосипеда", required = true, position = 11)
    private BicycleType bicycleType;

    @JsonView(JsonUtils.Views.AttributeColumn.class)
    @ApiModelProperty(notes = "Тип велосипедного соревнования", required = true, position = 12)
    private BicycleCompetitionType bicycleCompetitionType;

    public BicycleCompetition() {
        this.eventType = EventType.BICYCLE_COMPETITION;
    }

    private BicycleCompetition(final String jsonAttributes) throws IOException {
        JsonUtils.getJsonMapper().readerForUpdating(this).readValue(jsonAttributes);
    }

    //region GETTERS & SETTERS

    public BicycleType getBicycleType() {
        return bicycleType;
    }

    public void setBicycleType(final BicycleType bicycleType) {
        this.bicycleType = bicycleType;
    }

    public BicycleCompetitionType getBicycleCompetitionType() {
        return bicycleCompetitionType;
    }

    public void setBicycleCompetitionType(final BicycleCompetitionType bicycleCompetitionType) {
        this.bicycleCompetitionType = bicycleCompetitionType;
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
        final BicycleCompetition that = (BicycleCompetition) o;
        return bicycleType == that.bicycleType
            && bicycleCompetitionType == that.bicycleCompetitionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bicycleType, bicycleCompetitionType);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .appendSuper(super.toString())
            .append("bicycleType", bicycleType)
            .append("bicycleCompetitionType", bicycleCompetitionType)
            .toString();
    }
}
