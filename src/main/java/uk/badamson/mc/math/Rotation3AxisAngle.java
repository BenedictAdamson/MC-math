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
 * A rotation in 3D space represented by a rotation angle and a rotation axis.
 * </p>
 */
@Immutable
public final class Rotation3AxisAngle implements Rotation3 {

    /**
     * <p>
     * A rotation with zero rotation angle.
     * </p>
     * <ul>
     * <li>The {@linkplain #getAngle() rotation angle} of the zero rotation is
     * 0.</li>
     * </ul>
     */
    @Nonnull
    public static final Rotation3AxisAngle ZERO = new Rotation3AxisAngle(ImmutableVector3.ZERO, 0);

    @Nonnull
    private final ImmutableVector3 axis;
    private final double angle;

    private Rotation3AxisAngle(@Nonnull final ImmutableVector3 axis, final double angle) {
        this.axis = axis;
        this.angle = angle;
    }

    /**
     * <p>
     * Create a rotation that has a given quaternion representation.
     * </p>
     * <ul>
     * <li>Always creates a (non null) rotation.</li>
     * <li>The {@linkplain #getVersor() versor} of the created rotation is derived
     * from the given quaternion, {@linkplain Quaternion#scale(double) scaled} to
     * have unit {@linkplain Quaternion#norm() norm} (magnitude).</li>
     * <li>For the special case of a {@linkplain Quaternion#ZERO zero} (or near
     * zero) quaternion, the method gives the {@linkplain #ZERO zero} rotation.</li>
     * </ul>
     *
     * @param quaternion The quaternion of the rotation.
     * @throws NullPointerException If {@code quaternion} is null.
     */
    @Nonnull
    public static Rotation3AxisAngle valueOf(@Nonnull final Quaternion quaternion) {
        Objects.requireNonNull(quaternion, "quaternion");
        final Quaternion versor = quaternion.versor();
        final double c = versor.getA();
        final double s = versor.vector().norm();
        final double angle = Math.atan2(s, c) * 2.0;
        final ImmutableVector3 axis = Double.MIN_NORMAL < Math.abs(s)
                ? ImmutableVector3.create(versor.getB(), versor.getC(), versor.getD()).scale(1.0 / s)
                : ImmutableVector3.ZERO;
        return new Rotation3AxisAngle(axis, angle);
    }

    /**
     * <p>
     * Create a rotation quaternion that has a given axis-angle representation.
     * </p>
     * <ul>
     * <li>The {@linkplain #getAngle() rotation angle} of the created rotation is
     * equal to the given angle.</li>
     * <li>The {@linkplain #getAxis() rotation axis} of the created rotation points
     * in the same direction as the given axis.</li>
     * </ul>
     *
     * @param axis  The angle of rotation of the rotation, in radians.
     * @param angle The direction vector about which this rotation takes place. This
     *              direction need not have {@linkplain ImmutableVector3#magnitude()
     *              magnitude} of 1.
     * @return the rotation
     * @throws NullPointerException     If {@code axis} is null.
     * @throws IllegalArgumentException If {@code axis} as zero {@linkplain ImmutableVector3#magnitude()
     *                                  magnitude} but the rotation amount is not zero.
     */
    @Nonnull
    public static Rotation3AxisAngle valueOfAxisAngle(
            @Nonnull final ImmutableVector3 axis,
            final double angle
    ) {
        Objects.requireNonNull(axis, "axis");
        final ImmutableVector3 normalizedAxis = Double.MIN_NORMAL < Math.abs(angle) ? axis.scale(1.0 / axis.magnitude())
                : ImmutableVector3.ZERO;// TODO handle 2pi multiples
        return new Rotation3AxisAngle(normalizedAxis, angle);
    }

    @Override
    public @Nonnull ImmutableVector3 apply(@Nonnull final ImmutableVector3 v) {
        return asRotation3Quaternion().apply(v);
    }

    private @Nonnull Rotation3Quaternion asRotation3Quaternion() {
        return Rotation3Quaternion.valueOfAxisAngle(axis, angle);
    }

    /**
     * <p>
     * The angle of rotation of this rotation, in radians.
     * </p>
     * <ul>
     * <li>This representation can representation by multiple turns, so the angle is
     * not constrained to the range -2&pi; to 2&pi;</li>
     * </ul>
     */
    @Override
    public double getAngle() {
        return angle;
    }

    @Override
    @Nonnull
    public ImmutableVector3 getAxis() {
        return axis;
    }

    @Override
    @Nonnull
    public Quaternion getVersor() {
        return asRotation3Quaternion().getVersor();
    }

    /**
     * <p>
     * Create the rotation that is the opposite of this rotation.
     * </p>
     * <p>The opposite rotation has the same {@linkplain #getAxis() axis} but the
     * negative of the {@linkplain #getAngle() angle} of this rotation.</p>
     */
    @Override
    @Nonnull
    public Rotation3AxisAngle minus() {
        return new Rotation3AxisAngle(axis, -angle);
    }

    @Override
    @Nonnull
    public Rotation3 minus(@Nonnull final Rotation3 that) {
        Objects.requireNonNull(that);
        return asRotation3Quaternion().minus(that);
    }

    @Override
    @Nonnull
    public Rotation3 plus(@Nonnull final Rotation3 that) {
        Objects.requireNonNull(that);
        return asRotation3Quaternion().plus(that);
    }

    @Override
    @Nonnull
    public Rotation3AxisAngle scale(final double f) {
        return new Rotation3AxisAngle(axis, angle * f);
    }

    @Override
    @Nonnull
    public String toString() {
        return "Rotation3AxisAngle[" + getAngle() + " radians about " + getAxis() + "]";
    }

}
