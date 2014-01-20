package com.d2fn.passage.sketches;

import org.measures.passage.Sketch;
import org.measures.passage.geometry.SphericalPoint;

/**
 * SimpleTerrain01
 * @author Dietrich Featherston
 */
public class SimpleTerrain01 extends Sketch {

    float noiseScale = 0.01f;
    float dx = 7.5f;
    float dy = 7.5f;
    float maxZ = 300;
    float ycount = 250;
    float xcount = 250;
    boolean loop = false;

    float camRadius = 500, camPhi = 0, camTheta = PI/2 + PI/16;

    float eyeX, eyeY, eyeZ;

    boolean wave = true;

    boolean mouseControl = false;

    @Override
    public void setup() {
        initSize(P3D);
        noiseDetail(3, 0.5f);
        randomSeed(0);
        noiseSeed(0);
        smooth();
    }

    @Override
    protected void renderFrame() {
        background(0, 0, 0);

        if(mouseControl) {
            eyeX = 5 * (mouseX - width/2f);
            eyeY = 5 * (mouseY - height/2f);
            eyeZ = 500;
        }
        else {
            SphericalPoint p = new SphericalPoint(camRadius, camPhi, camTheta);
            eyeX = p.x();
            eyeY = p.y();
            eyeZ = p.z();
        }

        //eyeX = 125.0; eyeY = -1150.0;
        //eyeX = -25.0; eyeY = -745.0;
        eyeX = 45.0f; eyeY = -390.0f; eyeZ = 450;
        camera(eyeX, eyeY, eyeZ, 0, 0, 0,  0, -1, 0);
        //camera(0, 0, 500, 0, 0, 0,  0, -1, 0);
        translate(-xcount*dx/2, -ycount*dy/2, 0);

        stroke(150, 150, 150);
        strokeWeight(0.5f);
        pushMatrix();
        for(float y = 0; y < ycount; y++) {
            float yprogress = y/ycount;
            pushMatrix();
            for(float x = 0; x < xcount; x++) {
                float xprogress = x/xcount;
                float x1 = x * dx;
                float x2 = (x+1) * dx;
                float y1 = y * dy;
                float y2 = (y+1) * dy;
                float z1 = maxZ*noiseZ(x1, y1);
                float z2 = maxZ*noiseZ(x2, y1);
                float z3 = maxZ*noiseZ(x2, y2);
                //line(x1, y1, z1, x2, y1, z2);
                //line(x2, y2, z3, x2, y1, z2);
                float znoise = noiseZ(x1, y1);
                colorMode(HSB, 360);
                stroke(100+znoise*200, 360, znoise*360);
/*
      fill(znoise*(frameCount%360), 360, znoise*360);
      beginShape(TRIANGLES);
      vertex(0, 0, z1);
      vertex(dx, dy, z3);
      vertex(dx, 0, z2);
      endShape();
*/
                pushMatrix();
                //rotateZ(map(znoise, 0, 1, -TWO_PI, TWO_PI));
                line(0, 0, z1, dx, dy, z3);
                line(0, 0, z1, dx, 0, z2);
                popMatrix();
                translate(dx, 0, 0);
            }
            popMatrix();
            if(yprogress > 0.5) {
                //rotateX(-PI/16);
            }
            translate(0, dy, 0);
        }
        popMatrix();
    }

    float noiseZ(float x, float y) {
        float z = loop ? 0.3f*(1+sin(TWO_PI*frameCount/50)) : noiseScale * frameCount;
        float d = dist(0, 0, x, y);
        float strength = wave ? (1 + sin((100*frameCount+d)/200))/2f : 1;
        return strength * noise(noiseScale*(x+10000), noiseScale*(y+10000), (z + 10000));
    }
}
