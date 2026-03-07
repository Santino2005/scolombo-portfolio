package anaydis.sort;


import org.junit.Test;

import java.util.Comparator;
import java.util.List;

public class TestPractice02 extends SorterTest {

    //~ Methods ..................................................................................................................

    /** Test BubbleSorter with String generator. */
    @Test
    public void testBubbleWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.BUBBLE, 10);
        testSorter(createStringDataSetGenerator(), SorterType.BUBBLE, 50);
        testSorter(createStringDataSetGenerator(), SorterType.BUBBLE, 100);
    }

    /** Test BubbleSorter with Integer generator. */
    @Test public void testBubbleWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.BUBBLE, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.BUBBLE, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.BUBBLE, 100);
    }

    /** Test InsertionSorter with String generator. */
    @Test public void testInsertionWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.INSERTION, 10);
        testSorter(createStringDataSetGenerator(), SorterType.INSERTION, 50);
        testSorter(createStringDataSetGenerator(), SorterType.INSERTION, 100);
    }

    /** Test InsertionSorter with Integer generator. */
    @Test public void testInsertionWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.INSERTION, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.INSERTION, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.INSERTION, 100);
    }

    /** Test SelectionSorter with String generator. */
    @Test public void testSelectionWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.SELECTION, 10);
        testSorter(createStringDataSetGenerator(), SorterType.SELECTION, 50);
        testSorter(createStringDataSetGenerator(), SorterType.SELECTION, 100);
    }

    /** Test SelectionSorter with Integer generator. */
    @Test public void testSelectionWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.SELECTION, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.SELECTION, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.SELECTION, 100);
    }
    @Test public void testQuickWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.QUICK, 10);
        testSorter(createStringDataSetGenerator(), SorterType.QUICK, 50);
        testSorter(createStringDataSetGenerator(), SorterType.QUICK, 100);
    }
    @Test public void testQuickWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK, 100);
    }
    @Test public void testHSortWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.H, 10);
        testSorter(createStringDataSetGenerator(), SorterType.H, 50);
        testSorter(createStringDataSetGenerator(), SorterType.H, 100);
    }
    @Test public void testHSortWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.H, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.H, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.H, 100);
    }
    @Test public void testShellSortWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.SHELL, 10);
        testSorter(createStringDataSetGenerator(), SorterType.SHELL, 50);
        testSorter(createStringDataSetGenerator(), SorterType.SHELL, 100);
    }
    @Test public void testShellSortWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.SHELL, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.SHELL, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.SHELL, 100);
    }
    @Test public void testQuickNonRecursiveWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_NON_RECURSIVE, 10);
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_NON_RECURSIVE, 50);
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_NON_RECURSIVE, 100);
    }
    @Test public void testQuickNonRecursiveWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_NON_RECURSIVE, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_NON_RECURSIVE, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_NON_RECURSIVE, 100);
    }
    @Test public void testQuickCutSortWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_CUT, 10);
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_CUT, 50);
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_CUT, 100);
    }
    @Test public void testQuickCUtSortWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_CUT, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_CUT, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_CUT, 100);
    }
    @Test public void testQuickSort_Median_of_ThreeSortWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_MED_OF_THREE, 10);
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_MED_OF_THREE, 50);
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_MED_OF_THREE, 100);
    }
    @Test public void testQuickSort_Median_of_ThreeWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_MED_OF_THREE, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_MED_OF_THREE, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_MED_OF_THREE, 100);
    }
    @Test public void testQuickSort_Three_PartitionWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_THREE_PARTITION, 10);
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_THREE_PARTITION, 50);
        testSorter(createStringDataSetGenerator(), SorterType.QUICK_THREE_PARTITION, 100);
    }
    @Test public void testQuickSort_Three_PartitionWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_THREE_PARTITION, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_THREE_PARTITION, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.QUICK_THREE_PARTITION, 100);
    }
    @Test public void testMergeSortTWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.MERGE_TOP_DOWN, 10);
        testSorter(createStringDataSetGenerator(), SorterType.MERGE_TOP_DOWN, 50);
        testSorter(createStringDataSetGenerator(), SorterType.MERGE_TOP_DOWN, 100);
    }
    @Test public void testMergeSortTWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.MERGE_TOP_DOWN, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.MERGE_TOP_DOWN, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.MERGE_TOP_DOWN, 100);
    }

    @Test public void testMergeSorBtWithStringGenerator() {
        testSorter(createStringDataSetGenerator(), SorterType.MERGE_BOTTOM_UP, 10);
        testSorter(createStringDataSetGenerator(), SorterType.MERGE_BOTTOM_UP, 50);
        testSorter(createStringDataSetGenerator(), SorterType.MERGE_BOTTOM_UP, 100);
    }
    @Test public void testMergeSortBWithIntegerGenerator() {
        testSorter(createIntegerDataSetGenerator(), SorterType.MERGE_BOTTOM_UP, 10);
        testSorter(createIntegerDataSetGenerator(), SorterType.MERGE_BOTTOM_UP, 50);
        testSorter(createIntegerDataSetGenerator(), SorterType.MERGE_BOTTOM_UP, 100);
    }
    @Test public void testTimeSorterWorstCase(){
        Comparator<Integer> comp = Integer::compareTo;

        AbstractSorter BS = new AbstractSorter.BubbleSorter();
        AbstractSorter SS = new AbstractSorter.SelectionSorter();
        AbstractSorter IS = new AbstractSorter.InsertionSorter();

        List<Integer> worstCase = createIntegerDataSetGenerator().createDescending(10000);
        List<Integer> worstCase1 = createIntegerDataSetGenerator().createDescending(10000);
        List<Integer> worstCase2 = createIntegerDataSetGenerator().createDescending(10000);
        //Modifico el numero para completar la tabla: Valores utilizados, 10, 50, 500, 1000, 50000
        time(BS, worstCase, comp);
        time(SS, worstCase1, comp);
        time(IS, worstCase2, comp);
    }
    @Test
    public void testTimeSortBestCase(){
        Comparator<Integer> comp = Integer::compareTo;

        AbstractSorter BS = new AbstractSorter.BubbleSorter();
        AbstractSorter SS = new AbstractSorter.SelectionSorter();
        AbstractSorter IS = new AbstractSorter.InsertionSorter();
        //Modifico el numero para completar la tabla: Valores utilizados, 10, 50, 500, 1000, 50000
        List<Integer> BestCase = createIntegerDataSetGenerator().createAscending(10);
        List<Integer> BestCase1 = createIntegerDataSetGenerator().createAscending(10);
        List<Integer> BestCase2 = createIntegerDataSetGenerator().createAscending(10);

        time(BS, BestCase, comp);
        time(SS, BestCase1, comp);
        time(IS, BestCase2, comp);
    }

    @Test
    public void testTimeSortNormalCase(){
        Comparator<Integer> comp = Integer::compareTo;

        AbstractSorter BS = new AbstractSorter.BubbleSorter();
        AbstractSorter SS = new AbstractSorter.SelectionSorter();
        AbstractSorter IS = new AbstractSorter.InsertionSorter();
        //Modifico el numero para completar la tabla: Valores utilizados, 10, 50, 500, 1000, 50000
        List<Integer> NormalCase = createIntegerDataSetGenerator().createRandom(5000);
        List<Integer> NormalCase1 = createIntegerDataSetGenerator().createRandom(5000);
        List<Integer> NormalCase2 = createIntegerDataSetGenerator().createRandom(5000);

        time(BS, NormalCase, comp);
        time(SS, NormalCase1, comp);
        time(IS, NormalCase2, comp);
    }
    @Test
    public void testSwapsNormalCase(){
        Comparator<Integer> cmp = Integer::compareTo;

        AbstractSorter BS = new AbstractSorter.BubbleSorter();
        AbstractSorter SS = new AbstractSorter.SelectionSorter();
        AbstractSorter IS = new AbstractSorter.InsertionSorter();
        //Modifico el numero para completar la tabla: Valores utilizados, 10, 50, 500, 1000, 50000
        List<Integer> NormalCase = createIntegerDataSetGenerator().createRandom(10);
        List<Integer> NormalCase1 = createIntegerDataSetGenerator().createRandom(10);
        List<Integer> NormalCase2 = createIntegerDataSetGenerator().createRandom(10);

        swap(BS, NormalCase, cmp);
        swap(SS, NormalCase1, cmp);
        swap(IS, NormalCase2, cmp);
    }

    @Test
    public void testSwapsWorstCase(){
        Comparator<Integer> cmp = Integer::compareTo;

        AbstractSorter BS = new AbstractSorter.BubbleSorter();
        AbstractSorter SS = new AbstractSorter.SelectionSorter();
        AbstractSorter IS = new AbstractSorter.InsertionSorter();

        //Modifico el numero para completar la tabla: Valores utilizados, 10, 50, 500, 1000, 50000
        List<Integer> WorstCase = createIntegerDataSetGenerator().createDescending(500);
        List<Integer> WorstCase1 = createIntegerDataSetGenerator().createDescending(500);
        List<Integer> WorstCase2 = createIntegerDataSetGenerator().createDescending(500);

        swap(BS, WorstCase, cmp);
        swap(SS, WorstCase1, cmp);
        swap(IS, WorstCase2, cmp);
    }
    @Test
    public void testSwapsBestCase(){
        Comparator<Integer> cmp = Integer::compareTo;

        AbstractSorter BS = new AbstractSorter.BubbleSorter();
        AbstractSorter SS = new AbstractSorter.SelectionSorter();
        AbstractSorter IS = new AbstractSorter.InsertionSorter();

        //Modifico el numero para completar la tabla: Valores utilizados, 10, 50, 500, 1000, 50000
        List<Integer> BestCase = createIntegerDataSetGenerator().createAscending(10);
        List<Integer> BestCase1 = createIntegerDataSetGenerator().createAscending(10);
        List<Integer> BestCase2 = createIntegerDataSetGenerator().createAscending(10);

        swap(BS, BestCase, cmp);
        swap(SS, BestCase1, cmp);
        swap(IS, BestCase2, cmp);
    }
    @Test
    public void testComparacionesNormalCase(){
        Comparator<Integer> cmp = Integer::compareTo;

        AbstractSorter BS = new AbstractSorter.BubbleSorter();
        AbstractSorter SS = new AbstractSorter.SelectionSorter();
        AbstractSorter IS = new AbstractSorter.InsertionSorter();
        //Modifico el numero para completar la tabla: Valores utilizados, 10, 50, 500, 1000, 50000
        List<Integer> NormalCase = createIntegerDataSetGenerator().createRandom(1000);
        List<Integer> NormalCase1 = createIntegerDataSetGenerator().createRandom(1000);
        List<Integer> NormalCase2 = createIntegerDataSetGenerator().createRandom(5000);

        Comparation(BS, NormalCase, cmp);
        Comparation(SS, NormalCase1, cmp);
        Comparation(IS, NormalCase2, cmp);
    }
    @Test
    public void testComparacionesWorstCase(){
        Comparator<Integer> cmp = Integer::compareTo;

        AbstractSorter BS = new AbstractSorter.BubbleSorter();
        AbstractSorter SS = new AbstractSorter.SelectionSorter();
        AbstractSorter IS = new AbstractSorter.InsertionSorter();

        //Modifico el numero para completar la tabla: Valores utilizados, 10, 50, 500, 1000, 50000
        List<Integer> WorstCase = createIntegerDataSetGenerator().createDescending(5000);
        List<Integer> WorstCase1 = createIntegerDataSetGenerator().createDescending(5000);
        List<Integer> WorstCase2 = createIntegerDataSetGenerator().createDescending(50);

        Comparation(BS, WorstCase, cmp);
        Comparation(SS, WorstCase1, cmp);
        Comparation(IS, WorstCase2, cmp);
    }
    @Test
    public void testComparacionesBestCase(){
        Comparator<Integer> cmp = Integer::compareTo;

        AbstractSorter BS = new AbstractSorter.BubbleSorter();
        AbstractSorter SS = new AbstractSorter.SelectionSorter();
        AbstractSorter IS = new AbstractSorter.InsertionSorter();

        //Modifico el numero para completar la tabla: Valores utilizados, 10, 50, 500, 1000, 50000
        List<Integer> BestCase = createIntegerDataSetGenerator().createAscending(10);
        List<Integer> BestCase1 = createIntegerDataSetGenerator().createAscending(10);
        List<Integer> BestCase2 = createIntegerDataSetGenerator().createAscending(5000);

        Comparation(BS, BestCase, cmp);
        Comparation(SS, BestCase1, cmp);
        Comparation(IS, BestCase2, cmp);
    }
    @Test
    public void testQuickCuttSort(){
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QC = new AbstractSorter.QuickCut();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(1000);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(10000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(100000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(1000000);

        time(QC, case1, cmp);
        time(QC, case2, cmp);
        time(QC, case3, cmp);
        time(QC, case4, cmp);
    }
    @Test
    public void testQuickCuttime(){
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QC = new AbstractSorter.QuickCut();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(12500);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(25000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(50000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(100000);

        time(QC, case1, cmp);
        time(QC, case2, cmp);
        time(QC, case3, cmp);
        time(QC, case4, cmp);
    }
    @Test
    public void testQuickSorttime() {
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QS = new AbstractSorter.QuickSorter();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(12500);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(25000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(50000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(100000);

        time(QS, case1, cmp);
        time(QS, case2, cmp);
        time(QS, case3, cmp);
        time(QS, case4, cmp);
    }
    @Test
    public void testQuickMedSorttime() {
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QM = new AbstractSorter.QuickSort_Median_of_three();
        AbstractSorter QP = new AbstractSorter.QuickSort_Three_Partition();
        AbstractSorter QN = new AbstractSorter.QuickNonRecursive();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(12500);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(25000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(50000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(100000);

        time(QM, case1, cmp);
        time(QM, case2, cmp);
        time(QM, case3, cmp);
        time(QM, case4, cmp);
    }
    @Test
    public void testQuickPartitionSorttime() {
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QP = new AbstractSorter.QuickSort_Three_Partition();


        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(12500);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(25000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(50000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(100000);

        time(QP, case1, cmp);
        time(QP, case2, cmp);
        time(QP, case3, cmp);
        time(QP, case4, cmp);
    }
    @Test
    public void testQuickNonReSorttime() {
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QN = new AbstractSorter.QuickNonRecursive();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(12500);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(25000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(50000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(100000);

        time(QN, case1, cmp);
        time(QN, case2, cmp);
        time(QN, case3, cmp);
        time(QN, case4, cmp);
    }
    @Test
    public void testQuickCutComp(){
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QC = new AbstractSorter.QuickCut();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(12500);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(25000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(50000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(100000);

        Comparation(QC, case1, cmp);
        Comparation(QC, case2, cmp);
        Comparation(QC, case3, cmp);
        Comparation(QC, case4, cmp);
    }
    @Test
    public void testQuickSortComp() {
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QS = new AbstractSorter.QuickSorter();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(12500);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(25000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(50000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(100000);

        Comparation(QS, case1, cmp);
        Comparation(QS, case2, cmp);
        Comparation(QS, case3, cmp);
        Comparation(QS, case4, cmp);
    }
    @Test
    public void testQuickMedSortComp() {
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QM = new AbstractSorter.QuickSort_Median_of_three();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(12500);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(25000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(50000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(100000);

        Comparation(QM, case1, cmp);
        Comparation(QM, case2, cmp);
        Comparation(QM, case3, cmp);
        Comparation(QM, case4, cmp);
    }
    @Test
    public void testQuickPartitionSortComp() {
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QP = new AbstractSorter.QuickSort_Three_Partition();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(12500);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(25000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(50000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(100000);

        Comparation(QP, case1, cmp);
        Comparation(QP, case2, cmp);
        Comparation(QP, case3, cmp);
        Comparation(QP, case4, cmp);
    }
    @Test
    public void testQuickNonReSortComp() {
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QN = new AbstractSorter.QuickNonRecursive();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(12500);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(25000);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(50000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(100000);

        Comparation(QN, case1, cmp);
        Comparation(QN, case2, cmp);
        Comparation(QN, case3, cmp);
        Comparation(QN, case4, cmp);
    }
    @Test
    public void testMerge_Bottom(){
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QC = new AbstractSorter.QuickCut();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(100);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(500);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(1000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(2500);
        List<Integer> case5 = createIntegerDataSetGenerator().createRandom(5000);

        time(QC, case1, cmp);
        time(QC, case2, cmp);
        time(QC, case3, cmp);
        time(QC, case4, cmp);
        time(QC, case5, cmp);
    }
    @Test
    public void testMerge_Top(){
        Comparator<Integer> cmp = Integer::compareTo;
        AbstractSorter QC = new AbstractSorter.QuickCut();

        List<Integer> case1 = createIntegerDataSetGenerator().createRandom(100);
        List<Integer> case2 = createIntegerDataSetGenerator().createRandom(500);
        List<Integer> case3 = createIntegerDataSetGenerator().createRandom(1000);
        List<Integer> case4 = createIntegerDataSetGenerator().createRandom(2500);
        List<Integer> case5 = createIntegerDataSetGenerator().createRandom(5000);

        time(QC, case1, cmp);
        time(QC, case2, cmp);
        time(QC, case3, cmp);
        time(QC, case4, cmp);
        time(QC, case5, cmp);
    }

    private <T> void time(AbstractSorter sorter, List<T> lista, Comparator<T> comp){
        long InitialTime = System.nanoTime();
        sorter.sort(comp, lista);
        long EndTime = System.nanoTime();
        long Final = EndTime - InitialTime;
        System.out.println(Final + " Nanosegundos");
    }
    private <T> void swap(AbstractSorter sorter, List<T> lista, Comparator<T> cmp){
        sorter.sort(cmp, lista);
        System.out.println("Swaps: " + sorter.getSwaps());
    }
    private <T> void Comparation(AbstractSorter sorter, List<T> lista, Comparator<T> cmp){
        sorter.sort(cmp, lista);
        System.out.println("Comparations: " + sorter.getComparaciones());
    }
}