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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.badamson.mc.math.ImmutableVector3Test.closeToImmutableVector3;
import static uk.badamson.mc.math.Rotation3Test.closeToRotation3;

import org.junit.jupiter.api.Test;

import uk.badamson.dbc.assertions.ObjectVerifier;

/**
 * <p>
 * Unit tests for the class {@link Rotation3AxisAngle}
 * </p>
 */
@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
public class Rotation3AxisAngleTest {

    private static final double TOLERANCE = 4.0 * (Math.nextAfter(1.0, Double.POSITIVE_INFINITY) - 1.0);

    private static final double SMALL_ANGLE = Rotation3Test.SMALL_ANGLE;
    private static final double HALF_PI = Rotation3Test.HALF_PI;

    private static ImmutableVector3 apply(final Rotation3AxisAngle r, final ImmutableVector3 v) {
        final ImmutableVector3 rv = Rotation3Test.apply(r, v);// inherited

        assertInvariants(r);// check for side effects

        return rv;
    }

    private static void apply_0(final ImmutableVector3 v) {
        final double magnitude0 = v.magnitude();

        final ImmutableVector3 rv = apply(Rotation3AxisAngle.ZERO, v);

        assertThat("Rotation by the zero rotation produces a rotated vector equal to the given vector.", rv,
                ImmutableVector3Test.closeTo(v, TOLERANCE * (magnitude0 + 1.0)));
    }

    private static void apply_axis(final ImmutableVector3 axis, final double angle) {
        final double magnitude0 = axis.magnitude();
        final Rotation3AxisAngle r = Rotation3AxisAngle.valueOfAxisAngle(axis, angle);

        final ImmutableVector3 rv = apply(r, axis);

        assertTrue(axis.minus(rv).magnitude() < TOLERANCE * (magnitude0 + 1.0),
                "Rotation of a vector that lies along the rotation axis produces a rotated vector equal to the given vector.");
        assertThat(
                "Rotation of a vector that lies along the rotation axis produces a rotated vector equal to the given vector.",
                rv, ImmutableVector3Test.closeTo(axis, TOLERANCE * (magnitude0 + 1.0)));
    }

    private static void apply_basisHalfPi(final ImmutableVector3 e, final ImmutableVector3 eAxis,
            final ImmutableVector3 expected) {
        final Rotation3AxisAngle r = Rotation3AxisAngle.valueOfAxisAngle(eAxis, Math.PI * 0.5);

        final ImmutableVector3 actual = apply(r, e);

        assertThat(actual, ImmutableVector3Test.closeTo(expected, TOLERANCE));
    }

    public static void assertInvariants(final Rotation3AxisAngle rotation) {
        ObjectVerifier.assertInvariants(rotation);// inherited
        Rotation3Test.assertInvariants(rotation);// inherited
    }

    public static void assertInvariants(final Rotation3AxisAngle r1, final Rotation3AxisAngle r2) {
        ObjectVerifier.assertInvariants(r1, r2);// inherited
        Rotation3Test.assertInvariants(r1, r2);// inherited
    }

    public static Rotation3AxisAngle minus(final Rotation3AxisAngle r) {
        final double angle = r.getAngle();
        final ImmutableVector3 axis = r.getAxis();

        final Rotation3AxisAngle m = (Rotation3AxisAngle) Rotation3Test.minus(r);// inherited

        assertInvariants(r);// check for side effects
        assertInvariants(m);
        assertInvariants(m, r);

        assertThat("The opposite rotation has the same axis but the negative of the angle of this rotation.", m,
                closeToRotation3(Rotation3AxisAngle.valueOfAxisAngle(axis, -angle)));

        return m;
    }

    public static Rotation3 minus(final Rotation3AxisAngle r, final Rotation3 that) {
        final Rotation3 diff = Rotation3Test.minus(r, that);// inherited

        assertInvariants(r);// check for side effects

        return diff;
    }

    private static void minus_0(final Rotation3AxisAngle r) {
        final Rotation3 diff = minus(r, Rotation3AxisAngle.ZERO);

        assertThat("The difference between a rotation and the zero rotation is itself", diff, closeToRotation3(r));
    }

