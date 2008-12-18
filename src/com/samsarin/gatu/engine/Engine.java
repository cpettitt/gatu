/*
 * Copyright (c) 2008 Chris Pettitt
 */
package com.samsarin.gatu.engine;

import java.util.concurrent.Callable;

import com.samsarin.gatu.primitive.Candidate;

/**
 * @author cpettitt@samsarin.com
 */
public interface Engine extends Callable<Candidate> {

}
