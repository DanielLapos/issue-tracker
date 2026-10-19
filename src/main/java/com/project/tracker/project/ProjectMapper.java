package com.project.tracker.project;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper{
    ProjectResponseRecord toRecord(Project project);
    Project toProject(ProjectRequestRecord record);
    List<ProjectResponseRecord> toList(List<Project> list);
}
