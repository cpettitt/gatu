/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

import java.util.BitSet;

/**
 * @author cpettitt@samsarin.com
 */
/* package private */ abstract class AbstractChromosome implements Chromosome {
    
    /**
     * Returns the chromosome as a {@link BitSet}.
     * @return the BitSet view of this chromosome
     */
    abstract BitSet bitSetValue();

    public boolean get(int index) {
        checkBounds(index);
        return bitSetValue().get(index);
    }

    public Chromosome set(int index, boolean value) {
        checkBounds(index);
        
        Chromosome c = this;
        BitSet bitSet = bitSetValue();
        if (!bitSet.get(index) == value) {
            bitSet = (BitSet)bitSet.clone();
            bitSet.set(index, value);
            c = new ChromosomeImpl(c.length(), bitSet);
        }
        
        return c;
    }

    public Chromosome invert() {
        int length = length();
        BitSet orig = bitSetValue();
        BitSet bitSet = new BitSet(length);
        
        for (int i = 0; i < length; ++i) {
            bitSet.set(length - i - 1, orig.get(i));
        }

        return new ChromosomeImpl(length, bitSet);
    }

    public Chromosome mutate(int index) {
        checkBounds(index);
        BitSet bitSet = (BitSet)bitSetValue().clone();
        bitSet.flip(index);
        return new ChromosomeImpl(length(), bitSet);
    }

    public Chromosome range(int fromIndex, int toIndex) {
        checkBounds(fromIndex);
        checkBounds(toIndex - 1);
        return new ChromosomeImpl(toIndex - fromIndex, bitSetValue().get(fromIndex, toIndex));
    }
    
    public Chromosome append(Chromosome other) {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < length(); ++i) {
            bitSet.set(i, get(i));
        }
        for (int i = 0; i < other.length(); ++i) {
            bitSet.set(length() + i, other.get(i));
        }
        return new ChromosomeImpl(length() + other.length(), bitSet);
    }
    
    public Chromosome replace(int index, Chromosome chromosome) {
        int toIndex = index + chromosome.length();
        checkBounds(index);
        checkBounds(toIndex - 1);
        
        BitSet bitSet = new BitSet(length());

        for (int i = 0; i < index; ++i) {
            bitSet.set(i, get(i)); 
        }
        for (int i = index; i < toIndex; ++i) {
            bitSet.set(i, chromosome.get(i - index));
        }
        for (int i = toIndex; i < length(); ++i) {
            bitSet.set(i, get(i));
        }
        return new ChromosomeImpl(length(), bitSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        
        if (!(obj instanceof AbstractChromosome)) return false;
        return bitSetValue().equals(((AbstractChromosome)obj).bitSetValue());
    }

    @Override
    public int hashCode() {
        return bitSetValue().hashCode();
    }
    
    @Override
    public String toString() {
        return bitSetValue().toString();
    }
    
    private void checkBounds(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("Index out of bounds. Expected 0 <= index < " 
                    + length() + ". Actual: " + index);
        }
    }
}
