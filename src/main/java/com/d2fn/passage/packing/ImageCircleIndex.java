package com.d2fn.passage.packing;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.d2fn.passage.Sketch;
import com.d2fn.passage.geometry.Point2D;
import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.d2fn.passage.Sketch.*;

/**
 * ImageCircleIndex
 * Holds an index of candidate circles and their luminescence error rates
 * @author Dietrich Featherston
 */
public class ImageCircleIndex {

    // hold candidates
    private Map<String, Candidate> candidates;

    // map of positions to the number of circles on that point
    private ConcurrentMap<String, AtomicInteger> xyToCount;

    private final int errorThreshold;

    public static String pathFor(Sketch s, String imgName) {
        return s.getDataPath(imgName) + "-circle-index.csv";
    }

    public ImageCircleIndex(int errorThreshold) {
        this.candidates = Maps.newConcurrentMap();
        this.xyToCount = Maps.newConcurrentMap();
        this.errorThreshold = errorThreshold;
    }

    public Future load(Sketch s, String imgName, PImage img, float minR, float maxR) {
        File f = new File(pathFor(s, imgName));
        Runnable loader;
        if(f.exists()) {
            loader = new IndexFileReader(this, s, imgName, errorThreshold);
        }
        else {
            loader = new Calculator(this, s, img, imgName, minR, maxR);
        }
        ExecutorService es = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1));
        return es.submit(loader);
    }

    public void add(float x, float y, float r, float error) {
        add(round(x), round(y), round(r), round(error));
    }

    /**
     * record a candidate circle's error rate in the index
     * @param x - image x coordinate
     * @param y - image y coordinate
     * @param r - radius at x, y
     * @param error - the calculated error
     */
    public void add(int x, int y, int r, int error) {
        add(new Candidate(x, y, r, error));
    }

    private void add(Candidate c) {
        candidates.put(c.key(), c);
        if(c.getError() < errorThreshold) {
            String pkey = c.positionKey();
            xyToCount.putIfAbsent(pkey, new AtomicInteger(0));
            xyToCount.get(pkey).incrementAndGet();
        }
    }

    public int getCount(int x, int y) {
        String pkey = positionKey(x, y);
        if(xyToCount.containsKey(pkey)) {
            return xyToCount.get(pkey).get();
        }
        return 0;
    }

    public boolean check(float x, float y, float r, float errorThreshold) {
        return check(round(x), round(y), round(r), round(errorThreshold));
    }

    /**
     * @param x - image x coordinate
     * @param y - image y coordinate
     * @param r - radius at x, y
     * @param errorThreshold - max acceptable luminescence error
     * @return true if the candidate circle is under the given error threshold
     */
    public boolean check(int x, int y, int r, int errorThreshold) {
        Candidate c = candidates.get(key(x, y, r));
        return c != null && c.getError() < errorThreshold;
    }

    private static String positionKey(int x, int y) {
        return new Candidate(x, y, Integer.MAX_VALUE, Integer.MAX_VALUE).positionKey();
    }

    private static String key(int x, int y, int r) {
        return new Candidate(x, y, r, Integer.MAX_VALUE).key();
    }

    private static class Candidate {

        private final int x, y, r, error;

        Candidate(int x, int y, int r, int error) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.error = error;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }

        int getR() {
            return r;
        }

        int getError() {
            return error;
        }

        static Joiner j = Joiner.on(",");
        String key() {
            return j.join(x, y, r);
        }

        String positionKey() {
            return j.join(x, y);
        }

        @Override
        public String toString() {
            return j.join(x, y, r, error);
        }

        static Candidate parse(String line) {
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int r = Integer.parseInt(parts[2]);
            int error = Integer.parseInt(parts[3]);
            return new Candidate(x, y, r, error);
        }
    }

    public void save(Sketch s, String imgName) {
        try {
            PrintWriter pw = new PrintWriter(new File(pathFor(s, imgName)));
            List<Candidate> sortedCandidates = Lists.newArrayList();
            sortedCandidates.addAll(candidates.values());
            // store candidates from low error to high error so that parsing can terminate
            // as soon as a circle is reached that breaches the error threshold
            Collections.sort(sortedCandidates,
                    new Comparator<Candidate>() {
                        @Override
                        public int compare(Candidate a, Candidate b) {
                            return a.getError() - b.getError();
                        }
                    });
            for(Candidate c : sortedCandidates) {
                pw.println(c.toString());
            }
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    static class IndexFileReader implements Runnable {

        private final ImageCircleIndex index;
        private final Sketch s;
        private final String imgName;
        private final int errorThreshold;

        IndexFileReader(ImageCircleIndex index, Sketch s, String imgName, int errorThreshold) {
            this.index = index;
            this.s = s;
            this.imgName = imgName;
            this.errorThreshold = errorThreshold;
        }

        @Override
        public void run() {
            Scanner scanner = null;
            try {
                scanner = new Scanner(new File(pathFor(s, imgName)));
                while(scanner.hasNextLine()) {
                    Candidate c = Candidate.parse(scanner.nextLine());
                    if(c != null) {
                        if(c.getError() > errorThreshold) {
                            // don't read past the first circle that breaches the error threshold
                            break;
                        }
                        index.add(c);
//                        Thread.sleep(5);
                    }
                }
            }
            catch (FileNotFoundException e) {
                println(e.getMessage());
                e.printStackTrace(System.err);
            }
//            catch(InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
            finally {
                if(scanner != null) scanner.close();
            }
        }
    }

    static class Calculator implements Runnable {

        private final ImageCircleIndex index;
        private final Sketch s;
        private final PImage img;
        private final String imgName;
        private final float minR, maxR;

        Calculator(ImageCircleIndex index, Sketch s, PImage img, String imgName, float minR, float maxR) {
            this.index = index;
            this.s = s;
            this.img = img;
            this.imgName = imgName;
            this.minR = minR;
            this.maxR = maxR;
        }

        @Override
        public void run() {
            int cpus = Runtime.getRuntime().availableProcessors();
            ExecutorService es = new ThreadPoolExecutor(cpus, cpus, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
            List<Future> flist = Lists.newArrayList();
            for(int y = 0; y < img.height; y++) {
                for(int x = 0; x < img.width; x++) {
                    flist.add(es.submit(new ErrorCalculator(index, s, img, x, y, minR, maxR)));
                }
            }
            for(Future f : flist) {
                try {
                    f.get();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                catch (ExecutionException e) {
                    e.printStackTrace(System.err);
                }
            }
            index.save(s, imgName);
        }
    }

    static class ErrorCalculator implements Runnable {

        private final ImageCircleIndex index;
        private final Sketch s;
        private final PImage img;
        private final float x, y, minR, maxR;

        ErrorCalculator(ImageCircleIndex index, Sketch s, PImage img, float x, float y, float minR, float maxR) {
            this.index = index;
            this.s = s;
            this.img = img;
            this.x = x;
            this.y = y;
            this.minR = minR;
            this.maxR = maxR;
        }

        @Override
        public void run() {
            for(float r = minR; r <= maxR; r += 1) {
                float error = brightnessError(r);
                index.add(new Candidate(round(x), round(y), round(r), round(error)));
            }
        }

        private float brightnessError(float r) {
            float b = 0f;
            int n = 0;
            // compute average brightness
            for(int imgx = round(x - r); imgx < round(x + r); imgx++) {
                for(int imgy = round(y - r); imgy < round(y + r); imgy++) {
                    if(imgx >= 0 && imgx < img.width && imgy >= 0 && imgy < img.height) {
                        if(dist(new Point2D(x, y), new Point2D(imgx, imgy)) <= r) {
                            int clr = img.get(imgx, imgy);
                            b += s.brightness(clr);
                            n++;
                        }
                    }
                }
            }
            if(n > 0) {
                b /= n;
                // compute error
                float error = 0f;
                for(int imgx = round(x - r); imgx < round(x + r); imgx++) {
                    for(int imgy = round(y - r); imgy < round(y + r); imgy++) {
                        if(imgx >= 0 && imgx < img.width && imgy >= 0 && imgy < img.height) {
                            if(dist(x, y, imgx, imgy) <= r) {
                                error += sqrt(pow((s.brightness(img.get(imgx, imgy)) - b), 2));
                            }
                        }
                    }
                }
                return error;
            }
            return -1;
        }
    }
}
