import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] records;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        records = new double[trials];
        for (int i = 0; i < records.length; i++) {
            Percolation simulation = new Percolation(n);
            while (!simulation.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                while (simulation.isOpen(row, col)) {
                    row = StdRandom.uniformInt(1, n + 1);
                    col = StdRandom.uniformInt(1, n + 1);
                }
                simulation.open(row, col);
            }
            records[i] = (double) simulation.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(records);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(records);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double avg = mean();
        double sd = stddev();
        return avg - CONFIDENCE_95 * sd / Math.sqrt(records.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double avg = mean();
        double sd = stddev();
        return avg + CONFIDENCE_95 * sd / Math.sqrt(records.length);
    }

    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        int times = Integer.parseInt(args[1]);
        PercolationStats test = new PercolationStats(size, times);
        StdOut.println("mean                    = " + test.mean());
        StdOut.println("stddev                  = " + test.stddev());
        StdOut.println("95% confidence interval = [" + test.confidenceLo() + ", "
                               + test.confidenceHi() + "]");
    }
}
