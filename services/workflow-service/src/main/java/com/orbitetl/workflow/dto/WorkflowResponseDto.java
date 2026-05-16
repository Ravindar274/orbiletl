package com.orbitetl.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowResponseDto {

    private UUID id;
    private String name;
    private String description;
    private UUID sourceConnectionId;
    private String sourceConnectionName;
    private UUID targetConnectionId;
    private String targetConnectionName;
    private String status;
    @Builder.Default
    private List<WorkflowTransformationRuleDto> transformationRules = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}