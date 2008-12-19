/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.op;

import com.samsarin.gatu.primitive.Candidate;
import com.samsarin.gatu.primitive.CandidateKey;
import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.ChromosomeBuilder;
import com.samsarin.gatu.primitive.Pair;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author cpettitt@samsarin.com
 */
public class Ops {
    private static final Random random = new Random();
    
    private static class Cache<T1, T2> extends LinkedHashMap<T1, T2> {
        private final int numToCache;
        
        public Cache(int numToCache) {
            super(numToCache, 0.75f, true);
            
            this.numToCache = numToCache;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<T1, T2> e) {
            return size() > numToCache;
        }
    }
    
    public static FitnessOp cachingFitness(final FitnessOp fitnessOp, int numToCache) {
        final Cache<Chromosome, Double> cache = new Cache<Chromosome, Double>(numToCache);
        return new FitnessOp() {
            public double fitness(Chromosome chromosome) {
                Double fitness = cache.get(chromosome);
                if (fitness != null) {
                    return fitness;
                }
                
                fitness = fitnessOp.fitness(chromosome);
                cache.put(chromosome, fitness);
                return fitness;
            }
        };
    }
    
    public static MutationOp pointMutation(final double probability) {
        return new MutationOp() {
            public Chromosome mutate(Chromosome chromosome) {
                ChromosomeBuilder cb = new ChromosomeBuilder(chromosome);
                for (int i = 0; i < chromosome.length(); ++i) {
                    if (random.nextDouble() < probability) {
                        cb.mutate(i);
                    }
                }
                return cb.toChromosome();  
            }
        };
    }
    
    public static MutationOp inversion(final double probability) {
        return new MutationOp() {
            public Chromosome mutate(Chromosome chromosome) {
                ChromosomeBuilder cb = new ChromosomeBuilder(chromosome);
                for (int i = 0; i < chromosome.length(); ++i) {
                    if (random.nextDouble() < probability) {
                        int toIndex = random.nextInt(chromosome.length() - i) + i + 1;
                        cb.invert(i, toIndex);
                        i = toIndex;
                    }
                }
                return cb.toChromosome();
            }
        };
    }
    
    public static CrossoverOp singlePointCrossover() {
        return new CrossoverOp() {
            public Pair<Chromosome> crossover(Pair<Chromosome> pair) {
                assert pair.first().length() == pair.second().length();
                int crossoverPoint = random.nextInt(pair.first().length() - 1) + 1;
                int length = pair.first().length();
                
                ChromosomeBuilder first = new ChromosomeBuilder(pair.first());
                ChromosomeBuilder second = new ChromosomeBuilder(pair.second());
                
                for (int i = crossoverPoint; i < first.length(); ++i) {
                    swapGene(i, first, second);
                }
                return new Pair<Chromosome>(first.toChromosome(), second.toChromosome());
            }
        };
    }
    
    public static SelectionOp rouletteWheelSelection() {
        return new SelectionOp() {
            public Chromosome select(List<Candidate> candidates, double fitnessSum) {
                Candidate key = CandidateKey.createKey(random.nextDouble() * fitnessSum);
                int index = Collections.binarySearch(candidates, key);

                if (index < 0) {
                    index =  -(index + 1);
                    if (index == candidates.size()) {
                        index--;
                    }
                }
                
                return candidates.get(index).chromosome();                
            }
        };
    }
    
    public static TerminationOp terminateAtGeneration(final int generationNum) {
        return new TerminationOp() {
            public boolean shouldTerminate(int current, Candidate bestCandidate) {
                return generationNum == current;
            }
        };
    }
    
    private static void swapGene(int index, ChromosomeBuilder cb1, ChromosomeBuilder cb2) {
        boolean val1 = cb1.get(index);
        boolean val2 = cb2.get(index);
        cb1.set(index, val2);
        cb2.set(index, val1);
    }
}
