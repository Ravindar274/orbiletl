package com.orbitetl.config.mapper;

import com.orbitetl.config.dto.ConnectionRequestDto;
import com.orbitetl.config.dto.ConnectionResponseDto;
import com.orbitetl.config.entity.Connection;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ConnectionMapper {

    Connection toEntity(ConnectionRequestDto dto);

    ConnectionResponseDto toResponseDto(Connection entity);

    void updateEntityFromDto(ConnectionRequestDto dto, @MappingTarget Connection entity);
}