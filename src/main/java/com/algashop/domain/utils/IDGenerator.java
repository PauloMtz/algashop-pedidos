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

    /*
     * para utilização em produção, precisa passar duas variáveis de ambiente
     * TSID_NODE=0 --> instância inicial do microsserviço
     * TSID_NODE_COUNT=3 --> quantidade de microsserviços
     */
    public static TSID generateTSID() {
        return tsdiFactory.generate();
    }
}
