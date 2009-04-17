package com.samsarin.gatu.primitive;

/**
 * A candidate key can be used to search for a candidate with the given fitness
 * in a sorted list.
 *
 * @author chris@samsarin.com
 */
public class CandidateKey implements Candidate {
    private final double fitness;

    /**
     * Creates a new candidate key with the given fitness.
     *
     * @param fitness the fitness for this key
     * @return the new key
     */
    public static CandidateKey createKey(double fitness) {
        return new CandidateKey(fitness);
    }

    private CandidateKey(double fitness) {
        this.fitness = fitness;
    }

    /**
     * This method is unsupported - a key is only used for lookup.
     *
     * @return this method does not return
     * @throws UnsupportedOperationException
     */
    public Chromosome chromosome() {
        throw new UnsupportedOperationException("chromosome may not be called on a key");
    }

    /**
     * The fitness in this key.
     *
     * @return the fitness in this key
     */
    public double fitness() {
        return fitness;
    }

    public int compareTo(Candidate o) {
        return Double.compare(fitness, o.fitness());
    }
}
