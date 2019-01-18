package com.amydegregorio.mappers.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.amydegregorio.mappers.domain.Task;

@Mapper
public interface TaskMapper {
   TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
   TaskDto outgoing(Task task);
   Task incoming(TaskDto taskDto);
}
