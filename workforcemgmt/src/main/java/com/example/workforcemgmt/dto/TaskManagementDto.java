package com.example.workforcemgmt.dto;
import com.example.workforcemgmt.common.model.enums.Priority;
import com.example.workforcemgmt.common.model.enums.ReferenceType;
import com.example.workforcemgmt.common.model.enums.Task;
import com.example.workforcemgmt.common.model.enums.TaskStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskManagementDto {
    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private Task task;
    private String description;
    private TaskStatus status;
    private Long assigneeId;
    private Long taskDeadlineTime;
    private Priority priority;

    private List<TaskActivityDto> activityHistory;
    private List<TaskCommentDto> comments;
}

