package com.github.inkassso.mockinbean.issue23.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpringBootMockResolver;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * A working test, manually unwrapping the {@link ProviderService} proxy and creating the spy from the actual service instance,
 * before injecting it manually into the proxy. The proxy detection works same as in {@link SpringBootMockResolver}.
 * Then, during mock creation the information is stored under the hashCode of the actual service instance and,
 * at invocation verification, Mockito automatically unwraps the proxy, computes the hashCode from the spy
 * on the actual service instance and correctly finds the mock information.
 *
 * The aspect is invoked in this case, as the intercepting proxy is kept.
 */
@SpringBootTest
class WorkingLoggingServiceTest4_ManualInjectionWithinProxy {
    
    @Autowired
    protected LoggingService loggingService;
    @Autowired
    private ProviderService providerService;

    @BeforeEach
    public void setUp() throws Exception {
        Object beanToReplaceIn;
        String fieldToReplace;
        ProviderService providerServiceSpy;

        TargetSource targetSource = getProxyContainingUltimateTarget(providerService);
        if (targetSource == null) {
            beanToReplaceIn = loggingService;
            fieldToReplace = "providerService";
            providerServiceSpy = Mockito.spy(providerService);
        } else {
            beanToReplaceIn = targetSource;
            fieldToReplace = "target";
            providerServiceSpy = Mockito.spy((ProviderService) targetSource.getTarget());
        }

        ReflectionTestUtils.setField(beanToReplaceIn, fieldToReplace, providerServiceSpy);
    }

    private TargetSource getProxyContainingUltimateTarget(Object candidate) {
        try {
            if (AopUtils.isAopProxy(candidate) && candidate instanceof Advised advised) {
                TargetSource targetSource = advised.getTargetSource();
                if (targetSource.isStatic()) {
                    Object target = targetSource.getTarget();
                    if (target != null) {
                        if (!AopUtils.isAopProxy(target) || !(target instanceof Advised)) {
                            return advised.getTargetSource();
                        }
                        return getProxyContainingUltimateTarget(target);
                    }
                }
            }
        }
        catch (Throwable ex) {
            throw new IllegalStateException("Failed to unwrap proxied object", ex);
        }
        return null;
    }

    @AfterEach
    public void tearDown() {
        ReflectionTestUtils.setField(loggingService, "providerService", providerService);
    }

    @Test
    void testLogCurrentValue() {
        loggingService.logCurrentValue();

        verify(providerService).provideValue();
    }
}