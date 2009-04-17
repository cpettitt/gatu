/*
 * Copyright (c) 2009 Chris Pettitt
 */
package com.samsarin.gatu.primitive;

/**
 * An exception indicating that not enough bits remain to satisfy a read
 * request. 
 *
 * @author <chris@samsarin.com>
 */
public class NotEnoughBitsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotEnoughBitsException(String message) {
        super(message);
    }
}
