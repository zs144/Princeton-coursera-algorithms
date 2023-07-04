import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = StdIn.readString();
        int index = 1;
        while (!StdIn.isEmpty()) {
            index++;
            String new_champion = StdIn.readString();
            if (StdRandom.bernoulli(1 / (double) index)) {
                champion = new_champion;
            }
        }
        StdOut.println(champion);
    }
}
