/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.chromosome;

import org.junit.Test;

import com.samsarin.gatu.primitive.Chromosome;
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
    public void setAndGet() {
        Chromosome c = Chromosomes.empty(1);
        assertFalse(c.set(0, false).get(0));
        assertTrue(c.set(0, true).get(0));
        assertFalse(c.get(0)); // Ensure that the original chromosome didn't change
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void setOutOfBounds() {
        Chromosomes.empty(1).set(1, false);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void getOutOfBounds() {
        Chromosomes.empty(1).get(1);
    }
    
    @Test
    public void mutate() {
        assertTrue(Chromosomes.empty(1).mutate(0).get(0));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void mutateOutOfBounds() {
        Chromosomes.empty(1).mutate(1);
    }
    
    @Test
    public void range() {
        Chromosome c = Chromosomes.empty(5);
        c = c.set(1, true);
        c = c.set(3, true);
        
        Chromosome r = c.range(1, 4);
        assertEquals(3, r.length());
        assertTrue(r.get(0));
        assertFalse(r.get(1));
        assertTrue(r.get(2));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void rangeLowerOutOfBounds() {
        Chromosomes.empty(1).range(-1, 0);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void rangeUpperOutOfBounds() {
        Chromosomes.empty(1).range(0, 2);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void rangeLowerGreaterThanUpper() {
        Chromosomes.empty(3).range(1, 0);
    }

    @Test
    public void rangeLowerEqualsUpper() {
        Chromosome c = Chromosomes.empty(3).range(1, 1);
        assertEquals(0, c.length());
    }
    
    @Test
    public void rangeOfOne() {
        Chromosome c = Chromosomes.empty(3);
        c = c.set(0, true);
        
        Chromosome r = c.range(0, 1);
        assertEquals(1, r.length());
        assertTrue(r.get(0));
    }

    @Test
    public void invert() {
        Chromosome c = Chromosomes.empty(5);
        c = c.set(2, true);
        c = c.set(3, true);

        c = c.invert();
        assertEquals(5, c.length());
        assertFalse(c.get(0));
        assertTrue(c.get(1));
        assertTrue(c.get(2));
        assertFalse(c.get(3));
        assertFalse(c.get(4));
    }

    @Test
    public void replace() {
        Chromosome c = Chromosomes.empty(5);
        Chromosome c2 = Chromosomes.empty(3);
        c2 = c2.set(1, true);

        Chromosome r = c.replace(1, c2);
        assertEquals(5, r.length());
        assertFalse(r.get(0));
        assertFalse(r.get(1));
        assertTrue(r.get(2));
        assertFalse(r.get(3));
        assertFalse(r.get(4));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void replaceLowerOutOfBounds() {
        Chromosome c = Chromosomes.empty(5);
        Chromosome c2 = Chromosomes.empty(5);

        c.replace(-1, c2);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void replaceUpperOutOfBounds() {
        Chromosome c = Chromosomes.empty(5);
        Chromosome c2 = Chromosomes.empty(6);

        c.replace(0, c2);
    }
    
    @Test
    public void replaceLastElement() {
        Chromosome c = Chromosomes.empty(3);
        Chromosome c2 = Chromosomes.empty(1);
        
        c2 = c2.set(0, true);
        
        Chromosome r = c.replace(2, c2);
        
        assertEquals(3, r.length());
        assertFalse(r.get(0));
        assertFalse(r.get(1));
        assertTrue(r.get(2));
    }

    @Test
    public void replaceAll() {
        Chromosome c = Chromosomes.empty(2);
        Chromosome c2 = Chromosomes.empty(2);
        c2 = c2.set(0, true);
        c2 = c2.set(1, true);

        Chromosome r = c.replace(0, c2);
        assertEquals(2, r.length());
        assertTrue(r.get(0));
        assertTrue(r.get(1));
    }
    
    @Test
    public void append() {
        Chromosome c = Chromosomes.empty(2);
        Chromosome c2 = Chromosomes.empty(2);
        
        c2 = c2.set(0, true);
        c2 = c2.set(1, true);
        
        Chromosome r = c.append(c2);
        assertEquals(4, r.length());
        assertFalse(r.get(0));
        assertFalse(r.get(1));
        assertTrue(r.get(2));
        assertTrue(r.get(3));
    }

    @Test
    public void rangeToInt() {
        Chromosome c = Chromosomes.empty(4);
        c = c.set(0, true).set(1, true).set(2, false).set(3, false);

        assertEquals(12, Chromosomes.rangeToInt(c, 0, 4));
        assertEquals(6, Chromosomes.rangeToInt(c, 0, 3));
        assertEquals(0, Chromosomes.rangeToInt(c, 2, 4));
        assertEquals(2, Chromosomes.rangeToInt(c, 1, 3));
    }
    
    @Test 
    public void equals() {
        assertEquals(Chromosomes.empty(2).set(0, true),
                Chromosomes.empty(2).set(0, true));
        assertFalse(Chromosomes.empty(2).set(0, true).equals(Chromosomes.empty(2)));
    }
    
    @Test
    public void hash() {
        assertEquals(Chromosomes.empty(2).set(0, true).hashCode(),
                Chromosomes.empty(2).set(0, true).hashCode());
        assertTrue(Chromosomes.empty(2).set(0, true).hashCode() !=
                Chromosomes.empty(2).hashCode());
    }
}
