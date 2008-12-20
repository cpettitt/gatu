/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.engine;

import com.samsarin.gatu.primitive.Candidate;
import java.util.List;

/**
 * This class provides static methods for creating common {@link EngineListener}
 * types.
 *
 * @author cpettitt@samsarin.com
 */
public class EngineListeners {

    /**
     * For each generation this listener prints the generation number and
     * fitness to STDOUT. This can be useful to monitor the progress of an
     * engine run.
     *
     * @return the EngineListener
     */
    public static EngineListener generationPrinter() {
        return new EngineListener() {
            public void onGeneration(int generationNum, Candidate bestCandidate, List<Candidate> candidates) {
                System.out.println("Generation: " + generationNum + " Fitness: " + bestCandidate.fitness());
            }
        };
    }
}
