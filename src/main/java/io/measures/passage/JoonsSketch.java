package io.measures.passage;

import joons.JoonsRenderer;
import io.measures.passage.geometry.SphericalPoint;
import io.measures.passage.shapes.Rock;
import processing.event.KeyEvent;

/**
 * JoonsSketch
 * @author Dietrich Featherston
 */
public class JoonsSketch extends Sketch {

    protected JoonsRenderer jr;
    protected boolean triggerRender = false;
    protected boolean waitingForRender = false;

    @Override
    protected void beforeFrame() {
        super.beforeFrame();
    }

    @Override
    protected void afterFrame() {
        super.afterFrame();
        if(triggerRender && !jr.isRendering()) {
            triggerRender = false;
            waitingForRender = true;
            jr.render();
        }
        else if(jr.isRendered() && waitingForRender) {
            waitingForRender = false;
            snapshot();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        super.keyPressed(event);
        if (key == 'r' || key == 'R') {
            triggerRender = true;
        }
    }

    public void panelXY(float w, float h) {
        beginShape(QUADS);
        vertex(-w/2, -h/2, 0);
        vertex(-w/2,  h/2, 0);
        vertex( w/2,  h/2, 0);
        vertex( w/2, -h/2, 0);
        endShape();
    }
}
