package me.becycled.backend.model.entity.event.rollerblade;

import io.swagger.annotations.ApiModel;
import me.becycled.backend.model.entity.event.Competition;
import me.becycled.backend.model.entity.event.EventType;
import me.becycled.backend.model.utils.JsonUtils;

import java.io.IOException;

/**
 * @author binakot
 */
@ApiModel(description = "Соревнование по роликовым конькам")
public final class RollerbladeCompetition extends Competition {

    public RollerbladeCompetition() {
        this.eventType = EventType.ROLLERBLADE_COMPETITION;
    }

    private RollerbladeCompetition(final String jsonAttributes) throws IOException {
        JsonUtils.getJsonMapper().readerForUpdating(this).readValue(jsonAttributes);
    }
}
