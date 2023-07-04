import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;
    private boolean[] state;
    private int[] bottom;
    private final int length;
    private int openCount;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        openCount = 0;
        length = n;
        state = new boolean[n * n + 2]; // block = false, open = true
        state[state.length - 2] = true;
        state[state.length - 1] = true;

        uf = new WeightedQuickUnionUF(length * length + 2);
        for (int col = 1; col <= length; col++) {
            uf.union(length * length, indexOfSite(1, col));
        }
        bottom = new int[n * n + 2];
        for (int col = 1; col <= length; col++) {
            bottom[indexOfSite(length, col)] = col;
        }
    }

    private boolean isInBound(int row, int col) {
        return (row <= length && col <= length && row > 0 && col > 0);
    }

    private int indexOfSite(int row, int col) {
        if (!isInBound(row, col)) {
            throw new IllegalArgumentException();
        }
        return (row - 1) * length + col - 1;
    }

    private void updateBottom(int p, int q) {
        int pRoot = uf.find(p);
        int qRoot = uf.find(q);
        if (pRoot == qRoot) return;
        if (bottom[pRoot] != 0) {
            bottom[qRoot] = bottom[pRoot];
        }
        else {
            bottom[pRoot] = bottom[qRoot];
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
            if (length == 1 && isFull(row, col)) {
                updateBottom(indexOfSite(row, col), length * length + 1);
                uf.union(length * length + 1, indexOfSite(row, col));
            }
            if (isInBound(row + 1, col) && isOpen(row + 1, col)) {
                updateBottom(indexOfSite(row, col), indexOfSite(row + 1, col));
                uf.union(indexOfSite(row, col), indexOfSite(row + 1, col));
            }
            if (isInBound(row - 1, col) && isOpen(row - 1, col)) {
                updateBottom(indexOfSite(row, col), indexOfSite(row - 1, col));
                uf.union(indexOfSite(row, col), indexOfSite(row - 1, col));
            }
            if (isInBound(row, col + 1) && isOpen(row, col + 1)) {
                updateBottom(indexOfSite(row, col), indexOfSite(row, col + 1));
                uf.union(indexOfSite(row, col), indexOfSite(row, col + 1));
            }
            if (isInBound(row, col - 1) && isOpen(row, col - 1)) {
                updateBottom(indexOfSite(row, col), indexOfSite(row, col - 1));
                uf.union(indexOfSite(row, col), indexOfSite(row, col - 1));
            }
            int bottomCol = bottom[uf.find(indexOfSite(row, col))];
            if (bottomCol != 0 && isFull(length, bottomCol)) {
                updateBottom(indexOfSite(length, bottomCol), length * length + 1);
                uf.union(length * length + 1, indexOfSite(length, bottomCol));
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
        return isConnected(indexOfSite(row, col), length * length);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return isConnected(length * length, length * length + 1);
    }
}
