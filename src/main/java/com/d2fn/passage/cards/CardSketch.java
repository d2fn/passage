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

    protected Card card;

    @Override
    public void renderFrame() {
        if(card.done()) {
            card = card.next();
        }
        card.renderFrame();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        card.mouseWheelMoved(e);
    }

    @Override
    public void mousePressed() {
        super.mousePressed();
        card.mousePressed();
    }

    @Override
    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);
        card.mousePressed(event);
    }

    @Override
    public void mouseReleased() {
        super.mouseReleased();
        card.mouseReleased();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        super.mouseReleased(event);
        card.mouseReleased(event);
    }

    @Override
    public void mouseClicked() {
        super.mouseClicked();
        card.mouseClicked();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        super.mouseClicked(event);
        card.mouseClicked(event);
    }

    @Override
    public void mouseDragged() {
        super.mouseDragged();
        card.mouseDragged();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        super.mouseDragged(event);
        card.mouseDragged(event);
    }

    @Override
    public void mouseMoved() {
        super.mouseMoved();
        card.mouseMoved();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        super.mouseMoved(event);
        card.mouseMoved(event);
    }

    @Override
    public void mouseEntered() {
        super.mouseEntered();
        card.mouseEntered();
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        super.mouseEntered(event);
        card.mouseEntered(event);
    }

    @Override
    public void mouseExited() {
        super.mouseExited();
        card.mouseExited();
    }

    @Override
    public void mouseExited(MouseEvent event) {
        super.mouseExited(event);
        card.mouseExited(event);
    }

    @Override
    public void mouseWheel() {
        super.mouseWheel();
        card.mouseWheel();
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        super.mouseWheel(event);
        card.mouseWheel(event);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        card.keyPressed(e);
    }
}
