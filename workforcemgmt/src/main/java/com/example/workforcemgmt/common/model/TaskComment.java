package com.example.workforcemgmt.common.model;

import lombok.Data;

@Data
public class TaskComment {
    private Long id;
    private Long taskId;
    private Long timestamp;
    private String commentText;
    private Long userId;
}