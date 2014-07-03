package com.d2fn.passage.cards;

import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.event.MouseWheelEvent;

/**
 * Card
 * @author Dietrich Featherston
 */
public abstract class Card<I, O> {
    public abstract void begin(I input);
    public abstract boolean done();
    public abstract void renderFrame();
    public abstract Card<O, ?> next();
    public void mouseWheelMoved(MouseWheelEvent e) {}
    public void mousePressed() {}
    public void mousePressed(MouseEvent event) {}
    public void mouseReleased() {}
    public void mouseReleased(MouseEvent event) {}
    public void mouseClicked() {}
    public void mouseClicked(MouseEvent event) {}
    public void mouseDragged() {}
    public void mouseDragged(MouseEvent event) {}
    public void mouseMoved() {}
    public void mouseMoved(MouseEvent event) {}
    public void mouseEntered() {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited() {}
    public void mouseExited(MouseEvent event) {}
    public void mouseWheel() {}
    public void mouseWheel(MouseEvent event) {}
    public void keyPressed(KeyEvent e) {}
}
