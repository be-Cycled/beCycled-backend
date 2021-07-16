package me.becycled.backend.model.entity.event.rollerblade;

import io.swagger.annotations.ApiModel;
import me.becycled.backend.model.entity.event.EventType;
import me.becycled.backend.model.entity.event.Workout;
import me.becycled.backend.model.utils.JsonUtils;

import java.io.IOException;

/**
 * @author binakot
 */
@ApiModel(description = "Тренировка по роликовым конькам")
public final class RollerbladeWorkout extends Workout {

    public RollerbladeWorkout() {
        this.eventType = EventType.ROLLERBLADE_WORKOUT;
    }

    private RollerbladeWorkout(final String jsonAttributes) throws IOException {
        JsonUtils.getJsonMapper().readerForUpdating(this).readValue(jsonAttributes);
    }
}
