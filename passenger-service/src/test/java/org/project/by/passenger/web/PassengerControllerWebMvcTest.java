package org.project.by.passenger.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.project.by.common.constants.dto.Location;
import org.project.by.common.constants.dto.event.BookingRequestEvent;
import org.project.by.common.constants.dto.event.UserRatingEvent;
import org.project.by.passenger.SucceededWatcher;
import org.project.by.passenger.controller.PassengerController;
import org.project.by.passenger.dto.PassengerDto;
import org.project.by.passenger.keycloak.KeycloakClient;
import org.project.by.passenger.service.impl.PassengerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SucceededWatcher.class)
@WebMvcTest(controllers = PassengerController.class,
        excludeAutoConfiguration = {
                OAuth2ResourceServerAutoConfiguration.class,
                SecurityAutoConfiguration.class})
public class PassengerControllerWebMvcTest {

    private static final String API_URL = "/api/passenger";

    private static ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KeycloakClient keycloakClient;

    @MockitoBean
    private PassengerServiceImpl passengerService;

    @BeforeAll
    static void setupMapper() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnPassengerProfile() throws Exception {
        Long passengerId = 1L;
        PassengerDto passengerDto = new PassengerDto(passengerId, "Иван",
                "Иванов", "iv.ivanov@gmail.com");

        when(passengerService.getPassenger(passengerId)).thenReturn(passengerDto);
        mockMvc.perform(get(API_URL.concat("/{id}"), passengerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(passengerId));
        verify(passengerService).getPassenger(passengerId);
    }

    @Test
    void shouldEditProfile() throws Exception {
        Long passengerId = 1L;
        PassengerDto passengerDto = new PassengerDto(passengerId, "Иван",
                "Иванов", "iv.ivanov@gmail.com");
        mockMvc.perform(patch(API_URL.concat("/edit"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passengerDto)))
                .andExpect(status().isOk());
        verify(passengerService).editPassenger(passengerDto);
    }

    @Test
    void shouldCreateOrder() throws Exception {
        BookingRequestEvent dto = new BookingRequestEvent(
                1L, new Location(0.0, 0.0), new Location(0.0, 0.0),
                1L, null, null, null, null);
        mockMvc.perform(post(API_URL.concat("/create"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        ArgumentCaptor<BookingRequestEvent> captor = ArgumentCaptor.forClass(BookingRequestEvent.class);
        verify(passengerService).requestBooking(captor.capture());

        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(dto);
    }

    @Test
    void shouldRateDriver() throws Exception {
        UserRatingEvent userRatingEvent = new UserRatingEvent(1L, 3);
        mockMvc.perform(post(API_URL.concat("/rate"))
                        .content(objectMapper.writeValueAsString(userRatingEvent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(passengerService).rateDriver(userRatingEvent);
    }

}
