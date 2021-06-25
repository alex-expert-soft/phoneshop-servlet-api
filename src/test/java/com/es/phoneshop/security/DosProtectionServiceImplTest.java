package com.es.phoneshop.security;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DosProtectionServiceImplTest {
    private DosProtectionServiceImpl dosService;
    private static final long THRESHOLD = 600;

    @Before
    public void setup() {
        dosService = DosProtectionServiceImpl.getInstance();
    }

    @Test
    public void isAllowedSuccess() {
        boolean result = true;
        for (int i = 0; i < THRESHOLD - 10; i++) {
            result = dosService.isAllowed("192.168.5.1");
        }
        assertTrue(result);
    }

    @Test
    public void isAllowedFail() {
        boolean result = true;
        for (int i = 0; i < THRESHOLD + 10; i++) {
            result = dosService.isAllowed("192.168.5.2");
        }
        assertFalse(result);

    }
}