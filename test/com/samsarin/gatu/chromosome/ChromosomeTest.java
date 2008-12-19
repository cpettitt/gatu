/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.chromosome;

import org.junit.Test;

import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.ChromosomeBuilder;
import com.samsarin.gatu.primitive.Chromosomes;

import static org.junit.Assert.*;

/**
 * @author cpettitt@samsarin.com
 */
public class ChromosomeTest {
    @Test
    public void createRandom() {
        Chromosome c = Chromosomes.random(1000);
        assertEquals(1000, c.length());
        
        int numTrue = 0;
        for (int i = 0; i < c.length();  ++i) {
            if (c.get(i)) numTrue++;
        }
        assertEquals(0.5, ((double)numTrue / c.length()), 0.1);
    }
    
    @Test
    public void createEmpty() {
        Chromosome c = Chromosomes.empty(100);
        assertEquals(100, c.length());
        
        for (int i = 0; i < c.length(); ++i) {
            assertFalse(c.get(i));
        }
    }
    
    @Test
    public void rangeToInt() {
        Chromosome c = new ChromosomeBuilder(4)
                .set(0, true)
                .set(1, true)
                .set(2, false)
                .set(3, false)
                .toChromosome();

        assertEquals(12, Chromosomes.rangeToInt(c, 0, 4));
        assertEquals(6, Chromosomes.rangeToInt(c, 0, 3));
        assertEquals(0, Chromosomes.rangeToInt(c, 2, 4));
        assertEquals(2, Chromosomes.rangeToInt(c, 1, 3));
    }
    
    @Test 
    public void equals() {
        assertEquals(new ChromosomeBuilder(2).set(0, true).toChromosome(),
                new ChromosomeBuilder(2).set(0, true).toChromosome());
        assertFalse(new ChromosomeBuilder(2).set(0, true).toChromosome()
                .equals(new ChromosomeBuilder(2).toChromosome()));
    }
    
    @Test
    public void hash() {
        assertEquals(new ChromosomeBuilder(2).set(0, true).toChromosome().hashCode(),
                new ChromosomeBuilder(2).set(0, true).toChromosome().hashCode());
        assertTrue(new ChromosomeBuilder(2).set(0, true).toChromosome().hashCode() !=
                new ChromosomeBuilder(2).toChromosome().hashCode());
    }
}
