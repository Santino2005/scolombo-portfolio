package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class SorterProviderImpl implements SorterProvider{

    private final Map<SorterType, Sorter> sorters = new EnumMap<>(SorterType.class);

    public SorterProviderImpl(){
        sorters.put(SorterType.BUBBLE, new AbstractSorter.BubbleSorter());
        sorters.put(SorterType.INSERTION, new AbstractSorter.InsertionSorter());
        sorters.put(SorterType.SELECTION, new AbstractSorter.SelectionSorter());
        sorters.put(SorterType.QUICK, new AbstractSorter.QuickSorter());
        sorters.put(SorterType.H, new AbstractSorter.HSorter());
        sorters.put(SorterType.SHELL, new AbstractSorter.ShellSorter());
        sorters.put(SorterType.QUICK_CUT, new AbstractSorter.QuickCut());
        sorters.put(SorterType.QUICK_NON_RECURSIVE, new AbstractSorter.QuickNonRecursive());
        sorters.put(SorterType.QUICK_MED_OF_THREE, new AbstractSorter.QuickSort_Median_of_three());
        sorters.put(SorterType.QUICK_THREE_PARTITION, new AbstractSorter.QuickSort_Three_Partition());
        sorters.put(SorterType.MERGE_TOP_DOWN, new AbstractSorter.MergeSortTop_Down());
        sorters.put(SorterType.MERGE_BOTTOM_UP, new AbstractSorter.MergeSortBottom_Up());
    }
    @Override
    public @NotNull Iterable<Sorter> getAllSorters() {
        return sorters.values();
    }

    @Override
    public @NotNull Sorter getSorterForType(@NotNull SorterType type) {
        return sorters.get(type);
    }
}
