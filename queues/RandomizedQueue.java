// RandomizedQueue is a special generic queue that can select or remove elements
// in uniformly random order.

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int size;

    // Constructs an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        a = (Item[]) new Object[1];
    }

    // Resizes the array with all elements copied and placed in the same order
    private void resize(int max) {
        Item[] newArray = (Item[]) new Object[max];
        for (int i = 0; i < size; i++) {
            newArray[i] = a[i];
        }
        a = newArray;
    }

    // Checks if the randomized queue is empty
    public boolean isEmpty() {
        return size() == 0;
    }

    // Returns the number of items on the randomized queue
    public int size() {
        return size;
    }

    // Add an item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == a.length) resize(a.length * 2);
        a[size++] = item;
    }

    // Removes and returns a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int some = StdRandom.uniform(size);
        Item v = a[some];
        a[some] = a[--size];
        a[size] = null; // avoid object loitering
        if (size > 0 && size == a.length / 4) resize(a.length / 2);
        return v;
    }

    // Returns a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return a[StdRandom.uniform(size)];
    }

    // Returns an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current;
        private Item[] temp;

        public RandomizedQueueIterator() {
            current = 0;
            temp = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) temp[i] = a[i];
            StdRandom.shuffle(temp, 0, size);
        }

        public boolean hasNext() {
            return current < size;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return temp[current++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // Unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomQ = new RandomizedQueue<>();
        randomQ.enqueue(1);
        randomQ.enqueue(2);
        randomQ.enqueue(3);
        randomQ.enqueue(4);
        randomQ.enqueue(5);
        System.out.println(randomQ.dequeue());
        System.out.println(randomQ.sample());
        System.out.println(randomQ.sample());
        System.out.println(randomQ.sample());
        for (Integer num : randomQ) {
            System.out.print(num + " ");
        }
        System.out.println();
        for (Integer num : randomQ) {
            System.out.print(num + " ");
        }
        System.out.println();
        System.out.println(randomQ.dequeue());
        System.out.println(randomQ.dequeue());
        System.out.println(randomQ.dequeue());
        System.out.println(randomQ.dequeue());
        
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }
}
