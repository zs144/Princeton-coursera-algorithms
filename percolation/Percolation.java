import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final int ROWS; // the length of rows
    private final int COLS; // the number of columns
    private final int HEAD_INDEX;
    private final int TAIL_INDEX;
    private int openCount; // the number of open sites in the grid
    private boolean[] state; // state of each site in the grid
    private int[] connectedBottomSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        ROWS = n;
        COLS = n;
        openCount = 0;
        state = new boolean[n * n + 2]; // state: blocked = false, open = true
        HEAD_INDEX = ROWS * COLS;
        TAIL_INDEX = ROWS * COLS + 1;
        state[HEAD_INDEX] = true; // state of the head is always ture
        state[TAIL_INDEX] = true; // state of the tail is always true

        uf = new WeightedQuickUnionUF(ROWS * COLS + 2);
        for (int col = 1; col <= COLS; col++) { // NOTE: the grid is 1-indexed.
            uf.union(HEAD_INDEX, indexOfSite(1, col)); // connect top row with head
        }
        connectedBottomSite = new int[n * n + 2];
        for (int col = 1; col <= COLS; col++) {
            connectedBottomSite[indexOfSite(ROWS, col)] = col;
        }
    }

    private boolean isInBound(int row, int col) {
        return (row <= ROWS && col <= COLS && row > 0 && col > 0);
    }

    private int indexOfSite(int row, int col) {
        if (!isInBound(row, col)) {
            throw new IllegalArgumentException();
        }
        return (row - 1) * COLS + col - 1;
    }

    private void unionBottomSite(int p, int q) {
        int pRoot = uf.find(p);
        int qRoot = uf.find(q);
        if (pRoot == qRoot) return;
        if (connectedBottomSite[pRoot] != 0) {
            connectedBottomSite[qRoot] = connectedBottomSite[pRoot];
        }
        else {
            connectedBottomSite[pRoot] = connectedBottomSite[qRoot];
        }
    }

    private boolean isConnected(int p, int q) {
        return state[p] && state[q] && uf.find(p) == uf.find(q);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isInBound(row, col)) {
            throw new IllegalArgumentException();
        }
        if (!isOpen(row, col)) {
            state[indexOfSite(row, col)] = true;
            openCount++;
            if (ROWS == 1 && isFull(row, col)) {
                unionBottomSite(indexOfSite(row, col), TAIL_INDEX);
                uf.union(TAIL_INDEX, indexOfSite(row, col));
                return;
            }
            int[][] neighbors = new int[4][2];
            neighbors = new int[][]{{row + 1, col}, {row - 1, col},
                                    {row, col + 1}, {row, col - 1}};
            for (int[] neighbor : neighbors) {
                int neighborRow = neighbor[0];
                int neighborCol = neighbor[1];
                if (isInBound(neighborRow, neighborCol) && isOpen(neighborRow, neighborCol)) {
                    unionBottomSite(indexOfSite(row, col), indexOfSite(neighborRow, neighborCol));
                    uf.union(indexOfSite(row, col), indexOfSite(neighborRow, neighborCol));
                }
            }
            int bottomCol = connectedBottomSite[uf.find(indexOfSite(row, col))];
            if (bottomCol != 0 && isFull(ROWS, bottomCol)) {
                unionBottomSite(indexOfSite(ROWS, bottomCol), TAIL_INDEX);
                uf.union(indexOfSite(ROWS, bottomCol), TAIL_INDEX);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isInBound(row, col)) {
            throw new IllegalArgumentException();
        }
        return state[indexOfSite(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isConnected(indexOfSite(row, col), HEAD_INDEX);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return isConnected(HEAD_INDEX, TAIL_INDEX);
    }
}
