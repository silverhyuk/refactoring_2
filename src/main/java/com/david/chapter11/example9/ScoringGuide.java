package com.david.chapter11.example9;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 점수 가이드 클래스
 */
public class ScoringGuide {
    private static final Set<String> LOW_CERTIFICATION_STATES = 
            new HashSet<>(Arrays.asList("WY", "AL", "MN"));
    
    public boolean stateWithLowCertification(String state) {
        return LOW_CERTIFICATION_STATES.contains(state);
    }
} 