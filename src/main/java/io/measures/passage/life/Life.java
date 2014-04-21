package io.measures.passage.life;

/**
 * Life
 * @author Dietrich Featherston
 */
public class Life {

    private static final int WORDSIZE = 64;
    private final int rows, cols;

    private final long[][] mem;
    private int generation = 0;

    public Life(int cols, int rows) {
        if(rows%WORDSIZE != 0 || cols%WORDSIZE != 0) {
            throw new IllegalArgumentException("rows and columns must be a multiple of 64");
        }
        this.rows = rows;
        this.cols = cols;
        mem = new long[2][rows*cols/WORDSIZE];
        clearGeneration(0);
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    protected int addressOf(int x, int y) {
        while(x < 0) x = cols + x;
        while(y < 0) y = rows + y;
        return (y%rows)*cols/WORDSIZE + (x%cols)/WORDSIZE;
    }

    protected byte bitPosition(int x) {
        return (byte)(x%WORDSIZE);
    }

    protected boolean state(long word, byte bitPosition) {
        return 1 == ((word & (1L<<bitPosition)) >>> bitPosition);
    }

    public boolean get(int generation, int x, int y) {
        long word = mem[generation%2][addressOf(x, y)];
        return state(word, bitPosition(x));
    }

    public boolean get(int x, int y) {
        return get(generation, x, y);
    }

    public void set(int x, int y, boolean state) {
        set(generation, x, y, state);
    }

    private void set(int generation, int x, int y, boolean state) {
        int address = addressOf(x, y);
        long word = mem[generation%2][address];
        byte bit = bitPosition(x);
        long mask = 1L<<bit;
        if(state)
            word |= mask;
        else
            word &= ~mask;
        mem[generation%2][address] = word;
    }

    private void clearGeneration(int g) {
        for(int i = 0; i < mem[g%2].length; i++) {
            mem[g%2][i] = 0L;
        }
    }

    public void step() {
        int nextGeneration = generation + 1;
        clearGeneration(nextGeneration);
        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < cols; x++) {
                boolean alive = get(generation, x, y);
                int n = countNeighbors(x, y);
                if(!alive && (n == 3 || n == 6)) {
                    // a cell is born if it is dead and has exactly 3 neighbors
                    set(nextGeneration, x, y, true);
                }
                else if(alive && n != 2 && n != 3) {
                    // a cell dies if it has anything other than 2 or 3 neighbors
                    set(nextGeneration, x, y, false);
                }
            }
        }
        generation = nextGeneration;
    }

    public int getGeneration() {
        return generation;
    }

    private int countNeighbors(int x, int y) {
        int count = 0;

        // above
        if(get(x-1, y-1)) count++;
        if(get(x  , y-1)) count++;
        if(get(x+1, y-1)) count++;

        // adjacent
        if(get(x-1, y  )) count++;

        if(get(x+1, y  )) count++;

        // below
        if(get(x-1, y+1)) count++;
        if(get(x  , y+1)) count++;
        if(get(x+1, y+1)) count++;

        return count;
    }
}
