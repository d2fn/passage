package io.measures.passage;

import com.d2fn.passage.sketches.RockMatrixSketch;

import javax.swing.*;
import java.awt.*;

/**
 * Runner
 * @author Dietrich Featherston
 */
public class Runner {

    public Runner(final Sketch s) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SketchFrame(s).setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
//        new Runner(new SimpleTerrain01());
//        new Runner(new TestSketch());
        new Runner(new RockMatrixSketch());
    }

    private class SketchFrame extends JFrame {
        SketchFrame(final Sketch s) {
            super("passage");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            getContentPane().setPreferredSize(new Dimension(s.getTargetWidth(), s.getTargetHeight()));
            getContentPane().setSize(s.getTargetWidth(), s.getTargetHeight());
            getContentPane().add(s, BorderLayout.CENTER);
            pack();
            setVisible(true);
            s.init();
            setLocation(100, 100);
            setResizable(false);
        }
    }
}
