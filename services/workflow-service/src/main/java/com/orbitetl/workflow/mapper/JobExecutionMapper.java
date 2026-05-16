package com.orbitetl.workflow.mapper;

import com.orbitetl.workflow.dto.JobExecutionResponseDto;
import com.orbitetl.workflow.entity.JobExecution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface JobExecutionMapper {

    @Mapping(source = "workflow.id", target = "workflowId")
    @Mapping(source = "workflow.name", target = "workflowName")
    JobExecutionResponseDto toResponseDto(JobExecution entity);
}