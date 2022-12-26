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
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * <p>
 * A constant (immutable) mathematical vector or pseudo vector of
 * {@linkplain #getRows() size} (dimension) 1.
 * </p>
 */
@Immutable
public final class ImmutableVector1 implements Vector {

    /**
     * <p>
     * The 1 dimensional zero vector.
     * </p>
     */
    public static final ImmutableVector1 ZERO = new ImmutableVector1(0.0);

    /**
     * <p>
     * The unit direction vector point along the x axis.
     * </p>
     */
    public static final ImmutableVector1 I = new ImmutableVector1(1.0);
    private final double x;

    private ImmutableVector1(final double x) {
        this.x = x;
    }

    /**
     * <p>
     * Create a vector from its component.
     * </p>
     *
     * @param x The x component of this vector
     * @return the created vector
     */
    @Nonnull
    public static ImmutableVector1 create(final double x) {
        return new ImmutableVector1(x);
    }

    private static void requireDimension1(@Nonnull final Vector vector) {
        final var dimension = vector.getDimension();
        if (dimension != 1) {
            throw new IllegalArgumentException("Inconsistent dimension, " + dimension);
        }
    }

    /**
     * <p>
     * Calculate the sum of several 1 dimensional vectors.
     * </p>
     * <ul>
     * <li>The dimension of the sum equals the dimension of the summed vectors.</li>
     * </ul>
     *
     * @param x The vectors to sum
     * @return The sum; not null
     * @throws NullPointerException   If {@code x} is null. If {@code x} has any null elements.
     * @throws IllegalArgumentException If the elements of {@code x} do not have the same
     *                                  {@linkplain #getDimension() dimension}.
     * @see #plus(ImmutableVector1)
     */
    @Nonnull
    public static ImmutableVector1 sum(@Nonnull final ImmutableVector1... x) {
        Objects.requireNonNull(x, "x");

        double sumX = 0.0;
        for (final ImmutableVector1 xj : x) {
            Objects.requireNonNull(xj, "x[j]");
            sumX += xj.x;
        }

        return new ImmutableVector1(sumX);
    }

    /**
     * <p>
     * Calculate the weighted sum of several 1 dimensional vectors.
     * </p>
     *
     * @param weight The weights to apply; {@code weight[i]} is the weight for vector
     *               {@code x[i]}.
     * @param x      The vectors to sum
     * @return The weighted sum; not null
     * @throws NullPointerException     <ul>
     *                                              <li>If {@code weight} is null.</li>
     *                                              <li>If {@code x} is null.</li>
     *                                              <li>If {@code x} has any null elements.</li>
     *                                              </ul>
     * @throws IllegalArgumentException <ul>
     *                                              <li>If {@code weight} has a length of 0.</li>
     *                                              <li>If {@code weight} and {@code x} have different lengths.</li>
     *                                              </ul>
     */
    @Nonnull
    public static ImmutableVector1 weightedSum(
            @Nonnull final double[] weight,
            @Nonnull final ImmutableVector1[] x
    ) {
        Objects.requireNonNull(x, "x");
        Vector.requireValidWeights(weight, x);

        final int n = weight.length;
        double sumX = 0.0;
        for (int j = 0; j < n; ++j) {
            final double wj = weight[j];
            final ImmutableVector1 xj = x[j];
            Objects.requireNonNull(xj, "x[j]");
            sumX += wj * xj.x;
        }

        return new ImmutableVector1(sumX);
    }

    /**
     * <p>
     * Calculate the dot product of this vector and another 1 dimensional vector.
     * </p>
     *
     * @param that The other vector
     * @return the product
     * @throws NullPointerException     If {@code that} is null.
     */
    public double dot(@Nonnull final ImmutableVector1 that) {
        return x * that.x;
    }

    @Override
    public double dot(@Nonnull final Vector that) {
        if (that instanceof ImmutableVector1) {
            return dot((ImmutableVector1) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireDimension1(that);

            return x * that.get(0);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImmutableVector1 other = (ImmutableVector1) obj;
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public double get(final int i) {
        if (i == 0) {
            return x;
        }
        throw new IndexOutOfBoundsException("i " + i);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public double get(final int i, final int j) {
        if (j != 0) {
            throw new IndexOutOfBoundsException("j " + j);
        }
        return get(i);
    }

    @Override
    @Nonnegative
    public int getColumns() {
        return 1;
    }

    @Override
    @Nonnegative
    public int getDimension() {
        return 1;
    }

    @Override
    @Nonnegative
    public int getRows() {
        return 1;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x);
    }

    @Override
    public double magnitude() {
        return Math.abs(x);
    }

    @Override
    public double magnitude2() {
        return x * x;
    }

    /**
     * <p>
     * Create the vector that is the mean of this vector with another vector.
     * </p>
     * <ul>
     * <li>Always returns a (non null) vector.</li>
     * <li>The {@linkplain ImmutableVector1#getDimension() dimension} of the mean
     * vector is equal to the dimension of this vector.</li>
     * </ul>
     *
     * @param that The vector to take the mean with
     * @return the mean vector
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain ImmutableVector1#getDimension() dimension} of
     *                                  }@code that} is not equal to the dimension of this vector.
     */
    @Nonnull
    public ImmutableVector1 mean(@Nonnull final ImmutableVector1 that) {
        Objects.requireNonNull(that, "that");
        return new ImmutableVector1(0.5 * (x + that.x));
    }

    @Override
    @Nonnull
    public ImmutableVector1 mean(@Nonnull final Vector that) {
        if (that instanceof ImmutableVector1) {
            return mean((ImmutableVector1) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireDimension1(that);
            return new ImmutableVector1(0.5 * (x + that.get(0)));
        }
    }

    @Override
    @Nonnull
    public ImmutableVector1 minus() {
        return new ImmutableVector1(-x);
    }

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
     * @return the difference vector
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain #getDimension() dimension} of {@code that} is
     *                                  not equal to the dimension of this.
     */
    @Nonnull
    public ImmutableVector1 minus(@Nonnull final ImmutableVector1 that) {
        Objects.requireNonNull(that, "that");
        return new ImmutableVector1(x - that.x);
    }

    @Override
    @Nonnull
    public ImmutableVector1 minus(@Nonnull final Vector that) {
        if (that instanceof ImmutableVector1) {
            return minus((ImmutableVector1) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireDimension1(that);
            return new ImmutableVector1(x - that.get(0));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException     If {@code x} is null.
     * @throws IllegalArgumentException If {@code x} is not null, because a 3 dimensional vector can not
     *                                  be used to matrix-multiply a vector.
     */
    @Nonnull
    @Override
    public Vector multiply(@Nonnull final Vector x) {
        Objects.requireNonNull(x, "x");
        throw new IllegalArgumentException("Can not use a 1 dimensional vector to matrix-multiply a vector");
    }

    /**
     * <p>
     * Create the vector that is a given vector added to this vector; the sum of
     * this vector and another.
     * </p>
     * <ul>
     * <li>Always returns a (non null) vector.</li>
     * <li>The sum vector has the same {@linkplain #getDimension() dimension} as
     * this vector.</li>
     * <li>The {@linkplain #get(int) components} of the sum vector are the sum with
     * the corresponding component of this vector.</li>
     * </ul>
     *
     * @param that The other vector
     * @return the sum vector
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain #getDimension() dimension} of {@code that} is
     *                                  not equal to the dimension of this.
     * @see #sum(ImmutableVector1...)
     */
    @Nonnull
    public ImmutableVector1 plus(@Nonnull final ImmutableVector1 that) {
        Objects.requireNonNull(that, "that");
        return new ImmutableVector1(x + that.x);
    }

    @Override
    @Nonnull
    public ImmutableVector1 plus(@Nonnull final Vector that) {
        if (that instanceof ImmutableVector1) {
            return plus((ImmutableVector1) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireDimension1(that);
            return new ImmutableVector1(x + that.get(0));
        }
    }

    @Nonnull
    @Override
    public ImmutableVector1 scale(final double f) {
        return new ImmutableVector1(x * f);
    }

    @Override
    public String toString() {
        return "(" + x + ")";
    }

}
