package com.example.workforcemgmt.mapper;

import com.example.workforcemgmt.common.model.TaskActivity;
import com.example.workforcemgmt.common.model.TaskComment;
import com.example.workforcemgmt.common.model.TaskManagement;
import com.example.workforcemgmt.dto.TaskActivityDto;
import com.example.workforcemgmt.dto.TaskCommentDto;
import com.example.workforcemgmt.dto.TaskManagementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
public interface ITaskManagementMapper {
    ITaskManagementMapper INSTANCE = Mappers.getMapper(ITaskManagementMapper.class);

    @Mapping(target = "activityHistory", ignore = true)
    @Mapping(target = "comments", ignore = true)
    TaskManagementDto modelToDto(TaskManagement model);

    TaskManagement dtoToModel(TaskManagementDto dto);
    List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models);

    TaskActivityDto activityToDto(TaskActivity activity);

    default List<TaskActivityDto> activityListToDtoList(List<TaskActivity> activities) {
        if (activities == null) {
            return Collections.emptyList();
        }
        return activities.stream()
                .map(this::activityToDto)
                .collect(Collectors.toList());
    }

    TaskCommentDto commentToDto(TaskComment comment);

    default List<TaskCommentDto> commentListToDtoList(List<TaskComment> comments) {
        if (comments == null) {
            return Collections.emptyList();
        }
        return comments.stream()
                .map(this::commentToDto)
                .collect(Collectors.toList());
    }

    TaskActivity dtoToActivity(TaskActivityDto activityDto);

    TaskComment dtoToComment(TaskCommentDto commentDto);
}