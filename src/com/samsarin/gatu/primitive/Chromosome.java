/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

/**
 * An immutable object that represents the aggregation of a set of "genes". A
 * gene is simply a bit that may be on or off.
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

    /**
     * Copies this Chromosome and sets the gene at {@code index} to {@code value}.
     * 
     * @param index the index of the gene to set
     * @param value the value of the gene
     * @return the new chromsome
     * @throws IndexOutOfBoundsException if the index is not included in this
     *         chromosome.
     */
    public Chromosome set(int index, boolean value);
    
    /**
     * Returns a copy of this chromosome with the gene at {@code index} mutated.
     * 
     * @return a mutated copy of this chromosome
     * @throws IndexOutOfBoundsException if the index is not included in this
     *         chromosome.
     */
    Chromosome mutate(int index);
    
    /**
     * Returns a view of a portion of a chromosome between {@code fromIndex},
     * inclusive, and {@code toIndex}, exclusive.
     * 
     * @param fromIndex starting point (inclusive) for the sub chromosome
     * @param toIndex ending point (exclusive) for the sub chromosome
     * @return a view of a portion of this chromosome
     * @throws IndexOutOfBoundsException if either index is not included in this
     *         chromosome.
     */
    Chromosome range(int fromIndex, int toIndex);
    
    /**
     * Returns a new chromosome that is created by joining the genes from
     * this chromosome with those from {@code other}.
     * 
     * @param other the other chromosome to append with this chromosome
     * @return the new chromosome with appended genes
     */
    Chromosome append(Chromosome other);
    
    /**
     * Returns a new chromosome that contains the contents of this chromsome
     * inverted.
     * 
     * @return an inverted view of this chromosome
     */
    Chromosome invert();

    /**
     * Replaces a set of genes starting at {@code index} with the genes from {@code chromosome}.
     *
     * @param index the index to start replacing genes in this chromosome
     * @param chromosome the new genes
     * @return a new chromosome with the replaced genes
     * @throws IndexOutOfBoundsException if the start index or the end index (start index +
     *         chromosome.length) are not within the bounds of this chromosome.
     */
    Chromosome replace(int index, Chromosome chromosome);
}
