package org.project.by.passenger.feign;

import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "booking-service/api/booking")
public interface BookingClient {

    @GetMapping("/history/passenger/{passengerId}")
    PageDto<RidesDto> getPassengerHistory(@PathVariable("passengerId") Long passengerId, Pageable pageable);

}
