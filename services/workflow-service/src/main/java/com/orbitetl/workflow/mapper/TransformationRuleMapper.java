package com.orbitetl.workflow.mapper;


import com.orbitetl.workflow.dto.TransformationRuleRequestDto;
import com.orbitetl.workflow.dto.TransformationRuleResponseDto;
import com.orbitetl.workflow.entity.TransformationRule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TransformationRuleMapper {

    TransformationRule toEntity(TransformationRuleRequestDto dto);

    TransformationRuleResponseDto toResponseDto(TransformationRule entity);

    void updateEntityFromDto(TransformationRuleRequestDto dto,
                             @MappingTarget TransformationRule entity);
}