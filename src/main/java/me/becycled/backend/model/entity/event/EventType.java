package me.becycled.backend.model.entity.event;

import io.swagger.annotations.ApiModel;

/**
 * @author I1yi4
 * @author binakot
 */
@ApiModel(description = "Тип события")
public enum EventType {

    RUN_WORKOUT,
    RUN_COMPETITION,

    BICYCLE_WORKOUT,
    BICYCLE_COMPETITION,

    ROLLERBLADE_WORKOUT,
    ROLLERBLADE_COMPETITION
}
