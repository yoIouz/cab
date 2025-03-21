package org.project.by.payment.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.by.common.constants.dto.PageDto;
import org.project.by.payment.testcontainers.PostgresTestContainer;
import org.project.by.payment.SucceededWatcher;
import org.project.by.payment.config.NoKafkaConfiguration;
import org.project.by.payment.service.impl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(NoKafkaConfiguration.class)
@ExtendWith(SucceededWatcher.class)
@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
public class PaymentControllerTest implements PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentServiceImpl paymentService;

    @Test
    void shouldPingTransactions() throws Exception {
        Long userId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(paymentService.findUserTransactions(userId, pageRequest))
                .thenReturn(PageDto.empty());
        mockMvc.perform(get("/api/payment/transactions/{id}", userId)
                        .queryParam("page", "0")
                        .queryParam("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
        verify(paymentService).findUserTransactions(userId, pageRequest);
    }

}
