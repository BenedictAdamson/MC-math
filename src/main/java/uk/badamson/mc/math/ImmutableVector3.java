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
import java.util.Objects;

/**
 * <p>
 * A constant (immutable) mathematical vector or pseudo vector of
 * {@linkplain #getRows() size} (dimension) 3.
 * </p>
 */
@Immutable
public final class ImmutableVector3 implements Vector {

    /**
     * <p>
     * The 3 dimensional zero vector.
     * </p>
     */
    public static final ImmutableVector3 ZERO = new ImmutableVector3(0.0, 0.0, 0.0);

    /**
     * <p>
     * The unit direction vector point along the x axis.
     * </p>
     */
    public static final ImmutableVector3 I = new ImmutableVector3(1.0, 0.0, 0.0);

    /**
     * <p>
     * The unit direction vector point along the y axis.
     */
    public static final ImmutableVector3 J = new ImmutableVector3(0.0, 1.0, 0.0);

    /**
     * <p>
     * The unit direction vector point along the z axis.
     * </p>
     */
    public static final ImmutableVector3 K = new ImmutableVector3(0.0, 0.0, 1.0);
    private final double x;
    private final double y;
    private final double z;

    private ImmutableVector3(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * <p>
     * Create a vector from its components.
     * </p>
     * <ul>
     * <li>Always returns a (non null) vector.</li>
     * <li>This has the given values for its components.</li>
     * </ul>
     *
     * @param x The x component of this vector
     * @param y The y component of this vector
     * @param z The z component of this vector
     * @return the created vector
     */
    public static @NonNull ImmutableVector3 create(final double x, final double y, final double z) {
        return new ImmutableVector3(x, y, z);
    }

    private static void requireDimension3(final Vector vector) {
        if (vector.getDimension() != 3) {
            throw new IllegalArgumentException("Inconsistent dimension, " + vector.getDimension());
        }
    }

    /**
     * <p>
     * Calculate the sum of several 3 dimensional vectors.
     * </p>
     * </ul>
     * <li>Always returns a (non null) sum vector.</li>
     * <li>The dimension of the sum equals the dimension of the summed vectors.</li>
     * </ul>
     *
     * @param x The vectors to sum
     * @return The sum; not null
     * @throws NullPointerException     <ul>
     *                                              <li>If {@code x} is null.</li>
     *                                              <li>If {@code x} has any null elements.</li>
     *                                              </ul>
     * @throws IllegalArgumentException If the elements of {@code x} do not have the same
     *                                  {@linkplain #getDimension() dimension}.
     * @see #plus(ImmutableVector3)
     */
    public static @NonNull ImmutableVector3 sum(@NonNull final ImmutableVector3... x) {
        Objects.requireNonNull(x, "x");
        final int n = x.length;
        if (n == 0) {
            throw new IllegalArgumentException("Number of vector arguments");
        }
        Objects.requireNonNull(x[0], "x[0]");

        double sumX = 0.0;
        double sumY = 0.0;
        double sumZ = 0.0;
        for (int j = 0; j < n; ++j) {
            final ImmutableVector3 xj = x[j];
            Objects.requireNonNull(xj, "x[j]");
            sumX += xj.x;
            sumY += xj.y;
            sumZ += xj.z;
        }

        return new ImmutableVector3(sumX, sumY, sumZ);
    }

    /**
     * <p>
     * Calculate the weighted sum of several 3 dimensional vectors.
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
     * @throws IllegalArgumentException <ul>
     *                                              <li>If {@code weight} has a length of 0.</li>
     *                                              <li>If {@code weight} and {@code x} have different lengths.</li>
     *                                              </ul>
     */
    public static @NonNull ImmutableVector3 weightedSum(@NonNull final double[] weight,
                                                        @NonNull final ImmutableVector3[] x) {
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

        double sumX = 0.0;
        double sumY = 0.0;
        double sumZ = 0.0;
        for (int j = 0; j < n; ++j) {
            final double wj = weight[j];
            final ImmutableVector3 xj = x[j];
            Objects.requireNonNull(xj, "x[j]");
            sumX += wj * xj.x;
            sumY += wj * xj.y;
            sumZ += wj * xj.z;
        }

        return new ImmutableVector3(sumX, sumY, sumZ);
    }

    /**
     * <p>
     * Calculate the dot product of this vector and another 3 dimensional vector.
     * </p>
     *
     * @param that The other vector
     * @return the product
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain #getDimension() dimension} of {@code that} is
     *                                  not equal to the dimension of this.
     */
    public double dot(@NonNull final ImmutableVector3 that) {
        Objects.requireNonNull(that, "that");

        return x * that.x + y * that.y + z * that.z;
    }

    @Override
    public double dot(@NonNull final Vector that) {
        if (that instanceof ImmutableVector3) {
            return dot((ImmutableVector3) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireDimension3(that);

            return x * that.get(0) + y * that.get(1) + z * that.get(2);
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
        final ImmutableVector3 other = (ImmutableVector3) obj;
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
                && Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y)
                && Double.doubleToLongBits(z) == Double.doubleToLongBits(other.z);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public double get(final int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            default:
                throw new IndexOutOfBoundsException("i " + i);
        }
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
    public int getColumns() {
        return 1;
    }

    @Override
    public int getDimension() {
        return 3;
    }

    @Override
    public int getRows() {
        return 3;
    }

    private double getScale() {
        return Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(z);
        result = prime * result + (int) (temp ^ temp >>> 32);
        return result;
    }

    @Override
    public double magnitude() {
        final double scale = getScale();
        if (!Double.isFinite(scale) || scale < Double.MIN_NORMAL) {
            return scale;
        } else {
            final double r = 1.0 / scale;
            final double xScaled = x * r;
            final double yScaled = y * r;
            final double zScaled = z * r;
            return Math.sqrt(xScaled * xScaled + yScaled * yScaled + zScaled * zScaled) * scale;
        }
    }

    @Override
    public double magnitude2() {
        /* Use a scaling value to avoid overflow. */
        final double scale = getScale();
        final double scale2 = scale * scale;
        if (!Double.isFinite(scale) || scale < Double.MIN_NORMAL) {
            return scale2;
        } else {
            final double r = 1.0 / scale;
            final double xScaled = x * r;
            final double yScaled = y * r;
            final double zScaled = z * r;
            return (xScaled * xScaled + yScaled * yScaled + zScaled * zScaled) * scale2;
        }
    }

    /**
     * <p>
     * Create the vector that is the mean of this vector with another vector.
     * </p>
     * <ul>
     * <li>Always returns a (non null) vector.</li>
     * <li>The {@linkplain ImmutableVector3#getDimension() dimension} of the mean
     * vector is equal to the dimension of this vector.</li>
     * </ul>
     *
     * @param that The vector to take the mean with
     * @return the mean vector
     * @throws NullPointerException     If {@code that} is null.
     * @throws IllegalArgumentException If the {@linkplain ImmutableVector3#getDimension() dimension} of
     *                                  }@code that} is not equal to ehe dimension of this vector.
     */
    public @NonNull ImmutableVector3 mean(@NonNull final ImmutableVector3 that) {
        Objects.requireNonNull(that, "that");
        return new ImmutableVector3(0.5 * (x + that.x), 0.5 * (y + that.y), 0.5 * (z + that.z));
    }

    @Override
    public @NonNull ImmutableVector3 mean(@NonNull final Vector that) {
        if (that instanceof ImmutableVector3) {
            return mean((ImmutableVector3) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireDimension3(that);
            return new ImmutableVector3(0.5 * (x + that.get(0)), 0.5 * (y + that.get(1)), 0.5 * (z + that.get(2)));
        }
    }

    @Override
    public @NonNull ImmutableVector3 minus() {
        return new ImmutableVector3(-x, -y, -z);
    }

    /**
     * <p>
     * Create the vector that is a given vector subtracted from this vector; the
     * difference between this vector and another.
     * </p>
     * <ul>
     * <li>Always returns a (non null) vector.</li>
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
    public @NonNull ImmutableVector3 minus(@NonNull final ImmutableVector3 that) {
        Objects.requireNonNull(that, "that");
        return new ImmutableVector3(x - that.x, y - that.y, z - that.z);
    }

    @Override
    public @NonNull ImmutableVector3 minus(@NonNull final Vector that) {
        if (that instanceof ImmutableVector3) {
            return minus((ImmutableVector3) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireDimension3(that);
            return new ImmutableVector3(x - that.get(0), y - that.get(1), z - that.get(2));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException     If {@code x} is null.
     * @throws IllegalArgumentException If {@code x} is not null, because a 3 dimensional vector can not
     *                                  be used to matrix-multiply a vector.
     */
    @Override
    public @NonNull Vector multiply(@NonNull final Vector x) {
        Objects.requireNonNull(x, "x");
        throw new IllegalArgumentException("Can not use a 3 dimensional vector to matrix-multipley a vector");
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
     * @see #sum(ImmutableVector3...)
     */
    public @NonNull ImmutableVector3 plus(@NonNull final ImmutableVector3 that) {
        Objects.requireNonNull(that, "that");
        return new ImmutableVector3(x + that.x, y + that.y, z + that.z);
    }

    @Override
    public @NonNull ImmutableVector3 plus(@NonNull final Vector that) {
        if (that instanceof ImmutableVector3) {
            return plus((ImmutableVector3) that);
        } else {
            Objects.requireNonNull(that, "that");
            requireDimension3(that);
            return new ImmutableVector3(x + that.get(0), y + that.get(1), z + that.get(2));
        }
    }

    @Override
    public @NonNull ImmutableVector3 scale(final double f) {
        return new ImmutableVector3(x * f, y * f, z * f);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

}
