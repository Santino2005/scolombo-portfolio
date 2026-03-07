
// ...............................................................................................................................
//
// (C) Copyright  2011/2013 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package anaydis.sort;

import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Análisis y Diseño de Algoritmos.
 *
 * Basic Interface for all {@link Sorter sorters}. All Sorter implementations should implement {@link Sorter}.
 *
 * Common implementations:
 *
 * <ul>
 *   <li>BubbleSorter</li>
 *   <li>InsertionSorter</li>
 *   <li>SelectionSorter</li>
 *   <li>MergeSorter</li>
 *   <li>QuickSorter</li>
 *   <li>ShellSorter</li>
 *   <li>HSorter</li>
 * </ul>
 */
public interface Sorter {

    //~ Methods ..................................................................................................................

    /**
     * Sorts the specified list into ascending order, according to the ordering provided by given comparator.
     *
     * @param  comparator  to be used for sorting
     * @param  list        to be sorted.
     */
    <T> void sort(@NotNull final Comparator<T> comparator, @NotNull final List<T> list);

    /**
     * Returns the {@link SorterType type}.
     *
     * @return  Sorter's type.
     */
    @NotNull SorterType getType();
}
