package com.orbitetl.workflow.mapper;

import com.orbitetl.workflow.dto.WorkflowResponseDto;
import com.orbitetl.workflow.dto.WorkflowTransformationRuleDto;
import com.orbitetl.workflow.entity.Workflow;
import com.orbitetl.workflow.entity.WorkflowTransformationRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface WorkflowMapper {

    @Mapping(source = "sourceConnection.id", target = "sourceConnectionId")
    @Mapping(source = "sourceConnection.name", target = "sourceConnectionName")
    @Mapping(source = "targetConnection.id", target = "targetConnectionId")
    @Mapping(source = "targetConnection.name", target = "targetConnectionName")
    WorkflowResponseDto toResponseDto(Workflow entity);

    @Mapping(source = "transformationRule.id", target = "transformationRuleId")
    WorkflowTransformationRuleDto toRuleDto(WorkflowTransformationRule entity);
}