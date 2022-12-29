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
import javax.annotation.concurrent.Immutable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * <p>
 * A mutable mathematical vector or pseudo vector of any constant size.
 * </p>
 * <ul>
 * <li>A vector is a {@linkplain MutableMatrixN matrix} that has only one
 * {@linkplain #getColumns() column}.</li>
 * </ul>
 */
@Immutable
public final class MutableVectorN extends MutableMatrixN implements Vector {

    MutableVectorN(final double... x) {
        super(x.length, 1, x);
    }

    /**
     * <p>
     * Create a vector from its components.
     * </p>
     * <ul>
     * <li>The {@linkplain #getDimension() number of dimensions} of the vector is
     * equal to the length of the given array of components.</li>
     * </ul>
     *
     * @param x The components of this vector
     * @throws NullPointerException     If {@code x} is null.
     * @throws IllegalArgumentException If {@code x} is empty (length 0)
     */
    @Nonnull
    public static MutableVectorN create(@Nonnull final double... x) {
        Objects.requireNonNull(x, "x");
        final int n = x.length;
        if (n == 0) {
            throw new IllegalArgumentException("x is empty");
        }
        return new MutableVectorN(Arrays.copyOf(x, n));
    }

    /**
     * <p>
     * Create a zero vector having a given dimension.
     * </p>
     * <ul>
     * <li>The {@linkplain #getDimension() dimension} of the zero vector is the
     * given dimension.</li>
     * <li>The {@linkplain #get(int) elements} of the zero vector are all zero.</li>
     * </ul>
     *
     * @param dimension The dimension
     * @throws IllegalArgumentException If {@code dimension} is not positive
     */
    @Nonnull
    public static MutableVectorN create0(final int dimension) {
        if (dimension <= 0) {
            throw new IllegalArgumentException("dimension " + dimension);
        }
        return new MutableVectorN(new double[dimension]);
    }

    /**
     * <p>
     * Copy a {@link Vector}
     * </p>
     * <p>
     * Return an immutable vector that has the same {@linkplain Vector#getDimension() dimension}
     * and {@linkplain Vector#get(int) components}
     * as a given vector.
     * If the given {@link Vector} is subsequently modified, the returned Vector will not reflect such modifications.
     * </p>
     *
     * @see java.util.List#copyOf(Collection)
     */
    @Nonnull
    public static MutableVectorN copyOf(@Nonnull Vector x) {
        return new MutableVectorN(x.getComponentsAsArray());
    }

    /**
     * <p>
     * Calculate the sum of several vectors that have the same
     * {@linkplain #getDimension() dimension}.
     * </p>
     * <ul>
     * <li>The dimension of the sum equals the dimension of the summed vectors.</li>
     * </ul>
     *
     * @param x The vectors to sum
     * @throws NullPointerException     If {@code x} is null. If {@code x} has any null elements.
     * @throws IllegalArgumentException If the elements of {@code x} do not have the same
     *                                  {@linkplain #getDimension() dimension}.
     * @see #plus(Matrix)
     */
    @Nonnull
    public static MutableVectorN sum(@Nonnull final MutableVectorN... x) {
        return new MutableVectorN(Vector.sumAsArray(x));
    }

    /**
     * <p>
     * Calculate the weighted sum of several vectors that have the same
     * {@linkplain #getDimension() dimension}.
     * </p>
     * <ul>
     * <li>The dimension of the sum equals the dimension of the summed vectors.</li>
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
    public static MutableVectorN weightedSum(
            @Nonnull final double[] weight,
            @Nonnull final MutableVectorN[] x) {
        return new MutableVectorN(Vector.weightedSumAsArray(weight, x));
    }

    @Override
    public double dot(@Nonnull final Vector that) {
        Objects.requireNonNull(that, "that");
        Matrix.requireConsistentDimensions(this, that);
        return vectorDotProduct(that);
    }

    /**
     * <p>
     * One of the components of this vector.
     * </p>
     *
     * @param i The index of the component.
     * @return the component.
     * @throws IndexOutOfBoundsException If {@code i} is less than 0 or greater than or equal to the
     *                                   {@linkplain #getDimension() number of dimensions} of this vector.
     */
    @Override
    public double get(@Nonnegative final int i) {
        return elements[i];
    }

    /**
     * <p>
     * The number of dimensions of this vector.
     * </p>
     * <ul>
     * <li>The number of dimensions is positive.</li>
     * </ul>
     */
    @Override
    @Nonnegative
    public int getDimension() {
        return elements.length;
    }

    @Nonnull
    @Override
    public double[] getComponentsAsArray() {
        return Arrays.copyOf(elements, elements.length);
    }

    @Override
    public double magnitude() {
        return vectorMagnitude();
    }

    @Override
    public double magnitude2() {
        return vectorMagnitude2();
    }

    @Nonnull
    @Override
    public MutableVectorN mean(@Nonnull final Matrix that) {
        return new MutableVectorN(meanElements(that));
    }

    @Nonnull
    @Override
    public MutableVectorN minus() {
        return new MutableVectorN(minusElements());
    }

    @Override
    @Nonnull
    public MutableVectorN minus(@Nonnull final Matrix that) {
        return new MutableVectorN(minusElements(that));
    }

    @Override
    @Nonnull
    public MutableVectorN plus(@Nonnull final Matrix that) {
        return new MutableVectorN(plusElements(that));
    }

    @Nonnull
    @Override
    public MutableVectorN scale(final double f) {
        return new MutableVectorN(scaleElements(f));
    }

    /**
     * <p>
     * Change the value of an element of this vector.
     * </p>
     *
     * @param i the cardinal number of the row of the element (0 for the first
     *          row, 1 for the second row, and so on).
     * @param x the new value for the element
     * @throws IndexOutOfBoundsException If {@code i} is negative.
     *                                   If {@code i} is greater than or equal to the number of
     *                                   {@linkplain #getRows() rows} of this matrix.
     */
    public void set(@Nonnegative final int i, final double x) {
        requireRowInBounds(i);
        elements[i] = x;
    }
}
