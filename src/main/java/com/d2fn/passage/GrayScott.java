package com.d2fn.passage;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import processing.core.PImage;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class GrayScott {

    private static final double DU = 2e-5;
    private static final double DV = 1e-5;

    private final double[][] u;
    private final double[][] v;
    private final double[][] tmpU;
    private final double[][] tmpV;
    private final Function<Integer[], Double> k;
    private final Function<Integer[], Double> f;
    private final double h;
    private final double duDivh2;
    private final double dvDivh2;
    private final int width, height;

    private final ThreadPoolExecutor threadPool;

    public GrayScott(int width, int height, Function<Integer[], Double> f, Function<Integer[], Double> k, double h, ThreadPoolExecutor threadPool) {
        this.width = width;
        this.height = height;
        this.f = f;
        this.k = k;
        this.h = h;
        duDivh2 = DU / (h * h);
        dvDivh2 = DV / (h * h);
        u = new double[width][height];
        v = new double[width][height];
        tmpU = new double[width][height];
        tmpV = new double[width][height];
        this.threadPool = threadPool;
        initialState();
    }

    public void initialState() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tmpU[x][y] = 1;
                tmpV[x][y] = 0;
            }
        }
        for (int x = 0; x < (width / 3); x++) {
            for (int y = 0; y < height / 3; y++) {
                tmpU[width / 3 + x][height / 3 + y] = 0.5;
                tmpV[width / 3 + x][height / 3 + y] = 0.25;
            }
        }

        for (int x = 0; x < (width / 7); x++) {
            for (int y = 0; y < height / 5; y++) {
                tmpU[5 * width / 7 + x][3 * height / 5 + y] = 0.5;
                tmpV[5 * width / 7 + x][3 * height / 5 + y] = 0.25;
            }
        }
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    private double f(int x, int y) {
        return f.apply(new Integer[]{x, y});
    }

    private double k(int x, int y) {
        return k.apply(new Integer[]{x, y});
    }

    public void timeStep(final float dt) {
        List<Future> flist = Lists.newArrayList();
    /*centre*/

        for (int x = 1; x < width - 1; x++) {
            flist.add(threadPool.submit(new Worker(dt, x, 1, height-1)));
//            for (int y = 1; y < height - 1; y++) {
//                double f = f(x, y);
//                double k = k(x, y);
//                double uv2 = tmpU[x][y] * tmpV[x][y] * tmpV[x][y];
//                u[x][y] = tmpU[x][y] + dt * (duDivh2 * (tmpU[x + 1][y] + tmpU[x - 1][y] + tmpU[x][y + 1] + tmpU[x][y - 1] - 4 * tmpU[x][y]) - uv2 + f * (1 - tmpU[x][y]));
//                if (u[x][y] < 0) u[x][y] = 0;
//                v[x][y] = tmpV[x][y] + dt * (dvDivh2 * (tmpV[x + 1][y] + tmpV[x - 1][y] + tmpV[x][y + 1] + tmpV[x][y - 1] - 4 * tmpV[x][y]) + uv2 - k * tmpV[x][y]);
//                if (v[x][y] < 0) v[x][y] = 0;
//            }
        }

        flist.add(threadPool.submit(new RowWorker(dt)));
        flist.add(threadPool.submit(new ColumnWorker(dt)));

    /*edges*/
//        int x, y;
//        for (x = 0; x < width; x++) {
//            y = 0;
//            double f = f(x, y);
//            double k = k(x, y);
//            double uv2 = tmpU[x][y] * tmpV[x][y] * tmpV[x][y];
//            u[x][y] = tmpU[x][y] + dt * (duDivh2 * (tmpU[pBC(x + 1, width)][y] + tmpU[pBC(x - 1, width)][y] + tmpU[x][pBC(y + 1, height)] + tmpU[x][pBC(y - 1, height)] - 4 * tmpU[x][y]) - uv2 + f * (1 - tmpU[x][y]));
//            if (u[x][y] < 0) u[x][y] = 0;
//            v[x][y] = tmpV[x][y] + dt * (dvDivh2 * (tmpV[pBC(x + 1, width)][y] + tmpV[pBC(x - 1, width)][y] + tmpV[x][pBC(y + 1, height)] + tmpV[x][pBC(y - 1, height)] - 4 * tmpV[x][y]) + uv2 - k * tmpV[x][y]);
//            if (v[x][y] < 0) v[x][y] = 0;
//            y = height - 1;
//            uv2 = tmpU[x][y] * tmpV[x][y] * tmpV[x][y];
//            u[x][y] = tmpU[x][y] + dt * (duDivh2 * (tmpU[pBC(x + 1, width)][y] + tmpU[pBC(x - 1, width)][y] + tmpU[x][pBC(y + 1, height)] + tmpU[x][pBC(y - 1, height)] - 4 * tmpU[x][y]) - uv2 + f * (1 - tmpU[x][y]));
//            if (u[x][y] < 0) u[x][y] = 0;
//            v[x][y] = tmpV[x][y] + dt * (dvDivh2 * (tmpV[pBC(x + 1, width)][y] + tmpV[pBC(x - 1, width)][y] + tmpV[x][pBC(y + 1, height)] + tmpV[x][pBC(y - 1, height)] - 4 * tmpV[x][y]) + uv2 - k * tmpV[x][y]);
//            if (v[x][y] < 0) v[x][y] = 0;
//        }
//        for (y = 0; y < height; y++) {
//            x = 0;
//            double f = f(x, y);
//            double k = k(x, y);
//            double uv2 = tmpU[x][y] * tmpV[x][y] * tmpV[x][y];
//            u[x][y] = tmpU[x][y] + dt * (duDivh2 * (tmpU[pBC(x + 1, width)][y] + tmpU[pBC(x - 1, width)][y] + tmpU[x][pBC(y + 1, height)] + tmpU[x][pBC(y - 1, height)] - 4 * tmpU[x][y]) - uv2 + f * (1 - tmpU[x][y]));
//            if (u[x][y] < 0) u[x][y] = 0;
//            v[x][y] = tmpV[x][y] + dt * (dvDivh2 * (tmpV[pBC(x + 1, width)][y] + tmpV[pBC(x - 1, width)][y] + tmpV[x][pBC(y + 1, height)] + tmpV[x][pBC(y - 1, height)] - 4 * tmpV[x][y]) + uv2 - k * tmpV[x][y]);
//            if (v[x][y] < 0) v[x][y] = 0;
//            x = width - 1;
//            uv2 = tmpU[x][y] * tmpV[x][y] * tmpV[x][y];
//            u[x][y] = tmpU[x][y] + dt * (duDivh2 * (tmpU[pBC(x + 1, width)][y] + tmpU[pBC(x - 1, width)][y] + tmpU[x][pBC(y + 1, height)] + tmpU[x][pBC(y - 1, height)] - 4 * tmpU[x][y]) - uv2 + f * (1 - tmpU[x][y]));
//            if (u[x][y] < 0) u[x][y] = 0;
//            v[x][y] = tmpV[x][y] + dt * (dvDivh2 * (tmpV[pBC(x + 1, width)][y] + tmpV[pBC(x - 1, width)][y] + tmpV[x][pBC(y + 1, height)] + tmpV[x][pBC(y - 1, height)] - 4 * tmpV[x][y]) + uv2 - k * tmpV[x][y]);
//            if (v[x][y] < 0) v[x][y] = 0;
//        }

        // wait for everything to finish
        for(Future f : flist) {
            try {
                f.get();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        // copy everything over
        int x, y;
        for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {
                tmpU[x][y] = u[x][y];
                tmpV[x][y] = v[x][y];
            }
        }
    }

    private int pBC(int x, int max) {                        /*periodic boundary conditions*/
        int xp = x;
        while (xp < 0) xp += max;
        while (xp >= max) xp -= max;
        return xp;
    }

    public void loadImage(Sketch s, PImage img) {
        for (int x = 0; x<width; x++) {
            for (int y = 0; y<height; y++) {
                tmpU[x][y] = 1;
                tmpV[x][y] = 0;
            }
        }
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int ix = x;
                int iy = y;
                if(ix >= img.width) ix = img.width-1;
                if(ix < 0) ix = 0;
                if(iy >= img.height) iy = img.height-1;
                if(iy < 0) iy = 0;
                float lum = s.brightness(img.get(ix, iy))/ 255f;
                tmpU[x][y] = (1-lum)/2f;
                tmpV[x][y] = lum;
            }
        }
    }

    public double u(int x, int y) {
        return u[x][y];
    }

    public double v(int x, int y) {
        return v[x][y];
    }

    private class Worker implements Runnable {

        private final float dt;
        private final int x, y0, yf;

        private Worker(float dt, int x, int y0, int yf) {
            this.dt = dt;
            this.x = x;
            this.y0 = y0;
            this.yf = yf;
        }

        @Override
        public void run() {
            for (int y = y0; y < yf; y++) {
                double f = f(x, y);
                double k = k(x, y);
                double uv2 = tmpU[x][y] * tmpV[x][y] * tmpV[x][y];
                u[x][y] = tmpU[x][y] + dt * (duDivh2 * (tmpU[x + 1][y] + tmpU[x - 1][y] + tmpU[x][y + 1] + tmpU[x][y - 1] - 4 * tmpU[x][y]) - uv2 + f * (1 - tmpU[x][y]));
                if (u[x][y] < 0) u[x][y] = 0;
                v[x][y] = tmpV[x][y] + dt * (dvDivh2 * (tmpV[x + 1][y] + tmpV[x - 1][y] + tmpV[x][y + 1] + tmpV[x][y - 1] - 4 * tmpV[x][y]) + uv2 - k * tmpV[x][y]);
                if (v[x][y] < 0) v[x][y] = 0;
            }
        }
    }

    private class RowWorker implements Runnable {

        private final float dt;

        private RowWorker(float dt) {
            this.dt = dt;
        }

        @Override
        public void run() {
            int x, y;
            for (x = 0; x < width; x++) {
                y = 0;
                double f = f(x, y);
                double k = k(x, y);
                double uv2 = tmpU[x][y] * tmpV[x][y] * tmpV[x][y];
                u[x][y] = tmpU[x][y] + dt * (duDivh2 * (tmpU[pBC(x + 1, width)][y] + tmpU[pBC(x - 1, width)][y] + tmpU[x][pBC(y + 1, height)] + tmpU[x][pBC(y - 1, height)] - 4 * tmpU[x][y]) - uv2 + f * (1 - tmpU[x][y]));
                if (u[x][y] < 0) u[x][y] = 0;
                v[x][y] = tmpV[x][y] + dt * (dvDivh2 * (tmpV[pBC(x + 1, width)][y] + tmpV[pBC(x - 1, width)][y] + tmpV[x][pBC(y + 1, height)] + tmpV[x][pBC(y - 1, height)] - 4 * tmpV[x][y]) + uv2 - k * tmpV[x][y]);
                if (v[x][y] < 0) v[x][y] = 0;
                y = height - 1;
                uv2 = tmpU[x][y] * tmpV[x][y] * tmpV[x][y];
                u[x][y] = tmpU[x][y] + dt * (duDivh2 * (tmpU[pBC(x + 1, width)][y] + tmpU[pBC(x - 1, width)][y] + tmpU[x][pBC(y + 1, height)] + tmpU[x][pBC(y - 1, height)] - 4 * tmpU[x][y]) - uv2 + f * (1 - tmpU[x][y]));
                if (u[x][y] < 0) u[x][y] = 0;
                v[x][y] = tmpV[x][y] + dt * (dvDivh2 * (tmpV[pBC(x + 1, width)][y] + tmpV[pBC(x - 1, width)][y] + tmpV[x][pBC(y + 1, height)] + tmpV[x][pBC(y - 1, height)] - 4 * tmpV[x][y]) + uv2 - k * tmpV[x][y]);
                if (v[x][y] < 0) v[x][y] = 0;
            }
        }
    }

    private class ColumnWorker implements Runnable {

        private final float dt;

        private ColumnWorker(float dt) {
            this.dt = dt;
        }

        @Override
        public void run() {
            int x, y;
            for (y = 0; y < height; y++) {
                x = 0;
                double f = f(x, y);
                double k = k(x, y);
                double uv2 = tmpU[x][y] * tmpV[x][y] * tmpV[x][y];
                u[x][y] = tmpU[x][y] + dt * (duDivh2 * (tmpU[pBC(x + 1, width)][y] + tmpU[pBC(x - 1, width)][y] + tmpU[x][pBC(y + 1, height)] + tmpU[x][pBC(y - 1, height)] - 4 * tmpU[x][y]) - uv2 + f * (1 - tmpU[x][y]));
                if (u[x][y] < 0) u[x][y] = 0;
                v[x][y] = tmpV[x][y] + dt * (dvDivh2 * (tmpV[pBC(x + 1, width)][y] + tmpV[pBC(x - 1, width)][y] + tmpV[x][pBC(y + 1, height)] + tmpV[x][pBC(y - 1, height)] - 4 * tmpV[x][y]) + uv2 - k * tmpV[x][y]);
                if (v[x][y] < 0) v[x][y] = 0;
                x = width - 1;
                uv2 = tmpU[x][y] * tmpV[x][y] * tmpV[x][y];
                u[x][y] = tmpU[x][y] + dt * (duDivh2 * (tmpU[pBC(x + 1, width)][y] + tmpU[pBC(x - 1, width)][y] + tmpU[x][pBC(y + 1, height)] + tmpU[x][pBC(y - 1, height)] - 4 * tmpU[x][y]) - uv2 + f * (1 - tmpU[x][y]));
                if (u[x][y] < 0) u[x][y] = 0;
                v[x][y] = tmpV[x][y] + dt * (dvDivh2 * (tmpV[pBC(x + 1, width)][y] + tmpV[pBC(x - 1, width)][y] + tmpV[x][pBC(y + 1, height)] + tmpV[x][pBC(y - 1, height)] - 4 * tmpV[x][y]) + uv2 - k * tmpV[x][y]);
                if (v[x][y] < 0) v[x][y] = 0;
            }
        }
    }
}
