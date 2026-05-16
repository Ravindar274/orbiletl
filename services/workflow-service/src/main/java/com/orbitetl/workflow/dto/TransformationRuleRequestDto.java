package com.orbitetl.workflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransformationRuleRequestDto {

    @NotBlank(message = "Rule name is required")
    private String name;

    @NotBlank(message = "Rule type is required")
    private String type;

    private String description;

    private Map<String, Object> configuration;
}