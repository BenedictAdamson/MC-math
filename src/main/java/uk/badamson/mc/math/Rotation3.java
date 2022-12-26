package uk.badamson.mc.math;
/*
 * © Copyright Benedict Adamson 2018.
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

/**
 * <p>
 * A rotation in 3D space.
 * </p>
 */
public interface Rotation3 {

    /**
     * <p>
     * Produce the direction vector that results from applying this rotation to a
     * given direction vector.
     * </p>
     * <ul>
     * <li>Always produces a (non null) rotated vector.</li>
     * <li>The rotated vector has the same {@linkplain ImmutableVector3#magnitude()
     * magnitude} as the given vector.</li>
     * <li>Rotation by the zero rotation produces a rotated vector
     * {@linkplain ImmutableVector3#equals(Object) equal} to the given vector.</li>
     * <li>Rotation of a vector that lies along the {@linkplain #getAxis() rotation
     * axis} produces a rotated vector equal to the given vector.</li>
     * </ul>
     *
     * @param v The direction vector to be rotated.
     * @return The rotated vector.
     * @throws NullPointerException If {@code v} is null
     */
    @NonNull ImmutableVector3 apply(@NonNull ImmutableVector3 v);

    /**
     * <p>
     * The angle of rotation of this rotation, in radians.
     * </p>
     * <ul>
     * <li>Rotation by a complete circle has no effect. The angle might therefore be
     * forced to be in the range -2&pi; to 2&pi;.</li>
     * </ul>
     *
     * @return the angle
     */
    double getAngle();

    /**
     * <p>
     * The direction vector about which this rotation takes place.
     * </p>
     * <ul>
     * <li>Always have a (non-null) axis.</li>
     * <li>The axis has a {@linkplain ImmutableVector3#magnitude() magnitude} of 1
     * or 0.</li>
     * <li>The axis has a 0 magnitude for a zero rotation.</li>
     * </ul>
     *
     * @return the axis
     */
    @NonNull ImmutableVector3 getAxis();

    /**
     * <p>
     * The quaternion that represents this rotation.
     * </p>
     * <ul>
     * <li>Always have a (non null) versor.</li>
     * <li>The versor has unit {@linkplain Quaternion#norm() norm} (magnitude).</li>
     * <li>The {@linkplain Quaternion#getA() real component} of the versor is the
     * cosine of half the {@linkplain #getAngle() rotation angle}.</li>
     * <li>The {@linkplain Quaternion#vector() vector part} of the versor is the
     * unit direction vector of the {@linkplain #getAxis() rotation axis} multiplied
     * by the sine of half the rotation angle.</li>
     * </ul>
     *
     * @return the versor.
     */
    @NonNull Quaternion getVersor();

    /**
     * <p>
     * Create the rotation that is the opposite of this rotation.
     * </p>
     * <ul>
     * <li>Always produces a (non null) rotation.
     * <li>
     * <li>The opposite rotation <em>either</em> has the same {@linkplain #getAxis()
     * axis} but the negative of the {@linkplain #getAngle() angle} of this
     * rotation, <em>or</em> the same angle but an axis that points in the
     * {@linkplain Vector#minus() opposite} direction.</li>
     * </ul>
     *
     * @return the opposite rotation.
     */
    @NonNull Rotation3 minus();

    /**
     * <p>
     * Create the rotation that is the difference between this rotation and another.
     * </p>
     * <ul>
     * <li>Always produces a (non null) rotation.
     * <li>
     * <li>The difference between this rotation and the given rotation is the
     * rotation that, if {@linkplain #plus(Rotation3) added} to the given rotation
     * would produce this rotation.</li>
     * <li>The difference between a rotation and the zero rotation is itself.</li>
     * <li>The difference between a rotation and itself is the zero rotation.</li>
     * </ul>
     *
     * @param that The other rotation
     * @return the rotation difference.
     * @throws NullPointerException If {@code that} is null.
     */
    @NonNull Rotation3 minus(@NonNull Rotation3 that);

    /**
     * <p>
     * Create the rotation that is equivalent to this rotation followed by a given
     * rotation; the sum of this rotation and another.
     * </p>
     * <ul>
     * <li>Always returns a (non null) rotation.</li>
     * <li>Rotation addition is not commutative.</li>
     * <li>The sum of two rotations that have the same {@linkplain #getAxis() axis}
     * has the same axis, with the {@linkplain #getAngle() angle} of the sum
     * nominally equal to the sum of the angles of the two rotations. However, the
     * sum might be constrained to the range -2&pi; to 2&pi;.</li>
     * <li>Adding a zero rotation produces an equivalent rotation to the
     * original.</li>
     * </ul>
     *
     * @param that The other rotation
     * @return the sum rotation
     * @throws NullPointerException If {@code that} is null.
     */
    @NonNull Rotation3 plus(@NonNull Rotation3 that);

    /**
     * <p>
     * Create a rotation that has an equivalent {@linkplain #getAxis() rotation
     * axis} to this, but has its {@linkplain #getAngle() rotation angle} scaled by
     * a given amount.
     * </p>
     * <ul>
     * <li>Always returns a (non null) rotation.</li>
     * <li>The scaled rotation has same axis as this, unless the scaling factor is
     * zero.</li>
     * <li>The scaled rotation has its angle nominally scaled by the scaling factor.
     * However, the scaled value might be constrained to the range -2&pi; to
     * 2&pi;.</li>
     * <li>Scaling with a factor of 1 produces an equivalent rotation to the
     * original.</li>
     * </ul>
     *
     * @param f the scaling factor
     * @return the scaled rotation
     */
    @NonNull Rotation3 scale(double f);
}
