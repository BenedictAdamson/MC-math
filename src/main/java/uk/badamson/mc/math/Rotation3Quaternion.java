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
 * A rotation in 3D space represented by a versor (a {@linkplain Quaternion
 * quaternion} of unit {@linkplain Quaternion#norm() norm}).
 * </p>
 */
@Immutable
public final class Rotation3Quaternion implements Rotation3 {

    /**
     * <p>
     * A zero rotation quaternion.
     * </p>
     * <p>The {@linkplain #getAngle() rotation angle} of the zero rotation is
     * 0.</p>
     */
    @Nonnull
    public static final Rotation3Quaternion ZERO = new Rotation3Quaternion(Quaternion.create(1, 0, 0, 0));

    private final Quaternion versor;

    private Rotation3Quaternion(@Nonnull final Quaternion versor) {
        this.versor = versor;
    }

    /**
     * <p>
     * Create a rotation that has a given quaternion representation.
     * </p>
     * <ul>
     * <li>The {@linkplain #getVersor() versor} of the created rotation is derived
     * from the given quaternion, {@linkplain Quaternion#scale(double) scaled} to
     * have unit {@linkplain Quaternion#norm() norm} (magnitude).</li>
     * <li>For the special case of a {@linkplain Quaternion#ZERO zero} (or near
     * zero) quaternion, the method gives the {@linkplain #ZERO zero} rotation.</li>
     * </ul>
     *
     * @param quaternion The quaternion of the rotation.
     * @return the rotation
     * @throws NullPointerException If {@code quaternion} is null.
     */
    @Nonnull
    public static Rotation3Quaternion valueOf(@Nonnull final Quaternion quaternion) {
        Objects.requireNonNull(quaternion);
        final double norm = quaternion.norm();
        if (norm == 1.0) {
            return new Rotation3Quaternion(quaternion);
        } else if (Double.MIN_NORMAL <= norm) {
            return new Rotation3Quaternion(quaternion.scale(1.0 / norm));
        } else {
            return ZERO;
        }
    }

    /**
     * <p>
     * Create a rotation quaternion that has a given axis-angle representation.
     * </p>
     * <ul>
     * <li>The {@linkplain #getAngle() rotation angle} of the created rotation is
     * equal to the given angle (converted to the range -2&pi; to 2&pi;).</li>
     * <li>The {@linkplain #getAxis() rotation axis} of the created rotation points
     * in the same direction as the given axis.</li>
     * </ul>
     *
     * @param axis  The angle of rotation of the rotation, in radians.
     * @param angle The direction vector about which this rotation takes place. This
     *              direction need not have {@linkplain ImmutableVector3#magnitude()
     *              magnitude} of 1.
     * @throws NullPointerException     If {@code axis} is null.
     * @throws IllegalArgumentException If {@code axis} as zero {@linkplain ImmutableVector3#magnitude()
     *                                  magnitude} but the rotation amount is not zero.
     */
    @Nonnull
    public static Rotation3Quaternion valueOfAxisAngle(
            @Nonnull final ImmutableVector3 axis,
            final double angle
    ) {
        Objects.requireNonNull(axis);
        final double halfAngle = angle * 0.5;
        final double c = Math.cos(halfAngle);
        final double s = Math.sin(halfAngle);
        final double magnitude = axis.magnitude();
        final boolean smallAngle = Math.abs(s) < Double.MIN_NORMAL && 1.0 - c < Double.MIN_NORMAL;
        if (!smallAngle) {
            if (magnitude < Double.MIN_NORMAL) {
                throw new IllegalArgumentException("Zero axis " + axis);
            }
            final double f = s / magnitude;
            return new Rotation3Quaternion(Quaternion.create(c, f * axis.get(0), f * axis.get(1), f * axis.get(2)));
        } else {
            // Avoid division by zero.
            return ZERO;
        }
    }

    @Override
    @Nonnull
    public ImmutableVector3 apply(@Nonnull final ImmutableVector3 v) {
        Objects.requireNonNull(v);
        final Quaternion conj = versor.conjugation(Quaternion.create(0, v.get(0), v.get(1), v.get(2)));
        return ImmutableVector3.create(conj.getB(), conj.getC(), conj.getD());
    }

    /**
     * <p>
     * The angle of rotation of this rotation, in radians.
     * </p>
     * <ul>
     * <li>The angle is in the range -2&pi; to 2&pi;</li>
     * </ul>
     */
    @Override
    public double getAngle() {
        final double c = versor.getA();
        final double s = versor.vector().norm();
        return Math.atan2(s, c) * 2.0;
    }

    @Override
    @Nonnull
    public ImmutableVector3 getAxis() {
        final ImmutableVector3 su = ImmutableVector3.create(versor.getB(), versor.getC(), versor.getD());
        final double magnitude = su.magnitude();
        if (Double.MIN_NORMAL < magnitude) {
            return su.scale(1.0 / magnitude);
        } else {
            return ImmutableVector3.ZERO;
        }
    }

    @Override
    @Nonnull
    public Quaternion getVersor() {
        return versor;
    }

    @Override
    @Nonnull
    public Rotation3Quaternion minus() {
        return new Rotation3Quaternion(versor.conjugate());
    }

    @Override
    @Nonnull
    public Rotation3 minus(@Nonnull final Rotation3 that) {
        Objects.requireNonNull(that);
        return new Rotation3Quaternion(that.getVersor().conjugate().product(versor));
    }

    @Override
    @Nonnull
    public Rotation3Quaternion plus(@Nonnull final Rotation3 that) {
        Objects.requireNonNull(that);
        return new Rotation3Quaternion(versor.product(that.getVersor()));
    }

    @Override
    @Nonnull
    public Rotation3Quaternion scale(final double f) {
        return new Rotation3Quaternion(versor.pow(f));
    }

    @Override
    @Nonnull
    public String toString() {
        return "Rotation3Quaternion[" + getAngle() + " radians about " + getAxis() + "]";
    }

}
