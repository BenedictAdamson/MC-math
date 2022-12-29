package uk.badamson.mc.math;
/*
 * © Copyright Benedict Adamson 2018,22-23.
 *
 * This file is part of MC-math.
 *
 * MC-math is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MC-math is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MC-math.  If not, see <https://www.gnu.org/licenses/>.
 */

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.Arrays;

/**
 * <p>
 * A mutable 2D array of real numbers of any constant size.
 * </p>
 */
@NotThreadSafe
public class MutableMatrixN extends ArrayMatrix {

    MutableMatrixN(final int rows, final int columns, @Nonnull final double[] elements) {
        super(rows, columns, elements);
    }

    /**
     * <p>
     * Create a matrix with given element values.
     * </p>
     *
     * @param rows     The number of rows of the matrix.
     * @param columns  The number of columns of this matrix.
     * @param elements The values of the elements of the matrix; the elements are in
     *                 <i>row-major</i> order, so {@code element[i*columns + j]} is the
     *                 value of cardinal row <var>i</var>, cardinal column <var>j</var>.
     * @throws NullPointerException     If {@code elements} is null.
     * @throws IllegalArgumentException If {@code rows} is not positive.
     *                                  If {@code columns} is not positive.
     *                                  If the length of {@code elements} is not equal to
     *                                  {@code rows} multiplied by {@code columns}.
     */
    @Nonnull
    public static MutableMatrixN create(@Nonnegative final int rows,
                                        @Nonnegative final int columns,
                                        @Nonnull final double[] elements) {

        requireValidCreationArguments(rows, columns, elements);
        // TODO optimize to use a MutableVectorN if columns == 1
        return new MutableMatrixN(rows, columns, Arrays.copyOf(elements, elements.length));
    }

    @Nonnull
    @Override
    public MutableMatrixN minus() {
        return new MutableMatrixN(rows, columns, minusElements());
    }

    @Override
    @Nonnull
    public MutableMatrixN minus(@Nonnull final Matrix that) {
        return new MutableMatrixN(rows, columns, minusElements(that));
    }

    @Override
    @Nonnull
    public MutableMatrixN plus(@Nonnull final Matrix that) {
        return new MutableMatrixN(rows, columns, plusElements(that));
    }

    @Nonnull
    @Override
    public MutableMatrixN scale(double f) {
        return new MutableMatrixN(rows, columns, scaleElements(f));
    }

    @Nonnull
    @Override
    public MutableVectorN mean(@Nonnull final Matrix that) {
        return new MutableVectorN(meanElements(that));
    }

    /**
     * <p>
     * Change the value of an element of this matrix.
     * </p>
     *
     * @param i the cardinal number of the row of the element (0 for the first
     *          row, 1 for the second row, and so on).
     * @param j the cardinal number of the column of the element (0 for the first
     *          column, 1 for the second column, and so on).
     * @param x the new value for the element
     * @throws IndexOutOfBoundsException If {@code i} is negative.
     *                                   If {@code i} is greater than or equal to the number of
     *                                   {@linkplain #getRows() rows} of this matrix.
     *                                   If {@code j} is negative.
     *                                   If {@code j} is greater than or equal to the number of
     *                                   {@linkplain #getColumns() columns} of this matrix.
     */
    public final void set(@Nonnegative final int i, @Nonnegative final int j, final double x) {
        requireRowInBounds(i);
        requireColumnInBounds(j);
        elements[i * columns + j] = x;
    }

}
