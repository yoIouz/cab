package org.project.by.passenger.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.project.by.passenger.dto.PassengerDto;
import org.project.by.passenger.entity.Passenger;

@Mapper
public interface PassengerMapper {

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    PassengerDto toPassengerDto(Passenger passenger);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePassengerFromDto(PassengerDto dto, @MappingTarget Passenger passenger);

}
