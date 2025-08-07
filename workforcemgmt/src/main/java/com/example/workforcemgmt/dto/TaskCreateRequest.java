package com.example.workforcemgmt.dto;
import com.example.workforcemgmt.common.model.enums.Priority;
import com.example.workforcemgmt.common.model.enums.ReferenceType;
import com.example.workforcemgmt.common.model.enums.Task;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskCreateRequest {
    private List<RequestItem> requests;
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RequestItem {
        private Long referenceId;
        private ReferenceType referenceType;
        private Task task;
        private Long assigneeId;
        private Priority priority;
        private Long taskDeadlineTime;
    }
}