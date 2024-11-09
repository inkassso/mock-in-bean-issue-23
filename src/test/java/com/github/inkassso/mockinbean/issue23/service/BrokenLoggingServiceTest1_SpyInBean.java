package com.github.inkassso.mockinbean.issue23.service;

import static org.mockito.Mockito.verify;

import com.teketik.test.mockinbean.SpyInBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * A broken test, creating the spy using {@link SpyInBean} from the service proxy and automatically replacing it
 * in the {@link LoggingService} and the test class.
 *
 * Apparently, Mockito is not able to recognize its own mock within {@link org.mockito.Mockito#verify(Object)},
 * because it doesn't unwrap the proxy when creating the mock, and thus stores information about the mock
 * in a Map under a hashCode of the proxy.
 * Then during verification, when looking up the mock information again, it actually does unwrap the proxy beforehand,
 * therefore it is unable to find the mock information anymore, because the hashCode of the proxy and hashCode the actual
 * service instance do not match.
 *
 * Debug {@code org.mockito.internal.creation.bytebuddy.InlineDelegateByteBuddyMockMaker#getHandler(Object)}
 * where the lookup happens.
 * See {@link org.springframework.boot.test.mock.mockito.SpringBootMockResolver} responsible for unwrapping the proxy.
 */
@SpringBootTest
class BrokenLoggingServiceTest1_SpyInBean {

    @Autowired
    protected LoggingService loggingService;

    @SpyInBean(LoggingService.class)
    private ProviderService providerService;

    @Test
    void testLogCurrentValue() {
        loggingService.logCurrentValue();

        verify(providerService).provideValue();
    }
}