package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class IntegerDataSetGenerator implements DataSetGenerator<Integer> {

    private List<Integer> lista;
    public IntegerDataSetGenerator() {
        lista = IntegerSetGenerator();
    }

    private List<Integer> IntegerSetGenerator() {
        List<Integer> lista = new ArrayList<>();
        return lista;
    }

    @Override
    public @NotNull List<Integer> createAscending(int length) {
        for(int i = 0; i < length; i++){
            lista.add(i);
        }
        List<Integer> lista2 = new ArrayList<>(lista.subList(0, length));
        lista.clear();
        return lista2;
    }

    @Override
    public @NotNull List<Integer> createDescending(int length) {
        List<Integer> listA = new ArrayList<>();
        for(int i = length; i > 0; i--){
            listA.add(i);
        }
        return listA;
    }

    @Override
    public @NotNull List<Integer> createRandom(int length) {
        List<Integer> NN = new ArrayList<>();
        Random random = new Random();
        for(int i = length; i > 0; i--){
            NN.add(random.nextInt(length));
        }
        return NN;
    }

    @Override
    public @NotNull Comparator<Integer> getComparator() {
        return Comparator.naturalOrder();
    }
}