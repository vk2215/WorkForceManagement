package com.example.workforcemgmt.common.model;

import com.example.workforcemgmt.common.model.enums.TaskEventType;
import lombok.Data;

@Data
public class TaskActivity {
    private Long id;
    private Long taskId;
    private Long timestamp;
    private TaskEventType eventType;
    private String description;
    private Long userId;
}