package com.orbitetl.workflow.controller;

import com.orbitetl.workflow.dto.WorkflowRequestDto;
import com.orbitetl.workflow.dto.WorkflowResponseDto;
import com.orbitetl.workflow.service.WorkflowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workflows")
@RequiredArgsConstructor
@Slf4j
public class WorkflowController {

    private final WorkflowService workflowService;

    @PostMapping
    public ResponseEntity<WorkflowResponseDto> createWorkflow(
            @Valid @RequestBody WorkflowRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workflowService.createWorkflow(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowResponseDto> getWorkflowById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(workflowService.getWorkflowById(id));
    }

    @GetMapping
    public ResponseEntity<List<WorkflowResponseDto>> getAllWorkflows(
            @RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(workflowService.getWorkflowsByStatus(status));
        }
        return ResponseEntity.ok(workflowService.getAllWorkflows());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkflowResponseDto> updateWorkflow(
            @PathVariable UUID id,
            @Valid @RequestBody WorkflowRequestDto request) {
        return ResponseEntity.ok(workflowService.updateWorkflow(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable UUID id) {
        workflowService.deleteWorkflow(id);
        return ResponseEntity.noContent().build();
    }
}