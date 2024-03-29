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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * <p>
 * A number system that extends the complex numbers to four dimensions.
 * </p>
 */
@Immutable
public final class Quaternion {

    /**
     * <p>
     * The quaternion that has a value of zero for each of its components.
     * </p>
     */
    public static final Quaternion ZERO = new Quaternion(0, 0, 0, 0);

    /**
     * <p>
     * The real quaternion of unit {@linkplain #norm() norm}.
     * </p>
     */
    public static final Quaternion ONE = new Quaternion(1, 0, 0, 0);

    /**
     * <p>
     * The quaternion having a unit {@linkplain #getB() i component} with all other
     * components zero.
     * </p>
     */
    public static final Quaternion I = new Quaternion(0, 1, 0, 0);

    /**
     * <p>
     * The quaternion having unit {@linkplain #getC() j component} with all other
     * components zero.
     * </p>
     */
    public static final Quaternion J = new Quaternion(0, 0, 1, 0);

    /**
     * <p>
     * The quaternion having unit {@linkplain #getD() k component} with all other
     * components zero.
     * </p>
     */
    public static final Quaternion K = new Quaternion(0, 0, 0, 1);

    private static final double EXP_TOL = Math.pow(Double.MIN_NORMAL, 1.0 / 6.0) * 840.0;
    private final double a;
    private final double b;
    private final double c;
    private final double d;

    private Quaternion(final double a, final double b, final double c, final double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * <p>
     * Create a quaternion with given components.
     * </p>
     *
     * @param a The real-number component of this quaternion.
     * @param b The <b>i</b> component of this quaternion.
     * @param c The <b>j</b> component of this quaternion.
     * @param d The <b>k</b> component of this quaternion.
     */
    @Nonnull
    public static Quaternion create(final double a, final double b, final double c, final double d) {
        return new Quaternion(a, b, c, d);
    }

    /**
     * <p>
     * Create a quaternion that is the conjugate of this quaternion.
     * </p>
     */
    @Nonnull
    public Quaternion conjugate() {
        return new Quaternion(a, -b, -c, -d);
    }

    /**
     * <p>
     * Create a quaternion that is the conjugation of a quaternion by this
     * quaternion.
     * </p>
     * <p>
     * That is, if this is <var>q</var> and the other quaternion is <var>p</var>,
     * the method computes <var>q</var><var>p</var><var>q</var><sup>-1</sup>.
     * </p>
     *
     * @param p The quaternion to be conjugated.
     * @throws NullPointerException If {@code p} is null
     */
    @Nonnull
    public Quaternion conjugation(@Nonnull final Quaternion p) {
        Objects.requireNonNull(p);
        return product(p).product(conjugate()).scale(1.0 / norm2());
    }

    /**
     * <p>
     * The distance between this quaternion and another
     * </p>
     * <ul>
     * <li>The distance is nominally equal to
     * <code>this.minus(that).norm()</code>.</li>
     * </ul>
     *
     * @param that The other quaternion
     * @throws NullPointerException If {@code that} is null.
     */
    public double distance(@Nonnull final Quaternion that) {
        return minus(that).norm();
    }

    /**
     * <p>
     * Calculate the dot product (inner product) of this quaternion with another
     * quaternion.
     * </p>
     *
     * @param that The other quaternion with which to calculate the dot product.
     * @throws NullPointerException If {@code that} is null.
     */
    public double dot(@Nonnull final Quaternion that) {
        Objects.requireNonNull(that);
        return a * that.a + b * that.b + c * that.c + d * that.d;
    }

    /**
     * <p>
     * Whether this object is <dfn>equivalent</dfn> another object.
     * </p>
     * <p>
     * The {@link Quaternion} class has <i>value semantics</i>: this object is
     * equivalent to another object if, and only if, the other object is also a
     * {@link Quaternion} object, and the two objects have equivalent attributes.
     * </p>
     */
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
        final Quaternion other = (Quaternion) obj;
        return Double.doubleToLongBits(a) == Double.doubleToLongBits(other.a)
                && Double.doubleToLongBits(b) == Double.doubleToLongBits(other.b)
                && Double.doubleToLongBits(c) == Double.doubleToLongBits(other.c)
                && Double.doubleToLongBits(d) == Double.doubleToLongBits(other.d);
    }

