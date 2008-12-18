/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author cpettitt@samsarin.com
 */
public class PairTest {
    @Test
    public void first() {
        assertEquals(1, (int)new Pair<Integer>(1,2).first());
    }
    
    @Test
    public void second() {
        assertEquals(2, (int)new Pair<Integer>(1,2).second());
    }
    
    @Test
    public void equals() {
        assertEquals(new Pair<Integer>(1,2),
                new Pair<Integer>(1,2));
        assertFalse(new Pair<Integer>(1,2).equals(new Pair<Integer>(3,4)));
    }
    
    @Test
    public void hash() {
        assertEquals(new Pair<Integer>(1,2).hashCode(),
                new Pair<Integer>(1,2).hashCode());
        assertTrue(new Pair<Integer>(1,2).hashCode() != new Pair<Integer>(3,4).hashCode());
    }
}
