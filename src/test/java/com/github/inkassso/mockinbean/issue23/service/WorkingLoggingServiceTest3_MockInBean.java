package com.github.inkassso.mockinbean.issue23.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * A working test, creating a mock using {@link MockInBean} from the service proxy and automatically replacing it
 * in the {@link LoggingService} and the test class.
 */
@SpringBootTest
class WorkingLoggingServiceTest3_MockInBean {

    @Autowired
    protected LoggingService loggingService;

    @MockInBean(LoggingService.class)
    private ProviderService providerService;

    @Test
    void testLogCurrentValue() {
        when(providerService.provideValue()).thenReturn("mocked value");

        loggingService.logCurrentValue();

        verify(providerService).provideValue();
    }
}