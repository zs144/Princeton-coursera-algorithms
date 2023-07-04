// Deque is a generic data type that generalizes a stack and a queue by allowing
// adding and removing items from both the front and end of the data structure.

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    // An embedded class that simulates nodes, which constructs a deque
    private class Node {
        Item value; // item saved in a node
        Node prev;  // pointing to the previous node; null if it's the head
        Node next;  // pointing to the previous node; null if it's the tail
    }

    // Constructs an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // Checks if the deque is empty
    public boolean isEmpty() {
        return size() == 0;
    }

    // Returns the number of items on the deque
    public int size() {
        return size;
    }

    // Adds the item to the front. Throws IllegalArgumentException if the argument
    // is null.
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.value = item;
        first.prev = null;
        first.next = oldFirst;
        if (oldFirst != null) oldFirst.prev = first;
        else last = first;
        size++;
    }

    // Adds the item to the back. Throws IllegalArgumentException if the argument
    // is null.
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.value = item;
        last.prev = oldLast;
        last.next = null;
        if (oldLast != null) oldLast.next = last;
        else first = last;
        size++;
    }

    // Removes and returns the item from the front. Throws NoSuchElementException
    // if the deque is empty.
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item v = first.value;
        first = first.next;
        if (first == null) {
            last = null;
        }
        else {
            first.prev = null;
        }
        size--;
        return v;
    }

    // Removes and returns the item from the back. Throws NoSuchElementException
    // if the deque is empty.
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item v = last.value;
        last = last.prev;
        if (last == null) {
            first = null;
        }
        else {
            last.next = null;
        }
        size--;
        return v;
    }

    // Returns an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // The iterator class tailored for the deque
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item v = current.value;
            current = current.next;
            return v;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // Unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> test1 = new Deque<>();
        test1.addLast(31);
        test1.addLast(14);
        test1.addFirst(3);
        test1.addFirst(2022);
        int year = test1.removeFirst();
        int hour = test1.removeLast();
        System.out.print(year + " ");
        for (Integer num : test1) {
            System.out.print(num + " ");
        }
        // System.out.print(test1.removeLast() + " ");
        // System.out.print(test1.removeLast() + " ");
        System.out.print(hour); // Expected output: 2022 3 31 14
    }
}
