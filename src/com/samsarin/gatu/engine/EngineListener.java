/*
 * Copyright (c) 2008 Chris Pettitt
 */
package com.samsarin.gatu.engine;

import java.util.List;

import com.samsarin.gatu.primitive.Candidate;

/**
 * An interface that allows client code to observe changes in the population
 * from generation to generation. Implementations must be registered with the
 * {@link EngineBuilder} to receive notifications.
 *
 * @author cpettitt@samsarin.com
 */
public interface EngineListener {

    /**
     * Called for each new generation.
     *
     * @param generationNum the number of the generation, starting from 0
     * @param bestCandidate the best candidate for the generation
     * @param candidates a list of all candidates in the population for this generation
     */
    void onGeneration(int generationNum, Candidate bestCandidate, List<Candidate> candidates);
}
