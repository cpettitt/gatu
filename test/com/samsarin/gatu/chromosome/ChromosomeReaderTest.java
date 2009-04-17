/*
 * Copyright (c) 2009 Chris Pettitt
 */
package com.samsarin.gatu.chromosome;

import static org.junit.Assert.*;

import org.junit.Test;

import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.ChromosomeBuilder;
import com.samsarin.gatu.primitive.ChromosomeReader;

/**
 * @author <chris@samsarin.com>
 */
public class ChromosomeReaderTest {
    @Test
    public void readInt() {
        Chromosome c = new ChromosomeBuilder(32)
                .set(0, true)
                .toChromosome();
        
        ChromosomeReader reader = new ChromosomeReader(c);
        assertEquals(1 << 31, reader.readInt());
    }
    
    @Test
    public void readIntWithSize() {
        Chromosome c = new ChromosomeBuilder(4)
                .set(0, true)
                .set(1, true)
                .set(2, false)
                .set(3, false)
                .toChromosome();

        ChromosomeReader reader = new ChromosomeReader(c);
        assertEquals(12, reader.readInt(4));
        
        reader.reset();
        assertEquals(6, reader.readInt(3));
        
        reader.seek(2);
        assertEquals(0, reader.readInt(2));
        
        reader.seek(1);
        assertEquals(2, reader.readInt(2));
    }
    
    @Test
    public void readDouble() {
        double expected = 1245.6789;
        long longValue = Double.doubleToLongBits(expected);
        
        ChromosomeBuilder builder = new ChromosomeBuilder(64);
        for (int i = 0; i < builder.length(); ++i) {
            builder.set(i, ((longValue >>> (builder.length() - i - 1)) & 1) == 1 ? true : false);
        }
        
        Chromosome c = builder.toChromosome();
        ChromosomeReader reader = new ChromosomeReader(c);
        
        reader.reset();
        assertEquals(expected, reader.readDouble(), 0);
    }
}
