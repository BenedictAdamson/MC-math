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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * <p>
 * Unit tests of the class {@link ImmutableVectorN}.
 * </p>
 */
public class ImmutableVectorNTest {

    public static void assertInvariants(final ImmutableVectorN x) {
        ImmutableMatrixNTest.assertInvariants(x);// inherited
        VectorTest.assertInvariants(x);// inherited

        assertEquals(1, x.getColumns(), "columns");
        final int dimensions = x.getDimension();
        assertTrue(0 < dimensions, "The number of dimensions <" + dimensions + "> is positive");
    }

    public static void assertInvariants(final ImmutableVectorN x1, final ImmutableVectorN x2) {
        ImmutableMatrixNTest.assertInvariants(x1, x2);// inherited
        VectorTest.assertInvariants(x1, x2);// inherited

        if (x1.equals(x2)) {
            final int dimensions1 = x1.getDimension();
            assertEquals(dimensions1, x2.getDimension(), "Equality requires equal dimensions");// guard
            for (int i = 0; i < dimensions1; i++) {
                assertEquals(x1.get(i), x2.get(i), Double.MIN_NORMAL, "Equality requires equal components [" + i + "]");
            }
        }
    }

    private static void construct_equals(final double x) {
        final ImmutableVectorN v1 = ImmutableVectorN.create(x);
        final ImmutableVectorN v2 = ImmutableVectorN.create(x);

        assertInvariants(v1, v2);
        assertEquals(v1, v2, "Equivalent");
    }

    private static ImmutableVectorN create(final double... x) {
        final ImmutableVectorN v = ImmutableVectorN.create(x);

        assertInvariants(v);
        assertEquals(x.length, v.getDimension(), "dimension");
        for (int i = 0; i < x.length; i++) {
            assertEquals(x[i], v.get(i), Double.MIN_NORMAL, "x[" + i + "]");
        }

        return v;
    }

    private static ImmutableVectorN create0(final int dimension) {
        final ImmutableVectorN zero = ImmutableVectorN.create0(dimension);

        assertNotNull(zero, "Always returns a vector");// guard
        assertInvariants(zero);
        assertEquals(dimension, zero.getDimension(), "dimension");
        for (int i = 0; i < dimension; ++i) {
            assertEquals(0.0, zero.get(i), Double.MIN_NORMAL, "The elements of the zero vector are all zero.");
        }

        return zero;
    }

    private static ImmutableVectorN createOnLine(final ImmutableVectorN x0, final ImmutableVectorN dx, final double w) {
        final ImmutableVectorN x = ImmutableVectorN.createOnLine(x0, dx, w);

        assertNotNull(x, "Always returns a (non null) vector");
        assertEquals(x0.getDimension(), x.getDimension(), "dimension");

        return x;
    }

    private static ImmutableVectorN mean(final ImmutableVectorN x, final ImmutableVectorN that) {
        final ImmutableVectorN mean = (ImmutableVectorN) VectorTest.mean(x, that);// inherited

        assertInvariants(mean);
        assertInvariants(x, mean);
        assertInvariants(that, mean);

        return mean;
    }

    public static ImmutableVectorN mean(final ImmutableVectorN x, final Vector that) {
        final ImmutableVectorN mean = (ImmutableVectorN) VectorTest.mean(x, that);// inherited

        assertInvariants(mean);
        assertInvariants(x, mean);

        return mean;
    }

    public static final ImmutableVectorN minus(final ImmutableVectorN x) {
        final ImmutableVectorN minus = (ImmutableVectorN) VectorTest.minus(x);// inherited

        assertInvariants(minus);
        assertInvariants(x, minus);

        return minus;
    }

