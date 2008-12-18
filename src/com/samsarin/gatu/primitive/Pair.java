/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.primitive;

/**
 * @author cpettitt@samsarin.com
 */
public class Pair<T> {
    private final T first;
    private final T second;
    
    /**
     * Constructs a new pair. {@code first} and {@code second} may not be
     * {@code null}.
     * 
     * @throws NullPointerException if either value is {@code null}.
     */
    public Pair(T first, T second) {
        if (first == null) {
            throw new NullPointerException();
        }
        
        if (second == null) {
            throw new NullPointerException();
        }
        
        this.first = first;
        this.second = second;
    }

    public T first() {
        return first;
    }

    public T second() {
        return second;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (!(o instanceof Pair)) return false;
        Pair p = (Pair)o;
        
        return first.equals(p.first) && second.equals(p.second);
    }
    
    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode = hashCode * 37 + first.hashCode();
        hashCode = hashCode * 37 + second.hashCode();
        return hashCode;
    }
    
    @Override
    public String toString() {
        return "Pair[first=" + first + ", second=" + second + "]";
    }
}
