/*
 * Copyright (c) 2008 Chris Pettitt
 */
package com.samsarin.gatu.engine;

import java.util.List;

import com.samsarin.gatu.primitive.Candidate;

/**
 * @author cpettitt@samsarin.com
 */
public interface EngineListener {
    void onGeneration(int generationNum, Candidate bestCandidate, List<Candidate> candidates);
}
