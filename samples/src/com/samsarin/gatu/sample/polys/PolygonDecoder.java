/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.sample.polys;

import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.Chromosomes;
import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts a chromosome into its phenotype (set of colors and polygons).
 * This class is NOT thread-safe.
 *
 * @author chris@samsarin.com
 */
public class PolygonDecoder {
    private static final int BITS_PER_COLOR_ELEM = 8;

    private final int sizeX;
    private final int sizeY;
    private final int bitsPerX;
    private final int bitsPerY;
    private final int minVerticesPerPoly;
    private final int maxVerticesPerPoly;
    private final int bitsPerVertexCount;
    private int index;
    
    private PolygonDecoder(int sizeX, int sizeY, int minVerticesPerPoly, int maxVerticesPerPoly) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.bitsPerX = getBitsToStore(sizeX);
        this.bitsPerY = getBitsToStore(sizeY);
        this.minVerticesPerPoly = minVerticesPerPoly;
        this.maxVerticesPerPoly = maxVerticesPerPoly;
        this.bitsPerVertexCount = getBitsToStore(maxVerticesPerPoly - minVerticesPerPoly);
    }
    
    public static int estimatedBitsToEncodePolys(int numPolys, int pointsPerPoly, int sizeX, int sizeY) {
        return (BITS_PER_COLOR_ELEM * 4 +
                (getBitsToStore(sizeX) + getBitsToStore(sizeY)) * pointsPerPoly) * numPolys;
    }
    
    public static List<ColoredPolygon> decode(Chromosome chromosome, int sizeX, int sizeY,
            int minVerticesPerPoly, int maxVerticesPerPoly) {
        return new PolygonDecoder(sizeX, sizeY, minVerticesPerPoly, maxVerticesPerPoly).decode(chromosome);
    }

    public List<ColoredPolygon> decode(Chromosome chromosome) {
        List<ColoredPolygon> coloredPolys = new ArrayList<ColoredPolygon>();

        while (true) {
            if (!hasCapacity(chromosome, BITS_PER_COLOR_ELEM * 4)) break;
            Color color = new Color(getValue(chromosome, BITS_PER_COLOR_ELEM),
                                    getValue(chromosome, BITS_PER_COLOR_ELEM),
                                    getValue(chromosome, BITS_PER_COLOR_ELEM),
                                    getValue(chromosome, BITS_PER_COLOR_ELEM));

            
            if (!hasCapacity(chromosome, bitsPerVertexCount)) break;
            int numVertices = (getValue(chromosome, bitsPerVertexCount) % 
                    (maxVerticesPerPoly - minVerticesPerPoly)) + minVerticesPerPoly;
            
            if (!hasCapacity(chromosome, numVertices * (bitsPerX + bitsPerY))) break;
            Polygon polygon = new Polygon();
            for (int i = 0; i < numVertices; ++i) {
                int x = getValue(chromosome, bitsPerX);
                if (x >= sizeX) x = sizeX - 1;

                int y= getValue(chromosome, bitsPerY);
                if (y >= sizeY) y = sizeY - 1;

                polygon.addPoint(x, y);
            }

            coloredPolys.add(new ColoredPolygon(color, polygon));
        }

        return coloredPolys;
    }

    private int getValue(Chromosome chromosome, int numBits) {
        int value = Chromosomes.rangeToInt(chromosome, index, index + numBits);
        index += numBits;
        return value;
    }

    private boolean hasCapacity(Chromosome chromosome, int numBits) {
        return chromosome.length() - index >= numBits;
    }

    private static int getBitsToStore(int value) {
        int bits = 0;
        while (value > 0) {
            value >>= 1;
            bits++;
        }
        return bits;
    }
}
