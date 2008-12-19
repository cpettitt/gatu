/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

/**
 * An immutable object that represents the aggregation of a set of "genes". A
 * gene is simply a bit that may be on or off.
 * <p/>
 * This class is thread safe.
 * <p/>
 * Use {@link ChromosomeBuilder} to change a chromosome's values.
 * 
 * @author cpettitt@samsarin.com
 */
public interface Chromosome {
    /**
     * Returns the number of "genes" in this chromosome.
     */
    int length();
    
    /**
     * Returns a gene at {@code index}.
     * 
     * @param index the index from which to retrieve the gene.
     * @return the gene
     * @throws IndexOutOfBoundsException if the index is not included in this
     *         chromosome.
     */
    boolean get(int index);
}
