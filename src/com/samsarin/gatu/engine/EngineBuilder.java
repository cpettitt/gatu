/*
 * Copyright (c) 2008 Chris Pettitt
 */
package com.samsarin.gatu.engine;

import java.util.ArrayList;
import java.util.List;

import com.samsarin.gatu.op.CrossoverOp;
import com.samsarin.gatu.op.FitnessOp;
import com.samsarin.gatu.op.MutationOp;
import com.samsarin.gatu.op.Ops;
import com.samsarin.gatu.op.SelectionOp;
import com.samsarin.gatu.op.TerminationOp;
import com.samsarin.gatu.primitive.Chromosome;

/**
 * An object that can build and configure new {@link Engine} instances. This
 * object provides a simple way of chaining together configuration settings
 * using the builder pattern:
 * <p/>
 * <pre>
 * {@code
 * EngineBuilder builder = new EngineBuilder(intialPopulation(), Ops.cachingFitness(fitnessOp, 50))
 *     .addMutationOp(Ops.pointMutation(0.0001))
 *     .addMutationOp(Ops.inversion(0.00001))
 *     .addListener(EngineListeners.generationPrinter())
 *     .setTerminationOp(Ops.terminateAtGeneration(1000000));
 *
 * Engine engine = builder.build();
 * }
 * </pre>
 *
 * @author chris@samsarin.com
 */
public class EngineBuilder {
    private final List<Chromosome> initialPopulation;
    private final FitnessOp fitnessOp;
    private SelectionOp selectionOp;
    private CrossoverOp crossoverOp;
    private List<MutationOp> mutationOps;
    private TerminationOp terminationOp;
    private List<EngineListener> listeners;
    private int numBestToKeep;

    /**
     * Creates a new engine builder.
     *
     * @param initialPopulation a list of chromosomes in the generation
     * @param fitnessOp the strategy for evaluating chromosome fitness
     */
    public EngineBuilder(List<Chromosome> initialPopulation, FitnessOp fitnessOp) {
        this.initialPopulation = new ArrayList<Chromosome>(initialPopulation);
        this.fitnessOp = fitnessOp;
        this.selectionOp = Ops.rouletteWheelSelection();
        this.crossoverOp = Ops.singlePointCrossover();
        this.mutationOps = new ArrayList<MutationOp>();
        this.terminationOp = Ops.terminateAtGeneration(1000);
        this.listeners = new ArrayList<EngineListener>();
        this.numBestToKeep = 2;
    }

    /**
     * Sets the selection strategy for this engine.
     *
     * @param selectionOp the selection strategy
     * @return this EngineBuilder
     */
    public EngineBuilder setSelectionOp(SelectionOp selectionOp) {
        this.selectionOp = selectionOp;
        return this;
    }

    /**
     * Sets the crossover strategy for this engine.
     *
     * @param crossoverOp the crossover strategy
     * @return this EngineBuilder
     */
    public EngineBuilder setCrossoverOp(CrossoverOp crossoverOp) {
        this.crossoverOp = crossoverOp;
        return this;
    }

    /**
     * Adds a new mutation strategy for this engine. More than one mutation
     * strategy can be used in a single Engine.
     *
     * @param mutationOp a mutation strategy
     * @return this EngineBuilder
     */
    public EngineBuilder addMutationOp(MutationOp mutationOp) {
        this.mutationOps.add(mutationOp);
        return this;
    }

    /**
     * Adds a new {@link EngineListener} to this builder. More than one listener
     * can be used in a single Engine.
     *
     * @param listener an engien listener
     * @return this EngineBuilder
     */
    public EngineBuilder addListener(EngineListener listener) {
        this.listeners.add(listener);
        return this;
    }

    /**
     * Sets the termination strategy for this engine.
     *
     * @param terminationOp the termination strategy
     * @return this EngineBuilder
     */
    public EngineBuilder setTerminationOp(TerminationOp terminationOp) {
        this.terminationOp = terminationOp;
        return this;
    }

    /**
     * Determines the number of "best" candidates which should move from one
     * generation to the next.
     *
     * @param numBestToKeep the number of elite chromosomes to keep
     * @return this EngineBuilder
     */
    public EngineBuilder setNumBestToKeep(int numBestToKeep) {
        this.numBestToKeep = numBestToKeep;
        return this;
    }

    /**
     * Returns a new {@link Engine} using the configuration set on this builder.
     *
     * @return a new {@link Engine}
     */
    public Engine build() {
        return new EngineImpl(initialPopulation,
                              fitnessOp,
                              selectionOp,
                              crossoverOp,
                              mutationOps,
                              terminationOp,
                              listeners,
                              numBestToKeep);
    }
}
