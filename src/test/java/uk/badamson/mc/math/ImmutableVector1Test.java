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

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * Unit tests of the class {@link ImmutableVector1}.
 * </p>
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ImmutableVector1Test {

    public static void assertInvariants(final ImmutableVector1 x) {
        VectorTest.assertInvariants(x);// inherited
    }

    public static void assertInvariants(final ImmutableVector1 x1, final ImmutableVector1 x2) {
        VectorTest.assertInvariants(x1, x2);// inherited
    }

    public static Matcher<Vector> closeTo(final ImmutableVector1 operand, final double tolerance) {
        return VectorTest.closeToVector(operand, tolerance);
    }

    private static ImmutableVector1 create(final double x) {
        final ImmutableVector1 v = ImmutableVector1.create(x);

        assertNotNull(v, "Not null, result");
        assertInvariants(v);

        assertEquals(Double.doubleToLongBits(x), Double.doubleToLongBits(v.get(0)), "x");

        return v;
    }

    private static void create_2equals(final double x) {
        final ImmutableVector1 v1 = ImmutableVector1.create(x);
        final ImmutableVector1 v2 = ImmutableVector1.create(x);

        assertInvariants(v1, v2);
        assertEquals(v1, v2, "Equivalent");
    }

    private static ImmutableVector1 mean(final ImmutableVector1 x, final ImmutableVector1 that) {
        final ImmutableVector1 mean = (ImmutableVector1) MatrixTest.mean(x, that);// inherited

        assertInvariants(mean);
        assertInvariants(x, mean);
        assertInvariants(that, mean);

        return mean;
    }

    public static ImmutableVector1 mean(final ImmutableVector1 x, final Vector that) {
        final ImmutableVector1 mean = (ImmutableVector1) MatrixTest.mean(x, that);// inherited

        assertInvariants(mean);
        assertInvariants(x, mean);

        return mean;
    }

    public static ImmutableVector1 minus(final ImmutableVector1 x) {
        final ImmutableVector1 minus = (ImmutableVector1) VectorTest.minus(x);// inherited

        assertInvariants(minus);
        assertInvariants(x, minus);

        return minus;
    }

    private static ImmutableVector1 minus(final ImmutableVector1 x, final ImmutableVector1 that) {
        final ImmutableVector1 diff = (ImmutableVector1) VectorTest.minus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);
        assertInvariants(diff, that);
        return diff;
    }

    public static ImmutableVector1 minus(final ImmutableVector1 x, final Vector that) {
        final ImmutableVector1 diff = (ImmutableVector1) VectorTest.minus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);

        return diff;
    }

    private static ImmutableVector1 plus(final ImmutableVector1 x, final ImmutableVector1 that) {
        final ImmutableVector1 sum = (ImmutableVector1) VectorTest.plus(x, that);// inherited

        assertInvariants(sum);
        assertInvariants(sum, x);
        assertInvariants(sum, that);

        return sum;
    }

    public static ImmutableVector1 plus(final ImmutableVector1 x, final Vector that) {
        final ImmutableVector1 diff = (ImmutableVector1) VectorTest.plus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);

        return diff;
    }

    public static ImmutableVector1 scale(final ImmutableVector1 x, final double f) {
        final ImmutableVector1 scaled = (ImmutableVector1) VectorTest.scale(x, f);// inherited

        assertInvariants(scaled);
        assertInvariants(x, scaled);

        return scaled;
    }

    private static ImmutableVector1 sum(final ImmutableVector1... x) {
        final ImmutableVector1 sum = ImmutableVector1.sum(x);

        assertNotNull(sum, "Always returns a sum vector.");// guard
        assertInvariants(sum);
        for (final ImmutableVector1 xi : x) {
            assertInvariants(sum, xi);
        }

        assertEquals(x[0].getDimension(), sum.getDimension(),
                "The dimension of the sum equals the dimension of the summed vectors.");

        return sum;
    }

    private static void sum_multiple1(final double x) {
        final ImmutableVector1 sum = sum(ImmutableVector1.create(x));

        assertEquals(x, sum.get(0), Double.MIN_NORMAL, "sum x");
    }

    private static void sum_multiple2(final double x1, final double x2) {
        final ImmutableVector1 sum = sum(
                ImmutableVector1.create(x1), ImmutableVector1.create(x2));

        assertEquals(x1 + x2, sum.get(0), Double.MIN_NORMAL, "sum x");
    }

    private static ImmutableVector1 weightedSum(final double[] weight, final ImmutableVector1[] x) {
        final ImmutableVector1 sum = ImmutableVector1.weightedSum(weight, x);

        assertNotNull(sum, "Always returns a sum vector.");// guard
        assertInvariants(sum);
        for (final ImmutableVector1 xi : x) {
            assertInvariants(sum, xi);
        }

        return sum;
    }

    private static void weightedSum_1(final double weight, final double x) {
        final ImmutableVector1 sum = weightedSum(new double[] { weight },
                new ImmutableVector1[] { ImmutableVector1.create(x) });

        assertEquals(weight * x, sum.get(0), Double.MIN_NORMAL, "sum x");
    }

    @Test
    public void create_0() {
        final ImmutableVector1 x = create(0.0);

        assertEquals(0.0, x.magnitude2(), Double.MIN_NORMAL, "magnitude^2");
        assertEquals(0.0, x.magnitude(), Double.MIN_NORMAL, "magnitude");
    }

    @Test
    public void create_2equalsA() {
        create_2equals(0.0);
    }

    @Test
    public void create_2equalsB() {
        create_2equals(1.0);
    }

    @Test
    public void create_2equalsC() {
        create_2equals(Double.POSITIVE_INFINITY);
    }

    @Test
    public void create_2notEquals() {
        final ImmutableVector1 v1 = ImmutableVector1.create(0.0);
        final ImmutableVector1 v2 = ImmutableVector1.create(1.0);

        assertInvariants(v1, v2);
        assertNotEquals(v1, v2, "Not equivalent");
    }

    @Test
    public void create_max() {
        final double max = Double.MAX_VALUE;
        final ImmutableVector1 x = create(max);

        assertEquals(max, x.magnitude(), max * 1.0E-6, "magnitude");
    }

    @Test
    public void create_Nan() {
        final ImmutableVector1 x = create(Double.POSITIVE_INFINITY);

        final double magnitude2 = x.magnitude2();
        final double magnitude = x.magnitude();
        assertEquals(Double.doubleToLongBits(magnitude2), Double.doubleToLongBits(Double.POSITIVE_INFINITY),
                "magnitude^2 <" + magnitude2 + "> (bits)");
        assertEquals(Double.doubleToLongBits(magnitude), Double.doubleToLongBits(Double.POSITIVE_INFINITY),
                "magnitude <" + magnitude + "> (bits)");
    }

    @Test
    public void create_negative() {
        final ImmutableVector1 x = create(-1.0);

        assertEquals(1.0, x.magnitude2(), Double.MIN_NORMAL, "magnitude^2");
        assertEquals(1.0, x.magnitude(), Double.MIN_NORMAL, "magnitude");
    }

    @Test
    public void dot_x11() {
        final double d = ImmutableVector1.create(1).dot(ImmutableVector1.create(1));
        assertEquals(1.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_x12() {
        final double d = ImmutableVector1.create(1).dot(ImmutableVector1.create(2));
        assertEquals(2.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_x21() {
        final double d = ImmutableVector1.create(2).dot(ImmutableVector1.create(1));
        assertEquals(2.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_x22() {
        final double d = ImmutableVector1.create(2).dot(ImmutableVector1.create(2));
        assertEquals(4.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void mean_x02() {
        final ImmutableVector1 mean = mean(ImmutableVector1.create(0), ImmutableVector1.create(2));
        assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_x11() {
        final ImmutableVector1 mean = mean(ImmutableVector1.create(1), ImmutableVector1.create(1));
        assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_x1m1() {
        final ImmutableVector1 mean = mean(ImmutableVector1.create(1), ImmutableVector1.create(-1));
        assertEquals(0.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_x20() {
        final ImmutableVector1 mean = mean(ImmutableVector1.create(2), ImmutableVector1.create(0));
        assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void minus_0() {
        minus(ImmutableVector1.create(0));
    }

    @Test
    public void minus_1() {
        minus(ImmutableVector1.create(1));
    }

    @Test
    public void minus_infinity() {
        minus(ImmutableVector1.create(Double.POSITIVE_INFINITY));
    }

    @Test
    public void minus_m() {
        minus(ImmutableVector1.create(-1));
    }

    @Test
    public void minus_nan() {
        minus(ImmutableVector1.create(Double.NaN));
    }

    @Test
    public void minus_vector00() {
        final ImmutableVector1 x1 = ImmutableVector1.create(0);
        final ImmutableVector1 x2 = ImmutableVector1.create(0);

        minus(x1, x2);
    }

    @Test
    public void minus_vector01() {
        final ImmutableVector1 x1 = ImmutableVector1.create(0);
        final ImmutableVector1 x2 = ImmutableVector1.create(1);

        minus(x1, x2);
    }

    @Test
    public void minus_vector10() {
        final ImmutableVector1 x1 = ImmutableVector1.create(1);
        final ImmutableVector1 x2 = ImmutableVector1.create(0);

        minus(x1, x2);
    }

    @Test
    public void minus_vectorA() {
        final ImmutableVector1 x1 = ImmutableVector1.create(1);
        final ImmutableVector1 x2 = ImmutableVector1.create(2);

        minus(x1, x2);
    }

    @Test
    public void plus_00() {
        final ImmutableVector1 x1 = ImmutableVector1.create(0);
        final ImmutableVector1 x2 = ImmutableVector1.create(0);

        plus(x1, x2);
    }

    @Test
    public void plus_01() {
        final ImmutableVector1 x1 = ImmutableVector1.create(0);
        final ImmutableVector1 x2 = ImmutableVector1.create(1);

        plus(x1, x2);
    }

    @Test
    public void plus_0m1() {
        final ImmutableVector1 x1 = ImmutableVector1.create(0);
        final ImmutableVector1 x2 = ImmutableVector1.create(-1);

        plus(x1, x2);
    }

    @Test
    public void plus_10() {
        final ImmutableVector1 x1 = ImmutableVector1.create(1);
        final ImmutableVector1 x2 = ImmutableVector1.create(0);

        plus(x1, x2);
    }

    @Test
    public void plus_A() {
        final ImmutableVector1 x1 = ImmutableVector1.create(1);
        final ImmutableVector1 x2 = ImmutableVector1.create(2);

        plus(x1, x2);
    }

    @Test
    public void plus_C() {
        final ImmutableVector1 x1 = ImmutableVector1.create(1);
        final ImmutableVector1 x2 = ImmutableVector1.create(3);

        plus(x1, x2);
    }

    @Test
    public void plus_m10() {
        final ImmutableVector1 x1 = ImmutableVector1.create(-1);
        final ImmutableVector1 x2 = ImmutableVector1.create(0);

        plus(x1, x2);
    }

    @Test
    public void scale_01() {
        final ImmutableVector1 scaled = scale(ImmutableVector1.create(0), 1.0);

        assertEquals(0.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
    }

    @Test
    public void scale_10() {
        final ImmutableVector1 scaled = scale(ImmutableVector1.create(1), 0.0);

        assertEquals(0.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
    }

    @Test
    public void scale_11() {
        final ImmutableVector1 scaled = scale(ImmutableVector1.create(1), 1.0);

        assertEquals(1.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
    }

    @Test
    public void scale_1m2() {
        final ImmutableVector1 scaled = scale(ImmutableVector1.create(1), -2.0);

        assertEquals(-2.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
    }

    @Test
    public void scale_A() {
        final ImmutableVector1 scaled = scale(ImmutableVector1.create(1), 4.0);

        assertEquals(4.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
    }

    @Test
    public void sum_multiple1A() {
        sum_multiple1(1);
    }

    @Test
    public void sum_multiple1B() {
        sum_multiple1(7);
    }

    @Test
    public void sum_multiple2A() {
        sum_multiple2(1, 2);
    }

    @Test
    public void sum_multiple2B() {
        sum_multiple2(1, -2);
    }

    @Test
    public void sum_multiple2C() {
        sum_multiple2(-1, 2);
    }

    @Test
    public void weightedSum_1A() {
        weightedSum_1(1, 1);
    }

    @Test
    public void weightedSum_1B() {
        weightedSum_1(2, 1);
    }

    @Test
    public void weightedSum_1C() {
        weightedSum_1(1, 7);
    }

    @Test
    public void weightedSum_2() {
        final ImmutableVector1 sum = weightedSum(new double[]{1.0, 2.0},
                new ImmutableVector1[]{ImmutableVector1.create(3), ImmutableVector1.create(5)});

        assertEquals(13.0, sum.get(0), Double.MIN_NORMAL, "sum[0]");
    }

    public static double[] getComponentsAsArray(final ImmutableVector1 x) {
        final var result = VectorTest.getComponentsAsArray(x);// inherited
        assertInvariants(x);
        return result;
    }

    @Nested
    public class ComponentsAsArray {

        @Test
        public void zero() {
            test(0.0);
        }

        @Test
        public void one() {
            test(1.0);
        }

        private void test(double v) {
            final var x = ImmutableVector1.create(v);
            getComponentsAsArray(x);
        }
    }
}
