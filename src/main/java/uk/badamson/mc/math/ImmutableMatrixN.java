package uk.badamson.mc.math;
/*
 * © Copyright Benedict Adamson 2018,22.
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

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p>
 * A constant (immutable) 2D array of real numbers of any size.
 * </p>
 */
@Immutable
public class ImmutableMatrixN implements Matrix {

    protected final double[] elements;
    private final int rows;
    private final int columns;
    ImmutableMatrixN(final int rows, final int columns, @NonNull final double[] elements) {
        this.rows = rows;
        this.columns = columns;
        this.elements = elements;
    }

    /**
     * <p>
     * Create a matrix with given element values.
     * </p>
     * <ul>
     * <li>Always creates a (non null) matrix.</li>
     * <li>The created matrix has the given attribute values.</li>
     * </ul>
     *
     * @param rows     The number of rows of the matrix.
     * @param columns  The number of columns of this matrix.
     * @param elements The values of the elements of the matrix; the elements are in
     *                 <i>row-major</i> order, so {@code element[i*columns + j]} is the
     *                 value of cardinal row <var>i</var>, cardinal column <var>j</var>.
     * @return the created matrix
     * @throws NullPointerException     If {@code elements} is null.
     * @throws IllegalArgumentException <ul>
     *                                              <li>If {@code rows} is not positive.</li>
     *                                              <li>If {@code columns} is not positive.</li>
     *                                              <li>If the length of {@code elements} is not equal to
     *                                              {@code rows} multiplied by {@code columns}.</li>
     *                                              </ul>
     */
    public static ImmutableMatrixN create(final int rows, final int columns, @NonNull final double[] elements) {
        Objects.requireNonNull(elements, "elements");
        if (rows < 1) {
            throw new IllegalArgumentException("rows " + rows);
        }
        if (columns < 1) {
            throw new IllegalArgumentException("columns " + columns);
        }
        if (elements.length != rows * columns) {
            throw new IllegalArgumentException(
                    "Inconsistent rows " + rows + " columns " + columns + " elements.length " + elements.length);
        }
        if (columns == 1) {
            return ImmutableVectorN.create(elements);
        } else {
            return new ImmutableMatrixN(rows, columns, Arrays.copyOf(elements, elements.length));
        }
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof final ImmutableMatrixN other)) {
            return false;
        }
        return rows == other.rows && Arrays.equals(elements, other.elements);
    }

    @Override
    public final double get(final int i, final int j) {
        if (i < 0 || rows <= i) {
            throw new IndexOutOfBoundsException("i " + i);
        }
        if (j < 0 || columns <= j) {
            throw new IndexOutOfBoundsException("j " + j);
        }
        return elements[i * columns + j];
    }

    @Override
    public final int getColumns() {
        return columns;
    }

    @Override
    public final int getRows() {
        return rows;
    }

    @Override
    public final int hashCode() {
        final int prime = 37;
        int result = columns;
        result = prime * result + rows;
        result = prime * result + Arrays.hashCode(elements);
        return result;
    }

    @Override
    public final @NonNull ImmutableVectorN multiply(@NonNull final Vector x) {
        Objects.requireNonNull(x, "x");
        final int columns = getColumns();
        if (columns != x.getRows()) {
            throw new IllegalArgumentException("Inconsistent numbers of columns and rows");
        }
        final int n = getRows();
        final double[] ax = new double[n];
        for (int i = 0; i < n; ++i) {
            double dot = 0.0;
            final int j0 = i * columns;
            for (int j = 0; j < columns; ++j) {
                dot += elements[j0 + j] * x.get(j);
            }
            ax[i] = dot;
        }
        return new ImmutableVectorN(ax);
    }

}
