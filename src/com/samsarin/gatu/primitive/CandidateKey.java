package com.samsarin.gatu.primitive;

public class CandidateKey implements Candidate {
    private final double fitness;
    
    public static CandidateKey createKey(double fitness) {
        return new CandidateKey(fitness);
    }
    
    private CandidateKey(double fitness) {
        this.fitness = fitness;
    }
    
    public Chromosome chromosome() {
        throw new UnsupportedOperationException("chromosome may not be called on a key");
    }

    public double fitness() {
        return fitness;
    }

    public int compareTo(Candidate o) {
        return Double.compare(fitness, o.fitness());
    }
}
