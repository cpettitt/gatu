/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.op;

import com.samsarin.gatu.primitive.Candidate;
import com.samsarin.gatu.primitive.Chromosome;

import java.util.List;

/**
 * An operation that chooses a single chromosome from a population.
 *
 * @author cpettitt@samsarin.com
 */
public interface SelectionOp {

    /**
     * Given an ordered list of candidates (from least to most fit) this method
     * returns a single chromosome.
     *
     * @param candidates an ordered list candidates
     * @param fitnessSum the sum of each candidate's fitness
     * @return the selected chromosome
     */
    Chromosome select(List<Candidate> candidates, double fitnessSum);
}
