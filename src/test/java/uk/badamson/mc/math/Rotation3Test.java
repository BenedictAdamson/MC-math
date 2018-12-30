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

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.badamson.mc.math.VectorTest.closeToVector;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * <p>
 * Unit tests and auxiliary testing code for classes that implement the
 * {@link Rotation3} interface.
 * </p>
 */
public class Rotation3Test {

    private static class IsCloseTo extends TypeSafeMatcher<Rotation3> {
        private final double tolerance;
        private final Rotation3 value;

        private IsCloseTo(final Rotation3 value, final double tolerance) {
            this.tolerance = tolerance;
            this.value = value;
        }

        @Override
        public void describeMismatchSafely(final Rotation3 item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item).appendText(" differed by ")
                    .appendValue(Double.valueOf(distance(item)));
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("a rotation within ").appendValue(Double.valueOf(tolerance)).appendText(" of ")
                    .appendValue(value);
        }

        private final double distance(final Rotation3 item) {
            return value.getVersor().distance(item.getVersor());
        }

        @Override
        public boolean matchesSafely(final Rotation3 item) {
            return distance(item) <= tolerance;
        }
    }// class

    private static final double TOLERANCE = 4.0 * (Math.nextUp(1.0) - 1.0);

    public static final double SMALL_ANGLE = Math.PI / 180.0;

    public static final double HALF_PI = Math.PI * 0.5;

    public static ImmutableVector3 apply(final Rotation3 r, final ImmutableVector3 v) {
        final double magnitude0 = v.magnitude();

        final ImmutableVector3 rv = r.apply(v);

        assertNotNull(rv, "Always produces a rotated vector.");// guard
        assertInvariants(r);// check for side effects
        ImmutableVector3Test.assertInvariants(v);// check for side effects
        ImmutableVector3Test.assertInvariants(rv);
        ImmutableVector3Test.assertInvariants(rv, v);
        assertThat("The rotated vector has the same magnitude as the given vector.", Double.valueOf(rv.magnitude()),
                closeTo(magnitude0, TOLERANCE * (magnitude0 + 1.0)));

        return rv;
    }

    public static void assertInvariants(final Rotation3 rotation) {
        final Quaternion versor = rotation.getVersor();
        final ImmutableVector3 axis = rotation.getAxis();

        assertNotNull(versor, "Always have a versor.");// guard
        assertNotNull(axis, "Always have an axis.");// guard
        QuaternionTest.assertInvariants(versor);
        ImmutableVector3Test.assertInvariants(axis);

        final double axisMagnitude = axis.magnitude();

        assertThat("The versor has unit norm.", Double.valueOf(versor.norm()), closeTo(1.0, TOLERANCE));
        assertThat("The axis has a magnitude of 1 or 0.", Double.valueOf(axisMagnitude),
                anyOf(closeTo(0.0, TOLERANCE), closeTo(1.0, TOLERANCE)));
    }

    public static void assertInvariants(final Rotation3 r1, final Rotation3 r2) {
        // Do nothing
    }

    public static Matcher<Rotation3> closeToRotation3(final Rotation3 operand) {
        return new IsCloseTo(operand, TOLERANCE);
    }

    public static Matcher<Rotation3> closeToRotation3(final Rotation3 operand, final double tolerance) {
        return new IsCloseTo(operand, tolerance);
    }

    public static Rotation3 minus(final Rotation3 r) {
        final double angle = r.getAngle();
        final ImmutableVector3 axis = r.getAxis();

        final Rotation3 m = r.minus();

        assertNotNull(m, "Not null, result");

        assertInvariants(r);// check for side effects
        assertInvariants(m);
        assertInvariants(m, r);

        final double minusAngle = r.getAngle();
        final ImmutableVector3 minusAxis = r.getAxis();

        assertThat(
                "The opposite rotation either has the same axis but the negative of the angle of this rotation, "
                        + "or the same angle but an axis that points in the opposite direction (angle).",
                Double.valueOf(minusAngle), anyOf(closeTo(angle, TOLERANCE), closeTo(-angle, TOLERANCE)));
        assertThat(
                "The opposite rotation either has the same axis but the negative of the angle of this rotation, "
                        + "or the same angle but an axis that points in the opposite direction (axis).",
                minusAxis, anyOf(closeToVector(axis, TOLERANCE), closeToVector(axis.minus(), TOLERANCE)));

        return m;
    }

    public static Rotation3 minus(final Rotation3 r, final Rotation3 that) {
        final Rotation3 diff = r.minus(that);

        assertNotNull(diff, "Not null, result");

        assertInvariants(r);// check for side effects
        assertInvariants(diff);
        assertInvariants(diff, r);
        assertInvariants(diff, that);

        assertThat(
                "The difference between this rotation and the given rotation is the rotation that, "
                        + "if added to the given rotation would produce this rotation.",
                that.plus(diff), closeToRotation3(r));

        return diff;
    }

    public static double normalizedAngle(final double a) {
        return a % (2.0 * Math.PI);
    }

    public static Rotation3 plus(final Rotation3 r, final Rotation3 that) {
        final Rotation3 sum = r.plus(that);

        assertNotNull(sum, "Not null, result");
        assertInvariants(r);// check for side effects
        assertInvariants(that);// check for side effects
        assertInvariants(r, that);// check for side effects
        assertInvariants(sum);
        assertInvariants(sum, r);
        assertInvariants(sum, that);

        return sum;
    }

    public static Rotation3 scale(final Rotation3 r, final double f) {
        final Rotation3 fr = r.scale(f);

        assertNotNull(fr, "Not null, result");
        assertInvariants(r);// check for side effects
        assertInvariants(fr);
        assertInvariants(fr, r);

        assertTrue(
                Math.abs(f) < Double.MIN_NORMAL
                        || ImmutableVector3Test.closeTo(r.getAxis(), TOLERANCE).matches(fr.getAxis()),
                "The scaled rotation has same axis as this, unless the scaling factor is zero");
        assertThat("The scaled rotation has its angle nominally scaled by the scaling factor.",
                Double.valueOf(normalizedAngle(fr.getAngle())), closeTo(normalizedAngle(r.getAngle() * f), TOLERANCE));

        return fr;
    }
}
