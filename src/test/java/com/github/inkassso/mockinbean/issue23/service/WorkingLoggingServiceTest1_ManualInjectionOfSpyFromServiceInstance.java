package com.github.inkassso.mockinbean.issue23.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpringBootMockResolver;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * A working test, manually unwrapping the {@link ProviderService} proxy and creating the spy from the actual service instance,
 * before injecting it manually into the {@link LoggingService}, effectively bypassing the proxy.
 * Then, during mock creation the information is stored under the hashCode of the actual service instance and,
 * at invocation verification, Mockito has nothing to unwrap and finds the mock information under the same hashCode.
 *
 * The aspect is not invoked in this case, due to the proxy being removed. See {@link WorkingLoggingServiceTest4_ManualInjectionWithinProxy} where the proxy
 * is not bypassed and the aspect keeps working.
 */
@SpringBootTest
class WorkingLoggingServiceTest1_ManualInjectionOfSpyFromServiceInstance {
    
    @Autowired
    protected LoggingService loggingService;
    @Autowired
    private ProviderService providerService;

    private ProviderService providerServiceSpy;

    @BeforeEach
    public void setUp() {
        ProviderService unwrappedService = (ProviderService) new SpringBootMockResolver().resolve(providerService);
        providerServiceSpy = Mockito.spy(unwrappedService);
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