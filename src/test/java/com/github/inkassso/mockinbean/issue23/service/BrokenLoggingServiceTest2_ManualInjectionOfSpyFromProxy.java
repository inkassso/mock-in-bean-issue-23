package com.github.inkassso.mockinbean.issue23.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * A broken test, manually creating the spy from the {@link ProviderService} proxy and injecting that
 * into the {@link LoggingService}.
 *
 * Mockito doesn't recognize its own spy here either due to the same issue, it doesn't unwrap the proxy at mock creation,
 * but it does at invocation verification.
 */
@SpringBootTest
class BrokenLoggingServiceTest2_ManualInjectionOfSpyFromProxy {
    
    @Autowired
    protected LoggingService loggingService;
    @Autowired
    private ProviderService providerService;

    private ProviderService providerServiceSpy;

    @BeforeEach
    public void setUp() {
        providerServiceSpy = Mockito.spy(providerService);
        ReflectionTestUtils.setField(loggingService, "providerService", providerServiceSpy);
    }

    @AfterEach
    public void tearDown() {
        ReflectionTestUtils.setField(loggingService, "providerService", providerService);
    }

    @Test
    void testLogCurrentValue() {
        loggingService.logCurrentValue();

        verify(providerServiceSpy).provideValue();
    }
}