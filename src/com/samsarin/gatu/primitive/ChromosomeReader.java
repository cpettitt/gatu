/*
 * Copyright (c) 2009 Chris Pettitt
 */
package com.samsarin.gatu.primitive;

/**
 * A simple stream-like reader for a Chromosome.
 * <p/>
 * This class is not thread-safe.
 *
 * @author <chris@samsarin.com>
 */
public class ChromosomeReader {
    private final Chromosome _chromosome;
    private int _index;
    
    public ChromosomeReader(Chromosome chromosome) {
        _chromosome = chromosome;
    }
    
    /**
     * Resets this reader and seeks to the beginning of the Chromosome.
     */
    public void reset() {
        seek(0);
    }
    
    /**
     * Seeks to the specified index in the Chromosome.
     * 
     * @throws IndexOutOfBoundsException if the index is greater than the 
     *         length of the Chromosome.
     */
    public void seek(int index) {
        if (_chromosome.length() < index) {
            throw new IndexOutOfBoundsException();
        }
        _index = index;
    }
    
    /**
     * Returns the current index.
     * 
     * @return the current index
     */
    public int index() {
        return _index;
    }
    
    /**
     * Returns the number of bits still available to be read from this
     * Chromosome.
     * 
     * @return bits remaining
     */
    public int remaining() {
        return size() - index();
    }
    
    /**
     * Returns the size of the backing Chromosome.
     * 
     * @return Chromosome size
     */
    public int size() {
        return _chromosome.length();
    }
    
    /**
     * Returns an int value by reading {@link Integer#SIZE} bits.
     * 
     * @return an int value.
     * @throws NotEnoughBitsException if not enough bits are available
     */
    public int readInt() {
        return readInt(Integer.SIZE);
    }
    
    /**
     * Returns an int value by reading the specified number of bits.
     * 
     * @return an int value.
     * @throws NotEnoughBitsException if not enough bits are available
     * @throws IllegalArgumentException if the {@code numBits > Integer.SIZE}
     */    
    public int readInt(int numBits) {
        if (numBits > Integer.SIZE) {
            throw new IllegalArgumentException("requested bits would overflow int storage.");
        }
        
        return (int)readLong(numBits);
    }

    /**
     * Returns a long value by reading {@link Long#SIZE} bits.
     * 
     * @return a long value.
     * @throws NotEnoughBitsException if not enough bits are available
     */
    public long readLong() {
        return readLong(Long.SIZE);
    }
    
    /**
     * Returns a long value by reading the specified number of bits.
     * 
     * @return a long value.
     * @throws NotEnoughBitsException if not enough bits are available
     * @throws IllegalArgumentException if the {@code numBits > Long.SIZE}
     */       
    public long readLong(int numBits) {
        if (numBits > Long.SIZE) {
            throw new IllegalArgumentException("requested bits would overflow long storage.");
        }
     
        assertAvailable(numBits);
        
        int lastIndex = _index + numBits;
        long value = 0;
        for (; _index < lastIndex; ++_index) {
            value = (value << 1) + (_chromosome.get(_index) ? 1 : 0);
        }
        return value;
    }
    
    /**
     * Returns a float value by reading {@link Integer#SIZE} bits.
     * 
     * @return a float value.
     * @throws NotEnoughBitsException if not enough bits are available
     */    
    public float readFloat() {
        return Float.floatToIntBits(readInt());
    }
    
    /**
     * Returns a double value by reading {@link Long#SIZE} bits.
     * 
     * @return a double value.
     * @throws NotEnoughBitsException if not enough bits are available
     */        
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    /**
     * Asserts that enough bits remain to satisfy a request.
     * 
     * @param numBits the number of bits required
     * @throws NotEnoughBitsException if not enough bits are available
     */
    private void assertAvailable(int numBits) {
        if (numBits > remaining()) {
            throw new NotEnoughBitsException("requested: " + numBits + ". available: " + remaining());
        }
    }
}
