package com.example.workforcemgmt.repository;


import com.example.workforcemgmt.common.model.TaskActivity;
import com.example.workforcemgmt.common.model.TaskComment;
import com.example.workforcemgmt.common.model.TaskManagement;
import com.example.workforcemgmt.common.model.enums.Priority;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Optional<TaskManagement> findById(Long id);
    TaskManagement save(TaskManagement task);
    List<TaskManagement> findAll();
    List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, com.example.workforcemgmt.common.model.enums.ReferenceType
                                                                   referenceType);
    List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds);

    List<TaskManagement> findByPriority(Priority priority);

    TaskActivity saveActivity(TaskActivity activity);
    List<TaskActivity> findActivitiesByTaskId(Long taskId);
    TaskComment saveComment(TaskComment comment);
    List<TaskComment> findCommentsByTaskId(Long taskId);
}
