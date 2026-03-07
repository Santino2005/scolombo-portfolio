package anaydis.immutable;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class BankersQueue<T> implements Queue<T>{
    List<T> inside;
    List<T> outside;
    public BankersQueue(@NotNull List<T> inside, List<T> outside){
        this.inside = inside;
        this.outside = outside;
    }
    public static <T> BankersQueue<T> empty() {
        return null;
    }
    @Override
    public @NotNull Queue<T> enqueue(@NotNull T value) {
        return new BankersQueue<>(List.cons(value, inside), outside);
    }

    @Override
    public @NotNull Result<T> dequeue() {
        if(isEmpty()){
            throw new NoSuchElementException("Empty Queue");
        }
        if(outside.isEmpty()){
            List<T> outside2 = inside.reverse();
            return new Result<>(outside2.head(), new BankersQueue<>(List.nil(), outside2.tail()));
        }else{
            return new Result<>(outside.head(), new BankersQueue<>(inside, outside.tail()));
        }
    }

    @Override
    public boolean isEmpty() {
        return inside.isEmpty() && outside.isEmpty();
    }
}
