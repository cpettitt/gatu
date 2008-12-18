/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

/**
 * @author cpettitt@samsarin.com
 */
public interface Candidate extends Comparable<Candidate> {
    Chromosome chromosome();
    double fitness();
}
