/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.op;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.ChromosomeBuilder;
import com.samsarin.gatu.primitive.Chromosomes;
import com.samsarin.gatu.primitive.Pair;

/**
 * @author chris@samsarin.com
 */
public class OpsTest {
    @Test
    public void terminateAtGeneration() {
        TerminationOp op = Ops.terminateAtGeneration(2);
        assertFalse(op.shouldTerminate(0, null));
        assertFalse(op.shouldTerminate(1, null));
        assertTrue(op.shouldTerminate(2, null));
    }

    @Test
    public void pointMutation() {
        Chromosome c = Chromosomes.empty(5);

        // Mutate everything
        MutationOp op = Ops.pointMutation(1);
        Chromosome r = op.mutate(c);

        assertEquals(5, r.length());
        for (int i = 0; i < r.length(); ++i) {
            assertTrue(r.get(i));
        }
    }

    @Test
    public void singlePointCrossover() {
        Chromosome c1 = Chromosomes.empty(5);
        ChromosomeBuilder cb2 = new ChromosomeBuilder(5);

        for (int i = 0; i < cb2.length(); ++i) {
            cb2.set(i, true);
        }

        for (int i = 0; i < 10; ++i) {
            CrossoverOp op = Ops.singlePointCrossover();
            Pair<Chromosome> r = op.crossover(new Pair<Chromosome>(c1, cb2.toChromosome()));
    
            assertCrossover(r.first(), 5, false);
            assertCrossover(r.second(), 5, true);
        }
    }

    private void assertCrossover(Chromosome c, int length, boolean initialValue) {
        assertEquals(length, c.length());
        boolean hasInitialValue = false;
        boolean hasCrossedOver = false;
        for (int i = 0; i < c.length(); ++i) {
            if (!hasInitialValue) {
                assertEquals(initialValue, c.get(i));
                hasInitialValue = true;
            } else if (hasInitialValue && !hasCrossedOver) {
                if (c.get(i) != initialValue) {
                    hasCrossedOver = true;
                }
            } else {
                assertEquals(!initialValue, c.get(i));
            }
        } 

        assertTrue(hasInitialValue);
        assertTrue(hasCrossedOver);
    }
}
