/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.engine;

import com.samsarin.gatu.primitive.Candidate;
import com.samsarin.gatu.primitive.Chromosome;


/**
 * @author cpettitt@samsarin.com
 */
/* package private */ class CandidateImpl implements Candidate {
    private final double fitness;
    private final Chromosome chromosome;
    
    public CandidateImpl(double fitness, Chromosome chromosome) {
        assert chromosome != null;
        this.fitness = fitness;
        this.chromosome = chromosome;
    }
    
    public double fitness() {
        return fitness;
    }
    
    public Chromosome chromosome() {
        return chromosome;
    }
    
    public int compareTo(Candidate other) {
        return Double.compare(fitness, other.fitness());
    }    
    
    @Override
    public boolean equals(Object o) {
        return chromosome.equals(o);
    }
    
    @Override
    public int hashCode() {
        return chromosome.hashCode();
    }
    
    @Override
    public String toString() {
        return "Candidate[fitness=" + fitness + ",chromosome=" + chromosome + "]";
    }
}
