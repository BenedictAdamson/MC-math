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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nonnull;

/**
 * <p>
 * Unit tests of classes that implement the {@link Vector} interface.
 * </p>
 */
@SuppressWarnings("unused")
public class VectorTest {

    public static void assertInvariants(final Vector vector) {
        MatrixTest.assertInvariants(vector);// inherited
        assertEquals(1, vector.getColumns(), "columns");
        assertEquals(vector.getRows(), vector.getDimension(), "The number of dimensions equals the number of rows.");
    }

    public static void assertInvariants(final Vector vector1, final Vector vector2) {
        MatrixTest.assertInvariants(vector1, vector2);// inherited
    }

    public static Matcher<Vector> closeToVector(final Vector operand, final double tolerance) {
        return new IsCloseTo(operand, tolerance);
    }

    public static double dot(@Nonnull Vector x, @Nonnull Vector that) {
        final double result = x.dot(that);
        assertInvariants(x);
        assertInvariants(that);
        assertInvariants(x, that);
        return result;
    }

    public static double magnitude2(@Nonnull Vector x) {
        final double result = x.magnitude2();
        assertInvariants(x);
        return result;
    }

    public static Vector minus(final Vector x) {
        final Vector minus = x.minus();

        assertNotNull(minus, "Not null, result");// guard
        assertInvariants(minus);
        assertInvariants(x, minus);

        final int dimension = minus.getDimension();
        assertEquals(x.getDimension(), dimension, "dimension");
        for (int i = 0; i < dimension; i++) {
            final double xI = x.get(i);
            final double minusI = minus.get(i);
            final boolean signed = Double.isInfinite(xI) || Double.isFinite(xI);
            assertTrue(!signed || Double.doubleToLongBits(-xI) == Double.doubleToLongBits(minus.get(i)),
                    "minus[" + i + "] <" + xI + "," + minusI + ">");
        }

        return minus;
    }

    public static Vector minus(final Vector x, final Vector that) {
        final Vector diff = x.minus(that);

        assertNotNull(diff, "Not null, result");// guard
        assertInvariants(diff);
        assertInvariants(diff, x);
        assertInvariants(diff, that);

        final int dimension = diff.getDimension();
        assertEquals(x.getDimension(), dimension, "dimension");// guard
        for (int i = 0; i < dimension; i++) {
            assertEquals(x.get(i) - that.get(i), diff.get(i), Double.MIN_NORMAL, "diff[" + i + "]");
        }

        return diff;
    }

    public static Vector multiply(final Vector a, final Vector x) {
        final Vector ax = MatrixTest.multiply(a, x);// inherited

        assertInvariants(a);// check for side effects
        assertInvariants(a, ax);

        return ax;
    }

    public static Vector plus(final Vector x, final Vector that) {
        final Vector sum = x.plus(that);

        assertNotNull(sum, "Not null, result");// guard
        assertInvariants(sum);
        assertInvariants(sum, x);
        assertInvariants(sum, that);

        final int dimension = sum.getDimension();
        assertEquals(x.getDimension(), dimension, "dimension");// guard
        for (int i = 0; i < dimension; i++) {
            assertEquals(x.get(i) + that.get(i), sum.get(i), Double.MIN_NORMAL, "plus[" + i + "]");
        }

        return sum;
    }

    public static Vector scale(final Vector x, final double f) {
        final Vector scaled = x.scale(f);

        assertNotNull(scaled, "Not null, result");
        assertInvariants(scaled);
        assertInvariants(x, scaled);
        assertEquals(x.getDimension(), scaled.getDimension(), "dimension");

        return scaled;
    }

    @Nonnull
    public static double[] getComponentsAsArray(final Vector x) {
        final var result = x.getComponentsAsArray();

        assertInvariants(x);
        assertThat(result, notNullValue());

        final var dimension = x.getDimension();
        assertThat(result.length, is(x.getDimension()));
        for (int i = 0; i < dimension; ++i) {
            assertThat("[" + i + "]", result[i], is(x.get(i)));
        }

        return result;
    }

    private static class IsCloseTo extends TypeSafeMatcher<Vector> {
        private final double tolerance;
        private final Vector value;

        private IsCloseTo(final Vector value, final double tolerance) {
            this.tolerance = tolerance;
            this.value = value;
        }

        @Override
        public void describeMismatchSafely(final Vector item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item).appendText(" differed by ")
                    .appendValue(distance(item));
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("a vector within ").appendValue(tolerance).appendText(" of ")
                    .appendValue(value);
        }

        private double distance(final Vector item) {
            return value.minus(item).magnitude();
        }

        @Override
        public boolean matchesSafely(final Vector item) {
            return distance(item) <= tolerance;
        }
    }
}
