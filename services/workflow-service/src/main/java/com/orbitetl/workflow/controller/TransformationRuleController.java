package com.orbitetl.workflow.controller;

import com.orbitetl.workflow.dto.TransformationRuleRequestDto;
import com.orbitetl.workflow.dto.TransformationRuleResponseDto;
import com.orbitetl.workflow.service.TransformationRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transformation-rules")
@RequiredArgsConstructor
@Slf4j
public class TransformationRuleController {

    private final TransformationRuleService transformationRuleService;

    @PostMapping
    public ResponseEntity<TransformationRuleResponseDto> createRule(
            @Valid @RequestBody TransformationRuleRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transformationRuleService.createRule(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransformationRuleResponseDto> getRuleById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(transformationRuleService.getRuleById(id));
    }

    @GetMapping
    public ResponseEntity<List<TransformationRuleResponseDto>> getAllRules(
            @RequestParam(required = false) String type) {
        if (type != null) {
            return ResponseEntity.ok(transformationRuleService.getRulesByType(type));
        }
        return ResponseEntity.ok(transformationRuleService.getAllRules());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransformationRuleResponseDto> updateRule(
            @PathVariable UUID id,
            @Valid @RequestBody TransformationRuleRequestDto request) {
        return ResponseEntity.ok(transformationRuleService.updateRule(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        transformationRuleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}