/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.sample.polys;

import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.ChromosomeReader;
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

        ChromosomeReader reader = new ChromosomeReader(chromosome);
        
        while (true) {
            if (reader.remaining() < BITS_PER_COLOR_ELEM * 4) break;
            Color color = new Color(reader.readInt(BITS_PER_COLOR_ELEM),
                                    reader.readInt(BITS_PER_COLOR_ELEM),
                                    reader.readInt(BITS_PER_COLOR_ELEM),
                                    reader.readInt(BITS_PER_COLOR_ELEM));
            
            if (reader.remaining() < bitsPerVertexCount) break;
            int numVertices = (reader.readInt(bitsPerVertexCount) % 
                    (maxVerticesPerPoly - minVerticesPerPoly)) + minVerticesPerPoly;
            
            if (reader.remaining() < numVertices * (bitsPerX + bitsPerY)) break;
            Polygon polygon = new Polygon();
            for (int i = 0; i < numVertices; ++i) {
                int x = reader.readInt(bitsPerX);
                if (x >= sizeX) x = sizeX - 1;

                int y= reader.readInt(bitsPerY);
                if (y >= sizeY) y = sizeY - 1;

                polygon.addPoint(x, y);
            }

            coloredPolys.add(new ColoredPolygon(color, polygon));
        }

        return coloredPolys;
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
