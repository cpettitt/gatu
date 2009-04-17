/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.engine;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.samsarin.gatu.op.FitnessOp;
import com.samsarin.gatu.op.Ops;
import com.samsarin.gatu.primitive.Candidate;
import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.ChromosomeReader;
import com.samsarin.gatu.primitive.Chromosomes;

import static junit.framework.Assert.*;

/**
 * @author chris@samsarin.com
 */
public class EngineIntegrationTest {
    /**
     * This test runs the default number of times (1000) which should result
     * in convergence on the optimal solution, 15 (encoded as 1111).
     */
    @Test
    public void call() throws Exception {
        List<Chromosome> population = initialPopulation();
        Engine engine = new EngineBuilder(population, fitnessOp())
                            .addMutationOp(Ops.pointMutation(0.001))
                            .addMutationOp(Ops.inversion(0.01))
                            .build();
        Candidate candidate = engine.call();
        assertEquals((double)15, candidate.fitness());
    }
    
    private FitnessOp fitnessOp() {
        return new FitnessOp() {
            public double fitness(Chromosome chromosome) {
                return new ChromosomeReader(chromosome).readInt(4);
            }};
    }
    
    private List<Chromosome> initialPopulation() {
        List<Chromosome> population = new ArrayList<Chromosome>();
        for (int i = 0; i < 10; ++i) {
            population.add(Chromosomes.random(4));
        }
        return population;
    }
}

