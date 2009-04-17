/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.chromosome;

import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.ChromosomeBuilder;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 *
 * @author chris@samsarin.com
 */
public class ChromosomeBuilderTest {
    @Test
    public void initialState() {
        ChromosomeBuilder cb = new ChromosomeBuilder(20);
        assertEquals(20, cb.length());
        
        for (int i = 0; i < cb.length(); ++i) {
            assertFalse(cb.get(i));
        }
    }
    
    @Test
    public void setAndGet() {
        ChromosomeBuilder cb = new ChromosomeBuilder(1);
        cb.set(0, true);
        assertTrue(cb.get(0));
        cb.set(0, false);
        assertFalse(cb.get(0));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void setOutOfBounds() {
        new ChromosomeBuilder(1).set(1, false);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void getOutOfBounds() {
        new ChromosomeBuilder(1).get(1);
    }
    
    @Test
    public void mutate() {
        ChromosomeBuilder cb = new ChromosomeBuilder(1);
        cb.mutate(0);
        assertTrue(cb.get(0));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void mutateOutOfBounds() {
        new ChromosomeBuilder(1).mutate(1);
    }

    @Test
    public void invertAll() {
        ChromosomeBuilder cb = new ChromosomeBuilder(5);
        cb.set(2, true);
        cb.set(3, true);

        cb.invert(0, 5);
        assertEquals(5, cb.length());
        assertFalse(cb.get(0));
        assertTrue(cb.get(1));
        assertTrue(cb.get(2));
        assertFalse(cb.get(3));
        assertFalse(cb.get(4));
    }
    
    @Test
    public void invertRange() {
        ChromosomeBuilder cb = new ChromosomeBuilder(5);
        cb.set(2, true);
        
        cb.invert(2, 4);
        assertEquals(5, cb.length());
        assertFalse(cb.get(0));
        assertFalse(cb.get(1));
        assertFalse(cb.get(2));
        assertTrue(cb.get(3));
        assertFalse(cb.get(4));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void invertLowerOutOfBounds() {
        new ChromosomeBuilder(1).invert(-1, 1);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void invertUpperOutOfBounds() {
        new ChromosomeBuilder(1).invert(0, 2);
    }
    
    @Test
    public void toChromosome() {
        ChromosomeBuilder cb = new ChromosomeBuilder(5);
        cb.set(1, true).set(3, true);
        
        Chromosome c = cb.toChromosome();
        assertEquals(5, c.length());
        assertFalse(c.get(0));
        assertTrue(c.get(1));
        assertFalse(c.get(2));
        assertTrue(c.get(3));
        assertFalse(c.get(4));
    }
}
