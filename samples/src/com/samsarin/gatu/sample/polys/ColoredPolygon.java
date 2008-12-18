/*
 * Copyright (c) 2008 Chris Pettitt
 */

package com.samsarin.gatu.sample.polys;

import java.awt.Color;
import java.awt.Polygon;

/**
 * @author cpettitt@samsarin.com
 */
public class ColoredPolygon {
    private final Color color;
    private final Polygon polygon;
    
    public ColoredPolygon(Color color, Polygon polygon) {
        this.color = color;
        this.polygon = polygon;
    }
    
    public Color color() {
        return color;
    }
    
    public Polygon polygon() {
        return polygon;
    }
}
