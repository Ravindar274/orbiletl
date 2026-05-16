package com.orbitetl.workflow.controller;

import com.orbitetl.workflow.dto.JobExecutionResponseDto;
import com.orbitetl.workflow.service.JobExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/job-executions")
@RequiredArgsConstructor
@Slf4j
public class JobExecutionController {

    private final JobExecutionService jobExecutionService;

    @GetMapping("/{id}")
    public ResponseEntity<JobExecutionResponseDto> getJobExecutionById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(jobExecutionService.getJobExecutionById(id));
    }

    @GetMapping
    public ResponseEntity<List<JobExecutionResponseDto>> getJobExecutions(
            @RequestParam(required = false) UUID workflowId,
            @RequestParam(required = false) String status) {

        if (workflowId != null) {
            return ResponseEntity.ok(
                    jobExecutionService.getJobExecutionsByWorkflowId(workflowId)
            );
        }
        if (status != null) {
            return ResponseEntity.ok(
                    jobExecutionService.getJobExecutionsByStatus(status)
            );
        }
        return ResponseEntity.ok(jobExecutionService.getAllJobExecutions());
    }
}