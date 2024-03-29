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
 * A constant (immutable) mathematical vector or pseudo vector of any size.
 * </p>
 * <ul>
 * <li>A vector is a {@linkplain ImmutableMatrixN matrix} that has only one
 * {@linkplain #getColumns() column}.</li>
 * </ul>
 */
@Immutable
public final class ImmutableVectorN extends ImmutableMatrixN implements Vector {

    ImmutableVectorN(final double... x) {
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
    public static ImmutableVectorN create(@Nonnull final double... x) {
        Objects.requireNonNull(x, "x");
        final int n = x.length;
        if (n == 0) {
            throw new IllegalArgumentException("x is empty");
        }
        return new ImmutableVectorN(Arrays.copyOf(x, n));
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
    public static ImmutableVectorN create0(final int dimension) {
        if (dimension <= 0) {
            throw new IllegalArgumentException("dimension " + dimension);
        }
        return new ImmutableVectorN(new double[dimension]);
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
    public static ImmutableVectorN copyOf(@Nonnull Vector x) {
        Objects.requireNonNull(x);
        if (x instanceof ImmutableVectorN) {
            return (ImmutableVectorN) x;
        } else {
            return new ImmutableVectorN(x.getComponentsAsArray());
        }
    }

    /**
     * <p>
     * Create a vector that lies along a line given by an origin point and position
     * vector.
     * </p>
     * <ul>
     * <li>Always returns a (non null) vector.</li>
     * <li>The {@linkplain ImmutableVectorN#getDimension() dimension} of the
     * returned vector is equal to the dimension of the two input vectors.</li>
     * <li>Returns the vector <code>x0 + w dx</code></li>
     * </ul>
     *
     * @param x0 The original point
     * @param dx The direction vector along the line
     * @param w  Position parameter giving the position along the line.
     * @throws NullPointerException     If {@code x0} is null. If {@code dx} is null.
     * @throws IllegalArgumentException If {@code x0} and {@code dx} have different
     *                                  {@linkplain ImmutableVectorN#getDimension() dimensions}.
     */
    public static @Nonnull ImmutableVectorN createOnLine(
            @Nonnull final ImmutableVectorN x0,
            @Nonnull final ImmutableVectorN dx,
            final double w) {
        Objects.requireNonNull(x0, "x0");
        Objects.requireNonNull(dx, "dx");
        Matrix.requireConsistentDimensions(x0, dx);

        final int n = x0.getDimension();
        final double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = x0.elements[i] + w * dx.elements[i];
        }
        return new ImmutableVectorN(x);
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
    public static ImmutableVectorN sum(@Nonnull final ImmutableVectorN... x) {
        return new ImmutableVectorN(Vector.sumAsArray(x));
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
    public static ImmutableVectorN weightedSum(
            @Nonnull final double[] weight,
            @Nonnull final Vector[] x) {
        return new ImmutableVectorN(Vector.weightedSumAsArray(weight, x));
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

    @Override
    @Nonnull
    public ImmutableVectorN mean(@Nonnull final Matrix that) {
        return new ImmutableVectorN(meanElements(that));
    }

    @Nonnull
    @Override
    public ImmutableVectorN minus() {
        return new ImmutableVectorN(minusElements());
    }

    @Override
    @Nonnull
    public ImmutableVectorN minus(@Nonnull final Matrix that) {
        return new ImmutableVectorN(minusElements(that));
    }

    @Override
    @Nonnull
    public ImmutableVectorN plus(@Nonnull final Matrix that) {
        return new ImmutableVectorN(plusElements(that));
    }

    @Nonnull
    @Override
    public ImmutableVectorN scale(final double f) {
        return new ImmutableVectorN(scaleElements(f));
    }

}
