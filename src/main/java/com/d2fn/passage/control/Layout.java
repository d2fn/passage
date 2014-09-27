package com.d2fn.passage.control;

import controlP5.Controller;

/**
 * Layout
 * @author Dietrich Featherston
 */
public interface Layout {
    public <T extends Controller> void place(Controller<T> c);
}
