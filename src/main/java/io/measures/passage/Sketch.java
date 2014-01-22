package io.measures.passage;

import com.google.common.base.Joiner;
import io.measures.passage.geometry.Model3D;
import io.measures.passage.geometry.Projectable3D;
import io.measures.passage.geometry.Triangle3D;
import processing.core.PApplet;

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

    protected final String homeDir;
    protected final String baseDataDir;

    protected static final Joiner pathJoiner = Joiner.on(File.separatorChar);

    public Sketch() {
        homeDir = getParameter("PASSAGE_HOME");
        baseDataDir = getParameter("PASSAGE_DATA") == null ? homeDir : getParameter("PASSAGE_DATA");
        // ensure the snapshot directory exists
        new File(getSnapshotDir()).mkdirs();
        // ensure the data dir exists
        new File(getDataDir()).mkdirs();
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

    public int darker(int c) {
       return color(round(red(c)*0.6f), round(green(c)*0.6f), round(blue(c)*0.6f));
    }

    public void vertex(Projectable3D p) {
        vertex(p.x(), p.y(), p.z());
    }

    public void line(Projectable3D a, Projectable3D b) {
        line(a.x(), b.y(), b.z(), b.x(), b.y(), b.z());
    }

    void triangles(Projectable3D a, Projectable3D b, Projectable3D c, Projectable3D d) {
        beginShape(TRIANGLES);
        vertex(a);
        vertex(b);
        vertex(c);
        vertex(d);
        vertex(c);
        vertex(b);
        endShape();
    }

    public void renderModel(Model3D model) {
        beginShape(TRIANGLES);
        for(Triangle3D t : model.getTriangles()) {
            vertex(t.a());
            vertex(t.b());
            vertex(t.c());
        }
        endShape();
    }

    public float noiseZ(float noiseScale, float x, float y, float z) {
        return noise(noiseScale*(x+10000), noiseScale*(y+10000), noiseScale*(z+10000));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed();
        println("key pressed");
        if (key == ' ') {
            snapshot();
        }
    }

    public void snapshot() {
        System.out.println("saving code and raster");
        long now = System.currentTimeMillis()/1000L;
        copyFile(getSketchSourceFile(), getSnapshotPath("code-" + now + ".java"));
        saveFrame(getSnapshotPath("raster-" + now + ".png"));
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
        return pathJoiner.join(getSketchSourcePath(), getSketchPathComponent() + ".java");
    }

    private String getSketchSourcePath() {
        return pathJoiner.join(homeDir, "src/main/java");
    }

    public String getSketchPathComponent() {
        return this.getClass().getName().replace('.', File.separatorChar);
    }

    /**
     * @return the directory to which progress artifacts should be saved
     */
    public String getSnapshotDir() {
        return pathJoiner.join(baseDataDir, getSketchPathComponent(), "snapshots");
    }

    public String getSnapshotPath(String name) {
        return pathJoiner.join(getSnapshotDir(), name);
    }

    public String getDataDir() {
        return pathJoiner.join(baseDataDir, getSketchPathComponent(), "data");
    }

    public String getDataPath(String name) {
        return pathJoiner.join(getDataDir(), name);
    }

    @Override
    public File dataFile(String where) {
        File why = new File(where);
        if (why.isAbsolute()) return why;
        return new File(getDataPath(where));
    }

    @Override
    public String getParameter(String name) {
        return System.getenv(name);
    }

    public void printenv() {
        System.out.println("sketch name: " + this.getClass().getName());
        System.out.println("PASSAGE_HOME=" + homeDir);
        System.out.println("PASSAGE_DATA=" + baseDataDir);
        System.out.println("sketch-specific path = " + getSketchPathComponent());
        System.out.println("sketch source = " + getSketchSourcePath());
        System.out.println("sketch source file = " + getSketchSourceFile());
        System.out.println("snapshot dir = " + getSnapshotDir());
    }
}
