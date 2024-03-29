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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * Unit tests of the class {@link ImmutableVector3}.
 * </p>
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ImmutableVector3Test {

    private static class IsCloseTo extends TypeSafeMatcher<ImmutableVector3> {
        private final double tolerance;
        private final ImmutableVector3 value;

        private IsCloseTo(final ImmutableVector3 value, final double tolerance) {
            this.tolerance = tolerance;
            this.value = value;
        }

        @Override
        public void describeMismatchSafely(final ImmutableVector3 item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item).appendText(" differed by ")
                    .appendValue(distance(item));
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("a vector within ").appendValue(tolerance).appendText(" of ")
                    .appendValue(value);
        }

        private double distance(final ImmutableVector3 item) {
            return item.minus(value).magnitude();
        }

        @Override
        public boolean matchesSafely(final ImmutableVector3 item) {
            return item != null && distance(item) <= tolerance;
        }
    }// class

    public static void assertInvariants(final ImmutableVector3 x) {
        VectorTest.assertInvariants(x);// inherited

        assertEquals(1, x.getColumns(), "columns");
        final int dimensions = x.getDimension();
        assertTrue(0 < dimensions, "The number of dimensions <" + dimensions + "> is positive");
    }

    public static void assertInvariants(final ImmutableVector3 x1, final ImmutableVector3 x2) {
        VectorTest.assertInvariants(x1, x2);// inherited

        if (x1.equals(x2)) {
            final int dimensions1 = x1.getDimension();
            assertEquals(dimensions1, x2.getDimension(), "Equality requires equal dimensions");// guard
            for (int i = 0; i < dimensions1; i++) {
                assertEquals(x1.get(i), x2.get(i), Double.MIN_NORMAL, "Equality requires equal components [" + i + "]");
            }
        }
    }

    public static Matcher<Vector> closeTo(final ImmutableVector3 operand, final double tolerance) {
        return VectorTest.closeToVector(operand, tolerance);
    }

    public static Matcher<ImmutableVector3> closeToImmutableVector3(final ImmutableVector3 operand,
            final double tolerance) {
        return new IsCloseTo(operand, tolerance);
    }

    private static ImmutableVector3 create(final double x, final double y, final double z) {
        final ImmutableVector3 v = ImmutableVector3.create(x, y, z);

        assertNotNull(v, "Not null, result");
        assertInvariants(v);

        assertEquals(Double.doubleToLongBits(x), Double.doubleToLongBits(v.get(0)), "x");
        assertEquals(Double.doubleToLongBits(y), Double.doubleToLongBits(v.get(1)), "y");
        assertEquals(Double.doubleToLongBits(z), Double.doubleToLongBits(v.get(2)), "z");

        return v;
    }

    private static void create_2equals(final double x, final double y, final double z) {
        final ImmutableVector3 v1 = ImmutableVector3.create(x, y, z);
        final ImmutableVector3 v2 = ImmutableVector3.create(x, y, z);

        assertInvariants(v1, v2);
        assertEquals(v1, v2, "Equivalent");
    }

    private static ImmutableVector3 mean(final ImmutableVector3 x, final ImmutableVector3 that) {
        final ImmutableVector3 mean = (ImmutableVector3) MatrixTest.mean(x, that);// inherited

        assertInvariants(mean);
        assertInvariants(x, mean);
        assertInvariants(that, mean);

        return mean;
    }

    public static ImmutableVector3 mean(final ImmutableVector3 x, final Vector that) {
        final ImmutableVector3 mean = (ImmutableVector3) MatrixTest.mean(x, that);// inherited

        assertInvariants(mean);
        assertInvariants(x, mean);

        return mean;
    }

    public static ImmutableVector3 minus(final ImmutableVector3 x) {
        final ImmutableVector3 minus = (ImmutableVector3) VectorTest.minus(x);// inherited

        assertInvariants(minus);
        assertInvariants(x, minus);

        return minus;
    }

    private static ImmutableVector3 minus(final ImmutableVector3 x, final ImmutableVector3 that) {
        final ImmutableVector3 diff = (ImmutableVector3) VectorTest.minus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);
        assertInvariants(diff, that);
        return diff;
    }

    public static ImmutableVector3 minus(final ImmutableVector3 x, final Vector that) {
        final ImmutableVector3 diff = (ImmutableVector3) VectorTest.minus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);

        return diff;
    }

    private static ImmutableVector3 plus(final ImmutableVector3 x, final ImmutableVector3 that) {
        final ImmutableVector3 sum = (ImmutableVector3) VectorTest.plus(x, that);// inherited

        assertInvariants(sum);
        assertInvariants(sum, x);
        assertInvariants(sum, that);

        return sum;
    }

    public static ImmutableVector3 plus(final ImmutableVector3 x, final Vector that) {
        final ImmutableVector3 diff = (ImmutableVector3) VectorTest.plus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);

        return diff;
    }

    public static ImmutableVector3 scale(final ImmutableVector3 x, final double f) {
        final ImmutableVector3 scaled = (ImmutableVector3) VectorTest.scale(x, f);// inherited

        assertInvariants(scaled);
        assertInvariants(x, scaled);

        return scaled;
    }

    private static ImmutableVector3 sum(final ImmutableVector3... x) {
        final ImmutableVector3 sum = ImmutableVector3.sum(x);

        assertNotNull(sum, "Always returns a sum vector.");// guard
        assertInvariants(sum);
        for (final ImmutableVector3 xi : x) {
            assertInvariants(sum, xi);
        }

        assertEquals(x[0].getDimension(), sum.getDimension(),
                "The dimension of the sum equals the dimension of the summed vectors.");

        return sum;
    }

    private static void sum_multiple1(final double x, final double y, final double z) {
        final ImmutableVector3 sum = sum(ImmutableVector3.create(x, y, z));

        assertEquals(x, sum.get(0), Double.MIN_NORMAL, "sum x");
        assertEquals(y, sum.get(1), Double.MIN_NORMAL, "sum y");
        assertEquals(z, sum.get(2), Double.MIN_NORMAL, "sum z");
    }

    private static void sum_multipleX2(final double x1, final double x2) {
        final ImmutableVector3 sum = sum(
                ImmutableVector3.create(x1, 0, 0), ImmutableVector3.create(x2, 0, 0));

        assertEquals(x1 + x2, sum.get(0), Double.MIN_NORMAL, "sum x");
    }

    private static void sum_multipleY2(final double y1, final double y2) {
        final ImmutableVector3 sum = sum(
                ImmutableVector3.create(0, y1, 0), ImmutableVector3.create(0, y2, 0));

        assertEquals(y1 + y2, sum.get(1), Double.MIN_NORMAL, "sum y");
    }

    private static void sum_multipleZ2(final double z1, final double z2) {
        final ImmutableVector3 sum = sum(
                ImmutableVector3.create(0, 0, z1), ImmutableVector3.create(0, 0, z2));

        assertEquals(z1 + z2, sum.get(2), Double.MIN_NORMAL, "sum z");
    }

    private static ImmutableVector3 weightedSum(final double[] weight, final ImmutableVector3[] x) {
        final ImmutableVector3 sum = ImmutableVector3.weightedSum(weight, x);

        assertNotNull(sum, "Always returns a sum vector.");// guard
        assertInvariants(sum);
        for (final ImmutableVector3 xi : x) {
            assertInvariants(sum, xi);
        }

        return sum;
    }

    private static void weightedSum_1(final double weight, final double x, final double y, final double z) {
        final ImmutableVector3 sum = weightedSum(new double[] { weight },
                new ImmutableVector3[] { ImmutableVector3.create(x, y, z) });

        assertEquals(weight * x, sum.get(0), Double.MIN_NORMAL, "sum x");
        assertEquals(weight * y, sum.get(1), Double.MIN_NORMAL, "sum y");
        assertEquals(weight * z, sum.get(2), Double.MIN_NORMAL, "sum z");
    }

    @Test
    public void create_0() {
        final ImmutableVector3 x = create(0.0, 0.0, 0.0);

        assertEquals(0.0, x.magnitude2(), Double.MIN_NORMAL, "magnitude^2");
        assertEquals(0.0, x.magnitude(), Double.MIN_NORMAL, "magnitude");
    }

    @Test
    public void create_1Max() {
        final double max = Double.MAX_VALUE;
        final ImmutableVector3 x = create(max, 0, 0);

        assertEquals(max, x.magnitude(), max * 1.0E-6, "magnitude");
    }

    @Test
    public void create_1Nan() {
        final ImmutableVector3 x = create(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        final double magnitude2 = x.magnitude2();
        final double magnitude = x.magnitude();
        assertEquals(Double.doubleToLongBits(magnitude2), Double.doubleToLongBits(Double.POSITIVE_INFINITY),
                "magnitude^2 <" + magnitude2 + "> (bits)");
        assertEquals(Double.doubleToLongBits(magnitude), Double.doubleToLongBits(Double.POSITIVE_INFINITY),
                "magnitude <" + magnitude + "> (bits)");
    }

    @Test
    public void create_1X() {
        final ImmutableVector3 x = create(-1.0, 0.0, 0.0);

        assertEquals(1.0, x.magnitude2(), Double.MIN_NORMAL, "magnitude^2");
        assertEquals(1.0, x.magnitude(), Double.MIN_NORMAL, "magnitude");
    }

    @Test
    public void create_2equalsA() {
        create_2equals(0.0, 0.0, 0.0);
    }

    @Test
    public void create_2equalsB() {
        create_2equals(1.0, 2.0, 3.0);
    }

    @Test
    public void create_2equalsC() {
        create_2equals(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Test
    public void create_2notEqualsX() {
        final ImmutableVector3 v1 = ImmutableVector3.create(0.0, 0.0, 0.0);
        final ImmutableVector3 v2 = ImmutableVector3.create(1.0, 0.0, 0.0);

        assertInvariants(v1, v2);
        assertNotEquals(v1, v2, "Not equivalent");
    }

    @Test
    public void create_2notEqualsY() {
        final ImmutableVector3 v1 = ImmutableVector3.create(0.0, 0.0, 0.0);
        final ImmutableVector3 v2 = ImmutableVector3.create(0.0, 1.0, 0.0);

        assertInvariants(v1, v2);
        assertNotEquals(v1, v2, "Not equivalent");
    }

    @Test
    public void create_2notEqualsZ() {
        final ImmutableVector3 v1 = ImmutableVector3.create(0.0, 0.0, 0.0);
        final ImmutableVector3 v2 = ImmutableVector3.create(0.0, 0.0, 1.0);

        assertInvariants(v1, v2);
        assertNotEquals(v1, v2, "Not equivalent");
    }

    @Test
    public void dot_x11() {
        final double d = ImmutableVector3.create(1, 0, 0).dot(ImmutableVector3.create(1, 0, 0));
        assertEquals(1.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_x12() {
        final double d = ImmutableVector3.create(1, 0, 0).dot(ImmutableVector3.create(2, 0, 0));
        assertEquals(2.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_x21() {
        final double d = ImmutableVector3.create(2, 0, 0).dot(ImmutableVector3.create(1, 0, 0));
        assertEquals(2.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_x22() {
        final double d = ImmutableVector3.create(2, 0, 0).dot(ImmutableVector3.create(2, 0, 0));
        assertEquals(4.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_xyz() {
        final double d = ImmutableVector3.create(1, 1, 1).dot(ImmutableVector3.create(1, 1, 1));
        assertEquals(3.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void mean_x02() {
        final ImmutableVector3 mean = mean(ImmutableVector3.create(0, 0, 0), ImmutableVector3.create(2, 0, 0));
        assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_x11() {
        final ImmutableVector3 mean = mean(ImmutableVector3.create(1, 0, 0), ImmutableVector3.create(1, 0, 0));
        assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_x1m1() {
        final ImmutableVector3 mean = mean(ImmutableVector3.create(1, 0, 0), ImmutableVector3.create(-1, 0, 0));
        assertEquals(0.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_x20() {
        final ImmutableVector3 mean = mean(ImmutableVector3.create(2, 0, 0), ImmutableVector3.create(0, 0, 0));
        assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_xyz() {
        final ImmutableVector3 mean = mean(ImmutableVector3.create(1, 2, 3), ImmutableVector3.create(3, 4, 5));
        assertEquals(2.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
        assertEquals(3.0, mean.get(1), Double.MIN_NORMAL, "mean[1]");
        assertEquals(4.0, mean.get(2), Double.MIN_NORMAL, "mean[2]");
    }

    @Test
    public void minus_0() {
        minus(ImmutableVector3.create(0, 0, 0));
    }

    @Test
    public void minus_111() {
        minus(ImmutableVector3.create(1, 1, 1));
    }

    @Test
    public void minus_123() {
        minus(ImmutableVector3.create(1, 2, 3));
    }

    @Test
    public void minus_1Nan() {
        minus(ImmutableVector3.create(Double.NaN, Double.NaN, Double.NaN));
    }

    @Test
    public void minus_infinity() {
        minus(ImmutableVector3.create(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    }

    @Test
    public void minus_m1m1m1() {
        minus(ImmutableVector3.create(-1, -1, 1));
    }

    @Test
    public void minus_vector00() {
        final ImmutableVector3 x1 = ImmutableVector3.create(0, 0, 0);
        final ImmutableVector3 x2 = ImmutableVector3.create(0, 0, 0);

        minus(x1, x2);
    }

    @Test
    public void minus_vector01() {
        final ImmutableVector3 x1 = ImmutableVector3.create(0, 0, 0);
        final ImmutableVector3 x2 = ImmutableVector3.create(1, 1, 1);

        minus(x1, x2);
    }

    @Test
    public void minus_vector10() {
        final ImmutableVector3 x1 = ImmutableVector3.create(1, 1, 1);
        final ImmutableVector3 x2 = ImmutableVector3.create(0, 0, 0);

        minus(x1, x2);
    }

    @Test
    public void minus_vectorA() {
        final ImmutableVector3 x1 = ImmutableVector3.create(1, 2, 3);
        final ImmutableVector3 x2 = ImmutableVector3.create(2, 6, 10);

        minus(x1, x2);
    }

    @Test
    public void plus_00() {
        final ImmutableVector3 x1 = ImmutableVector3.create(0, 0, 0);
        final ImmutableVector3 x2 = ImmutableVector3.create(0, 0, 0);

        plus(x1, x2);
    }

    @Test
    public void plus_01() {
        final ImmutableVector3 x1 = ImmutableVector3.create(0, 0, 0);
        final ImmutableVector3 x2 = ImmutableVector3.create(1, 1, 1);

        plus(x1, x2);
    }

    @Test
    public void plus_0m1() {
        final ImmutableVector3 x1 = ImmutableVector3.create(0, 0, 0);
        final ImmutableVector3 x2 = ImmutableVector3.create(-1, -1, -1);

        plus(x1, x2);
    }

    @Test
    public void plus_10() {
        final ImmutableVector3 x1 = ImmutableVector3.create(1, 1, 1);
        final ImmutableVector3 x2 = ImmutableVector3.create(0, 0, 0);

        plus(x1, x2);
    }

    @Test
    public void plus_A() {
        final ImmutableVector3 x1 = ImmutableVector3.create(1, 2, 3);
        final ImmutableVector3 x2 = ImmutableVector3.create(2, 6, 10);

        plus(x1, x2);
    }

    @Test
    public void plus_C() {
        final ImmutableVector3 x1 = ImmutableVector3.create(1, 2, 3);
        final ImmutableVector3 x2 = ImmutableVector3.create(3, 4, 5);

        plus(x1, x2);
    }

    @Test
    public void plus_m10() {
        final ImmutableVector3 x1 = ImmutableVector3.create(-1, -1, -1);
        final ImmutableVector3 x2 = ImmutableVector3.create(0, 0, 0);

        plus(x1, x2);
    }

    @Test
    public void scale_01() {
        final ImmutableVector3 scaled = scale(ImmutableVector3.create(0, 0, 0), 1.0);

        assertEquals(0.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
        assertEquals(0.0, scaled.get(1), Double.MIN_NORMAL, "scaled[1]");
        assertEquals(0.0, scaled.get(2), Double.MIN_NORMAL, "scaled[2]");
    }

    @Test
    public void scale_10() {
        final ImmutableVector3 scaled = scale(ImmutableVector3.create(1, 1, 1), 0.0);

        assertEquals(0.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
        assertEquals(0.0, scaled.get(1), Double.MIN_NORMAL, "scaled[1]");
        assertEquals(0.0, scaled.get(2), Double.MIN_NORMAL, "scaled[2]");
    }

    @Test
    public void scale_11() {
        final ImmutableVector3 scaled = scale(ImmutableVector3.create(1, 1, 1), 1.0);

        assertEquals(1.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
        assertEquals(1.0, scaled.get(1), Double.MIN_NORMAL, "scaled[1]");
        assertEquals(1.0, scaled.get(2), Double.MIN_NORMAL, "scaled[2]");
    }

    @Test
    public void scale_1m2() {
        final ImmutableVector3 scaled = scale(ImmutableVector3.create(1, 1, 1), -2.0);

        assertEquals(-2.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
        assertEquals(-2.0, scaled.get(1), Double.MIN_NORMAL, "scaled[1]");
        assertEquals(-2.0, scaled.get(2), Double.MIN_NORMAL, "scaled[2]");
    }

    @Test
    public void scale_A() {
        final ImmutableVector3 scaled = scale(ImmutableVector3.create(1, 2, 4), 4.0);

        assertEquals(4.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
        assertEquals(8.0, scaled.get(1), Double.MIN_NORMAL, "scaled[1]");
        assertEquals(16.0, scaled.get(2), Double.MIN_NORMAL, "scaled[2]");
    }

    @Test
    public void sum_multiple1A() {
        sum_multiple1(1, 2, 3);
    }

    @Test
    public void sum_multiple1B() {
        sum_multiple1(7, 6, 5);
    }

    @Test
    public void sum_multipleX2A() {
        sum_multipleX2(1, 2);
    }

    @Test
    public void sum_multipleX2B() {
        sum_multipleX2(1, -2);
    }

    @Test
    public void sum_multipleX2C() {
        sum_multipleX2(-1, 2);
    }

    @Test
    public void sum_multipleY2A() {
        sum_multipleY2(1, 2);
    }

    @Test
    public void sum_multipleY2B() {
        sum_multipleY2(1, -2);
    }

    @Test
    public void sum_multipleY2C() {
        sum_multipleY2(-1, 2);
    }

    @Test
    public void sum_multipleZ2A() {
        sum_multipleZ2(1, 2);
    }

    @Test
    public void sum_multipleZ2B() {
        sum_multipleZ2(1, -2);
    }

    @Test
    public void sum_multipleZ2C() {
        sum_multipleZ2(-1, 2);
    }

    @Test
    public void weightedSum_1A() {
        weightedSum_1(1, 1, 2, 3);
    }

    @Test
    public void weightedSum_1B() {
        weightedSum_1(2, 1, 2, 3);
    }

    @Test
    public void weightedSum_1C() {
        weightedSum_1(1, 7, 6, 5);
    }

    @Test
    public void weightedSum_2() {
        final ImmutableVector3 sum = weightedSum(new double[]{1.0, 2.0},
                new ImmutableVector3[]{ImmutableVector3.create(3, 0, 0), ImmutableVector3.create(5, 0, 0)});

        assertEquals(13.0, sum.get(0), Double.MIN_NORMAL, "sum[0]");
    }

    public static double[] getComponentsAsArray(final ImmutableVector3 x) {
        final var result = VectorTest.getComponentsAsArray(x);// inherited
        assertInvariants(x);
        return result;
    }

    @Nested
    public class ComponentsAsArray {

        @Test
        public void zero() {
            test(0.0, 0.0, 0.0);
        }

        @Test
        public void nonZero() {
            test(1.0, 2.0, 3.0);
        }

        private void test(double x, double y, double z) {
            final var v = ImmutableVector3.create(x, y, z);
            getComponentsAsArray(v);
        }
    }
}