    private static void minus_axisAngle(final double angle, final ImmutableVector3 axis) {
        final Rotation3AxisAngle r = Rotation3AxisAngle.valueOfAxisAngle(axis, angle);

        minus(r);
    }

    private static void minus_self(final Rotation3AxisAngle r) {
        final Rotation3 diff = minus(r, r);

        assertThat("The difference between a rotation and itself is the zero rotation.", diff,
                closeToRotation3(Rotation3AxisAngle.ZERO));
    }

    private static double normalizedAngle(final double a) {
        return a % (2.0 * Math.PI);
    }

    public static Rotation3 plus(final Rotation3AxisAngle r, final Rotation3 that) {
        final Rotation3 sum = Rotation3Test.plus(r, that);

        assertInvariants(r);// check for side effects

        return sum;
    }

    private static void plus_0r(final Rotation3AxisAngle that) {
        final Rotation3 sum = plus(Rotation3AxisAngle.ZERO, that);
        assertThat("sum", sum, closeToRotation3(that));
    }

    private static void plus_r0(final Rotation3AxisAngle r) {
        final Rotation3 sum = plus(r, Rotation3AxisAngle.ZERO);
        assertThat("sum", sum, closeToRotation3(r));
    }

    private static void plus_sameAxis(final ImmutableVector3 axis, final double angle1, final double angle2) {
        final Rotation3AxisAngle r1 = Rotation3AxisAngle.valueOfAxisAngle(axis, angle1);
        final Rotation3AxisAngle r2 = Rotation3AxisAngle.valueOfAxisAngle(axis, angle2);

        final Rotation3 sum = plus(r1, r2);

        assertThat("axis", sum.getAxis(), closeToImmutableVector3(axis, TOLERANCE));
        assertThat("normalized angle", normalizedAngle(sum.getAngle()),
                closeTo(normalizedAngle(angle1 + angle2), TOLERANCE));
    }

    public static Rotation3AxisAngle scale(final Rotation3AxisAngle r, final double f) {
        final Rotation3AxisAngle fr = (Rotation3AxisAngle) Rotation3Test.scale(r, f);

        assertInvariants(r);// check for side effects
        assertInvariants(fr);
        assertInvariants(fr, r);

        return fr;
    }

    private static Rotation3AxisAngle valueOf(final Quaternion quaternion) {
        final Rotation3AxisAngle rotation = Rotation3AxisAngle.valueOf(quaternion);

        assertNotNull(rotation, "Always creates a rotation");// guard
        assertInvariants(rotation);

        return rotation;
    }

    private static void valueOf_quaternionForAxisAngle(final ImmutableVector3 axis, final double angle,
            final double magnitude) {
        final Rotation3AxisAngle rotation0 = Rotation3AxisAngle.valueOfAxisAngle(axis, angle);
        final Quaternion quaternion = rotation0.getVersor().scale(magnitude);

        final Rotation3AxisAngle rotation = valueOf(quaternion);

        assertThat("rotation", rotation, closeToRotation3(rotation0, TOLERANCE * 2));
    }

    private static Rotation3AxisAngle valueOfAxisAngle(final ImmutableVector3 axis, final double angle) {
        final double sinAngle = Math.sin(angle);
        final double axisMagnitude = Math.abs(sinAngle) < Double.MIN_NORMAL ? 0.0 : axis.magnitude();

        final Rotation3AxisAngle rotation = Rotation3AxisAngle.valueOfAxisAngle(axis, angle);

        assertNotNull(rotation, "Always creates a rotation");// guard
        assertInvariants(rotation);
        assertEquals(angle, rotation.getAngle(), TOLERANCE, "angle.");
        assertThat("The rotation axis of the created rotation points in the same direction as the given axis.",
                axisMagnitude, closeTo(axis.dot(rotation.getAxis()), axisMagnitude * TOLERANCE));

        return rotation;
    }

    @Test
    public void apply_02i() {
        apply_0(ImmutableVector3.I.scale(2.0));
    }

    @Test
    public void apply_0i() {
        apply_0(ImmutableVector3.I);
    }

    @Test
    public void apply_0j() {
        apply_0(ImmutableVector3.J);
    }

    @Test
    public void apply_0k() {
        apply_0(ImmutableVector3.K);
    }

    @Test
    public void apply_axis_halfPiI() {
        apply_axis(ImmutableVector3.I, Math.PI * 0.5);
    }

