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
 * @author cpettitt@samsarin.com
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
    
    public EngineBuilder setSelectionOp(SelectionOp selectionOp) {
        this.selectionOp = selectionOp;
        return this;
    }
    
    public EngineBuilder setCrossoverOp(CrossoverOp crossoverOp) {
        this.crossoverOp = crossoverOp;
        return this;
    }
    
    public EngineBuilder addMutationOp(MutationOp mutationOp) {
        this.mutationOps.add(mutationOp);
        return this;
    }
    
    public EngineBuilder addListener(EngineListener listener) {
        this.listeners.add(listener);
        return this;
    }
    
    public EngineBuilder setTerminationOp(TerminationOp terminationOp) {
        this.terminationOp = terminationOp;
        return this;
    }
    
    public EngineBuilder setNumBestToKeep(int numBestToKeep) {
        this.numBestToKeep = numBestToKeep;
        return this;
    }
    
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
