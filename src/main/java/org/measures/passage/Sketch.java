package org.measures.passage;

import processing.core.PApplet;

import org.measures.passage.geometry.SphericalPoint;
import processing.event.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Sketch
 * @author Dietrich Featherston
 */
public class Sketch extends PApplet {

    protected final long startedAt = System.currentTimeMillis()/1000L;

    protected final String passageHome;
    protected final String passageArtifacts;

    public Sketch() {
        passageHome = getParameter("PASSAGE_HOME");
        passageArtifacts = getParameter("PASSAGE_ARTIFACTS");
        // make dirs to progress dir
        if(passageArtifacts != null) {
            new File(getArtifactDir()).mkdirs();
        }
    }

    public void initSize(String type) {
        size(getTargetWidth(), getTargetHeight(), type);
    }

    public int getTargetWidth() {
        return 1280;
    }

    public int getTargetHeight() {
        return 720;
    }

     @Override
    public final void draw() {
        beforeFrame();
        renderFrame();
        afterFrame();
    }

    protected void beforeFrame() {}
    protected void renderFrame() {}
    protected void afterFrame() {}

    void vertex(SphericalPoint p) {
        vertex(p.x(), p.y(), p.z());
    }

    void line(SphericalPoint a, SphericalPoint b) {
        line(a.x(), b.y(), b.z(), b.x(), b.y(), b.z());
    }

    void triangles(SphericalPoint a, SphericalPoint b, SphericalPoint c, SphericalPoint d) {
        beginShape(TRIANGLES);
        vertex(a);
        vertex(b);
        vertex(c);
        vertex(d);
        vertex(c);
        vertex(b);
        endShape();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed();
        println("key pressed");
        if (key == ' ') {
            save();
        }
    }

    public void save() {
        System.out.println("saving code and raster");
        long now = System.currentTimeMillis()/1000L;
        copyFile(getSketchSourceFile(), getArtifactPath("code-" + now + ".java"));
        saveFrame(getArtifactPath("raster-" + now + ".png"));
    }

    // todo - compatibility
    void copyFile(String from, String to) {
        try {
            String command = "cp " + from + " " + to;
            println("running command \"" + command + "\"");
            Process p = Runtime.getRuntime().exec(command);
            copy(p.getInputStream(), System.out);
            copy(p.getErrorStream(), System.err);
            int exitValue = p.waitFor();
            println("command exited with code " + exitValue);
        }
        catch(Exception ignored) {
            println(ignored.getMessage());
            ignored.printStackTrace();
        }
    }

    void copy(InputStream in, OutputStream out) throws IOException {
        while (true) {
            int c = in.read();
            if (c == -1) break;
            out.write((char)c);
        }
    }

    public String getSketchSourceFile() {
        StringBuilder javaPath = new StringBuilder();
        javaPath.append(getSketchSourcePath()).append(File.separatorChar);
        javaPath.append(getSketchSpecificPath()).append(".java");
        return javaPath.toString();
    }

    public String getSketchSourcePath() {
        StringBuilder path = new StringBuilder();
        path.append(passageHome).append(File.separatorChar);
        path.append("src/main/java");
        return path.toString();
    }

    public String getSketchSpecificPath() {
        return this.getClass().getName().replace('.', File.separatorChar);
    }

    /**
     * @return the directory to which progress artifacts should be saved
     */
    public String getArtifactDir() {
        StringBuilder path = new StringBuilder();
        path.append(passageArtifacts).append(File.separatorChar);
        path.append(getSketchSpecificPath());
        return path.toString();
    }

    public String getArtifactPath(String name) {
        StringBuilder path = new StringBuilder();
        path.append(getArtifactDir()).append(File.separatorChar);
        path.append(name);
        return path.toString();
    }


    @Override
    public String getParameter(String name) {
        return System.getenv(name);
    }

    public void debug() {
        System.out.println("sketch name: " + this.getClass().getName());
        System.out.println("PASSAGE_HOME=" + passageHome);
        System.out.println("PASSAGE_ARTIFACTS=" + passageArtifacts);
        System.out.println("sketch-specific path = " + getSketchSpecificPath());
        System.out.println("sketch source = " + getSketchSourcePath());
        System.out.println("sketch source file = " + getSketchSourceFile());
        System.out.println("progress artifact dir = " + getArtifactDir());
    }
}
