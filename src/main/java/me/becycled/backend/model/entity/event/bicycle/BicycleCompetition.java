package me.becycled.backend.model.entity.event.bicycle;

import io.swagger.annotations.ApiModel;
import me.becycled.backend.model.entity.event.Competition;
import me.becycled.backend.model.entity.event.EventType;
import me.becycled.backend.model.utils.JsonUtils;

import java.io.IOException;

/**
 * @author binakot
 */
@ApiModel(description = "Соревнование по велоспорту")
public final class BicycleCompetition extends Competition {

    public BicycleCompetition() {
        this.eventType = EventType.BICYCLE_COMPETITION;
    }

    private BicycleCompetition(final String jsonAttributes) throws IOException {
        JsonUtils.getJsonMapper().readerForUpdating(this).readValue(jsonAttributes);
    }
}
