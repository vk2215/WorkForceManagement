package com.example.workforcemgmt.service;

import com.example.workforcemgmt.common.model.enums.Priority;
import com.example.workforcemgmt.dto.*;


import java.util.List;

public interface TaskManagementService {
    List<TaskManagementDto> createTasks(TaskCreateRequest request);
    List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
    String assignByReference(AssignByReferenceRequest request);
    List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
    TaskManagementDto findTaskById(Long id);

    TaskManagementDto updateTaskPriority(UpdateTaskPriorityRequest request);
    List<TaskManagementDto> fetchTasksByPriority(Priority priority);

    TaskCommentDto addCommentToTask(AddCommentRequest request);
}