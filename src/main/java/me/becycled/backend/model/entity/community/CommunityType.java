package me.becycled.backend.model.entity.community;

import io.swagger.annotations.ApiModel;

/**
 * @author I1yi4
 */
@ApiModel(description = "Тип сообщества")
public enum CommunityType {
    ORGANIZATION,
    CLUB
}
