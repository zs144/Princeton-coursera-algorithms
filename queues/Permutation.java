// Reads a sequence of several string from a input file stream, and prints part
// of them randomly. Each string can be printed at most once.

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        int num = Integer.parseInt(args[0]);
        for (int i = 0; i < num; i++) {
            System.out.println(queue.dequeue());
        }
    }
}
