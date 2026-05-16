package com.orbitetl.workflow.mapper;

import com.orbitetl.workflow.dto.ConnectionRequestDto;
import com.orbitetl.workflow.dto.ConnectionResponseDto;
import com.orbitetl.workflow.entity.Connection;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ConnectionMapper {

    Connection toEntity(ConnectionRequestDto dto);

    ConnectionResponseDto toResponseDto(Connection entity);

    void updateEntityFromDto(ConnectionRequestDto dto, @MappingTarget Connection entity);
}