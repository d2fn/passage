package io.measures.passage.math;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Alignment
 * Model a particular layout of numbers in fixed width blocks (of 10, 100, etc)
 * @author Dietrich Featherston
 */
public class Alignment {

    private long blocksize;

    public Alignment(long blocksize) {
        this.blocksize = blocksize;
    }

    public final long getBlockSize() {
        return blocksize;
    }

    public final Block at(long value) {
        return new Block(this, value / blocksize * blocksize);
    }

    public final List<Block> getBlocks(long from, long to) {
        if(from > to) {
            long t = from;
            from = to;
            to = t;
        }
        List<Block> blocks = Lists.newArrayListWithExpectedSize((int)((to - from)/blocksize + 2));
        Block b = this.at(from);
        blocks.add(b);
        while(b.getTo() < to) {
            b = b.next();
            blocks.add(b);
        }
        return blocks;
    }
}
