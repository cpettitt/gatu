/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

/**
 * A candidate is the collection of a {@link Chromosome} with its fitness.
 * Candidates are used for selection and reporting on the best chromosome in
 * a population.
 *
 * @author cpettitt@samsarin.com
 */
public interface Candidate extends Comparable<Candidate> {

    /**
     * Returns the chromosome in this candidate.
     *
     * @return the chromosome in this candidate
     */
    Chromosome chromosome();

    /**
     * Returns the fitness for this {@link Chromosome} in this candidate.
     *
     * @return the fitness for this chromosome
     */
    double fitness();
}
