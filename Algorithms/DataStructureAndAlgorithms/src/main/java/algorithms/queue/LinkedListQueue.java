package algorithms.queue;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListQueue<E> implements algorithms.queue.Queue<E>{

    private int changes;
    private int size;

    private class Node<E> {
        E item;
        Node<E> next;
    }
    Node<E> start;
    Node<E> end;
    public LinkedListQueue(){
        this.size = 0;
    }
    @Override
    public void enqueue(@NotNull E item){
        if(start == null){
            end = new Node();
            end.item = item;
            start = end;
        }else {
            Node<E> NewNode = new Node();
            NewNode.item = item;
            end.next = NewNode;
            end = NewNode;
        }
        size++;
        changes++;
    }
    @Override
    public E dequeue(){
        if(size < 1){
            throw new NoSuchElementException("Not possible");
        }
        E item = start.item;
        start = start.next;
        size--;
        changes++;
        return item;
    }
    @Override
    public boolean isEmpty(){
        return size == 0;
    }
    @Override
    public int size(){
        return size;
    }

    @Override
    public Iterator<E> iterator(){
        return new LinkedListQueueIterator();
    }
    public class LinkedListQueueIterator implements Iterator<E>{
        private Node<E> initial = start;
        int Changes = changes;

        @Override
        public boolean hasNext(){
            return initial != null;
        }
        @Override
        public E next(){
            if(Changes == changes){
                if (!hasNext()){
                    throw new NoSuchElementException("No Next");
                }else{
                    E item = initial.item;
                    initial = initial.next;
                    return item;
                }
            }else{
                throw new IllegalStateException("Queue being modified");
            }
        }
    }
}