    private static final ImmutableVectorN minus(final ImmutableVectorN x, final ImmutableVectorN that) {
        final ImmutableVectorN diff = (ImmutableVectorN) VectorTest.minus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);
        assertInvariants(diff, that);
        return diff;
    }

    public static final ImmutableVectorN minus(final ImmutableVectorN x, final Vector that) {
        final ImmutableVectorN diff = (ImmutableVectorN) VectorTest.minus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);

        return diff;
    }

    private static final ImmutableVectorN plus(final ImmutableVectorN x, final ImmutableVectorN that) {
        final ImmutableVectorN diff = (ImmutableVectorN) VectorTest.plus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);
        assertInvariants(diff, that);

        return diff;
    }

    public static final ImmutableVectorN plus(final ImmutableVectorN x, final Vector that) {
        final ImmutableVectorN diff = (ImmutableVectorN) VectorTest.plus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);

        return diff;
    }

    public static ImmutableVectorN scale(final ImmutableVectorN x, final double f) {
        final ImmutableVectorN scaled = (ImmutableVectorN) VectorTest.scale(x, f);// inherited

        assertInvariants(scaled);
        assertInvariants(x, scaled);

        return scaled;
    }

    private static ImmutableVectorN sum(final ImmutableVectorN... x) {
        final ImmutableVectorN sum = ImmutableVectorN.sum(x);

        assertNotNull(sum, "Always returns a sum vector.");// guard
        assertInvariants(sum);
        for (final ImmutableVectorN xi : x) {
            assertInvariants(sum, xi);
        }

        assertEquals(x[0].getDimension(), sum.getDimension(),
                "The dimension of the sum equals the dimension of the summed vectors.");

        return sum;
    }

    private static final void sum_multiple1(final double x) {
        final ImmutableVectorN sum = sum(new ImmutableVectorN[] { ImmutableVectorN.create(x) });

        assertEquals(x, sum.get(0), Double.MIN_NORMAL, "sum[0]");
    }

    private static final void sum_multiple2(final double x1, final double x2) {
        final ImmutableVectorN sum = sum(
                new ImmutableVectorN[] { ImmutableVectorN.create(x1), ImmutableVectorN.create(x2) });

        assertEquals(x1 + x2, sum.get(0), Double.MIN_NORMAL, "sum[0]");
    }

    private static ImmutableVectorN weightedSum(final double[] weight, final ImmutableVectorN[] x) {
        final ImmutableVectorN sum = ImmutableVectorN.weightedSum(weight, x);

        assertNotNull(sum, "Always returns a sum vector.");// guard
        assertInvariants(sum);
        for (final ImmutableVectorN xi : x) {
            assertInvariants(sum, xi);
        }

        assertEquals(x[0].getDimension(), sum.getDimension(),
                "The dimension of the sum equals the dimension of the summed vectors.");

        return sum;
    }

    private static final void weightedSum_11(final double weight, final double x) {
        final ImmutableVectorN sum = weightedSum(new double[] { weight },
                new ImmutableVectorN[] { ImmutableVectorN.create(x) });

        assertEquals(weight * x, sum.get(0), Double.MIN_NORMAL, "sum[0]");
    }

    @Test
    public void construct_equals1A() {
        construct_equals(0.0);
    }

    @Test
    public void construct_equals1B() {
        construct_equals(1.0);
    }

    @Test
    public void construct_equals1C() {
        construct_equals(Double.POSITIVE_INFINITY);
    }

    @Test
    public void construct_equals2() {
        final double x1 = 0.0;
        final double x2 = 1.0;
        final ImmutableVectorN v1 = ImmutableVectorN.create(x1, x2);
        final ImmutableVectorN v2 = ImmutableVectorN.create(x1, x2);

        assertInvariants(v1, v2);
        assertEquals(v1, v2, "Equivalent");
    }

    @Test
    public void construct_notEqualsA() {
        final ImmutableVectorN v1 = ImmutableVectorN.create(0.0);
        final ImmutableVectorN v2 = ImmutableVectorN.create(1.0);

        assertInvariants(v1, v2);
        assertNotEquals(v1, v2, "Not equivalent");
    }

    @Test
    public void construct_notEqualsB() {
        final ImmutableVectorN v1 = ImmutableVectorN.create(0.0);
        final ImmutableVectorN v2 = ImmutableVectorN.create(0.0, 1.0);

        assertInvariants(v1, v2);
        assertNotEquals(v1, v2, "Not equivalent");
    }

    @Test
    public void create_10() {
        final ImmutableVectorN x = create(0.0);

        assertEquals(0.0, x.magnitude2(), Double.MIN_NORMAL, "magnitude^2");
        assertEquals(0.0, x.magnitude(), Double.MIN_NORMAL, "magnitude");
    }

    @Test
    public void create_1B() {
        final ImmutableVectorN x = create(-1.0);

        assertEquals(1.0, x.magnitude2(), Double.MIN_NORMAL, "magnitude^2");
        assertEquals(1.0, x.magnitude(), Double.MIN_NORMAL, "magnitude");
    }

    @Test
    public void create_1Max() {
        final double xI = Double.MAX_VALUE;
        final ImmutableVectorN x = create(xI);

        assertEquals(xI, x.magnitude(), xI * 1.0E-6, "magnitude");
    }

    @Test
    public void create_1Nan() {
        final ImmutableVectorN x = create(Double.POSITIVE_INFINITY);

        final double magnitude2 = x.magnitude2();
        final double magnitude = x.magnitude();
        assertEquals(Double.doubleToLongBits(magnitude2), Double.doubleToLongBits(Double.POSITIVE_INFINITY),
                "magnitude^2 <" + magnitude2 + "> (bits)");
        assertEquals(Double.doubleToLongBits(magnitude), Double.doubleToLongBits(Double.POSITIVE_INFINITY),
                "magnitude <" + magnitude + "> (bits)");
    }

    @Test
    public void create_2A() {
        final ImmutableVectorN x = create(0.0, 1.0);

        assertEquals(1.0, x.magnitude(), Double.MIN_NORMAL, "magnitude");
    }

    @Test
    public void create_2B() {
        final ImmutableVectorN x = create(1.0, 1.0);

        assertEquals(Math.sqrt(2.0), x.magnitude(), Double.MIN_NORMAL, "magnitude");
    }

    @Test
    public void create01() {
        create0(1);
    }

    @Test
    public void create02() {
        create0(2);
    }

    @Test
    public void createOnLine_A() {
        final ImmutableVectorN x0 = ImmutableVectorN.create(0.0);
        final ImmutableVectorN dx = ImmutableVectorN.create(1.0);
        final double w = 1.0;

        final ImmutableVectorN x = createOnLine(x0, dx, w);

        assertEquals(1.0, x.get(0), Double.MIN_NORMAL, "x");
    }

    @Test
    public void createOnLine_B() {
        final ImmutableVectorN x0 = ImmutableVectorN.create(1.0);
        final ImmutableVectorN dx = ImmutableVectorN.create(1.0);
        final double w = 1.0;

        final ImmutableVectorN x = createOnLine(x0, dx, w);

        assertEquals(2.0, x.get(0), Double.MIN_NORMAL, "x");
    }

    @Test
    public void createOnLine_C() {
        final ImmutableVectorN x0 = ImmutableVectorN.create(0.0);
        final ImmutableVectorN dx = ImmutableVectorN.create(2.0);
        final double w = 1.0;

        final ImmutableVectorN x = createOnLine(x0, dx, w);

        assertEquals(2.0, x.get(0), Double.MIN_NORMAL, "x");
    }

    @Test
    public void createOnLine_D() {
        final ImmutableVectorN x0 = ImmutableVectorN.create(0.0);
        final ImmutableVectorN dx = ImmutableVectorN.create(1.0);
        final double w = 2.0;

        final ImmutableVectorN x = createOnLine(x0, dx, w);

        assertEquals(2.0, x.get(0), Double.MIN_NORMAL, "x");
    }

    @Test
    public void dot_A() {
        final double d = ImmutableVectorN.create(1.0).dot(ImmutableVectorN.create(1.0));
        assertEquals(1.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_B() {
        final double d = ImmutableVectorN.create(2.0).dot(ImmutableVectorN.create(1.0));
        assertEquals(2.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_C() {
        final double d = ImmutableVectorN.create(1.0).dot(ImmutableVectorN.create(2.0));
        assertEquals(2.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_D() {
        final double d = ImmutableVectorN.create(2.0).dot(ImmutableVectorN.create(2.0));
        assertEquals(4.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void dot_E() {
        final double d = ImmutableVectorN.create(1.0, 1.0).dot(ImmutableVectorN.create(1.0, 1.0));
        assertEquals(2.0, d, Double.MIN_NORMAL, "dot product");
    }

    @Test
    public void mean_1A() {
        final ImmutableVectorN mean = mean(ImmutableVectorN.create(1.0), ImmutableVectorN.create(1.0));
        assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_1B() {
        final ImmutableVectorN mean = mean(ImmutableVectorN.create(1.0), ImmutableVectorN.create(-1.0));
        assertEquals(0.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_1C() {
        final ImmutableVectorN mean = mean(ImmutableVectorN.create(2.0), ImmutableVectorN.create(0.0));
        assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_1D() {
        final ImmutableVectorN mean = mean(ImmutableVectorN.create(0.0), ImmutableVectorN.create(2.0));
        assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
    }

    @Test
    public void mean_2() {
        final ImmutableVectorN mean = mean(ImmutableVectorN.create(1.0, 2.0), ImmutableVectorN.create(3.0, 4.0));
        assertEquals(2.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
        assertEquals(3.0, mean.get(1), Double.MIN_NORMAL, "mean[1]");
    }

    @Test
    public void minus_0() {
        minus(ImmutableVectorN.create(0.0));
    }

    @Test
    public void minus_1A() {
        minus(ImmutableVectorN.create(1.0));
    }

    @Test
    public void minus_1B() {
        minus(ImmutableVectorN.create(-2.0));
    }

    @Test
    public void minus_1Infinity() {
        minus(ImmutableVectorN.create(Double.POSITIVE_INFINITY));
    }

    @Test
    public void minus_1Nan() {
        minus(ImmutableVectorN.create(Double.NaN));
    }

    @Test
    public void minus_2() {
        minus(ImmutableVectorN.create(1.0, 2.0));
    }

    @Test
    public void minus_vector0A() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(0);
        final ImmutableVectorN x2 = ImmutableVectorN.create(0);

        minus(x1, x2);
    }

    @Test
    public void minus_vector0B() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(1);
        final ImmutableVectorN x2 = ImmutableVectorN.create(0);

        minus(x1, x2);
    }

    @Test
    public void minus_vector0C() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(1, 2);
        final ImmutableVectorN x2 = ImmutableVectorN.create(0, 0);

        minus(x1, x2);
    }

    @Test
    public void minus_vectorA() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(2);
        final ImmutableVectorN x2 = ImmutableVectorN.create(1);

        minus(x1, x2);
    }

    @Test
    public void minus_vectorB() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(2);
        final ImmutableVectorN x2 = ImmutableVectorN.create(-1);

        minus(x1, x2);
    }

    @Test
    public void minus_vectorC() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(1, 2);
        final ImmutableVectorN x2 = ImmutableVectorN.create(3, 4);

        minus(x1, x2);
    }

    @Test
    public void plus_0A() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(0);
        final ImmutableVectorN x2 = ImmutableVectorN.create(0);

        plus(x1, x2);
    }

    @Test
    public void plus_0B() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(1);
        final ImmutableVectorN x2 = ImmutableVectorN.create(0);

        plus(x1, x2);
    }

    @Test
    public void plus_0C() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(1, 2);
        final ImmutableVectorN x2 = ImmutableVectorN.create(0, 0);

        plus(x1, x2);
    }

    @Test
    public void plus_A() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(2);
        final ImmutableVectorN x2 = ImmutableVectorN.create(1);

        plus(x1, x2);
    }

    @Test
    public void plus_B() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(2);
        final ImmutableVectorN x2 = ImmutableVectorN.create(-1);

        plus(x1, x2);
    }

    @Test
    public void plus_C() {
        final ImmutableVectorN x1 = ImmutableVectorN.create(1, 2);
        final ImmutableVectorN x2 = ImmutableVectorN.create(3, 4);

        plus(x1, x2);
    }

    @Test
    public void scale_1A() {
        final ImmutableVectorN scaled = scale(ImmutableVectorN.create(1.0), 0.0);

        assertEquals(0.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
    }

    @Test
    public void scale_1B() {
        final ImmutableVectorN scaled = scale(ImmutableVectorN.create(0.0), 1.0);

        assertEquals(0.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
    }

    @Test
    public void scale_1C() {
        final ImmutableVectorN scaled = scale(ImmutableVectorN.create(1.0), 1.0);

        assertEquals(1.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
    }

    @Test
    public void scale_1D() {
        final ImmutableVectorN scaled = scale(ImmutableVectorN.create(1.0), -2.0);

        assertEquals(-2.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
    }

    @Test
    public void scale_2() {
        final ImmutableVectorN scaled = scale(ImmutableVectorN.create(1.0, 2.0), 4.0);

        assertEquals(4.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
        assertEquals(8.0, scaled.get(1), Double.MIN_NORMAL, "scaled[1]");
    }

    @Test
    public void sum_multiple1A() {
        sum_multiple1(1.0);
    }

    @Test
    public void sum_multiple1B() {
        sum_multiple1(0.0);
    }

    @Test
    public void sum_multiple1C() {
        sum_multiple1(-1.0);
    }

    @Test
    public void sum_multiple1D() {
        final ImmutableVectorN sum = sum(new ImmutableVectorN[] { ImmutableVectorN.create(1.0, 3.0) });

        assertEquals(1.0, sum.get(0), Double.MIN_NORMAL, "sum[0]");
        assertEquals(3.0, sum.get(1), Double.MIN_NORMAL, "sum[1]");
    }

    @Test
    public void sum_multiple2A() {
        sum_multiple2(1.0, 0.0);
    }

    @Test
    public void sum_multiple2B() {
        sum_multiple2(0.0, 1.0);
    }

    @Test
    public void sum_multiple2C() {
        sum_multiple2(1.0, 1.0);
    }

    @Test
    public void sum_multiple2D() {
        sum_multiple2(1.0, 2.0);
    }

    @Test
    public void weightedSum_11A() {
        weightedSum_11(0.0, 1.0);
    }

    @Test
    public void weightedSum_11B() {
        weightedSum_11(1.0, 0.0);
    }

    @Test
    public void weightedSum_11C() {
        weightedSum_11(1.0, 1.0);
    }

    @Test
    public void weightedSum_11D() {
        weightedSum_11(2.0, 1.0);
    }

    @Test
    public void weightedSum_11E() {
        weightedSum_11(1.0, 2.0);
    }

    @Test
    public void weightedSum_12() {
        final ImmutableVectorN sum = weightedSum(new double[] { 2.0 },
                new ImmutableVectorN[] { ImmutableVectorN.create(1.0, 3.0) });

        assertEquals(2.0, sum.get(0), Double.MIN_NORMAL, "sum[0]");
        assertEquals(6.0, sum.get(1), Double.MIN_NORMAL, "sum[1]");
    }

    @Test
    public void weightedSum_21() {
        final ImmutableVectorN sum = weightedSum(new double[] { 1.0, 2.0 },
                new ImmutableVectorN[] { ImmutableVectorN.create(3.0), ImmutableVectorN.create(5.0) });

        assertEquals(13.0, sum.get(0), Double.MIN_NORMAL, "sum[0]");
    }

}
