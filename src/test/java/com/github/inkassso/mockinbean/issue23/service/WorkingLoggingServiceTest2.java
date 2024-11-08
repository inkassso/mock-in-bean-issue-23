package com.github.inkassso.mockinbean.issue23.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

/**
 * A working test using Spring's {@link SpyBean}, which dirties the context.
 *
 * Spring is able to create the spy during bean creation and then wrap the spy (not the actual service instance) in the proxy.
 * This keeps the Aspect working while the spy is enabled and of course Mockito is then able to work with the spy correctly.
 */
@SpringBootTest
class WorkingLoggingServiceTest2 {
    
    @Autowired
    protected LoggingService loggingService;
    @SpyBean
    private ProviderService providerService;

    @Test
    void testLogCurrentValue() {
        loggingService.logCurrentValue();

        verify(providerService).provideValue();
    }
}