/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.sample.polys;

import com.samsarin.gatu.engine.Engine;
import com.samsarin.gatu.engine.EngineBuilder;
import com.samsarin.gatu.engine.EngineListener;
import com.samsarin.gatu.engine.EngineListeners;
import com.samsarin.gatu.op.FitnessOp;
import com.samsarin.gatu.op.Ops;
import com.samsarin.gatu.primitive.Candidate;
import com.samsarin.gatu.primitive.Chromosome;
import com.samsarin.gatu.primitive.Chromosomes;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author cpettitt@samsarin.com
 */
public class Main {
    public static void usage() {
        System.err.println("Usage: java " + Main.class.getName() + " src-image dest-dir [num-polys]");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2 || args.length > 4) {
            usage();
        }

        final String srcFile = args[0];
        final String destDir = args[1];
        final int numPolys = 200;
        final int minVerticesPerPoly = 3;
        final int maxVerticesPerPoly = 10;
        
        if (args.length >= 3) {
            Integer.parseInt(args[2]);
        }

        final BufferedImage srcImage = ImageIO.read(new File(srcFile));
        
        int chromosomeLength = PolygonDecoder.estimatedBitsToEncodePolys(numPolys,
                (minVerticesPerPoly + maxVerticesPerPoly) / 2, srcImage.getWidth(), srcImage.getHeight());
        
        FitnessOp fitnessOp = new ImageFitnessOp(srcImage, minVerticesPerPoly, maxVerticesPerPoly);
        EngineBuilder builder = new EngineBuilder(intialPopulation(10, chromosomeLength), Ops.cachingFitness(fitnessOp, 50))
                .addMutationOp(Ops.pointMutation(5 / chromosomeLength))
                .addMutationOp(Ops.inversion(0.1 / chromosomeLength))
                .addListener(EngineListeners.generationPrinter())
                .setTerminationOp(Ops.terminateAtGeneration(1000000));

        builder.addListener(new EngineListener() {
            public void onGeneration(int generationNum, Candidate bestCandidate, List<Candidate> candidates) {
                if (generationNum % 100 == 0) {
                    saveCandidate(bestCandidate, Integer.toString(generationNum), 
                            srcImage.getWidth(), srcImage.getHeight(), 
                            minVerticesPerPoly, maxVerticesPerPoly, destDir);
                }
            }
        });

        Engine engine = builder.build();
        Candidate best = engine.call();

        System.out.println("Best fitness: " + best.fitness());
        saveCandidate(best, "best", srcImage.getWidth(), srcImage.getHeight(), 
                minVerticesPerPoly, maxVerticesPerPoly, destDir);
    }

    private static List<Chromosome> intialPopulation(int populationSize, int chromosomeLength) {
        List<Chromosome> population = new ArrayList<Chromosome>();

        for (int i = 0; i < populationSize; ++i) {
            population.add(Chromosomes.random(chromosomeLength));
        }

        return population;
    }
    
    private static void saveCandidate(Candidate candidate, String id, int sizeX, int sizeY, 
            int minVerticesPerPoly, int maxVerticesPerPoly, String destDir) {
        saveFitness(candidate, id, destDir);
        saveSvg(candidate.chromosome(), id, sizeX, sizeY, minVerticesPerPoly, maxVerticesPerPoly, destDir);
    }
    
    private static void saveSvg(Chromosome chromosome, String id, int sizeX, int sizeY,
            int minVerticesPerPoly, int maxVerticesPerPoly, String destDir) {
        PrintStream out = null;
        try {
            out = new PrintStream(new File(destDir + "/" + id + ".svg"));
            out.println("<?xml version=\"1.0\" standalone=\"no\"?>");
            out.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"");
            out.println("    \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
            out.println("<svg width=\"" + sizeX + "px\" height=\"" + sizeY + "px\"");
            out.println("    version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");

            out.println("    <rect width=\"" + sizeX + "\" height=\"" + sizeY + "\" style=\"fill:black\"/>");

            List<ColoredPolygon> polygons = PolygonDecoder.decode(chromosome, sizeX, sizeY,
                    minVerticesPerPoly, maxVerticesPerPoly);
            for (ColoredPolygon coloredPoly : polygons) {
                out.print("    <polygon points=\"");
                Polygon poly = coloredPoly.polygon();
                for (int i = 0; i < poly.npoints; ++i) {
                    out.print(poly.xpoints[i] + "," + poly.ypoints[i] + " ");
                }

                Color color = coloredPoly.color();
                out.println("\" fill=\"rgb(" + color.getRed() + "," + color.getGreen() + "," +
                        color.getBlue() + ")\" fill-opacity=\"" + (color.getAlpha() / (double)255) + "\"/>");
            }

            out.println("</svg>");
        } catch (Exception e) {
            System.err.println("Failed to save image: " + e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static void saveFitness(Candidate candidate, String id, String destDir) {
        PrintStream out = null;

        try {
            out = new PrintStream(new File(destDir +"/" + id + ".fitness.txt"));
            out.println("Fitness: " + candidate.fitness());
            out.println("Chromosome: " + candidate.chromosome());
        } catch (Exception e) {
            System.err.println("Failed to save fitness: " + e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
