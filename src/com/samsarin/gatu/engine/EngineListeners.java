/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.engine;

import com.samsarin.gatu.primitive.Candidate;
import java.util.List;

/**
 * @author cpettitt@samsarin.com
 */
public class EngineListeners {
    public static EngineListener generationPrinter() {
        return new EngineListener() {
            public void onGeneration(int generationNum, Candidate bestCandidate, List<Candidate> candidates) {
                System.out.println("Generation: " + generationNum + " Fitness: " + bestCandidate.fitness());
            }
        };
    }
}
