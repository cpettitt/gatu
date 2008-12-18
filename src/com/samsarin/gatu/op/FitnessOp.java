/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.op;

import com.samsarin.gatu.primitive.Chromosome;

/**
 *
 * @author cpettitt@samsarin.com
 */
public interface FitnessOp {
    
    /**
     * Returns the fitness for {@code chromosome}. The larger the returned
     * value, the more "fit" the chromosome. 
     * <p/>
     * The fitness value must be deterministic. This function should always
     * return the same fitness value for the same chromosome.
     * 
     * @param chromosome the chromosome to measure
     * @return the fitness value for this chromosome
     */
    double fitness(Chromosome chromosome);
}
