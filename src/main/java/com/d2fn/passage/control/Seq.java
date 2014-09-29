package com.d2fn.passage.control;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

/**
 * /usr/bin/seq
 * @author Dietrich Featherston
 */
public class Seq {

    private final int lo, hi, inc;
    private final List<Integer> values;
    private final Set<Integer> set;

    public Seq(int lo, int hi, int inc) {

        this.lo = lo;
        this.hi = hi;
        this.inc = inc;

        List<Integer> tmp = Lists.newArrayListWithCapacity((hi-lo)/inc);
        for(int v = lo; v < hi; v += inc) {
            tmp.add(v);
        }

        this.values =
            ImmutableList.copyOf(tmp);

        this.set =
            ImmutableSet.copyOf(tmp);
    }

    public int lo()  { return this.lo;  }
    public int hi()  { return this.hi;  }
    public int inc() { return this.inc; }

    public List<Integer> values() {
        return this.values;
    }

    public boolean contains(int i) {
        return set.contains(i);
    }

    private static class Builder {
        private int lo;
        private int hi;
        private int inc = 1;
        public Builder range(int lo, int hi) {
            this.lo = lo < hi ? lo : hi;
            this.hi = hi > lo ? hi : lo;
            return this;
        }
        public Builder inc(int inc) {
            this.inc = inc;
            return this;
        }
        public Seq build() {
            return new Seq(lo, hi, inc);
        }
    }
}
