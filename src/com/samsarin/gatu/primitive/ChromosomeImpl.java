/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

import java.util.BitSet;

/**
 *
 * @author chris@samsarin.com
 */
/* package private */ class ChromosomeImpl extends AbstractChromosome {
    private final BitSet genes;
    private final int length;
    
    /* package private */ ChromosomeImpl(int length, BitSet genes) {
        this.length = length;
        this.genes = genes;
    }

    @Override
    BitSet bitSetValue() {
        return genes;
    }

    public int length() {
        return length;
    }
}
