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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * <p>
 * A rectangular (2D) array of real numbers.
 * </p>
 */
public interface Matrix {

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

}