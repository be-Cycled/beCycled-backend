package me.becycled.backend.model.entity.event.bicycle;

import io.swagger.annotations.ApiModel;
import me.becycled.backend.model.entity.event.EventType;
import me.becycled.backend.model.entity.event.Workout;
import me.becycled.backend.model.utils.JsonUtils;

import java.io.IOException;

/**
 * @author binakot
 */
@ApiModel(description = "Тренировка по велоспорту")
public final class BicycleWorkout extends Workout {

    public BicycleWorkout() {
        this.eventType = EventType.BICYCLE_WORKOUT;
    }

    private BicycleWorkout(final String jsonAttributes) throws IOException {
        JsonUtils.getJsonMapper().readerForUpdating(this).readValue(jsonAttributes);
    }
}
