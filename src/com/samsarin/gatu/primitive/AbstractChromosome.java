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
        StringBuilder sb = new StringBuilder();
        sb.append("Chromosome[");
        for (int i = 0; i < length(); ++i) {
            sb.append(get(i) ? '1' : '0');
        }
        sb.append("]");
        return sb.toString();
    }
    
    private void checkBounds(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("Index out of bounds. Expected 0 <= index < " 
                    + length() + ". Actual: " + index);
        }
    }
}