    /**
     * <p>
     * Create a quaternion that is the exponential of this quaternion.
     * </p>
     *
     * @return the exponential
     * @see Math#exp(double)
     */
    @Nonnull
    public Quaternion exp() {
        final double ea = Math.exp(a);
        final Quaternion v = vector();
        final double vn = v.norm();
        final double cos = Math.cos(vn);
        final double sinTerm;
        if (EXP_TOL < Math.abs(vn)) {
            sinTerm = Math.sin(vn) / vn;
        } else {
            /* Special handling for scalars and near scalars. */
            final double x2 = vn * vn;
            sinTerm = 1.0 - x2 * (1.0 / 6.0) * (1.0 - x2 * 0.05);
        }
        final Quaternion c1 = new Quaternion(ea * cos, 0, 0, 0);
        final Quaternion s1 = v.scale(ea * sinTerm);
        return c1.plus(s1);
    }

    /**
     * <p>
     * The real-number component of this quaternion.
     * </p>
     * <p>
     * Its <i>scalar part</i>.
     * </p>
     */
    public double getA() {
        return a;
    }

    /**
     * <p>
     * The <b>i</b> component of this quaternion.
     * </p>
     */
    public double getB() {
        return b;
    }

    /**
     * <p>
     * The <b>j</b> component of this quaternion.
     * </p>
     */
    public double getC() {
        return c;
    }

    /**
     * <p>
     * The <b>k</b> component of this quaternion.
     * </p>
     */
    public double getD() {
        return d;
    }

