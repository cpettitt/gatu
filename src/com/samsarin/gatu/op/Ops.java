/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.op;

import com.samsarin.gatu.primitive.Candidate;
import com.samsarin.gatu.primitive.CandidateKey;
import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.ChromosomeBuilder;
import com.samsarin.gatu.primitive.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class provides a set of static methods for creating common implementations
 * of various genetic algorithm operations.
 *
 * @author cpettitt@samsarin.com
 */
public class Ops {
    private static final Random random = new Random();

    private Ops() {}

    private static class Cache<T1, T2> extends LinkedHashMap<T1, T2> {
        private static final long serialVersionUID = -1;

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

    /**
     * A {@link FitnessOp} wrapper that caches the fitness for chromosomes
     * previously calculated. Older fitness scores are purged as newer values
     * are cached.
     *
     * @param fitnessOp the actual fitness op to run if the fitness is not cached
     * @param numToCache the number of chromosomes to cache
     * @return the fitness op
     */
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

    /**
     * A {@link MutationOp} that randomly changes zero or more genes in
     * a chromosome. The chance that a gene will be mutated is determined
     * by the given probability.
     *
     * @param probability the chance that a gene will be mutated
     * @return the mutation op
     */
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

    /**
     * A {@link MutationOp} that randomly reverses the order of a range of genes in a
     * chromosome.
     *
     * @param probability the change that a range of genes will be reversed in
     *        a chromosome
     * @return the mutation op
     */
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

    /**
     * A {@link CrossoverOp} that causes chromosomes to crossover at a single
     * point. For example, the children below are created when chromosome 1
     * and chromosome 2 crossover at gene 3.
     *
     * <pre>
     * {@literal
     * Gene #:       0 1 2 3 4 5 6
     *
     * Chromosome 1: A B C D E F G
     * Chromosome 2: 1 2 3 4 5 6 7
     *
     * Child 1:      A B C 4 5 6 7
     * Child 2:      1 2 3 D E F G
     * }
     * </pre>
     *
     * @return the crossover operation
     */
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

    /**
     * A {@link CrossoverOp} that randomly swaps genes from the two parents
     * in the children. This differs from {@link #singlePointCrossover()} where
     * each child gets one parent's genes exclusively until the crossover point,
     * after which they get the other parent's gene.
     *
     * @return the crossover op
     */
    public static CrossoverOp uniformCrossover() {
        return new CrossoverOp() {
            public Pair<Chromosome> crossover(Pair<Chromosome> pair) {
                assert pair.first().length() == pair.second().length();
                ChromosomeBuilder first = new ChromosomeBuilder(pair.first());
                ChromosomeBuilder second = new ChromosomeBuilder(pair.second());

                for (int i = 0; i < pair.first().length(); ++i) {
                    if (random.nextBoolean()) {
                        swapGene(i, first, second);
                    }
                }

                return new Pair<Chromosome>(first.toChromosome(), second.toChromosome());
            }
        };
    }

    /**
     * A {@link SelectionOp} that randomly selects a chromosome from a population,
     * giving favor to those with better fitness.
     *
     * @return the selection operation
     */
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

    /**
     * A {@link SelectionOp} that randomly selects a subset of the population
     * and then chooses the most fit {@link Chromosome} from that subpopulation.
     *
     * @param tournamentSize the number of chromosomes to select for the tournament
     * @return the selection op
     */
    public static SelectionOp tournamentSelection(final int tournamentSize) {
        return new SelectionOp() {
            public Chromosome select(List<Candidate> candidates, double fitnessSum) {
                List<Candidate> tournament = new ArrayList<Candidate>(tournamentSize);
                while (tournament.size() < tournamentSize) {
                    Candidate candidate = candidates.get(random.nextInt(candidates.size()));
                    if (!tournament.contains(candidate)) {
                        tournament.add(candidate);
                    }
                }

                return tournament.get(random.nextInt(tournamentSize)).chromosome();
            }
        };
    }

    /**
     * A {@link TerminationOp} that terminates the engine when it has reached
     * the specified generation number.
     *
     * @param generationNum the generation number at which to terminate
     * @return the termination op
     */
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
