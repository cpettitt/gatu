/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.op;

import com.samsarin.gatu.primitive.Candidate;

/**
 * An operation that determines when the engine should finish.
 *
 * @author cpettitt@samsarin.com
 */
public interface TerminationOp {

    /**
     * Returns {@code true} if the engine should terminate. Otherwise the
     * engine will continue with the next generation.
     *
     * @param generationNum the current generation, starting with 0
     * @param bestCandidate the best candidate from this generation
     * @return {@code true} if the engine should terminate
     */
    boolean shouldTerminate(int generationNum, Candidate bestCandidate);
}