    @Test
    public void apply_axis_halfPiJ() {
        apply_axis(ImmutableVector3.J, Math.PI * 0.5);
    }

    @Test
    public void apply_axis_halfPiK() {
        apply_axis(ImmutableVector3.K, Math.PI * 0.5);
    }

    @Test
    public void apply_basisHalfPiIJ() {
        apply_basisHalfPi(ImmutableVector3.I, ImmutableVector3.J, ImmutableVector3.K.minus());
    }

    @Test
    public void apply_basisHalfPiIK() {
        apply_basisHalfPi(ImmutableVector3.I, ImmutableVector3.K, ImmutableVector3.J);
    }

    @Test
    public void apply_basisHalfPiJI() {
        apply_basisHalfPi(ImmutableVector3.J, ImmutableVector3.I, ImmutableVector3.K);
    }

    @Test
    public void apply_basisHalfPiJK() {
        apply_basisHalfPi(ImmutableVector3.J, ImmutableVector3.K, ImmutableVector3.I.minus());
    }

    @Test
    public void apply_basisHalfPiKI() {
        apply_basisHalfPi(ImmutableVector3.K, ImmutableVector3.I, ImmutableVector3.J.minus());
    }

    @Test
    public void apply_basisHalfPiKJ() {
        apply_basisHalfPi(ImmutableVector3.K, ImmutableVector3.J, ImmutableVector3.I);
    }

    @Test
    public void minus_00() {
        minus_0(Rotation3AxisAngle.ZERO);
    }

    @Test
    public void minus_i() {
        minus_axisAngle(HALF_PI, ImmutableVector3.I);
    }

    @Test
    public void minus_i0() {
        minus_0(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void minus_iJ() {
        minus(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE),
                Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE));
    }

