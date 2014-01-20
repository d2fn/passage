package org.measures.passage.shapes;

public class Rock {

    private float r, dr, noiseScale, detail;

    public Rock(float r, float dr, float noiseScale, float detail) {
        this.r = r;
        this.dr = dr;
        this.noiseScale = noiseScale;
        this.detail = detail;
    }

    public float getR() {
        return r;
    }

    public float getRDevation() {
        return dr;
    }

    public float getNoiseScale() {
        return noiseScale;
    }

    public float getDetail() {
        return detail;
    }
}
