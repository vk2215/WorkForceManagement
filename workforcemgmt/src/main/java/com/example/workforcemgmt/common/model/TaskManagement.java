package com.example.workforcemgmt.common.model;

import com.example.workforcemgmt.common.model.enums.Priority;
import com.example.workforcemgmt.common.model.enums.ReferenceType;
import com.example.workforcemgmt.common.model.enums.Task;
import com.example.workforcemgmt.common.model.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskManagement {
    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private Task task;
    private String description;
    private TaskStatus status;
    private Long assigneeId;
    private Long taskDeadlineTime;
    private Priority priority;
}