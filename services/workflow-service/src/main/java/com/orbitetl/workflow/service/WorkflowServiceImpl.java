package com.orbitetl.workflow.service;

import com.orbitetl.workflow.dto.WorkflowRequestDto;
import com.orbitetl.workflow.dto.WorkflowResponseDto;
import com.orbitetl.workflow.entity.Connection;
import com.orbitetl.workflow.entity.TransformationRule;
import com.orbitetl.workflow.entity.Workflow;
import com.orbitetl.workflow.entity.WorkflowTransformationRule;
import com.orbitetl.workflow.exception.ResourceNotFoundException;
import com.orbitetl.workflow.mapper.WorkflowMapper;
import com.orbitetl.workflow.repository.ConnectionRepository;
import com.orbitetl.workflow.repository.TransformationRuleRepository;
import com.orbitetl.workflow.repository.WorkflowRepository;
import com.orbitetl.workflow.repository.WorkflowTransformationRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowServiceImpl implements WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final ConnectionRepository connectionRepository;
    private final TransformationRuleRepository transformationRuleRepository;
    private final WorkflowTransformationRuleRepository workflowTransformationRuleRepository;
    private final WorkflowMapper workflowMapper;

    @Override
    @Transactional
    public WorkflowResponseDto createWorkflow(WorkflowRequestDto request) {
        log.info("Creating workflow with name: {}", request.getName());

        if (workflowRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException(
                    "Workflow with name '" + request.getName() + "' already exists"
            );
        }

        Connection sourceConnection = connectionRepository
                .findById(request.getSourceConnectionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Source connection not found with id: " + request.getSourceConnectionId()
                ));

        Connection targetConnection = connectionRepository
                .findById(request.getTargetConnectionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Target connection not found with id: " + request.getTargetConnectionId()
                ));

        Workflow workflow = Workflow.builder()
                .name(request.getName())
                .description(request.getDescription())
                .sourceConnection(sourceConnection)
                .targetConnection(targetConnection)
                .status("DRAFT")
                .transformationRules(new ArrayList<>())
                .build();

        Workflow saved = workflowRepository.saveAndFlush(workflow);

        if (request.getTransformationRules() != null) {
            List<WorkflowTransformationRule> rules = new ArrayList<>();

            request.getTransformationRules().forEach(ruleDto -> {
                TransformationRule rule = transformationRuleRepository
                        .findById(ruleDto.getTransformationRuleId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Transformation rule not found with id: "
                                        + ruleDto.getTransformationRuleId()
                        ));

                rules.add(WorkflowTransformationRule.builder()
                        .workflow(saved)
                        .transformationRule(rule)
                        .executionOrder(ruleDto.getExecutionOrder())
                        .build());
            });

            workflowTransformationRuleRepository.saveAll(rules);
        }

        log.info("Workflow created successfully with id: {}", saved.getId());
        return workflowMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkflowResponseDto getWorkflowById(UUID id) {
        log.info("Fetching workflow with id: {}", id);

        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workflow not found with id: " + id
                ));

        return workflowMapper.toResponseDto(workflow);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowResponseDto> getAllWorkflows() {
        log.info("Fetching all workflows");

        return workflowRepository.findAll()
                .stream()
                .map(workflowMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowResponseDto> getWorkflowsByStatus(String status) {
        log.info("Fetching workflows by status: {}", status);

        return workflowRepository.findByStatus(status)
                .stream()
                .map(workflowMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public WorkflowResponseDto updateWorkflow(UUID id, WorkflowRequestDto request) {
        log.info("Updating workflow with id: {}", id);

        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workflow not found with id: " + id
                ));

        Connection sourceConnection = connectionRepository
                .findById(request.getSourceConnectionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Source connection not found with id: " + request.getSourceConnectionId()
                ));

        Connection targetConnection = connectionRepository
                .findById(request.getTargetConnectionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Target connection not found with id: " + request.getTargetConnectionId()
                ));

        workflow.setName(request.getName());
        workflow.setDescription(request.getDescription());
        workflow.setSourceConnection(sourceConnection);
        workflow.setTargetConnection(targetConnection);

        if (request.getTransformationRules() != null) {
            workflowTransformationRuleRepository.deleteByWorkflowId(id);

            List<WorkflowTransformationRule> rules = new ArrayList<>();
            request.getTransformationRules().forEach(ruleDto -> {
                TransformationRule rule = transformationRuleRepository
                        .findById(ruleDto.getTransformationRuleId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Transformation rule not found with id: "
                                        + ruleDto.getTransformationRuleId()
                        ));

                rules.add(WorkflowTransformationRule.builder()
                        .workflow(workflow)
                        .transformationRule(rule)
                        .executionOrder(ruleDto.getExecutionOrder())
                        .build());
            });

            workflowTransformationRuleRepository.saveAll(rules);
        }

        Workflow updated = workflowRepository.saveAndFlush(workflow);
        log.info("Workflow updated successfully with id: {}", updated.getId());
        return workflowMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteWorkflow(UUID id) {
        log.info("Deleting workflow with id: {}", id);

        if (!workflowRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Workflow not found with id: " + id
            );
        }

        workflowTransformationRuleRepository.deleteByWorkflowId(id);
        workflowRepository.deleteById(id);
        log.info("Workflow deleted successfully with id: {}", id);
    }
}