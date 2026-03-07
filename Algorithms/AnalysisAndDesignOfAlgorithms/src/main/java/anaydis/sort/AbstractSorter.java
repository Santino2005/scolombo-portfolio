package anaydis.sort;
import org.jetbrains.annotations.NotNull;
import java.util.*;
/**
 * Abstract sorter: all sorter implementations should subclass this class.
 */
abstract class AbstractSorter<T> implements Sorter {

    private int swaps;
    private int Comparaciones;
    boolean less(T v, T w, Comparator<T> comp) {
        Comparaciones++;
        return comp.compare(v, w) < 0;
    }
    void exch(List<T> datos, int i, int j) {
        T t = datos.get(i);
        datos.set(i, datos.get(j));
        datos.set(j, t);
        swaps++;
    }
    void compExch(List<T> datos, int i, int j, Comparator<T> comp) {
        if (less(datos.get(j), datos.get(i), comp)) {
            exch(datos, i, j);
        }
    }

    public static class BubbleSorter extends AbstractSorter {
        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> lista) {
            int N = lista.size();
            for (int i = 0; i < N - 1; i++) {
                for(int j = N-1; j > i; j--){
                    compExch(lista, j-1 , j, comparator);
                }
            }
        }
        @Override
        public @NotNull SorterType getType() {
            return SorterType.BUBBLE;
        }
    }
    public static class SelectionSorter extends AbstractSorter  {
        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> List) {
            int length = List.size();
            for (int i = 0; i < length; i++) {
                int minium = i;
                for (int j = 1 + i; j < length; j++) {
                    if (less(List.get(j), List.get(minium), comparator)) {
                        minium = j;
                    }
                }
                exch(List, i, minium);
            }
        }
        @Override
        public @NotNull SorterType getType() {
            return SorterType.SELECTION;
        }
    }
    public static class InsertionSorter extends AbstractSorter {
        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> Listaa){
            int N = Listaa.size();
            for (int i = 0; i < N; i++) {
                for (int j = i; j > 0; j--) {
                    if (less(Listaa.get(j), Listaa.get(j - 1), comparator)) {
                        exch(Listaa, j, j - 1);
                    }else{
                        break;
                        }
                    }
                }
            }

        @Override
        public @NotNull SorterType getType() {
            return SorterType.INSERTION;
        }
    }
    public static class QuickSorter extends AbstractSorter{

        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
            sort(comparator, list, 0, list.size()-1);
        }

        @Override
        public @NotNull SorterType getType() {
            return SorterType.QUICK;
        }
        public void sort(@NotNull Comparator comp, List list, int l, int h){
            if(h <= l){
                return;
            }
            int pivot = partition(list, comp, l, h);
            sort(comp, list, l, pivot -1);
            sort(comp, list, pivot + 1, h);
        }
        private int partition(@NotNull List list ,Comparator cmp, int l, int h){
            int k = l - 1;
            int p = h;
            while(true){
                while(less(list.get(++k), list.get(h), cmp)){
                    if(k == h){
                        break;
                    }
                }while(less(list.get(h), list.get(--p), cmp)){
                    if(p == l){
                        break;
                    }
                }
                if(k >= p){
                    break;
                }
                exch(list, k, p);
            }
            exch(list, k, h);
            return k;
        }

    }
    public static class HSorter extends AbstractSorter{
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
            sort(comparator, list, 1);
        }
        public <T> void sort(Comparator<T> comparator, List<T> list, int i) {
            int r = list.size();
            for (int j = i; j < r; j++) {
                T temp = list.get(j);
                int h = j;
                while (h >= i && less(temp, list.get(h - i), comparator)) {
                    list.set(h, list.get(h - i));
                    h -= i;
                }
                list.set(h, temp);
            }
        }
        @Override
        public @NotNull SorterType getType() {
            return SorterType.H;
        }
    }
    public static class ShellSorter extends AbstractSorter{

        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
            Shell(comparator, list, 0, list.size() -1);
        }
        public <T> void Shell(@NotNull Comparator v, List<T> list, int l, int r){
            int k;
            for(k = 1; k <= (r-l)/9; k = 3*k+1);
            for(; k > 0; k/= 3){
                for(int i = l+k; i <= r; i++){
                    int j = i;
                    T temp = list.get(i);
                    while(j >= l+k && less(temp, list.get(j-k), v)){
                        list.set(j, list.get(j-k));
                        j -= k;
                    }
                    list.set(j, temp);
                }
            }
        }        @Override
        public @NotNull SorterType getType() {
            return SorterType.SHELL;
        }
    }
    public static class QuickNonRecursive extends AbstractSorter{

        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
            QuickSortNonRecursive(comparator, list,0, list.size() - 1);
        }
        public void QuickSortNonRecursive(@NotNull Comparator Cmp, List list, int l, int r){
            Stack<Integer> queue = new Stack<>();
            queue.push(l);
            queue.push(r);
            while(!queue.isEmpty()){
                r = queue.pop();
                l = queue.pop();
                if(r <= l){
                    continue;
                }
                int k = partition(list, Cmp, l, r);
                if(k - l > r - k){
                    queue.push(l);
                    queue.push(k-1);
                }
                queue.push(k +1);
                queue.push(r);
                if(r - k >= k-l ){
                    queue.push(l);
                    queue.push(k-1);
                }
            }
        }
        private int partition(@NotNull List list ,Comparator cmp, int l, int h){
            int k = l - 1;
            int p = h;
            while(true){
                while(less(list.get(++k), list.get(h), cmp)){
                    if(k == h){
                        break;
                    }
                }while(less(list.get(h), list.get(--p), cmp)){
                    if(p == l){
                        break;
                    }
                }
                if(k >= p){
                    break;
                }
                exch(list, k, p);
            }
            exch(list, k, h);
            return k;
        }
        @Override
        public @NotNull SorterType getType() {
            return SorterType.QUICK_NON_RECURSIVE;
        }
    }
    public static class QuickCut extends AbstractSorter{

        private final static int M = 10;
        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
            hybridsort(comparator, list, 0, list.size() - 1);
        }
        void quicksort(Comparator cmp, List list, int l, int r){
            if(r <= l + M){return;}
            int k = partition(list, cmp, l, r);
            quicksort(cmp, list, l, k - 1);
            quicksort(cmp, list, k + 1, r);
        }
        void hybridsort(Comparator cmp, List list, int l, int r){
            AbstractSorter sorter = new AbstractSorter.InsertionSorter();
            quicksort(cmp, list, l, r);
            sorter.sort(cmp, list);
        }
        @Override
        public @NotNull SorterType getType() {
            return SorterType.QUICK_CUT;
        }
        private int partition(@NotNull List list ,Comparator cmp, int l, int h){
            int k = l - 1;
            int p = h;
            while(true){
                while(less(list.get(++k), list.get(h), cmp)){
                    if(k == h){
                        break;
                    }
                }while(less(list.get(h), list.get(--p), cmp)){
                    if(p == l){
                        break;
                    }
                }
                if(k >= p){
                    break;
                }
                exch(list, k, p);
            }
            exch(list, k, h);
            return k;
        }

    }
    public static class QuickSort_Median_of_three extends AbstractSorter{

        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
            hybridsort(comparator, list, 0, list.size()-1);
        }
        private static int M = 10;
        void quicksort(Comparator cmp, List list, int l, int r){
            if(r <= M + l) {return;}
            exch(list, (l+r)/2, r - 1 );
            compExch(list, l, r-1, cmp);
            compExch(list, l, r, cmp);
            compExch(list, r-1, r, cmp);
            int i = partition(list, cmp, l+1, r-1);
            quicksort(cmp, list, l, i-1);
            quicksort(cmp, list, i+1, r);
        }
        void hybridsort(Comparator cmp, List list, int l, int r){
            AbstractSorter sort = new AbstractSorter.InsertionSorter();
            quicksort(cmp, list, l, r);
            sort.sort(cmp, list);
        }
        private int partition(@NotNull List list ,Comparator cmp, int l, int h){
            int k = l - 1;
            int p = h;
            while(true){
                while(less(list.get(++k), list.get(h), cmp)){
                    if(k == h){
                        break;
                    }
                }while(less(list.get(h), list.get(--p), cmp)){
                    if(p == l){
                        break;
                    }
                }
                if(k >= p){
                    break;
                }
                exch(list, k, p);
            }
            exch(list, k, h);
            return k;
        }

        @Override
        public @NotNull SorterType getType() {
            return SorterType.QUICK_MED_OF_THREE;
        }
    }
    public static class QuickSort_Three_Partition extends AbstractSorter{

        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
            QuickSort(comparator, list, 0, list.size() - 1);}
        void QuickSort(Comparator cmp, List list, int l, int r){

            if(r <= l){return;}
            int i = l;
            int j = r ;
            int k = l + 1;

            Object item = list.get(l);
            while(k <= j){
                if(less(list.get(k), item, cmp)){
                    exch(list, i++, k++);}
                else if(less(item, list.get(k), cmp)){
                    exch(list, k, j--);
                }else{
                    k++;}
            }
            QuickSort(cmp, list, l, i-1);
            QuickSort(cmp, list, j+1, r);
        }
        @Override
        public @NotNull SorterType getType() {
            return SorterType.QUICK_THREE_PARTITION;
        }
    }
    public static class MergeSortTop_Down extends AbstractSorter{

        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
            MergeSort(comparator, list, 0, list.size()-1);
        }
        void MergeSort(Comparator cmp, List list, int l, int r){
            if(r <= l){
                return;
            }
            int medio = l + (r - l)/2;
            MergeSort(cmp, list, l, medio);
            MergeSort(cmp, list, medio + 1, r);
            merge(cmp, list, l, medio, r);
        }

        @Override
        public @NotNull SorterType getType() {
            return SorterType.MERGE_TOP_DOWN;
        }
    }
    public static class MergeSortBottom_Up extends AbstractSorter{

        @Override
        public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
            MergeSortBottom(comparator, list, 0, list.size() - 1);
        }

        public void MergeSortBottom(Comparator cmp, List list, int l, int r){
            for(int mid = 1; mid <= r-l; mid *= 2){
                int NMid = mid *2;
                for(int i = l; i <= r - mid; i += NMid){
                    int h = Math.min(i - l + NMid - 1, r);
                    merge(cmp, list, i, i + mid - 1, h);
                }
            }
        }
        @Override
        public @NotNull SorterType getType() {
            return SorterType.MERGE_BOTTOM_UP;
        }
    }
    public void merge(Comparator cmp, @NotNull List<T> list, int l, int M, int r) {

        List<T> aux = new ArrayList<>();

        int i = l;
        int j = M + 1;

        for(int k = 0; k < list.size();k++){
            aux.add(list.get(k));
        }
        for(int k = l; k <= r;k++){
           if(i > M){
               list.set(k, aux.get(j++));
           }else if( j > r){
               list.set(k, aux.get(i++));
           }else if(less(aux.get(j), aux.get(i), cmp)){
               list.set(k, aux.get(j++));
           }else{
               list.set(k, aux.get(i++));
           }
        }
    }
    public int getSwaps(){
        return swaps;
    }
    public int getComparaciones(){
        return Comparaciones;
    }
}
