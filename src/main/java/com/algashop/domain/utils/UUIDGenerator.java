package com.algashop.domain.utils;

import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;

public class UUIDGenerator {

    private static final TimeBasedEpochRandomGenerator timeBasedEpochRandomGenerator = 
        Generators.timeBasedEpochRandomGenerator();
    
    private UUIDGenerator() {}

    public static UUID generateTimeBaseUuid() {
        return timeBasedEpochRandomGenerator.generate();
    }
}
