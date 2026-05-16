package com.orbitetl.workflow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTransformationRuleDto {

    @NotNull(message = "Transformation rule ID is required")
    private UUID transformationRuleId;

    @NotNull(message = "Execution order is required")
    private Integer executionOrder;
}