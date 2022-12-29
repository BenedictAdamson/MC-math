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
import java.util.Objects;

/**
 * <p>
 * A column vector.
 * </p>
 */
public interface Vector extends Matrix {

    static void requireValidWeights(double[] weight, Vector[] x) {
        Objects.requireNonNull(weight, "weight");
        if (weight.length == 0) {
            throw new IllegalArgumentException("weight.length " + weight.length);
        }
        if (weight.length != x.length) {
            throw new IllegalArgumentException("Inconsistent lengths weight.length " + weight.length + " x.length " + x.length);
        }
    }

    /**
     * <p>
     * Calculate the dot product of this vector and another vector.
     * </p>
     *
     * @param that The other vector
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain #getDimension() dimension} of {@code that} is
     *                                  not equal to the dimension of this.
     */
    double dot(@Nonnull Vector that);

    /**
     * <p>
     * The value of an element of this vector.
     * </p>
     *
     * @param i the cardinal number of the row of the element (0 for the first
     *          row, 1 for the second row, and so on).
     * @return the value of the element
     * @throws IndexOutOfBoundsException If {@code i} is negative.
     *                                   If {@code i} is greater than or equal to the number of
     *                                   {@linkplain #getRows() rows} of this vector.
     */
    double get(@Nonnegative int i);

    /**
     * {@inheritDoc}
     *
     * <ul>
     * <li>The number of columns of a vector is always 1.</li>
     * </ul>
     */
    @Override
    @Nonnegative
    int getColumns();

    /**
     * <p>
     * The number of dimensions of this vector.
     * </p>
     * <ul>
     * <li>The number of dimensions is positive.</li>
     * <li>The number of dimensions equals the {@linkplain #getRows() number of
     * rows}.</li>
     * </ul>
     */
    @Nonnegative
    int getDimension();

    /**
     * <p>
     * Create an array that holds the components of this vector.
     * </p>
     * <p>
     * The <var>i</var><sup>th</sup> component of the returned array is equal to the
     * <var>i</var><sup>th</sup>
     * {@linkplain #get(int) component}
     * of this vector.
     * </p>
     */
    @Nonnull
    double[] getComponentsAsArray();

    /**
     * <p>
     * The magnitude of this vector.
     * </p>
     * <ul>
     * <li>The magnitude of this vector is the square root of the dot product of
     * this vector with itself.</li>
     * <li>The magnitude will usually be non-negative. It will however be NaN if any components of  this vector are NaN.</li>
     * </ul>
     */
    double magnitude();

    /**
     * <p>
     * The square of the magnitude of this vector.
     * </p>
     * <p>
     * The method takes care to properly handle vectors with components that are
     * large, not numbers, or which differ greatly in magnitude. It is otherwise
     * similar to the {@linkplain #dot(Vector) dot product} of the vector with
     * itself.
     * The square magnitude will usually be non-negative. It will however be NaN if any components of this vector are NaN or if the computation overflows.
     * </p>
     */
    double magnitude2();

    /**
     * <p>
     * Create the vector that is the mean of this vector with another vector.
     * </p>
     * <p>The {@linkplain #getDimension() dimension} of the mean vector is equal to
     * the dimension of this vector.</p>
     *
     * @param that The vector to take the mean with
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain ImmutableVector3#getDimension() dimension} of
     *                                  }@code that} is not equal to the dimension of this vector.
     */
    @Nonnull
    Vector mean(@Nonnull Vector that);

    /**
     * <p>
     * Create the vector that is opposite in direction of this vector.
     * </p>
     * <ul>
     * <li>The opposite vector has the same {@linkplain #getDimension() dimension}
     * as this vector.</li>
     * <li>The {@linkplain #get(int) components} of the opposite vector are the
     * negative of the corresponding component of this vector.</li>
     * </ul>
     */
    @Nonnull
    Vector minus();

    /**
     * <p>
     * Create the vector that is a given vector subtracted from this vector; the
     * difference between this vector and another.
     * </p>
     * <ul>
     * <li>The difference vector has the same {@linkplain #getDimension() dimension}
     * as this vector.</li>
     * <li>The {@linkplain #get(int) components} of the difference vector are the
     * difference of the corresponding component of this vector.</li>
     * </ul>
     *
     * @param that The other vector
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain #getDimension() dimension} of {@code that} is
     *                                  not equal to the dimension of this.
     */
    @Nonnull
    Vector minus(@Nonnull Vector that);

    /**
     * <p>
     * Create the vector that is a given vector added to this vector; the sum of
     * this vector and another.
     * </p>
     * <ul>
     * <li>The sum vector has the same {@linkplain #getDimension() dimension} as
     * this vector.</li>
     * <li>The {@linkplain #get(int) components} of the sum vector are the sum with
     * the corresponding component of this vector.</li>
     * </ul>
     *
     * @param that The other vector
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain #getDimension() dimension} of {@code that} is
     *                                  not equal to the dimension of this.
     */
    @Nonnull
    Vector plus(@Nonnull Vector that);

    /**
     * <p>
     * Create a vector that is this vector scaled by a given scalar.
     * </p>
     * <p>The {@linkplain ImmutableVector3#getDimension() dimension} of the scaled
     * vector is equal to the dimension of this vector.</p>
     *
     * @param f the scalar
     */
    @Nonnull
    Vector scale(double f);

}