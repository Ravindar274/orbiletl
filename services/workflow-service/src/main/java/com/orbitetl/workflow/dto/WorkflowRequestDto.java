package com.orbitetl.workflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowRequestDto {

    @NotBlank(message = "Workflow name is required")
    private String name;

    private String description;

    @NotNull(message = "Source connection is required")
    private UUID sourceConnectionId;

    @NotNull(message = "Target connection is required")
    private UUID targetConnectionId;

    private List<WorkflowTransformationRuleDto> transformationRules;
}