package com.example.workforcemgmt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskCommentDto {
    private Long id;
    private Long taskId;
    private Long timestamp;
    private String commentText;
    private Long userId;
}