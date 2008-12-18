package com.samsarin.gatu.engine;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.samsarin.gatu.primitive.CandidateKey;
import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.Chromosomes;

public class CandidateTest {
    @Test
    public void compare() {
        Chromosome c1 = Chromosomes.empty(1);
        Chromosome c2 = Chromosomes.empty(1);
        
        assertTrue(new CandidateImpl(5, c1).compareTo(new CandidateImpl(10, c2)) < 0);
        assertTrue(new CandidateImpl(0.5, c1).compareTo(new CandidateImpl(0.5, c2)) == 0);
        assertTrue(new CandidateImpl(-5, c1).compareTo(new CandidateImpl(-10, c2)) > 0);
    }
    
    @Test
    public void compareWithCandidateKey() {
        Chromosome c = Chromosomes.empty(1);
        
        assertTrue(new CandidateImpl(5, c).compareTo(CandidateKey.createKey(10)) < 0);
        assertTrue(new CandidateImpl(0.5, c).compareTo(CandidateKey.createKey(0.5)) == 0);
        assertTrue(new CandidateImpl(-5, c).compareTo(CandidateKey.createKey(-10)) > 0);
        
    }
}
