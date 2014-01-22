package io.measures.passage.shapes;

import com.google.common.collect.Lists;

import io.measures.passage.Sketch;
import static io.measures.passage.Sketch.*;

import io.measures.passage.geometry.Model3D;
import io.measures.passage.geometry.SphericalPoint;
import io.measures.passage.geometry.Triangle3D;

import java.util.List;

public class Rock implements Model3D {

    private final Sketch s;
    private final float r, dr, noiseScale, detail;

    public Rock(Sketch s, float r, float dr, float noiseScale, float detail) {
        this.s = s;
        this.r = r;
        this.dr = dr;
        this.noiseScale = noiseScale;
        this.detail = detail;
    }

    @Override
    public Iterable<Triangle3D> getTriangles() {
        List<Triangle3D> out = Lists.newArrayList();
        float dphi = PI/detail;
        float dtheta = TWO_PI/detail;
        for(float phi = 0; phi <= TWO_PI; phi += dphi) {
            for(float theta = 0; theta <= PI; theta += dtheta) {
                SphericalPoint a = new SphericalPoint(r, phi, theta);
                SphericalPoint b = new SphericalPoint(r, phi+dphi, theta);
                SphericalPoint c = new SphericalPoint(r, phi, theta+dtheta);
                SphericalPoint d = new SphericalPoint(r, phi+dphi, theta+dtheta);
                float noise1 = s.noiseZ(noiseScale, a.x(), a.y(), a.z());
                float noise2 = s.noiseZ(noiseScale, b.x(), b.y(), b.z());
                float noise3 = s.noiseZ(noiseScale, c.x(), c.y(), c.z());
                float noise4 = s.noiseZ(noiseScale, d.x(), d.y(), d.z());
                float h1 = map(noise1, 0, 1, -dr/2, dr/2);
                float h2 = map(noise2, 0, 1, -dr/2, dr/2);
                float h3 = map(noise3, 0, 1, -dr/2, dr/2);
                float h4 = map(noise4, 0, 1, -dr/2, dr/2);
                a = a.extend(h1);
                b = b.extend(h2);
                c = c.extend(h3);
                d = d.extend(h4);
                out.add(new Triangle3D(a, b, c));
                out.add(new Triangle3D(d, c, b));
            }
        }
        return out;
    }
}
