/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.op;

import com.samsarin.gatu.primitive.Chromosome;

/**
 * An operation that changes a chromosome in some way.
 * 
 * @author chris@samsarin.com
 */
public interface MutationOp {
    
    /**
     * Returns a chromosome that may have had one or more of its genes changed
     * in value.
     * 
     * @param chromosome the chromosome to use as a basis for a new mutated
     *        chromosome
     * @return the mutated chromosome
     */
    Chromosome mutate(Chromosome chromosome);
}
