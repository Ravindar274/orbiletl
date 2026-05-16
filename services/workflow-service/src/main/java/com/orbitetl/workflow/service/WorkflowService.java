package com.orbitetl.workflow.service;

import com.orbitetl.workflow.dto.WorkflowRequestDto;
import com.orbitetl.workflow.dto.WorkflowResponseDto;

import java.util.List;
import java.util.UUID;

public interface WorkflowService {

    WorkflowResponseDto createWorkflow(WorkflowRequestDto request);

    WorkflowResponseDto getWorkflowById(UUID id);

    List<WorkflowResponseDto> getAllWorkflows();

    List<WorkflowResponseDto> getWorkflowsByStatus(String status);

    WorkflowResponseDto updateWorkflow(UUID id, WorkflowRequestDto request);

    void deleteWorkflow(UUID id);
}