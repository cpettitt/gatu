/*
 * Copyright (c) 2008 Chris Pettitt
 */
package com.samsarin.gatu.engine;

import java.util.concurrent.Callable;

import com.samsarin.gatu.primitive.Candidate;

/**
 * The Gatu engine runs the genetic algorithms simulation. Once the engine
 * terminates (determined by the {@link com.samsarin.gatu.op.TerminationOp})
 * the best {@link Candidate} is returned.
 * <p/>
 * To construct an Engine use the {@link EngineBuilder}.
 *
 * @author chris@samsarin.com
 */
public interface Engine extends Callable<Candidate> {

    /**
     * Runs the engine until it is terminated, as determined by the {@link
     * com.samsarin.gatu.op.TerminationOp}. The best candidate is returned.
     *
     * @return the best candidate at the time the engine terminates.
     */
    Candidate call();
}
