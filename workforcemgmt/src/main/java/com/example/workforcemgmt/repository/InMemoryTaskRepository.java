package com.example.workforcemgmt.repository;

import com.example.workforcemgmt.common.model.TaskActivity;
import com.example.workforcemgmt.common.model.TaskComment;
import com.example.workforcemgmt.common.model.TaskManagement;
import com.example.workforcemgmt.common.model.enums.Priority;
import com.example.workforcemgmt.common.model.enums.ReferenceType;
import com.example.workforcemgmt.common.model.enums.Task;
import com.example.workforcemgmt.common.model.enums.TaskStatus;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private final Map<Long, TaskManagement> taskStore = new ConcurrentHashMap<>();
    private final Map<Long, List<TaskActivity>> activityStore = new ConcurrentHashMap<>(); // Feature 3: Stores activities per taskId
    private final Map<Long, List<TaskComment>> commentStore = new ConcurrentHashMap<>(); // Feature 3: Stores comments per taskId
    private final AtomicLong taskIdCounter = new AtomicLong(0);
    private final AtomicLong activityIdCounter = new AtomicLong(0); // Feature 3: Counter for activity IDs
    private final AtomicLong commentIdCounter = new AtomicLong(0); // Feature 3: Counter for comment IDs


    public InMemoryTaskRepository() {

        createSeedTask(101L, ReferenceType.ORDER, Task.CREATE_INVOICE, 1L,
                TaskStatus.ASSIGNED, Priority.HIGH);
        createSeedTask(101L, ReferenceType.ORDER, Task.ARRANGE_PICKUP, 1L,
                TaskStatus.COMPLETED, Priority.HIGH);
        createSeedTask(102L, ReferenceType.ORDER, Task.CREATE_INVOICE, 2L,
                TaskStatus.ASSIGNED, Priority.MEDIUM);
        createSeedTask(201L, ReferenceType.ENTITY,
                Task.ASSIGN_CUSTOMER_TO_SALES_PERSON, 2L, TaskStatus.ASSIGNED,
                Priority.LOW);
        createSeedTask(201L, ReferenceType.ENTITY,
                Task.ASSIGN_CUSTOMER_TO_SALES_PERSON, 3L, TaskStatus.ASSIGNED,
                Priority.LOW); // Duplicate for Bug #1 scenario
        createSeedTask(103L, ReferenceType.ORDER, Task.COLLECT_PAYMENT, 1L,
                TaskStatus.CANCELLED, Priority.MEDIUM); // For Bug #2 scenario
    }

    private void createSeedTask(Long refId, ReferenceType refType, Task
            task, Long assigneeId, TaskStatus status, Priority priority) {
        long newId = taskIdCounter.incrementAndGet();
        TaskManagement newTask = new TaskManagement();
        newTask.setId(newId);
        newTask.setReferenceId(refId);
        newTask.setReferenceType(refType);
        newTask.setTask(task);
        newTask.setAssigneeId(assigneeId);
        newTask.setStatus(status);
        newTask.setPriority(priority);
        newTask.setDescription("This is a seed task.");
        newTask.setTaskDeadlineTime(System.currentTimeMillis() + 86400000); // 1 day from now
        taskStore.put(newId, newTask);
    }

    @Override
    public Optional<TaskManagement> findById(Long id) {
        return Optional.ofNullable(taskStore.get(id));
    }

    @Override
    public TaskManagement save(TaskManagement task) {
        if (task.getId() == null) {
            task.setId(taskIdCounter.incrementAndGet());
        }
        taskStore.put(task.getId(), task);
        return task;
    }

    @Override
    public List<TaskManagement> findAll() {
        return List.copyOf(taskStore.values());
    }

    @Override
    public List<TaskManagement> findByReferenceIdAndReferenceType(Long
                                                                          referenceId, ReferenceType referenceType) {
        return taskStore.values().stream()
                .filter(task -> task.getReferenceId().equals(referenceId)
                        && task.getReferenceType().equals(referenceType))
                .collect(Collectors.toList());
    }
    @Override
    public List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds)
    {
        return taskStore.values().stream()
                .filter(task -> assigneeIds.contains(task.getAssigneeId()))
                .collect(Collectors.toList());
    }
    @Override
    public List<TaskManagement> findByPriority(Priority priority) {
        return taskStore.values().stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }
    @Override
    public TaskActivity saveActivity(TaskActivity activity) {
        if (activity.getId() == null) {
            activity.setId(activityIdCounter.incrementAndGet());
        }
        activityStore.computeIfAbsent(activity.getTaskId(), k -> new ArrayList<>()).add(activity);
        return activity;
    }
    @Override
    public List<TaskActivity> findActivitiesByTaskId(Long taskId) {
        return activityStore.getOrDefault(taskId, new ArrayList<>()).stream()
                .sorted(Comparator.comparing(TaskActivity::getTimestamp))
                .collect(Collectors.toList());
    }
    @Override
    public TaskComment saveComment(TaskComment comment) {
        if (comment.getId() == null) {
            comment.setId(commentIdCounter.incrementAndGet());
        }
        commentStore.computeIfAbsent(comment.getTaskId(), k -> new ArrayList<>()).add(comment);
        return comment;
    }
    @Override
    public List<TaskComment> findCommentsByTaskId(Long taskId) {
        return commentStore.getOrDefault(taskId, new ArrayList<>()).stream()
                .sorted(Comparator.comparing(TaskComment::getTimestamp))
                .collect(Collectors.toList());
    }
}