package org.project.by.driver.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.project.by.driver.dto.DriverDto;
import org.project.by.driver.entity.Driver;

@Mapper
public interface DriverMapper {

    @Mapping(target = "driverId", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "car", source = "car")
    @Mapping(target = "rating", source = "rating")
    DriverDto toDtoDriver(Driver driver);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDriverFromDto(DriverDto dto, @MappingTarget Driver driver);

}
