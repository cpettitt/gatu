/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

import java.util.BitSet;

/**
 * A mutable representation of a {@link Chromosome}. This class can be used to
 * make several successive changes to a chromosome before generating an
 * immutable copy.
 * <p/>
 * Methods that change the chromosome builder's state can be chained. For
 * example: {@code new ChromosomeBuilder(2).set(0, true).set(1, true)}. 
 * <p/>
 * This class is not thread safe.
 * 
 * @author cpettitt@samsarin.com
 */
public class ChromosomeBuilder {
    private final BitSet bitSet;
    private final int length;
    
    /**
     * Constructs a new builder with {@code length} number of genes.
     * 
     * @param length number of genes for the new chromosome
     */
    public ChromosomeBuilder(int length) {
        this.bitSet = new BitSet();
        this.length = length;
    }
    
    /**
     * Creates a new builder from {@link chromosome}. Changes to the builder
     * are not reflected in the chromosome.
     * 
     * @param chromosome the chromosome to copy
     */
    public ChromosomeBuilder(Chromosome chromosome) {
        AbstractChromosome coerced = (AbstractChromosome)chromosome;
        this.bitSet = (BitSet)coerced.bitSetValue().clone();
        this.length = chromosome.length();
    }

   /**
     * Returns a gene at {@code index}.
     * 
     * @param index the index from which to retrieve the gene.
     * @return the gene value
     * @throws IndexOutOfBoundsException if the index is not included in this
     *         chromosome.
     */    
    public boolean get(int index) {
        checkBounds(index);
        return bitSet.get(index);
    }

   /**
     * Sets a gene at {@code index} to {@code value}.
     * 
     * @param index the index for which to set the gene value.
     * @param value the new value for the gene
     * @return the chromosome builder
     * @throws IndexOutOfBoundsException if the index is not included in this
     *         chromosome.
     */
    public ChromosomeBuilder set(int index, boolean value) {
        checkBounds(index);
        bitSet.set(index, value);
        return this;
    }
    
    /**
     * Reverses the bit values between {@code fromIndex}, inclusive, and
     * {@code toIndex}, exclusive.
     * 
     * @return the chromosome builder
     * @throws IndexOutOfBoundsException if either index is not included in this
     *         chromosome.
     */
    public ChromosomeBuilder invert(int fromIndex, int toIndex) {
        toIndex--;
        checkBounds(fromIndex);
        checkBounds(toIndex);
        
        while(fromIndex < toIndex) {
            boolean fromValue = bitSet.get(fromIndex);
            boolean toValue = bitSet.get(toIndex);
            bitSet.set(fromIndex++, toValue);
            bitSet.set(toIndex--, fromValue);
        }
        
        return this;
    }

    /**
     * Alters the value of the gene at {@code index}.
     * 
     * @param index the gene to mutate
     * @return the chromosome builder
     */
    public ChromosomeBuilder mutate(int index) {
        checkBounds(index);
        bitSet.flip(index);
        return this;
    }
    
    /**
     * Returns the length of the chromosome.
     */
    public int length() {
        return length;
    }
    
    /**
     * Returns an immutable copy of this builder's chromosome. This copy can
     * be used safely across threads.
     * 
     * @see Chromosome
     */
    public Chromosome toChromosome() {
        return new ChromosomeImpl(length, (BitSet)bitSet.clone());
    }
    
    private void checkBounds(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("Index out of bounds. Expected 0 <= index < " 
                    + length() + ". Actual: " + index);
        }
    }
}