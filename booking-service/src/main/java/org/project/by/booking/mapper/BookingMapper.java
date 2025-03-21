package org.project.by.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.project.by.booking.entity.Ride;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.BookingRequestEvent;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface BookingMapper {

    @Mapping(source = "passengerId", target = "passengerId")
    @Mapping(target = "status", source = "status")
    @Mapping(source = "initialLocation", target = "initialLocation")
    @Mapping(source = "destinationLocation", target = "destinationLocation")
    @Mapping(source = "distance", target = "distance")
    Ride fromDto(BookingRequestEvent dto);

    @Mapping(source = "status", target = "status")
    @Mapping(source = "insertedAt", target = "date", qualifiedByName = "mapToDate")
    RidesDto toRidesDto(Ride ride);

    @Named("mapToDate")
    default LocalDate mapToDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    default PageDto<RidesDto> toRideDtoPage(Page<Ride> rides) {
        return new PageDto<>(rides.map(this::toRidesDto));
    }

}
