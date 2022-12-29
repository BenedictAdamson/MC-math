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

/**
 * <p>
 * A rectangular (2D) array of real numbers.
 * </p>
 */
public interface Matrix {


    /**
     * Throw an IllegalArgumentException if two {@link Matrix} objects do not have equal {@linkplain #getRows() numbers of rows}
     * and {@linkplain #getColumns() numbers of columns}.
     */
    static void requireConsistentDimensions(final Matrix m1, final Matrix m2) throws IllegalArgumentException {
        var c1 = m1.getColumns();
        var c2 = m2.getColumns();
        if (c1 != c2) {
            throw new IllegalArgumentException(
                    "Inconsistent columns, " + c1 + ", " + c2);
        }
        var r1 = m1.getRows();
        var r2 = m2.getRows();
        if (r1 != r2) {
            throw new IllegalArgumentException(
                    "Inconsistent rows, " + r1 + ", " + r2);
        }
    }

    /**
     * <p>
     * Whether this object is <dfn>equivalent</dfn> to another object.
     * </p>
     * <p>
     * The {@link Matrix} class has <i>value semantics</i>: this object is
     * equivalent to another if, and only if, the other object is also an
     * {@link Matrix} and they have equivalent attributes.
     * </p>
     */
    @Override
    boolean equals(Object obj);

    /**
     * <p>
     * The value of an element of this matrix.
     * </p>
     *
     * @param i the cardinal number of the row of the element (0 for the first
     *          row, 1 for the second row, and so on).
     * @param j the cardinal number of the column of the element (0 for the first
     *          column, 1 for the second column, and so on).
     * @return the value of the element
     * @throws IndexOutOfBoundsException If {@code i} is negative.
     *                                   If {@code i} is greater than or equal to the number of
     *                                   {@linkplain #getRows() rows} of this matrix.
     *                                   If {@code j} is negative.
     *                                   If {@code j} is greater than or equal to the number of
     *                                   {@linkplain #getColumns() columns} of this matrix.
     */
    double get(@Nonnegative int i, @Nonnegative int j);

    /**
     * <p>
     * The number of columns of this matrix.
     * </p>
     * <p>
     * positive.
     */
    @Nonnegative
    int getColumns();

    /**
     * <p>
     * The number of rows of this matrix.
     * </p>
     * <p>
     * positive.
     */
    @Nonnegative
    int getRows();

    /**
     * <p>
     * Create the matrix that has all its components the unary minus of the corresponding components of this matrix.
     * </p>
     * <ul>
     * <li>The minus matrix has the same {@linkplain #getColumns() number of columns}
     * as this matrix.</li>
     * <li>The minus matrix has the same {@linkplain #getRows()  number of rows}
     * as this matrix.</li>
     * <li>The {@linkplain #get(int, int)}  components} of the minus matrix are the
     * negative of the corresponding component of this matrix.</li>
     * </ul>
     */
    @Nonnull
    Matrix minus();

    /**
     * <p>
     * Create the matrix that is a given matrix subtracted from this matrix; the
     * difference between this matrix and another.
     * </p>
     * <ul>
     * <li>The difference matrix has the same {@linkplain #getRows() number of rows}
     * as this matrix.</li>
     * <li>The difference matrix has the same {@linkplain #getColumns() number of columns}
     * as this matrix.</li>
     * <li>The {@linkplain #get(int, int)  components} of the difference matrix are the
     * difference of the corresponding components of this and that matrices.</li>
     * </ul>
     *
     * @param that The other matrix
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain #getRows() number of rows} of {@code that} is
     *                                  not equal to the number of rows of this.
     *                                  If the {@linkplain #getColumns()  number of columns} of {@code that} is
     *                                  not equal to the number of columns of this.
     */
    @Nonnull
    Matrix minus(@Nonnull Matrix that);


    /**
     * <p>
     * Create the matrix that is the sum of this matrix and a given matrix.
     * </p>
     * <ul>
     * <li>The sum matrix has the same {@linkplain #getRows() number of rows}
     * as this matrix.</li>
     * <li>The sum matrix has the same {@linkplain #getColumns() number of columns}
     * as this matrix.</li>
     * <li>The {@linkplain #get(int, int)  components} of the sum matrix are the
     * sum of the corresponding components of the matrices.</li>
     * </ul>
     *
     * @param that The other matrix
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain #getRows() number of rows} of {@code that} is
     *                                  not equal to the number of rows of this.
     *                                  If the {@linkplain #getColumns()  number of columns} of {@code that} is
     *                                  not equal to the number of columns of this.
     */
    @Nonnull
    Matrix plus(@Nonnull Matrix that);

    /**
     * <p>
     * Create a matrix that is this vector scaled by a given scalar.
     * </p>
     * <ul>
     * <li>The {@linkplain #getRows()  number of rows} of the scaled
     * matrix is equal to the number of rows of this matrix.</li>
     * <li>The {@linkplain #getColumns() number of columns} of the scaled
     * matrix is equal to the number of columns of this vector.</li>
     * <li>Each {@linkplain #get(int, int) components} of the scaled
     * matrix is equal to the scaled corresponding component of this matrix.</li>
     * </ul>
     *
     * @param f the scalar
     */
    @Nonnull
    Matrix scale(double f);

    /**
     * <p>
     * Calculate the result of multiplying a vector by this matrix.
     * </p>
     * <ul>
     * <li>The {@linkplain ImmutableVectorN#getRows() number of rows} of the product
     * is equal to the number of rows of this matrix.</li>
     * </ul>
     *
     * @param x The vector to multiply
     * @return the product of this and the given vector.
     * @throws NullPointerException     If {@code x} is null.
     * @throws IllegalArgumentException If the {@linkplain ImmutableVectorN#getRows() number of rows} of
     *                                  {@code x} is not equal to the {@linkplain #getColumns() number of
     *                                  columns} of this.
     */
    @Nonnull
    Vector multiply(@Nonnull Vector x);

    /**
     * <p>
     * Create the matrix that is the mean of this matrix with another matrix.
     * </p>
     * <ul>
     * <li>The {@linkplain #getRows()  number of rows} of the mean
     * matrix is equal to the number of rows of this matrix.</li>
     * <li>The {@linkplain #getColumns()} number of columns} of the mean
     * matrix is equal to the number of columns of this matrix.</li>
     * </ul>
     *
     * @param that The matrix to take the mean with
     * @return the mean
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain #getRows() number of rows} of
     *                                  {@code that} is not equal to the number of rows of this matrix.
     *                                  If the {@linkplain #getColumns()  number of columns} of
     *                                  {@code that} is not equal to the number of columns of this matrix.
     */
    @Nonnull
    Matrix mean(@Nonnull Matrix that);
}