package com.algashop.domain.utils;

import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;

import io.hypersistence.tsid.TSID;

public class IDGenerator {

    private static final TimeBasedEpochRandomGenerator timeBasedEpochRandomGenerator = 
        Generators.timeBasedEpochRandomGenerator();

    private static final TSID.Factory tsdiFactory = TSID.Factory.INSTANCE;
    
    private IDGenerator() {}

    public static UUID generateTimeBaseUuid() {
        return timeBasedEpochRandomGenerator.generate();
    }

    public static TSID generateTSID() {
        return tsdiFactory.generate();
    }
}
