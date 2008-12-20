/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.sample.polys;

import com.samsarin.gatu.op.FitnessOp;
import com.samsarin.gatu.primitive.Chromosome;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.List;

/**
 * @author cpettitt@samsarin.com
 */
public class ImageFitnessOp implements FitnessOp {
    private final int sizeX;
    private final int sizeY;
    private final int[] srcColors;
    
    public ImageFitnessOp(BufferedImage image) {
        sizeX = image.getWidth();
        sizeY = image.getHeight();
        srcColors = imageToByteArray(image);
    }

    public double fitness(Chromosome chromosome) {
        double delta = 0;
        
        List<ColoredPolygon> polygons = PolygonDecoder.decode(chromosome, sizeX, sizeY);
        int[] phenotype = createPhenotype(polygons);

        assert phenotype.length == srcColors.length;

        for (int i = 0; i < phenotype.length; ++i) {
            delta += delta(srcColors[i], phenotype[i]);
        }

        if (delta == 0) delta = 1;

        return 1 / delta;
    }

    private double delta(int first, int second) {
        int redDelta = getRed(first) - getRed(second);
        int greenDelta = getGreen(first) - getGreen(second);
        int blueDelta = getBlue(first) - getBlue(second);
        return redDelta * redDelta + greenDelta * greenDelta + blueDelta * blueDelta;
    }

    private int getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    private int getGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    private int getBlue(int color) {
        return color & 0xFF;
    }

    private int[] createPhenotype(List<ColoredPolygon> coloredPolys) {
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = null;
        try {
            graphics = image.createGraphics();
            graphics.setBackground(Color.BLACK);
            for (ColoredPolygon coloredPoly: coloredPolys) {
                graphics.setColor(coloredPoly.color());
                graphics.fillPolygon(coloredPoly.polygon());
            }
            return imageToByteArray(image);
        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }
    }

    private int[] imageToByteArray(BufferedImage image) {
        int[] colors = new int[image.getWidth() * image.getHeight()];
        if (image.getType() == BufferedImage.TYPE_INT_ARGB ||
                image.getType() == BufferedImage.TYPE_INT_RGB) {
            WritableRaster raster = image.getRaster();
            raster.getDataElements(0, 0, sizeX, sizeY, colors);
        } else {
            image.getRGB(0, 0, sizeX, sizeY, colors, 0, sizeX);
        }
        return colors;
    }
}
