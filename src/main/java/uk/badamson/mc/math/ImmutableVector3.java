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
     *
     * @param x The x component of this vector
     * @param y The y component of this vector
     * @param z The z component of this vector
     */
    @Nonnull
    public static ImmutableVector3 create(final double x, final double y, final double z) {
        return new ImmutableVector3(x, y, z);
    }

    private static void requireDimension3(final Vector vector) {
        var dimension = vector.getDimension();
        if (dimension != 3) {
            throw new IllegalArgumentException("Inconsistent dimension, " + dimension);
        }
    }

    /**
     * <p>
     * Calculate the sum of several 3 dimensional vectors.
     * </p>
     *
     * @param x The vectors to sum
     * @return The sum
     * @throws NullPointerException If {@code x} is null. If {@code x} has any null elements.
     * @see #plus(ImmutableVector3)
     */
    @Nonnull
    public static ImmutableVector3 sum(@Nonnull final ImmutableVector3... x) {
        Objects.requireNonNull(x, "x");

        double sumX = 0.0;
        double sumY = 0.0;
        double sumZ = 0.0;
        for (final ImmutableVector3 xj : x) {
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
     * @return The weighted sum
     * @throws NullPointerException     If {@code weight} is null. If {@code x} is null. If {@code x} has any null elements.
     * @throws IllegalArgumentException If {@code weight} has a length of 0. If {@code weight} and {@code x} have different lengths.
     */
    public static @Nonnull ImmutableVector3 weightedSum(@Nonnull final double[] weight,
                                                        @Nonnull final ImmutableVector3[] x) {
        Objects.requireNonNull(x, "x");
        Vector.requireValidWeights(weight, x);

        final int n = weight.length;
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
    public double dot(@Nonnull final ImmutableVector3 that) {
        Objects.requireNonNull(that, "that");

        return x * that.x + y * that.y + z * that.z;
    }

    @Override
    public double dot(@Nonnull final Vector that) {
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
    public double get(@Nonnegative final int i) {
        return switch (i) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> throw new IndexOutOfBoundsException("i " + i);
        };
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
        return 3;
    }

    @Override
    @Nonnegative
    public int getRows() {
        return 3;
    }

    @Nonnull
    @Override
    public double[] getComponentsAsArray() {
        return new double[]{x, y, z};
    }

    @Nonnegative
    private double getScale() {
        return Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Double.hashCode(x);
        result = prime * result + Double.hashCode(y);
        result = prime * result + Double.hashCode(z);
        return result;
    }

    @Override
    public double magnitude() {
        final double scale = getScale();
        if (!Double.isFinite(scale) || scale < Double.MIN_NORMAL) {
            return scale;
        } else {
            return Math.sqrt(m2(scale)) * scale;
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
            return m2(scale) * scale2;
        }
    }

    private double m2(double scale) {
        final double r = 1.0 / scale;
        final double xScaled = x * r;
        final double yScaled = y * r;
        final double zScaled = z * r;
        return xScaled * xScaled + yScaled * yScaled + zScaled * zScaled;
    }

    /**
     * <p>
     * Create the vector that is the mean of this vector with another vector.
     * </p>
     *
     * @throws NullPointerException If {@code that} is null.
     */
    @Nonnull
    public ImmutableVector3 mean(@Nonnull final ImmutableVector3 that) {
        Objects.requireNonNull(that);
        return new ImmutableVector3(0.5 * (x + that.x), 0.5 * (y + that.y), 0.5 * (z + that.z));
    }

    @Nonnull
    @Override
    public ImmutableVector3 mean(@Nonnull final Matrix that) {
        if (that instanceof ImmutableVector3) {
            return mean((ImmutableVector3) that);
        } else {
            Objects.requireNonNull(that, "that");
            Matrix.requireConsistentDimensions(this, that);
            return new ImmutableVector3(0.5 * (x + that.get(0, 0)), 0.5 * (x + that.get(1, 0)), 0.5 * (x + that.get(2, 0)));
        }
    }

    @Override
    @Nonnull
    public ImmutableVector3 minus() {
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
     * @throws NullPointerException If {@code that} is null.
     */
    @Nonnull
    public ImmutableVector3 minus(@Nonnull final ImmutableVector3 that) {
        Objects.requireNonNull(that);
        return new ImmutableVector3(x - that.x, y - that.y, z - that.z);
    }

    @Override
    public @Nonnull ImmutableVector3 minus(@Nonnull final Matrix that) {
        if (that instanceof ImmutableVector3) {
            return minus((ImmutableVector3) that);
        } else {
            Objects.requireNonNull(that);
            Matrix.requireConsistentDimensions(this, that);
            return new ImmutableVector3(x - that.get(0, 0), y - that.get(1, 0), z - that.get(2, 0));
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
        Objects.requireNonNull(x);
        throw new IllegalArgumentException("Can not use a 3 dimensional vector to matrix-multiply a vector");
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
     * @throws NullPointerException If {@code that} is null.
     * @see #sum(ImmutableVector3...)
     */
    @Nonnull
    public ImmutableVector3 plus(@Nonnull final ImmutableVector3 that) {
        Objects.requireNonNull(that);
        return new ImmutableVector3(x + that.x, y + that.y, z + that.z);
    }

    @Override
    @Nonnull
    public ImmutableVector3 plus(@Nonnull final Matrix that) {
        if (that instanceof ImmutableVector3) {
            return plus((ImmutableVector3) that);
        } else {
            Objects.requireNonNull(that);
            Matrix.requireConsistentDimensions(this, that);
            return new ImmutableVector3(x + that.get(0, 0), y + that.get(1, 0), z + that.get(2, 0));
        }
    }

    @Nonnull
    @Override
    public ImmutableVector3 scale(final double f) {
        return new ImmutableVector3(x * f, y * f, z * f);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

}
