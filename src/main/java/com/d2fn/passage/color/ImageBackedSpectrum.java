package com.d2fn.passage.color;

import processing.core.PImage;
import static com.d2fn.passage.Sketch.*;

/**
 * ImageBackedSpectrum
 * @author Dietrich Featherston
 */
public class ImageBackedSpectrum implements Spectrum {

    private final PImage img;

    public ImageBackedSpectrum(PImage img) {
        this.img = img;
    }

    @Override
    public int get(float value) {
        return img.get(0, round(map(value, 0, 1, img.height-1, 0)));
    }
}
