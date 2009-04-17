/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

import java.util.BitSet;
import java.util.Random;

/**
 * Helper methods for creating or working with {@link Chromosome}s.
 *
 * @author chris@samsarin.com
 */
public class Chromosomes {
    private static final Random random = new Random();

    private Chromosomes() {}

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
}
