package com.example.workforcemgmt.dto;

import com.example.workforcemgmt.common.model.enums.TaskEventType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskActivityDto {
    private Long id;
    private Long taskId;
    private Long timestamp;
    private TaskEventType eventType;
    private String description;
    private Long userId;
}