    private double getScale() {
        final double s = Double.max(Double.max(Math.abs(a), Math.abs(b)), Double.max(Math.abs(c), Math.abs(d)));
        if (Double.isFinite(s) && Double.MIN_NORMAL <= s) {
            return s;
        } else {
            /* Must accept overflow or underflow. */
            return 1.0;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Double.hashCode(a);
        result = prime * result + Double.hashCode(b);
        result = prime * result + Double.hashCode(c);
        result = prime * result + Double.hashCode(d);
        return result;
    }

    /**
     * <p>
     * Create a quaternion that is the natural logarithm of this quaternion.
     * </p>
     * <p>
     * The method takes care to properly handle quaternions with components that are
     * large, not numbers, or which differ greatly in magnitude, and quaternions
     * that are close to {@linkplain #ZERO zero}, or which have a small
     * {@linkplain #vector() vector} part.
     * </p>
     *
     * @see Math#log(double)
     */
    @Nonnull
    public Quaternion log() {
        final Quaternion v = vector();
        final double n = norm();
        final Quaternion sTerm = new Quaternion(Math.log(n), 0, 0, 0);
        final Quaternion vTerm = v.versor().scale(Math.acos(a / n) / n);
        return sTerm.plus(vTerm);
    }

    /**
     * <p>
     * Create the quaternion that is the mean of this quaternion with another
     * quaternion.
     * </p>
     *
     * @param that The quaternion to take the mean with
     * @throws NullPointerException If {@code that} is null.
     */
    @Nonnull
    public Quaternion mean(@Nonnull final Quaternion that) {
        Objects.requireNonNull(that);
        return new Quaternion((a + that.a) * 0.5, (b + that.b) * 0.5, (c + that.c) * 0.5, (d + that.d) * 0.5);
    }

    /**
     * <p>
     * Create the quaternion that is a given quaternion subtracted from this
     * quaternion; the difference of this quaternion and another.
     * </p>
     *
     * @param that The other quaternion
     * @throws NullPointerException If {@code that} is null.
     */
    @Nonnull
    public Quaternion minus(@Nonnull final Quaternion that) {
        Objects.requireNonNull(that);
        return new Quaternion(a - that.a, b - that.b, c - that.c, d - that.d);
    }

    /**
     * <p>
     * The norm of this quaternion.
     * </p>
     * <p>
     * The method takes care to properly handle quaternions with components that are
     * large, not numbers, or which differ greatly in magnitude.
     * </p>
     */
    public double norm() {
        final double s = getScale();
        return Math.sqrt(sn2(s)) * s;
    }

    /**
     * <p>
     * The square of the {@linkplain #norm() norm} of this quaternion.
     * </p>
     * <p>
     * The method takes care to properly handle quaternions with components that are
     * large, not numbers, or which differ greatly in magnitude.
     * </p>
     */
    public double norm2() {
        final double s = getScale();
        return sn2(s) * (s * s);
    }

    private double sn2(double s) {
        final double f = 1.0 / s;
        final double as = a * f;
        final double bs = b * f;
        final double cs = c * f;
        final double ds = d * f;
        return as * as + bs * bs + cs * cs + ds * ds;
    }

    /**
     * <p>
     * Create the quaternion that is a given quaternion added to this quaternion;
     * the sum of this quaternion and another.
     * </p>
     *
     * @param that The other quaternion
     * @throws NullPointerException If {@code that} is null.
     */
    @Nonnull
    public Quaternion plus(@Nonnull final Quaternion that) {
        Objects.requireNonNull(that);
        return new Quaternion(a + that.a, b + that.b, c + that.c, d + that.d);
    }

    /**
     * <p>
     * Create a quaternion that is this quaternion raised to a given real power.
     * </p>
     * <p>
     * The method takes care to properly handle quaternions with components that are
     * large, not numbers, or which differ greatly in magnitude, and quaternions
     * that are close to {@linkplain #ZERO zero}, or which have a small
     * {@linkplain #vector() vector} part.
     * </p>
     *
     * @param p The power to raise this quaternion to
     * @see Math#pow(double, double)
     */
    @Nonnull
    public Quaternion pow(final double p) {
        final double n = norm();
        final Quaternion v = vector();
        final Quaternion direction = v.versor();
        final double y = direction.conjugate().product(v).getA();
        final double theta = Math.atan2(y, a);
        return direction.scale(theta * p).exp().scale(Math.pow(n, p));
    }

    /**
     * <p>
     * Create a quaternion that is the Hamilton product of this quaternion and a
     * given quaternion.
     * </p>
     *
     * @param that the other quaternion
     * @throws NullPointerException If {@code that} is null.
     */
    @Nonnull
    public Quaternion product(@Nonnull final Quaternion that) {
        Objects.requireNonNull(that, "that");
        return new Quaternion(a * that.a - b * that.b - c * that.c - d * that.d,
                a * that.b + b * that.a + c * that.d - d * that.c, a * that.c - b * that.d + c * that.a + d * that.b,
                a * that.d + b * that.c - c * that.b + d * that.a);
    }

    /**
     * <p>
     * Create a quaternion that is the reciprocal of this quaternion.
     * </p>
     */
    @Nonnull
    public Quaternion reciprocal() {
        return conjugate().scale(1.0 / norm2());
    }

    /**
     * <p>
     * Create a quaternion that is this quaternion scaled by a given scalar.
     * </p>
     *
     * @param f the scalar
     */
    @Nonnull
    public Quaternion scale(final double f) {
        return new Quaternion(a * f, b * f, c * f, d * f);
    }

    @Override
    @Nonnull
    public String toString() {
        return a + "+" + b + "i+" + c + "j+" + d + "k";
    }

    /**
     * <p>
     * Create a quaternion that is the vector part of this quaternion.
     * </p>
     */
    @Nonnull
    public Quaternion vector() {
        return new Quaternion(0, b, c, d);
    }

    /**
     * <p>
     * Create a versor (a quaternion that has unit {@linkplain #norm() norm}) that
     * points in the same direction as this quaternion.
     * </p>
     * <p>
     * The method takes care to properly handle quaternions with components that are
     * large, not numbers, or which differ greatly in magnitude, and quaternions
     * that are close to {@linkplain #ZERO zero}. In the case of a quaternion very
     * close to zero, the method returns zero as the versor (rather than a versor
     * with {@linkplain Double#isFinite(double) non finite} components).
     * </p>
     */
    @Nonnull
    public Quaternion versor() {
        final double s = getScale();
        final double f1 = 1.0 / s;
        final double hypot = Math.sqrt(sn2(s));
        if (Double.MIN_NORMAL < hypot) {
            final double f2 = f1 / hypot;
            return scale(f2);
        } else {
            return ZERO;
        }
    }
}
