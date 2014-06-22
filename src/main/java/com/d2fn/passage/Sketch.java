package com.d2fn.passage;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.d2fn.passage.color.ImageBackedSpectrum;
import com.d2fn.passage.color.Spectrum;
import com.d2fn.passage.geometry.Line2D;
import com.d2fn.passage.geometry.Model3D;
import com.d2fn.passage.geometry.Point2D;
import com.d2fn.passage.geometry.Projectable2D;
import com.d2fn.passage.geometry.Projectable3D;
import com.d2fn.passage.geometry.Rect2D;
import com.d2fn.passage.geometry.SphericalPoint;
import com.d2fn.passage.geometry.Triangle3D;
import processing.core.PApplet;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

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
    public final int purple = color(79, 91, 206);
    public final int blue = color(49, 130, 189);
    public final int darkblue = color(49, 130, 189);
    public final int lightblue = color(222, 235, 247);
    public final int redorange = color(240, 59, 32);
    public final int sepia = color(221, 219, 205);

    private PGraphics paperGraphics;

    protected boolean recordPdf = false;
    protected long pdfTime = 0L;

    private boolean saveAnimation = false;
    private int numAnimationFrames = -1; // save an unbounded number of frames

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

    public void saveAnimation() {
        this.saveAnimation = true;
    }

    public void animationFrames(int n) {
        numAnimationFrames = n;
    }

    public int animationFrames() {
        return numAnimationFrames;
    }

    @Override
    public void setup() {
        initSize(P2D);
    }

     @Override
    public final void draw() {
        beforeFrame();
        try {
            renderFrame();
        } catch(Exception e) {
            println(e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            afterFrame();
        }
    }

    public void beforeFrame() {
        if(recordPdf) {
            beginRaw(PDF, getSnapshotPath("vector-" + pdfTime + ".pdf"));
        }
    }

    public void renderFrame() {
        println("ERROR - sketch renderFrame() not implemented");
        noLoop();
    }

    public void afterFrame() {
        if(recordPdf) {
            endRaw();
            recordPdf = false;
        }
        if(saveAnimation) {
            saveFrame(getSnapshotPath("frames-" + startedAt + "/frame-#######.jpg"));
            if(numAnimationFrames > 0 && frameCount >= numAnimationFrames) {
                exit();
            }
        }
    }

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

    public void points(Iterable<? extends Projectable2D> points) {
        for(Projectable2D p : points) {
            point(p);
        }
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

    public void line(Line2D line) {
        line(line.a(), line.b());
    }

    public void line(Projectable2D a, Projectable2D b) {
        line(a.x(), a.y(), b.x(), b.y());
    }

    public Rect2D viewport() {
        return new Rect2D(new Point2D(0, 0), width, height);
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

    public static float dist(Projectable2D p) {
        return dist(p.x(), p.y(), 0, 0);
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
        if(key == ' ') {
            // snapshot raster graphics + code
            snapshot();
        }
        else if(key == 'p') {
            // snapshot a pdf + code
            recordPdf = true;
            pdfTime = now();
            snapshotCode(pdfTime);
        }
    }

    public long now() {
        return System.currentTimeMillis()/1000;
    }

    public void snapshot() {
        long time = now();
        snapshotCode(time);
        snapshotFrame(time);
    }

    public void snapshotCode() {
        snapshotCode(startedAt);
    }

    public void snapshotCode(long time) {
        println("taking code snapshot");
        copyFile(getSketchSourceFile(), getSnapshotPath("code-" + time + ".java"));
    }

    public void snapshotFrame() {
        snapshotFrame(now());
    }

    public void snapshotFrame(long time) {
        println("taking raster snapshot");
        saveFrame(getSnapshotPath("raster-" + time + ".jpg"));
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

    public String getHomeDir() {
        return homeDir;
    }

    public String getHomePath(String name) {
        return pathJoiner.join(getHomeDir(), name);
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

    public void fit(PImage img) {
        if((img.width > width) || (img.height > height)) {
            if(((float) img.width / (float)img.height) > ((float) width / (float) height)) {
                img.resize(width, 0);
            }
            else {
                img.resize(0, height);
            }
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
            File f = new File(pathJoiner.join(baseDataDir, "data", where));
            if(f.exists()) {
                return f;
            }
        }
        return why;
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

    public Spectrum loadSpectrum(String path) {
        return new ImageBackedSpectrum(loadImage(path));
    }

    public static float sinF(float t, float wavelength, float phase, float min, float max) {
        return 0.5f*(1+sin(t/wavelength+phase))*(max-min) + min;
    }

    public static float bias(float x, float b) {
        if(b < 0) {
            return (1 - exp(b*x))/(1-exp(b));
        }
        return exp(b*x)/exp(b);
    }

    public static float sgn(float n) {
        return n > 0 ? 1 : -1;
    }

    public PImage[] loadImageSequence(String folderName) {
        String dir = dataPath(folderName);
        String[] names = new File(dir).list();
        Arrays.sort(names);
        List<PImage> images = Lists.newArrayList();
        for(String name : names) {
            if(!name.startsWith(".")) {
                images.add(loadImage(pathJoiner.join(folderName, name)));
            }
        }
        return images.toArray(new PImage[images.size()]);
    }
}
