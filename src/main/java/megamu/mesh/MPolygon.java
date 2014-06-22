package megamu.mesh;

import com.d2fn.passage.geometry.Point2D;
import processing.core.*;

public class MPolygon {

	float[][] coords;
	int count;
	
	public MPolygon(){
		this(0);
	}

	public MPolygon(int points){
		coords = new float[points][2];
		count = 0;
	}

	public void add(float x, float y){
		coords[count][0] = x;
		coords[count++][1] = y;
	}

	public void draw(PApplet p){
		draw(p.g);
	}

	public void draw(PGraphics g){
		g.beginShape();
		for(int i=0; i<count; i++){
			g.vertex(coords[i][0], coords[i][1]);
		}
		g.endShape(PApplet.CLOSE);
	}

	public int count(){
		return count;
	}

	public float[][] getCoords(){
		return coords;
	}

    public Point2D getCentroid() {
        float cx = 0f, cy = 0f;
        for(float[] p : coords) {
            cx += p[0]/coords.length;
            cy += p[1]/coords.length;
        }
        return new Point2D(cx, cy);
    }

    public boolean isEmpty() {
        return coords.length == 0;
    }
}