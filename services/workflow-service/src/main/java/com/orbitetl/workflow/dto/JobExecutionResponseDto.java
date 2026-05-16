package com.orbitetl.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionResponseDto {

    private UUID id;
    private UUID workflowId;
    private String workflowName;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Long rowsExtracted;
    private Long rowsTransformed;
    private Long rowsLoaded;
    private String errorMessage;
    private String triggeredBy;
    private LocalDateTime createdAt;
}