package io.measures.passage;

import com.google.common.base.Joiner;
import io.measures.passage.geometry.Model3D;
import io.measures.passage.geometry.Point2D;
import io.measures.passage.geometry.Projectable2D;
import io.measures.passage.geometry.Projectable3D;
import io.measures.passage.geometry.SphericalPoint;
import io.measures.passage.geometry.Triangle3D;
import processing.core.PApplet;

import processing.core.PGraphics;
import processing.core.PImage;
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

    public final int slate = color(50);
    public final int gray = color(180);
    public final int yellow = color(255, 255, 1);
    public final int pink = color(255, 46, 112);
    public final int teal = color(85, 195, 194);
    public final int nak = color(0, 170, 255);
    public final int blue = color(49, 130, 189);
    public final int darkblue = color(49, 130, 189);
    public final int lightblue = color(222, 235, 247);
    public final int redorange = color(240, 59, 32);

    private PGraphics paperGraphics;

    public Sketch() {

        String homeDir = getParameter("PASSAGE_HOME");
        String userDir = System.getProperty("user.dir");
        if(homeDir == null) {
            homeDir = userDir;
        }
        this.homeDir = homeDir;

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

    public float getAspectRatio() {
        return (float)getTargetWidth() / (float)getTargetHeight();
    }

     @Override
    public final void draw() {
        beforeFrame();
        renderFrame();
        afterFrame();
    }

    public void beforeFrame() {}
    public void renderFrame() {}
    public void afterFrame() {}

    public int darker(int c) {
       return color(round(red(c)*0.6f), round(green(c)*0.6f), round(blue(c)*0.6f));
    }

    public int lighter(int c) {
        return color(
                constrain(round(red(c)*1.1f), 0, 255),
                constrain(round(green(c)*1.1f), 0, 255),
                constrain(round(blue(c)*1.1f), 0, 255));
    }

    public void point(Projectable2D p) {
        point(p.x(), p.y());
    }

    public void point(Projectable3D p) {
        point(p.x(), p.y(), p.z());
    }

    public void vertex(Projectable3D p) {
        vertex(p.x(), p.y(), p.z());
    }

    public void vertex(Projectable2D p) {
        vertex(p.x(), p.y());
    }

    public void line(Projectable3D a, Projectable3D b) {
        line(a.x(), a.y(), a.z(), b.x(), b.y(), b.z());
    }

    public void line(Projectable2D a, Projectable2D b) {
        line(a.x(), a.y(), b.x(), b.y());
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

    public void triangle(Triangle3D t) {
        beginShape(TRIANGLE);
        vertex(t.a());
        vertex(t.b());
        vertex(t.c());
        endShape();
    }

    public void translate(Projectable2D p) {
        translate(p.x(), p.y());
    }

    public void translate(Projectable3D p) {
        translate(p.x(), p.y(), p.z());
    }

    public void translateNormal(SphericalPoint p) {
        rotateZ(p.phi());
        rotateY(p.theta());
        translate(0, 0, p.r());
    }

    public static float dist(Projectable2D a, Projectable2D b) {
        return dist(a.x(), a.y(), b.x(), b.y());
    }

    public static float dist(Projectable3D a, Projectable3D b) {
        return dist(a.x(), a.y(), a.z(), b.x(), b.y(), b.z());
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
        snapshotCode();
        snapshotFrame();
    }

    public void snapshotCode() {
        println("taking code snapshot");
        copyFile(getSketchSourceFile(), getSnapshotPath("code-" + startedAt + ".java"));
    }

    public void snapshotFrame() {
        println("taking raster snapshot");
        saveFrame(getSnapshotPath("raster-" + startedAt + ".png"));
    }

    // todo - compatibility
    public void copyFile(String from, String to) {
        try {
            String command = "cp " + from + " " + to;
            Process p = Runtime.getRuntime().exec(command);
            copy(p.getInputStream(), System.out);
            copy(p.getErrorStream(), System.err);
        }
        catch(Exception ignored) {
            println(ignored.getMessage());
            ignored.printStackTrace();
        }
    }

    public void copy(InputStream in, OutputStream out) throws IOException {
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

    public static void fit(PImage img, int maxWidth, int maxHeight) {
        // oblig image resizing to fit in our space
        float imgratio = (float)img.width / (float)img.height;
        if(img.width > img.height) {
            img.resize(round(imgratio * maxHeight), maxHeight);
        }
        else {
            img.resize(maxWidth, round(maxWidth/imgratio));
        }
    }

    @Override
    public File dataFile(String where) {
        File why = new File(where);
        if (why.isAbsolute()) return why;
        File sketchSpecificFile = new File(getDataPath(where));
        if(sketchSpecificFile.exists()) {
            return sketchSpecificFile;
        }
        else {
            return why;
        }
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

    public void oldPaperBackground() {
        if(paperGraphics == null) {
            int center = color(252, 243, 211);
            int edge = color(245, 222, 191);
            PGraphics g = createGraphics(10, 10);
            g.beginDraw();
            g.noStroke();
            g.background(edge);
            g.fill(center);
            g.ellipse(g.width/2, g.height/2, g.width*0.8f, g.height*0.8f);
            g.filter(BLUR, 2);
            g.endDraw();
            /*
            g.filter(BLUR, width/30);
            g.filter(BLUR, width/30);
            g.filter(BLUR, width/30);
            */
            paperGraphics = g;
        }
    }
}
