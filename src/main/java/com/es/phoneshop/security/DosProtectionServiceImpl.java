package com.es.phoneshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DosProtectionServiceImpl implements DosProtectionService {

    private static volatile DosProtectionServiceImpl instance;

    private DosProtectionServiceImpl() {
    }

    public static DosProtectionServiceImpl getInstance() {
        if (instance == null) {
            synchronized (DosProtectionServiceImpl.class) {
                if (instance == null) {
                    instance = new DosProtectionServiceImpl();
                }
            }
        }
        return instance;
    }

    private static final long THRESHOLD = 600;

    private final Map<String, Long> countMap = new ConcurrentHashMap<>();

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}
