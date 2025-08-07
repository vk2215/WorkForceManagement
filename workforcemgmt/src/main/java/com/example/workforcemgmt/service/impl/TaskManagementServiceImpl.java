package com.example.workforcemgmt.service.impl;

import com.example.workforcemgmt.common.exception.ResourceNotFoundException;
import com.example.workforcemgmt.common.model.TaskActivity;
import com.example.workforcemgmt.common.model.TaskComment;
import com.example.workforcemgmt.common.model.TaskManagement;
import com.example.workforcemgmt.common.model.enums.Priority;
import com.example.workforcemgmt.common.model.enums.Task;
import com.example.workforcemgmt.common.model.enums.TaskEventType;
import com.example.workforcemgmt.common.model.enums.TaskStatus;
import com.example.workforcemgmt.dto.*;
import com.example.workforcemgmt.mapper.ITaskManagementMapper;
import com.example.workforcemgmt.repository.TaskRepository;
import com.example.workforcemgmt.service.TaskManagementService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;

    public TaskManagementServiceImpl(TaskRepository taskRepository,
                                     ITaskManagementMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }
    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        TaskManagementDto dto = taskMapper.modelToDto(task);
        List<TaskActivity> activities = taskRepository.findActivitiesByTaskId(id);
        List<TaskComment> comments = taskRepository.findCommentsByTaskId(id);

        dto.setActivityHistory(taskMapper.activityListToDtoList(activities));
        dto.setComments(taskMapper.commentListToDtoList(comments));

        return dto;
    }
    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        List<TaskManagementDto> createdTaskDtos = new ArrayList<>();
        for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssigneeId(item.getAssigneeId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");
            TaskManagement savedTask = taskRepository.save(newTask);


            TaskActivity activity = new TaskActivity();
            activity.setTaskId(savedTask.getId());
            activity.setTimestamp(System.currentTimeMillis());
            activity.setEventType(TaskEventType.CREATED);
            activity.setDescription("Task created with ID: " + savedTask.getId());
            activity.setUserId(item.getAssigneeId());
            taskRepository.saveActivity(activity);

            createdTaskDtos.add(findTaskById(savedTask.getId()));
        }
        return createdTaskDtos;
    }
    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagementDto> updatedTaskDtos = new ArrayList<>();
        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));

            TaskStatus oldStatus = task.getStatus();
            String oldDescription = task.getDescription();
            boolean changed = false;

            if (item.getTaskStatus() != null && item.getTaskStatus() != oldStatus) {
                task.setStatus(item.getTaskStatus());
                changed = true;

                TaskActivity activity = new TaskActivity();
                activity.setTaskId(task.getId());
                activity.setTimestamp(System.currentTimeMillis());
                activity.setEventType(TaskEventType.STATUS_CHANGED);
                activity.setDescription("Status changed from '" + oldStatus + "' to '" + item.getTaskStatus() + "'");
                taskRepository.saveActivity(activity);
            }
            if (item.getDescription() != null && !item.getDescription().equals(oldDescription)) {
                task.setDescription(item.getDescription());
                changed = true;

                TaskActivity activity = new TaskActivity();
                activity.setTaskId(task.getId());
                activity.setTimestamp(System.currentTimeMillis());
                activity.setDescription("Description updated to: \"" + item.getDescription() + "\"");
                taskRepository.saveActivity(activity);
            }
            if (changed) {
                taskRepository.save(task);
            }
            updatedTaskDtos.add(findTaskById(task.getId()));
        }
        return updatedTaskDtos;
    }

    @Override
    public String assignByReference(AssignByReferenceRequest request) {

        List<Task> applicableTaskTypes = Task.getTasksByReferenceType(request.getReferenceType());

        List<TaskManagement> existingTasksForReference =
                taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTaskTypes) {

            List<TaskManagement> activeTasksOfType = existingTasksForReference.stream()
                    .filter(t -> t.getTask() == taskType &&
                            (t.getStatus() == TaskStatus.ASSIGNED || t.getStatus() == TaskStatus.STARTED))
                    .collect(Collectors.toList());

            if (!activeTasksOfType.isEmpty()) {
                for (TaskManagement taskToUpdate : activeTasksOfType) {
                    Long oldAssigneeId = taskToUpdate.getAssigneeId();
                    taskToUpdate.setStatus(TaskStatus.CANCELLED);
                    taskRepository.save(taskToUpdate);


                    TaskActivity activity = new TaskActivity();
                    activity.setTaskId(taskToUpdate.getId());
                    activity.setTimestamp(System.currentTimeMillis());
                    activity.setEventType(TaskEventType.ASSIGNMENT_CHANGED);
                    activity.setDescription("Task (ID: " + taskToUpdate.getId() + ") cancelled due to reassignment from Assignee " + oldAssigneeId);
                    taskRepository.saveActivity(activity);
                }
            }
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(request.getReferenceId());
            newTask.setReferenceType(request.getReferenceType());
            newTask.setTask(taskType);
            newTask.setAssigneeId(request.getAssigneeId());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setPriority(Priority.MEDIUM);
            newTask.setTaskDeadlineTime(System.currentTimeMillis() + 86400000); // 1 day from now
            newTask.setDescription("Task reassigned to new assignee: " + request.getAssigneeId());
            TaskManagement savedNewTask = taskRepository.save(newTask);

            TaskActivity activity = new TaskActivity();
            activity.setTaskId(savedNewTask.getId());
            activity.setTimestamp(System.currentTimeMillis());
            activity.setEventType(TaskEventType.CREATED);
            activity.setDescription("New task created (ID: " + savedNewTask.getId() + ") for Assignee " + request.getAssigneeId() + " due to reassignment.");
            activity.setUserId(request.getAssigneeId());
            taskRepository.saveActivity(activity);
        }
        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }
    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> tasks =
                taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task -> {
                    if (task.getStatus() == TaskStatus.CANCELLED) {
                        return false;
                    }
                    if (task.getStatus() == TaskStatus.COMPLETED) {
                        return false;
                    }
                    Long taskDeadlineTime = task.getTaskDeadlineTime();
                    Long startDate = request.getStartDate();
                    Long endDate = request.getEndDate();

                    if (taskDeadlineTime == null) {
                        return true;
                    }
                    boolean isWithinRange = taskDeadlineTime >= startDate && taskDeadlineTime <= endDate;
                    boolean isActiveAndStartedBefore = taskDeadlineTime < startDate &&
                            (task.getStatus() == TaskStatus.ASSIGNED ||
                                    task.getStatus() == TaskStatus.STARTED);

                    return isWithinRange || isActiveAndStartedBefore;
                })
                .collect(Collectors.toList());

        return taskMapper.modelListToDtoList(filteredTasks);
    }
    @Override
    public TaskManagementDto updateTaskPriority(UpdateTaskPriorityRequest request) {
        TaskManagement task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));

        Priority oldPriority = task.getPriority();
        if (request.getNewPriority() != null && request.getNewPriority() != oldPriority) {
            task.setPriority(request.getNewPriority());
            TaskManagement updatedTask = taskRepository.save(task);

            TaskActivity activity = new TaskActivity();
            activity.setTaskId(updatedTask.getId());
            activity.setTimestamp(System.currentTimeMillis());
            activity.setEventType(TaskEventType.PRIORITY_CHANGED);
            activity.setDescription("Priority changed from " + oldPriority + " to " + request.getNewPriority());

            taskRepository.saveActivity(activity);

            return taskMapper.modelToDto(updatedTask);
        }
        return taskMapper.modelToDto(task);
    }
    @Override
    public List<TaskManagementDto> fetchTasksByPriority(Priority priority) {
        List<TaskManagement> tasks = taskRepository.findByPriority(priority);

        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED && task.getStatus() != TaskStatus.COMPLETED)
                .collect(Collectors.toList());
        return taskMapper.modelListToDtoList(filteredTasks);
    }
    @Override
    public TaskCommentDto addCommentToTask(AddCommentRequest request) {

        taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));

        TaskComment newComment = new TaskComment();
        newComment.setTaskId(request.getTaskId());
        newComment.setCommentText(request.getCommentText());
        newComment.setUserId(request.getUserId());
        newComment.setTimestamp(System.currentTimeMillis());

        TaskComment savedComment = taskRepository.saveComment(newComment);

        TaskActivity activity = new TaskActivity();
        activity.setTaskId(savedComment.getTaskId());
        activity.setTimestamp(System.currentTimeMillis());
        activity.setEventType(TaskEventType.COMMENT_ADDED);
        activity.setDescription("Comment added: \"" + savedComment.getCommentText() + "\" by User ID: " + savedComment.getUserId());
        activity.setUserId(savedComment.getUserId());
        taskRepository.saveActivity(activity);

        return taskMapper.commentToDto(savedComment);
    }
}