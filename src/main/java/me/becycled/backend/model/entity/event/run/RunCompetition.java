package me.becycled.backend.model.entity.event.run;

import io.swagger.annotations.ApiModel;
import me.becycled.backend.model.entity.event.Competition;
import me.becycled.backend.model.entity.event.EventType;
import me.becycled.backend.model.utils.JsonUtils;

import java.io.IOException;

/**
 * @author binakot
 */
@ApiModel(description = "Соревнование по бегу")
public final class RunCompetition extends Competition {

    public RunCompetition() {
        this.eventType = EventType.RUN_COMPETITION;
    }

    private RunCompetition(final String jsonAttributes) throws IOException {
        JsonUtils.getJsonMapper().readerForUpdating(this).readValue(jsonAttributes);
    }
}
