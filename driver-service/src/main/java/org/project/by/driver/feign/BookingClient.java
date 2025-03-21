package org.project.by.driver.feign;

import org.project.by.common.constants.dto.PageDto;
import org.project.by.common.constants.dto.RidesDto;
import org.project.by.common.constants.dto.event.RideEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "booking-service/api/booking")
public interface BookingClient {

    @GetMapping("/history/driver/{driverId}")
    PageDto<RidesDto> getDriverHistory(@PathVariable("driverId") Long driverId, Pageable pageable);

    @PostMapping("/ride/cancel")
    void cancelRide(@RequestBody RideEvent rideCancelledEvent);

}
