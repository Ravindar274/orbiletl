package com.orbitetl.workflow.service;

import com.orbitetl.workflow.dto.JobExecutionResponseDto;

import java.util.List;
import java.util.UUID;

public interface JobExecutionService {

    JobExecutionResponseDto getJobExecutionById(UUID id);

    List<JobExecutionResponseDto> getAllJobExecutions();

    List<JobExecutionResponseDto> getJobExecutionsByWorkflowId(UUID workflowId);

    List<JobExecutionResponseDto> getJobExecutionsByStatus(String status);
}