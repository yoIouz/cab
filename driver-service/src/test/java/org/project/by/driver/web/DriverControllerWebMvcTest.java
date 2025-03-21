package org.project.by.driver.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.by.driver.SucceededWatcher;
import org.project.by.driver.controller.DriverController;
import org.project.by.driver.dto.DriverDto;
import org.project.by.driver.service.impl.DriverServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SucceededWatcher.class)
@WebMvcTest(controllers = DriverController.class,
        excludeAutoConfiguration = {
                OAuth2ResourceServerAutoConfiguration.class,
                SecurityAutoConfiguration.class})
public class DriverControllerWebMvcTest {

    private static final String API_URL = "/api/driver";

    private static ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DriverServiceImpl driverService;

    @BeforeAll
    static void setupMapper() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnPassengerProfile() throws Exception {
        Long driverId = 1L;
        DriverDto driverDto = new DriverDto(driverId, "Иван",
                "Иванов", null);

        when(driverService.getDriverProfile(driverId)).thenReturn(driverDto);
        mockMvc.perform(get(API_URL.concat("/{id}"), driverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverId").value(driverId));
        verify(driverService).getDriverProfile(driverId);
    }

    @Test
    void shouldEditProfile() throws Exception {
        Long driverId = 1L;
        DriverDto driverDto = new DriverDto(driverId, "Иван",
                "Иванов", null);
        mockMvc.perform(patch(API_URL.concat("/edit"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isOk());
        verify(driverService).editProfile(driverDto);
    }

}
