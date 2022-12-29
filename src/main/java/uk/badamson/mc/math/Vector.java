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
     * Calculate the sum of several vectors that have the same
     * {@linkplain #getDimension() dimension}, provide the result as an array of components.
     * </p>
     * <ul>
     * <li>The length of the returned array equals the dimension of the summed vectors.</li>
     * <li>Element i of the returned array is the ith element of the sum vector</li>
     * </ul>
     *
     * @param x The vectors to sum
     * @throws NullPointerException     If {@code x} is null. If {@code x} has any null elements.
     * @throws IllegalArgumentException If the elements of {@code x} do not have the same
     *                                  {@linkplain #getDimension() dimension}.
     */
    @Nonnull
    static double[] sumAsArray(@Nonnull final Vector... x) {
        Objects.requireNonNull(x, "x");
        final int n = x.length;
        if (n == 0) {
            throw new IllegalArgumentException("Number of vector arguments");
        }
        Objects.requireNonNull(x[0], "x[0]");

        final int d = x[0].getDimension();
        final double[] sum = new double[d];
        for (final Vector xj : x) {
            Objects.requireNonNull(xj, "x[j]");
            if (xj.getDimension() != d) {
                throw new IllegalArgumentException("Inconsistent dimension " + d + ", " + xj.getDimension());
            }

            for (int i = 0; i < d; ++i) {
                sum[i] += xj.get(i);
            }
        }

        return sum;
    }


    /**
     * <p>
     * Calculate the weighted sum of several vectors that have the same
     * {@linkplain #getDimension() dimension}, returning the result as an array of the vector component.
     * </p>
     * <ul>
     * <li>The length of the returned array equals the dimension of the summed vectors.</li>
     * <li>Element i of the returned array is the ith element of the sum vector</li>
     * </ul>
     *
     * @param weight The weights to apply; {@code weight[i]} is the weight for vector
     *               {@code x[i]}.
     * @param x      The vectors to sum
     * @throws NullPointerException     If {@code weight} is null. If {@code x} is null. If {@code x} has any null elements.
     * @throws IllegalArgumentException If {@code weight} has a length of 0. If {@code weight} and {@code x} have different lengths. If the elements of {@code x} do not have the same
     *                                  {@linkplain #getDimension() dimension}.
     */
    @Nonnull
    static double[] weightedSumAsArray(
            @Nonnull final double[] weight,
            @Nonnull final Vector[] x) {
        Objects.requireNonNull(weight, "weight");
        Objects.requireNonNull(x, "x");
        final int n = weight.length;
        if (n == 0) {
            throw new IllegalArgumentException("weight.length " + n);
        }
        if (n != x.length) {
            throw new IllegalArgumentException("Inconsistent lengths weight.length " + n + " x.length " + x.length);
        }
        Objects.requireNonNull(x[0], "x[0]");

        final int d = x[0].getDimension();
        final double[] sum = new double[d];
        for (int j = 0; j < n; ++j) {
            final double wj = weight[j];
            final Vector xj = x[j];
            Objects.requireNonNull(xj, "x[j]");
            if (xj.getDimension() != d) {
                throw new IllegalArgumentException("Inconsistent dimension " + d + ", " + xj.getDimension());
            }

            for (int i = 0; i < d; ++i) {
                sum[i] += wj * xj.get(i);
            }
        }

        return sum;
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
     * Create the vector that is opposite in direction of this vector.
     * </p>
     * <ul>
     * <li>The opposite vector has the same {@linkplain #getDimension() dimension}
     * as this vector.</li>
     * <li>The {@linkplain #get(int) components} of the opposite vector are the
     * negative of the corresponding component of this vector.</li>
     * </ul>
     */
    @Override
    @Nonnull
    Vector minus();

    @Override
    @Nonnull
    Vector minus(@Nonnull Matrix that);

    @Override
    @Nonnull
    Vector plus(@Nonnull Matrix that);

    @Nonnull
    @Override
    Vector scale(double f);
}