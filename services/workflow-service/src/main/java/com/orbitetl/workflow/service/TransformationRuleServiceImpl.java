package com.orbitetl.workflow.service;

import com.orbitetl.workflow.dto.TransformationRuleRequestDto;
import com.orbitetl.workflow.dto.TransformationRuleResponseDto;
import com.orbitetl.workflow.entity.TransformationRule;
import com.orbitetl.workflow.exception.ResourceNotFoundException;
import com.orbitetl.workflow.mapper.TransformationRuleMapper;
import com.orbitetl.workflow.repository.TransformationRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransformationRuleServiceImpl implements TransformationRuleService {

    private final TransformationRuleRepository transformationRuleRepository;
    private final TransformationRuleMapper transformationRuleMapper;

    @Override
    @Transactional
    public TransformationRuleResponseDto createRule(TransformationRuleRequestDto request) {
        log.info("Creating transformation rule with name: {}", request.getName());

        if (transformationRuleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException(
                    "Transformation rule with name '" + request.getName() + "' already exists"
            );
        }

        TransformationRule rule = transformationRuleMapper.toEntity(request);
        TransformationRule saved = transformationRuleRepository.saveAndFlush(rule);

        log.info("Transformation rule created successfully with id: {}", saved.getId());
        return transformationRuleMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TransformationRuleResponseDto getRuleById(UUID id) {
        log.info("Fetching transformation rule with id: {}", id);

        TransformationRule rule = transformationRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transformation rule not found with id: " + id
                ));

        return transformationRuleMapper.toResponseDto(rule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransformationRuleResponseDto> getAllRules() {
        log.info("Fetching all transformation rules");

        return transformationRuleRepository.findAll()
                .stream()
                .map(transformationRuleMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransformationRuleResponseDto> getRulesByType(String type) {
        log.info("Fetching transformation rules by type: {}", type);

        return transformationRuleRepository.findByType(type)
                .stream()
                .map(transformationRuleMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public TransformationRuleResponseDto updateRule(UUID id,
                                                    TransformationRuleRequestDto request) {
        log.info("Updating transformation rule with id: {}", id);

        TransformationRule rule = transformationRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transformation rule not found with id: " + id
                ));

        transformationRuleMapper.updateEntityFromDto(request, rule);
        TransformationRule updated = transformationRuleRepository.saveAndFlush(rule);

        log.info("Transformation rule updated successfully with id: {}", updated.getId());
        return transformationRuleMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteRule(UUID id) {
        log.info("Deleting transformation rule with id: {}", id);

        if (!transformationRuleRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Transformation rule not found with id: " + id
            );
        }

        transformationRuleRepository.deleteById(id);
        log.info("Transformation rule deleted successfully with id: {}", id);
    }
}