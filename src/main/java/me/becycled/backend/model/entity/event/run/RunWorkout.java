package me.becycled.backend.model.entity.event.run;

import io.swagger.annotations.ApiModel;
import me.becycled.backend.model.entity.event.EventType;
import me.becycled.backend.model.entity.event.Workout;
import me.becycled.backend.model.utils.JsonUtils;

import java.io.IOException;

/**
 * @author binakot
 */
@ApiModel(description = "Тренировка по бегу")
public final class RunWorkout extends Workout {

    public RunWorkout() {
        this.eventType = EventType.RUN_WORKOUT;
    }

    private RunWorkout(final String jsonAttributes) throws IOException {
        JsonUtils.getJsonMapper().readerForUpdating(this).readValue(jsonAttributes);
    }
}
