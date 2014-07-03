package com.d2fn.passage.cards;

import com.d2fn.passage.Sketch;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.event.MouseWheelEvent;

/**
 * CardSketch
 * @author Dietrich Featherston
 */
public abstract class CardSketch extends Sketch {

    protected Card<?, ?> card;

    @Override
    public void renderFrame() {
        if(card.done()) {
            card = card.next();
        }
        card.renderFrame();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        card.mouseWheelMoved(e);
    }

    @Override
    public void mousePressed() {
        card.mousePressed();
    }

    @Override
    public void mousePressed(MouseEvent event) {
        card.mousePressed(event);
    }

    @Override
    public void mouseReleased() {
        card.mouseReleased();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        card.mouseReleased(event);
    }

    @Override
    public void mouseClicked() {
        card.mouseClicked();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        card.mouseClicked(event);
    }

    @Override
    public void mouseDragged() {
        card.mouseDragged();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        card.mouseDragged(event);
    }

    @Override
    public void mouseMoved() {
        card.mouseMoved();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        card.mouseMoved(event);
    }

    @Override
    public void mouseEntered() {
        card.mouseEntered();
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        card.mouseEntered(event);
    }

    @Override
    public void mouseExited() {
        card.mouseExited();
    }

    @Override
    public void mouseExited(MouseEvent event) {
        card.mouseExited(event);
    }

    @Override
    public void mouseWheel() {
        card.mouseWheel();
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        card.mouseWheel(event);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        card.keyPressed(e);
    }
}
