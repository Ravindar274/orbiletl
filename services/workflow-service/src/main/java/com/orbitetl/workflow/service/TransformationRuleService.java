package com.orbitetl.workflow.service;

import com.orbitetl.workflow.dto.TransformationRuleRequestDto;
import com.orbitetl.workflow.dto.TransformationRuleResponseDto;

import java.util.List;
import java.util.UUID;

public interface TransformationRuleService {

    TransformationRuleResponseDto createRule(TransformationRuleRequestDto request);

    TransformationRuleResponseDto getRuleById(UUID id);

    List<TransformationRuleResponseDto> getAllRules();

    List<TransformationRuleResponseDto> getRulesByType(String type);

    TransformationRuleResponseDto updateRule(UUID id, TransformationRuleRequestDto request);

    void deleteRule(UUID id);
}