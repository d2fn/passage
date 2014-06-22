package com.d2fn.passage;

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

    private static final void usage(String[] args) {
        System.out.println("usage: " + args[0] + " [sketchname]");
    }

    public static void main(String[] args) throws Exception {
        if(args.length != 2) usage(args);
        String className = args[1];
        Class<? extends Sketch> c = (Class<Sketch>)Class.forName(className);
        Sketch s = c.newInstance();
        new Runner(s);
    }

    private class SketchFrame extends JFrame {
        SketchFrame(final Sketch s) {
            super("passage");
            Dimension d = new Dimension(s.getTargetWidth(), s.getTargetHeight());
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            getContentPane().setPreferredSize(d);
            getContentPane().setSize(d);
            getContentPane().add(s, BorderLayout.CENTER);
            setMinimumSize(d);
            setSize(d);
            setPreferredSize(d);
            pack();
            setLocation(0, 0);
            setVisible(true);
            s.init();
            setResizable(false);
        }
    }
}
