/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

import java.util.BitSet;
import java.util.Random;

/**
 * Helper methods for creating chromosomes.
 * 
 * @author cpettitt@samsarin.com
 */
public class Chromosomes {
    private static final Random random = new Random();
    
    /**
     * Creates a chromosome of the given length with a random set of genes.
     * 
     * @param length the number of genes in the chromosome
     * @return the chromosome
     */
    public static Chromosome random(int length) {
        BitSet genes = new BitSet();
        for (int i = 0; i < length; ++i) {
            genes.set(i, random.nextBoolean());
        }
        return new ChromosomeImpl(length, genes);
    }
    
    /**
     * Creates an empty chromosome, where all genes are false, with {@code length}.
     * 
     * @param length the length of the genes in the chromosome
     * @return the chromsome
     */
    public static Chromosome empty(int length) {
        BitSet genes = new BitSet(length);
        return new ChromosomeImpl(length, genes);
    }

    /**
     * Returns a range of bits from {@code chromosome} packed into an integer. Bits
     * are inserted into the integer from the most significant bit to the least
     * signficant bit.
     *
     * @param chromosome the chromosome from which to pull the bits
     * @param fromIndex index to start from (inclusive)
     * @param toIndex index to end at (exclusive)
     * @return new integer 
     */
    public static int rangeToInt(Chromosome chromosome, int fromIndex, int toIndex) {
        assert toIndex - fromIndex <= Integer.SIZE;
        
        int value = 0;
        for (int i = fromIndex; i < toIndex; ++i) {
            value = (value << 1) + (chromosome.get(i) ? 1 : 0);
        }
        return value;
    }
}
