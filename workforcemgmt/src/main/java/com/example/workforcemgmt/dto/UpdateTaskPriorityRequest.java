package com.example.workforcemgmt.dto;

import com.example.workforcemgmt.common.model.enums.Priority;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateTaskPriorityRequest {
    private Long taskId;
    private Priority newPriority;
}