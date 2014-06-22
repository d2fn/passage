package com.d2fn.passage.geometry;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.d2fn.passage.math.Alignment;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.round;

/**
 * PointIndex
 * @author Dietrich Featherston
 */
public class PointIndex<T extends Projectable2D> {

    private final Alignment xalign;
    private final Alignment yalign;

    private final LoadingCache<Key, List<T>> index;

    public PointIndex(int dx, int dy) {
        xalign = new Alignment(dx);
        yalign = new Alignment(dy);
        index = CacheBuilder.newBuilder().build(new CacheLoader<Key, List<T>>() {
            @Override
            public List<T> load(Key key) throws Exception {
                return Lists.newArrayList();
            }
        });
    }

    public void add(T p) {
        try {
        index.get(fromPoint(p)).add(p);
        } catch (ExecutionException ignored) { }
    }

    public void visitProximity(T center, Function<T, Void> f) {
        for(Key k : inProximity(center)) {
            try {
                for(T p : index.get(k)) {
                    f.apply(p);
                }
            } catch(Exception ignored) { }
        }
    }

    public Key fromPoint(T p) {
        long x = xalign.at(round(p.x())).getFrom();
        long y = yalign.at(round(p.y())).getFrom();
        return new Key(x, y);
    }

    public List<Key> inProximity(final T center) {
        Key k = fromPoint(center);
        List<Key> out = Lists.newArrayListWithCapacity(9);
        out.add(k);

        k = k.up();    out.add(k);
        k = k.right(); out.add(k);
        k = k.down();  out.add(k);
        k = k.down();  out.add(k);
        k = k.left();  out.add(k);
        k = k.left();  out.add(k);
        k = k.up();    out.add(k);
        k = k.up();    out.add(k);

        return out;
    }


    private class Key {

        private final long x, y;

        private Key(long x, long y) {
            this.x = x;
            this.y = y;
        }

        private Key up() {
            return new Key(
                    x,
                    y - yalign.getBlockSize());
        }

        private Key down() {
            return new Key(
                    x,
                    y + yalign.getBlockSize());
        }

        private Key left() {
            return new Key(
                    x - xalign.getBlockSize(),
                    y);
        }

        private Key right() {
            return new Key(
                    x + xalign.getBlockSize(),
                    y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (x != key.x) return false;
            if (y != key.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) (x ^ (x >>> 32));
            result = 31 * result + (int) (y ^ (y >>> 32));
            return result;
        }
    }
}
