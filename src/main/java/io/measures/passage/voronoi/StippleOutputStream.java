package io.measures.passage.voronoi;

import java.io.ByteArrayOutputStream;

/**
 * StippleOutputStream
 * @author Dietrich Featherston
 */
public class StippleOutputStream extends ByteArrayOutputStream {

    private final ByteArrayOutputStream os;

    public StippleOutputStream(ByteArrayOutputStream os) {
        this.os = os;
    }
}
