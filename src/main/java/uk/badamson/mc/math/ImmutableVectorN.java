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
        requireConsistentDimensions(x0, dx);

        final int n = x0.getDimension();
        final double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = x0.elements[i] + w * dx.elements[i];
        }
        return new ImmutableVectorN(x);
    }

    private static void requireConsistentDimensions(final Vector v1, final Vector v2) {
        var dimension1 = v1.getDimension();
        var dimension2 = v2.getDimension();
        if (dimension1 != dimension2) {
            throw new IllegalArgumentException(
                    "Inconsistent dimensions, " + dimension1 + ", " + dimension2);
        }
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
     * @see #plus(ImmutableVectorN)
     */
    @Nonnull
    public static ImmutableVectorN sum(@Nonnull final ImmutableVectorN... x) {
        Objects.requireNonNull(x, "x");
        final int n = x.length;
        if (n == 0) {
            throw new IllegalArgumentException("Number of vector arguments");
        }
        Objects.requireNonNull(x[0], "x[0]");

        final int d = x[0].getDimension();
        final double[] sum = new double[d];
        for (final ImmutableVectorN xj : x) {
            Objects.requireNonNull(xj, "x[j]");
            if (xj.getDimension() != d) {
                throw new IllegalArgumentException("Inconsistent dimension " + d + ", " + xj.getDimension());
            }

            for (int i = 0; i < d; ++i) {
                sum[i] += xj.get(i);
            }
        }

        return new ImmutableVectorN(sum);
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
            @Nonnull final ImmutableVectorN[] x) {
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
            final ImmutableVectorN xj = x[j];
            Objects.requireNonNull(xj, "x[j]");
            if (xj.getDimension() != d) {
                throw new IllegalArgumentException("Inconsistent dimension " + d + ", " + xj.getDimension());
            }

            for (int i = 0; i < d; ++i) {
                sum[i] += wj * xj.get(i);
            }
        }

        return new ImmutableVectorN(sum);
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
    public double dot(@Nonnull final ImmutableVectorN that) {
        Objects.requireNonNull(that, "that");
        requireConsistentDimensions(this, that);

        double d = 0.0;
        for (int i = 0, n = elements.length; i < n; ++i) {
            d += elements[i] * that.elements[i];
        }
        return d;
    }

    @Override
    public double dot(@Nonnull final Vector that) {
        if (that instanceof ImmutableVectorN) {
            return dot((ImmutableVectorN) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireConsistentDimensions(this, that);
            double d = 0.0;
            for (int i = 0, n = elements.length; i < n; ++i) {
                d += elements[i] * that.get(i);
            }
            return d;
        }
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

    @Nonnegative
    private double getScale() {
        double scale = 0.0;
        for (final double xI : elements) {
            scale = Math.max(scale, Math.abs(xI));
        }
        return scale;
    }

    /**
     * <p>
     * The magnitude of this vector.
     * </p>
     */
    @Override
    public double magnitude() {
        final double scale = getScale();
        if (!Double.isFinite(scale) || scale < Double.MIN_NORMAL) {
            return scale;
        } else {
            final double r = 1.0 / scale;
            double m2 = 0.0;
            for (final double xI : elements) {
                final double xIScaled = xI * r;
                m2 += xIScaled * xIScaled;
            }
            return Math.sqrt(m2) * scale;
        }
    }

    /**
     * <p>
     * The square of the magnitude of this vector.
     * </p>
     * <p>
     * The method takes care to properly handle vectors with components that are
     * large, not numbers, or which differ greatly in magnitude. It is otherwise
     * similar to the {@linkplain #dot(ImmutableVectorN) dot product} of the vector
     * with itself.
     * </p>
     */
    @Override
    public double magnitude2() {
        /* Use a scaling value to avoid overflow. */
        final double scale = getScale();
        final double scale2 = scale * scale;
        if (!Double.isFinite(scale) || scale < Double.MIN_NORMAL) {
            return scale2;
        } else {
            final double r = 1.0 / scale;
            double m2 = 0.0;
            for (final double xI : elements) {
                final double xIScaled = xI * r;
                m2 += xIScaled * xIScaled;
            }
            return m2 * scale2;
        }
    }

    /**
     * <p>
     * Create the vector that is the mean of this vector with another vector.
     * </p>
     * <ul>
     * <li>The {@linkplain ImmutableVectorN#getDimension() dimension} of the mean
     * vector is equal to the dimension of this vector.</li>
     * </ul>
     *
     * @param that The vector to take the mean with
     * @return the mean vector
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain ImmutableVectorN#getDimension() dimension} of
     *                                  {}}@code that} is not equal to ehe dimension of this vector.
     */
    @Nonnull
    public ImmutableVectorN mean(@Nonnull final ImmutableVectorN that) {
        Objects.requireNonNull(that, "that");
        requireConsistentDimensions(this, that);
        final int n = elements.length;
        final double[] mean = new double[n];
        for (int i = 0; i < n; i++) {
            mean[i] = (elements[i] + that.elements[i]) * 0.5;
        }
        return new ImmutableVectorN(mean);
    }

    @Override
    @Nonnull
    public ImmutableVectorN mean(@Nonnull final Vector that) {
        if (that instanceof ImmutableVectorN) {
            return mean((ImmutableVectorN) that);
        } else {
            Objects.requireNonNull(that);
            requireConsistentDimensions(this, that);
            final int n = elements.length;
            final double[] mean = new double[n];
            for (int i = 0; i < n; i++) {
                mean[i] = (elements[i] + that.get(i)) * 0.5;
            }
            return new ImmutableVectorN(mean);
        }
    }

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
    public ImmutableVectorN minus() {
        final int n = elements.length;
        final double[] minus = new double[n];
        for (int i = 0; i < n; ++i) {
            minus[i] = -elements[i];
        }
        return new ImmutableVectorN(minus);
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
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain #getDimension() dimension} of {@code that} is
     *                                  not equal to the dimension of this.
     */
    @Nonnull
    public ImmutableVectorN minus(@Nonnull final ImmutableVectorN that) {
        Objects.requireNonNull(that, "that");
        requireConsistentDimensions(this, that);

        final int n = elements.length;
        final double[] minus = new double[n];
        for (int i = 0; i < n; ++i) {
            minus[i] = elements[i] - that.elements[i];
        }
        return new ImmutableVectorN(minus);
    }

    @Override
    @Nonnull
    public ImmutableVectorN minus(@Nonnull final Vector that) {
        if (that instanceof ImmutableVectorN) {
            return minus((ImmutableVectorN) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireConsistentDimensions(this, that);

            final int n = elements.length;
            final double[] minus = new double[n];
            for (int i = 0; i < n; ++i) {
                minus[i] = elements[i] - that.get(i);
            }
            return new ImmutableVectorN(minus);
        }
    }

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
     * @see #sum(ImmutableVectorN...)
     */
    @Nonnull
    public ImmutableVectorN plus(@Nonnull final ImmutableVectorN that) {
        Objects.requireNonNull(that, "that");
        requireConsistentDimensions(this, that);

        final int n = elements.length;
        final double[] minus = new double[n];
        for (int i = 0; i < n; ++i) {
            minus[i] = elements[i] + that.elements[i];
        }
        return new ImmutableVectorN(minus);
    }

    @Override
    @Nonnull
    public ImmutableVectorN plus(@Nonnull final Vector that) {
        if (that instanceof ImmutableVectorN) {
            return plus((ImmutableVectorN) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireConsistentDimensions(this, that);

            final int n = elements.length;
            final double[] minus = new double[n];
            for (int i = 0; i < n; ++i) {
                minus[i] = elements[i] + that.get(i);
            }
            return new ImmutableVectorN(minus);
        }
    }

    /**
     * <p>
     * Create a vector that is this vector scaled by a given scalar.
     * </p>
     * <ul>
     * <li>Always returns a (non null) vector.
     * <li>
     * <li>The {@linkplain ImmutableVectorN#getDimension() dimension} of the scaled
     * vector is equal to the dimension of this vector.</li>
     * </ul>
     *
     * @param f the scalar
     * @return the scaled vector
     */
    @Nonnull
    @Override
    public ImmutableVectorN scale(final double f) {
        final int n = elements.length;
        final double[] s = new double[n];
        for (int i = 0; i < n; ++i) {
            s[i] = elements[i] * f;
        }
        return new ImmutableVectorN(s);
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }

}
