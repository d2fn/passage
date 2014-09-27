package com.d2fn.passage.control;

import controlP5.Controller;

/**
 * LinearLayout
 * @author Dietrich Featherston
 */
public class LinearLayout implements Layout {

    private int componentWidth = 850;
    private int componentHeight = 10;
    private int rowSpacing = 10;
    private int placeX = 10, placeY = 10;

    public <T extends Controller> void place(Controller<T> c) {
        place(c, 1);
    }

    public <T extends Controller> void place(Controller<T> c, int amt) {
        c.setSize(componentWidth, componentHeight)
            .setPosition(placeX, placeY);
        placeY += amt*componentHeight + rowSpacing;
    }
}
