/*
 * Copyright (c) 2008 Chris Pettitt
 */
package com.samsarin.gatu.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.samsarin.gatu.op.CrossoverOp;
import com.samsarin.gatu.op.FitnessOp;
import com.samsarin.gatu.op.MutationOp;
import com.samsarin.gatu.op.SelectionOp;
import com.samsarin.gatu.op.TerminationOp;
import com.samsarin.gatu.primitive.Candidate;
import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.Pair;

/**
 * @author cpettitt@samsarin.com
 */
/* package private */class EngineImpl implements Engine {
    private final List<Chromosome> initialPopulation;
    private final FitnessOp fitnessOp;
    private final SelectionOp selectionOp;
    private final CrossoverOp crossoverOp;
    private final List<MutationOp> mutationOps;
    private final TerminationOp terminationOp;
    private final List<EngineListener> listeners;
    private final int numBestToKeep;

    EngineImpl(List<Chromosome> initialPopulation, FitnessOp fitnessOp,
            SelectionOp selectionOp, CrossoverOp crossoverOp,
            List<MutationOp> mutationOps, TerminationOp terminationOp,
            List<EngineListener> listeners, int numBestToKeep) {
        this.initialPopulation = Collections.unmodifiableList(new ArrayList<Chromosome>(initialPopulation));
        this.fitnessOp = fitnessOp;
        this.selectionOp = selectionOp;
        this.crossoverOp = crossoverOp;
        this.mutationOps = Collections.unmodifiableList(new ArrayList<MutationOp>(mutationOps));
        this.terminationOp = terminationOp;
        this.listeners = Collections.unmodifiableList(new ArrayList<EngineListener>(listeners));
        this.numBestToKeep = numBestToKeep;
    }

    public Candidate call() {
        List<Chromosome> population = initialPopulation;
        int generationNum = 0;

        List<Candidate> candidates = getCandidates(population);
        while (!done(generationNum, bestCandidate(candidates))) {
            notifyListeners(generationNum, bestCandidate(candidates), candidates);

            generationNum++;
            List<Chromosome> nextGen = createNextGen(population.size() - numBestToKeep, candidates);
            addBestToNextGen(candidates, nextGen);
            population = nextGen;
            candidates = getCandidates(population);
        }

        return bestCandidate(candidates);
    }

    private List<Candidate> getCandidates(List<Chromosome> chromosomes) {
        List<Candidate> candidates = new ArrayList<Candidate>(chromosomes
                .size());
        for (Chromosome chromosome : chromosomes) {
            Candidate candidate = new CandidateImpl(fitnessOp
                    .fitness(chromosome), chromosome);
            candidates.add(candidate);
        }
        Collections.sort(candidates);
        return candidates;
    }

    private Candidate bestCandidate(List<Candidate> candidates) {
        return candidates.get(candidates.size() - 1);
    }

    private boolean done(int generationNum, Candidate bestCandidate) {
        return terminationOp.shouldTerminate(generationNum, bestCandidate);
    }

    private void notifyListeners(int generationNum, Candidate bestCandidate,
            List<Candidate> candidates) {
        for (EngineListener listener : listeners) {
            listener.onGeneration(generationNum, bestCandidate, candidates);
        }
    }

    private List<Chromosome> createNextGen(int size, List<Candidate> candidates) {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(size);

        double fitnessSum = getFitnessSum(candidates);

        int numPairs = size / 2;
        for (int i = 0; i < numPairs; ++i) {
            Pair<Chromosome> pair = selectPair(candidates, fitnessSum);
            pair = crossoverOp.crossover(pair);
            chromosomes.add(mutate(pair.first()));
            chromosomes.add(mutate(pair.second()));
        }

        return chromosomes;
    }

    private double getFitnessSum(List<Candidate> candidates) {
        double sum = 0;
        for (Candidate candidate : candidates) {
            sum += candidate.fitness();
        }
        return sum;
    }

    private Pair<Chromosome> selectPair(List<Candidate> candidates, double fitnessSum) {
        Chromosome first = selectionOp.select(candidates, fitnessSum);
        Chromosome second;
        do {
            second = selectionOp.select(candidates, fitnessSum);
        } while (first == second);
        return new Pair<Chromosome>(first, second);
    }

    private Chromosome mutate(Chromosome chromosome) {
        for (MutationOp mutationOp : mutationOps) {
            chromosome = mutationOp.mutate(chromosome);
        }
        return chromosome;
    }

    private void addBestToNextGen(List<Candidate> candidates, List<Chromosome> nextGen) {
        for (int i = 0; i < numBestToKeep; ++i) {
            nextGen.add(candidates.get(candidates.size() - i - 1).chromosome());
        }
    }
}
