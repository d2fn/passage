package io.measures.passage.math;

/**
 * Block
 * Represents a particular block generated from an Alignment layout
 * @author Dietrich Featherston
 */
public class Block {

    private final Alignment alignment;
    private final long start;

    public Block(Alignment a, long start) {
        this.alignment = a;
        this.start = start;
    }

    public Block next() {
        return new Block(alignment, start + alignment.getBlockSize());
    }

    public Block prev() {
        return new Block(alignment, start - alignment.getBlockSize());
    }

    public long getFrom() {
        return start;
    }

    public long getTo() {
        return start + alignment.getBlockSize() - 1;
    }

    public boolean overlaps(Block b) {
        return overlaps(b.getFrom(), b.getTo());
    }

    public boolean overlaps(long from, long to) {
        // this block wholly fits inside
        if(getFrom() >= from && getTo() <= to)
            return true;
            // the input block wholly fits inside this one
        else if(getFrom() <= from && getTo() >= to)
            return true;
            // the input block sits on the left edge
        else if(from <= getFrom() && to >= getFrom())
            return true;
            // the input block site on the right edge
        else if(from <= getTo() && to >= getTo())
            return true;
        return false;
    }

    public String toString() {
        return "(" + getFrom() + ", " + getTo() + ")";
    }
}
