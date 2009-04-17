/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.op;

import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.Pair;

/**
 * An operation that crosses-over (mates) two chromosomes and produces two
 * new chromomsomes.
 *
 * @author chris@samsarin.com
 */
public interface CrossoverOp {

    /**
     * Returns a new pair of chromosomes that are created by crossing over
     * the given pair of chromosomes. "Crossing over" is the creation of
     * new chromosomes using genes selected from two parent chromosomes.
     *
     * @param pair the parent chromosomes
     * @return the child chromosomes created through crossover
     */
    Pair<Chromosome> crossover(Pair<Chromosome> pair);
}
