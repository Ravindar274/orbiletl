package com.orbitetl.workflow.service;

import com.orbitetl.workflow.dto.JobExecutionResponseDto;
import com.orbitetl.workflow.entity.JobExecution;
import com.orbitetl.workflow.exception.ResourceNotFoundException;
import com.orbitetl.workflow.mapper.JobExecutionMapper;
import com.orbitetl.workflow.repository.JobExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobExecutionServiceImpl implements JobExecutionService {

    private final JobExecutionRepository jobExecutionRepository;
    private final JobExecutionMapper jobExecutionMapper;

    @Override
    @Transactional(readOnly = true)
    public JobExecutionResponseDto getJobExecutionById(UUID id) {
        log.info("Fetching job execution with id: {}", id);

        JobExecution jobExecution = jobExecutionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job execution not found with id: " + id
                ));

        return jobExecutionMapper.toResponseDto(jobExecution);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobExecutionResponseDto> getAllJobExecutions() {
        log.info("Fetching all job executions");

        return jobExecutionRepository.findAll()
                .stream()
                .map(jobExecutionMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobExecutionResponseDto> getJobExecutionsByWorkflowId(UUID workflowId) {
        log.info("Fetching job executions for workflow id: {}", workflowId);

        return jobExecutionRepository
                .findByWorkflowIdOrderByCreatedAtDesc(workflowId)
                .stream()
                .map(jobExecutionMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobExecutionResponseDto> getJobExecutionsByStatus(String status) {
        log.info("Fetching job executions by status: {}", status);

        return jobExecutionRepository.findByStatus(status)
                .stream()
                .map(jobExecutionMapper::toResponseDto)
                .toList();
    }
}