    @Test
    public void minus_ik() {
        minus(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE),
                Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE));
    }

    @Test
    public void minus_iSmall() {
        minus_axisAngle(SMALL_ANGLE, ImmutableVector3.I);
    }

    @Test
    public void minus_j() {
        minus_axisAngle(HALF_PI, ImmutableVector3.J);
    }

    @Test
    public void minus_j0() {
        minus_0(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE));
    }

    @Test
    public void minus_jI() {
        minus(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE),
                Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void minus_jK() {
        minus(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE),
                Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE));
    }

    @Test
    public void minus_k() {
        minus_axisAngle(HALF_PI, ImmutableVector3.K);
    }

    @Test
    public void minus_k0() {
        minus_0(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE));
    }

    @Test
    public void minus_kI() {
        minus(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE),
                Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void minus_kJ() {
        minus(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE),
                Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void minus_selfI() {
        minus_self(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void minus_selfJ() {
        minus_self(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE));
    }

    @Test
    public void minus_selfK() {
        minus_self(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE));
    }

    @Test
    public void plus_00() {
        plus_0r(Rotation3AxisAngle.ZERO);
    }

    @Test
    public void plus_0ISmall() {
        plus_0r(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void plus_0JSmall() {
        plus_0r(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE));
    }

    @Test
    public void plus_0KSmall() {
        plus_0r(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE));
    }

    @Test
    public void plus_ISmall0() {
        plus_r0(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE));
    }

    @Test
    public void plus_JSmall0() {
        plus_r0(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE));
    }

    @Test
    public void plus_KSmall0() {
        plus_r0(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE));
    }

    @Test
    public void plus_sameAxisISmallSmall() {
        plus_sameAxis(ImmutableVector3.I, SMALL_ANGLE, SMALL_ANGLE);
    }

    @Test
    public void plus_sameAxisJSmallSmall() {
        plus_sameAxis(ImmutableVector3.J, SMALL_ANGLE, SMALL_ANGLE);
    }

    @Test
    public void plus_sameAxisKSmallSmall() {
        plus_sameAxis(ImmutableVector3.K, SMALL_ANGLE, SMALL_ANGLE);
    }

    @Test
    public void scale_00() {
        scale(Rotation3AxisAngle.ZERO, 0);
    }

    @Test
    public void scale_01() {
        scale(Rotation3AxisAngle.ZERO, 1);
    }

    @Test
    public void scale_ISmall1() {
        scale(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE), 1);
    }

    @Test
    public void scale_ISmall2() {
        scale(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE), 2);
    }

    @Test
    public void scale_JSmall1() {
        scale(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE), 1);
    }

    @Test
    public void scale_JSmall2() {
        scale(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE), 2);
    }

    @Test
    public void scale_KSmall1() {
        scale(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE), 1);
    }

    @Test
    public void scale_KSmall2() {
        scale(Rotation3AxisAngle.valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE), 2);
    }

    @Test
    public void statics() {
        assertNotNull(Rotation3AxisAngle.ZERO, "Has a zero rotation");
        assertInvariants(Rotation3AxisAngle.ZERO);
        assertEquals(0.0, Rotation3AxisAngle.ZERO.getAngle(), Double.MIN_NORMAL, "rotation angle of the zero rotation");
    }

    @Test
    public void valueOf_quaternionForAxisAngle_2iSmall() {
        valueOf_quaternionForAxisAngle(ImmutableVector3.I, SMALL_ANGLE, 2.0);
    }

    @Test
    public void valueOf_quaternionForAxisAngle_iHalfPi() {
        valueOf_quaternionForAxisAngle(ImmutableVector3.I, Math.PI * 0.5, 1.0);
    }

    @Test
    public void valueOf_quaternionForAxisAngle_iSmall() {
        valueOf_quaternionForAxisAngle(ImmutableVector3.I, SMALL_ANGLE, 1.0);
    }

    @Test
    public void valueOf_quaternionForAxisAngle_jSmall() {
        valueOf_quaternionForAxisAngle(ImmutableVector3.J, SMALL_ANGLE, 1.0);
    }

    @Test
    public void valueOf_quaternionForAxisAngle_kSmall() {
        valueOf_quaternionForAxisAngle(ImmutableVector3.K, SMALL_ANGLE, 1.0);
    }

    @Test
    public void valueOfAxisAngle_0I() {
        valueOfAxisAngle(ImmutableVector3.I, 0);
    }

    @Test
    public void valueOfAxisAngle_0J() {
        valueOfAxisAngle(ImmutableVector3.J, 0);
    }

    @Test
    public void valueOfAxisAngle_0K() {
        valueOfAxisAngle(ImmutableVector3.K, 0);
    }

    @Test
    public void valueOfAxisAngle_2HalfPiI() {
        valueOfAxisAngle(ImmutableVector3.create(2, 0, 0), Math.PI * 0.5);
    }

    @Test
    public void valueOfAxisAngle_2HalfPiJ() {
        valueOfAxisAngle(ImmutableVector3.create(0, 2, 0), Math.PI * 0.5);
    }

    @Test
    public void valueOfAxisAngle_2HalfPiK() {
        valueOfAxisAngle(ImmutableVector3.create(0, 0, 2), Math.PI * 0.5);
    }

    @Test
    public void valueOfAxisAngle_2PiI() {
        valueOfAxisAngle(ImmutableVector3.I, Math.PI * 2.0);
    }

    @Test
    public void valueOfAxisAngle_4PiI() {
        valueOfAxisAngle(ImmutableVector3.I, Math.PI * 4.0);
    }

    @Test
    public void valueOfAxisAngle_halfPiI() {
        valueOfAxisAngle(ImmutableVector3.I, Math.PI * 0.5);
    }

    @Test
    public void valueOfAxisAngle_halfPiJ() {
        valueOfAxisAngle(ImmutableVector3.J, Math.PI * 0.5);
    }

    @Test
    public void valueOfAxisAngle_halfPiK() {
        valueOfAxisAngle(ImmutableVector3.K, Math.PI * 0.5);
    }

    @Test
    public void valueOfAxisAngle_piI() {
        valueOfAxisAngle(ImmutableVector3.I, Math.PI);
    }

    @Test
    public void valueOfAxisAngle_smallI() {
        valueOfAxisAngle(ImmutableVector3.I, SMALL_ANGLE);
    }

    @Test
    public void valueOfAxisAngle_smallJ() {
        valueOfAxisAngle(ImmutableVector3.J, SMALL_ANGLE);
    }

    @Test
    public void valueOfAxisAngle_smallK() {
        valueOfAxisAngle(ImmutableVector3.K, SMALL_ANGLE);
    }
}
