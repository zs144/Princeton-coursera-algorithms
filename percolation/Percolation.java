import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final int rows; // the length of rows
    private final int cols; // the number of columns
    private final int headIndex;
    private final int tailIndex;
    private int openCount; // the number of open sites in the grid
    private boolean[] state; // state of each site in the grid
    private int[] connectedBottomSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        rows = n;
        cols = n;
        openCount = 0;
        state = new boolean[n * n + 2]; // state: blocked = false, open = true
        headIndex = rows * cols;
        tailIndex = rows * cols + 1;
        state[headIndex] = true; // state of the head is always ture
        state[tailIndex] = true; // state of the tail is always true

        uf = new WeightedQuickUnionUF(rows * cols + 2);
        for (int col = 1; col <= cols; col++) { // NOTE: the grid is 1-indexed.
            uf.union(headIndex, indexOfSite(1, col)); // connect top row with head
        }
        connectedBottomSite = new int[n * n + 2];
        for (int col = 1; col <= cols; col++) {
            connectedBottomSite[indexOfSite(rows, col)] = col;
        }
    }

    private boolean isInBound(int row, int col) {
        return (row <= rows && col <= cols && row > 0 && col > 0);
    }

    private int indexOfSite(int row, int col) {
        if (!isInBound(row, col)) {
            throw new IllegalArgumentException();
        }
        return (row - 1) * cols + col - 1;
    }

    private void unionBottomSite(int p, int q) {
        int pRoot = uf.find(p);
        int qRoot = uf.find(q);
        if (pRoot == qRoot) return;
        if (connectedBottomSite[pRoot] != 0) {
            connectedBottomSite[qRoot] = connectedBottomSite[pRoot];
        } else {
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
            if (rows == 1 && isFull(row, col)) {
                unionBottomSite(indexOfSite(row, col), tailIndex);
                uf.union(tailIndex, indexOfSite(row, col));
                return;
            }
            int[][] neighbors = new int[][]{{row + 1, col}, {row - 1, col},
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
            if (bottomCol != 0 && isFull(rows, bottomCol)) {
                unionBottomSite(indexOfSite(rows, bottomCol), tailIndex);
                uf.union(indexOfSite(rows, bottomCol), tailIndex);
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
        return isConnected(indexOfSite(row, col), headIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return isConnected(headIndex, tailIndex);
    }
}
