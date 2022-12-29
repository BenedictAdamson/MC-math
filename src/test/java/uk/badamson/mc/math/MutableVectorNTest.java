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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class MutableVectorNTest {

    public static void assertInvariants(@Nonnull final MutableVectorN x) {
        MutableMatrixNTest.assertInvariants(x);// inherited
        VectorTest.assertInvariants(x);// inherited

        final var dimensions = x.getDimension();
        final var columns = x.getColumns();
        assertAll(
                () -> assertThat("columns", columns, is(1)),
                () -> assertThat("dimensions", dimensions, greaterThan(0))
        );
    }

    public static void assertInvariants(@Nonnull final MutableVectorN x1, @Nonnull final MutableVectorN x2) {
        MutableMatrixNTest.assertInvariants(x1, x2);// inherited
        VectorTest.assertInvariants(x1, x2);// inherited
    }

    private static MutableVectorN create(final double... x) {
        final MutableVectorN v = MutableVectorN.create(x);

        assertInvariants(v);
        assertEquals(x.length, v.getDimension(), "dimension");
        for (int i = 0; i < x.length; i++) {
            assertEquals(x[i], v.get(i), Double.MIN_NORMAL, "x[" + i + "]");
        }

        return v;
    }

    private static MutableVectorN create0(final int dimension) {
        final MutableVectorN zero = MutableVectorN.create0(dimension);

        assertNotNull(zero, "Always returns a vector");// guard
        assertInvariants(zero);
        assertEquals(dimension, zero.getDimension(), "dimension");
        for (int i = 0; i < dimension; ++i) {
            assertEquals(0.0, zero.get(i), Double.MIN_NORMAL, "The elements of the zero vector are all zero.");
        }

        return zero;
    }

    private static MutableVectorN mean(final MutableVectorN x, final MutableVectorN that) {
        final MutableVectorN mean = (MutableVectorN) MatrixTest.mean(x, that);// inherited

        assertInvariants(mean);
        assertInvariants(x, mean);
        assertInvariants(that, mean);

        return mean;
    }

    public static MutableVectorN mean(final MutableVectorN x, final Vector that) {
        final MutableVectorN mean = (MutableVectorN) MatrixTest.mean(x, that);// inherited

        assertInvariants(mean);
        assertInvariants(x, mean);

        return mean;
    }

    public static MutableVectorN minus(final MutableVectorN x) {
        final MutableVectorN minus = (MutableVectorN) VectorTest.minus(x);// inherited

        assertInvariants(minus);
        assertInvariants(x, minus);

        return minus;
    }

    private static MutableVectorN minus(final MutableVectorN x, final MutableVectorN that) {
        final MutableVectorN diff = (MutableVectorN) VectorTest.minus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);
        assertInvariants(diff, that);
        return diff;
    }

    public static MutableVectorN minus(final MutableVectorN x, final Vector that) {
        final MutableVectorN diff = (MutableVectorN) VectorTest.minus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);

        return diff;
    }

    private static MutableVectorN plus(final MutableVectorN x, final MutableVectorN that) {
        final MutableVectorN diff = (MutableVectorN) VectorTest.plus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);
        assertInvariants(diff, that);

        return diff;
    }

    public static MutableVectorN plus(final MutableVectorN x, final Vector that) {
        final MutableVectorN diff = (MutableVectorN) VectorTest.plus(x, that);// inherited

        assertInvariants(diff);
        assertInvariants(diff, x);

        return diff;
    }

    public static MutableVectorN scale(final MutableVectorN x, final double f) {
        final MutableVectorN scaled = (MutableVectorN) VectorTest.scale(x, f);// inherited

        assertInvariants(scaled);
        assertInvariants(x, scaled);

        return scaled;
    }

    private static MutableVectorN sum(final MutableVectorN... x) {
        final MutableVectorN sum = MutableVectorN.sum(x);

        assertNotNull(sum, "Always returns a sum vector.");// guard
        assertInvariants(sum);
        for (final MutableVectorN xi : x) {
            assertInvariants(sum, xi);
        }

        assertEquals(x[0].getDimension(), sum.getDimension(),
                "The dimension of the sum equals the dimension of the summed vectors.");

        return sum;
    }

    private static MutableVectorN weightedSum(final double[] weight, final MutableVectorN[] x) {
        final MutableVectorN sum = MutableVectorN.weightedSum(weight, x);

        assertNotNull(sum, "Always returns a sum vector.");// guard
        assertInvariants(sum);
        for (final MutableVectorN xi : x) {
            assertInvariants(sum, xi);
        }

        assertEquals(x[0].getDimension(), sum.getDimension(),
                "The dimension of the sum equals the dimension of the summed vectors.");

        return sum;
    }

    private static MutableVectorN copyOf(@Nonnull Vector x) {
        final var copy = MutableVectorN.copyOf(x);

        assertThat(copy, notNullValue());
        assertInvariants(copy);
        VectorTest.assertInvariants(x);
        VectorTest.assertInvariants(x, copy);

        final var dimension = copy.getDimension();
        assertThat(dimension, is(x.getDimension()));
        for (int i = 0; i < dimension; ++i) {
            assertThat("[" + i + "]", copy.get(i), is(x.get(i)));
        }

        return copy;
    }

    public static double[] getComponentsAsArray(final MutableVectorN x) {
        final var result = VectorTest.getComponentsAsArray(x);// inherited
        assertInvariants(x);
        return result;
    }

    private static void set(@Nonnull final MutableVectorN v, @Nonnegative final int i, final double x) {
        v.set(i, x);

        assertInvariants(v);
        assertThat(v.get(i), is(x));
    }

    @Nested
    public class Create {


        @Test
        public void zero() {
            final MutableVectorN x = create(0.0);

            assertEquals(0.0, x.magnitude2(), Double.MIN_NORMAL, "magnitude^2");
            assertEquals(0.0, x.magnitude(), Double.MIN_NORMAL, "magnitude");
        }

        @Test
        public void minusOne() {
            final MutableVectorN x = create(-1.0);

            assertEquals(1.0, x.magnitude2(), Double.MIN_NORMAL, "magnitude^2");
            assertEquals(1.0, x.magnitude(), Double.MIN_NORMAL, "magnitude");
        }

        @Test
        public void max() {
            final double xI = Double.MAX_VALUE;
            final MutableVectorN x = create(xI);

            assertEquals(xI, x.magnitude(), xI * 1.0E-6, "magnitude");
        }

        @Test
        public void nan() {
            final MutableVectorN x = create(Double.POSITIVE_INFINITY);

            final double magnitude2 = x.magnitude2();
            final double magnitude = x.magnitude();
            assertEquals(Double.doubleToLongBits(magnitude2), Double.doubleToLongBits(Double.POSITIVE_INFINITY),
                    "magnitude^2 <" + magnitude2 + "> (bits)");
            assertEquals(Double.doubleToLongBits(magnitude), Double.doubleToLongBits(Double.POSITIVE_INFINITY),
                    "magnitude <" + magnitude + "> (bits)");
        }

        @Test
        public void twoDimensionalA() {
            final MutableVectorN x = create(0.0, 1.0);

            assertEquals(1.0, x.magnitude(), Double.MIN_NORMAL, "magnitude");
        }

        @Test
        public void twoDimensionalB() {
            final MutableVectorN x = create(1.0, 1.0);

            assertEquals(Math.sqrt(2.0), x.magnitude(), Double.MIN_NORMAL, "magnitude");
        }


        @Nested
        public class TwoEquivalent {

            @Test
            public void twoDimensional() {
                final double x1 = 0.0;
                final double x2 = 1.0;
                final var v1 = MutableVectorN.create(x1, x2);
                final var v2 = MutableVectorN.create(x1, x2);

                assertInvariants(v1, v2);
                assertEquals(v1, v2, "Equivalent");
            }

            @Nested
            public class OneDimensional {


                @Test
                public void a() {
                    test(0.0);
                }

                @Test
                public void b() {
                    test(1.0);
                }

                @Test
                public void withNan() {
                    test(Double.POSITIVE_INFINITY);
                }

                private void test(final double x) {
                    final MutableVectorN v1 = MutableVectorN.create(x);
                    final MutableVectorN v2 = MutableVectorN.create(x);

                    assertInvariants(v1, v2);
                    assertEquals(v1, v2, "Equivalent");
                }
            }

        }

        @Nested
        public class TwoDifferent {


            @Test
            public void sameDimension() {
                final MutableVectorN v1 = MutableVectorN.create(0.0);
                final MutableVectorN v2 = MutableVectorN.create(1.0);

                assertInvariants(v1, v2);
                assertNotEquals(v1, v2, "Not equivalent");
            }

            @Test
            public void differentDimensions() {
                final MutableVectorN v1 = MutableVectorN.create(0.0);
                final MutableVectorN v2 = MutableVectorN.create(0.0, 1.0);

                assertInvariants(v1, v2);
                assertNotEquals(v1, v2, "Not equivalent");
            }
        }
    }

    @Nested
    public class Create0 {

        @Test
        public void one() {
            create0(1);
        }

        @Test
        public void two() {
            create0(2);
        }
    }

    @Nested
    public class Dot {

        @Test
        public void a() {
            final double d = MutableVectorN.create(1.0).dot(MutableVectorN.create(1.0));
            assertEquals(1.0, d, Double.MIN_NORMAL, "dot product");
        }

        @Test
        public void b() {
            final double d = MutableVectorN.create(2.0).dot(MutableVectorN.create(1.0));
            assertEquals(2.0, d, Double.MIN_NORMAL, "dot product");
        }

        @Test
        public void c() {
            final double d = MutableVectorN.create(1.0).dot(MutableVectorN.create(2.0));
            assertEquals(2.0, d, Double.MIN_NORMAL, "dot product");
        }

        @Test
        public void d() {
            final double d = MutableVectorN.create(2.0).dot(MutableVectorN.create(2.0));
            assertEquals(4.0, d, Double.MIN_NORMAL, "dot product");
        }

        @Test
        public void e() {
            final double d = MutableVectorN.create(1.0, 1.0).dot(MutableVectorN.create(1.0, 1.0));
            assertEquals(2.0, d, Double.MIN_NORMAL, "dot product");
        }
    }

    @Nested
    public class Mean {

        @Test
        public void twoDimensional() {
            final MutableVectorN mean = mean(MutableVectorN.create(1.0, 2.0), MutableVectorN.create(3.0, 4.0));
            assertEquals(2.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
            assertEquals(3.0, mean.get(1), Double.MIN_NORMAL, "mean[1]");
        }

        @Nested
        public class OneDimensional {

            @Test
            public void a() {
                final MutableVectorN mean = mean(MutableVectorN.create(1.0), MutableVectorN.create(1.0));
                assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
            }

            @Test
            public void b() {
                final MutableVectorN mean = mean(MutableVectorN.create(1.0), MutableVectorN.create(-1.0));
                assertEquals(0.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
            }

            @Test
            public void c() {
                final MutableVectorN mean = mean(MutableVectorN.create(2.0), MutableVectorN.create(0.0));
                assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
            }

            @Test
            public void d() {
                final MutableVectorN mean = mean(MutableVectorN.create(0.0), MutableVectorN.create(2.0));
                assertEquals(1.0, mean.get(0), Double.MIN_NORMAL, "mean[0]");
            }

        }
    }

    @Nested
    public class UnaryMinus {

        @Test
        public void twoDimensional() {
            minus(MutableVectorN.create(1.0, 2.0));
        }

        @Nested
        public class OneDimensional {
            @Test
            public void zero() {
                minus(MutableVectorN.create(0.0));
            }

            @Test
            public void minusOne() {
                minus(MutableVectorN.create(1.0));
            }

            @Test
            public void minusTwo() {
                minus(MutableVectorN.create(-2.0));
            }

            @Test
            public void infinity() {
                minus(MutableVectorN.create(Double.POSITIVE_INFINITY));
            }

            @Test
            public void minus_1Nan() {
                minus(MutableVectorN.create(Double.NaN));
            }

        }

    }

    @Nested
    public class BinaryMinus {


        @Nested
        public class OneDimensional {


            @Test
            public void zeroes() {
                final MutableVectorN x1 = MutableVectorN.create(0);
                final MutableVectorN x2 = MutableVectorN.create(0);

                minus(x1, x2);
            }

            @Test
            public void firstNonZero() {
                final MutableVectorN x1 = MutableVectorN.create(1);
                final MutableVectorN x2 = MutableVectorN.create(0);

                minus(x1, x2);
            }

            @Test
            public void bothPositive() {
                final MutableVectorN x1 = MutableVectorN.create(2);
                final MutableVectorN x2 = MutableVectorN.create(1);

                minus(x1, x2);
            }

            @Test
            public void secondNegative() {
                final MutableVectorN x1 = MutableVectorN.create(2);
                final MutableVectorN x2 = MutableVectorN.create(-1);

                minus(x1, x2);
            }

        }

        @Nested
        public class TwoDimensional {

            @Test
            public void secondZero() {
                final MutableVectorN x1 = MutableVectorN.create(1, 2);
                final MutableVectorN x2 = MutableVectorN.create(0, 0);

                minus(x1, x2);
            }

            @Test
            public void bothNonZero() {
                final MutableVectorN x1 = MutableVectorN.create(1, 2);
                final MutableVectorN x2 = MutableVectorN.create(3, 4);

                minus(x1, x2);
            }

        }

    }

    @Nested
    public class Plus {

        @Nested
        public class OneDimensional {

            @Test
            public void bothZero() {
                final MutableVectorN x1 = MutableVectorN.create(0);
                final MutableVectorN x2 = MutableVectorN.create(0);

                plus(x1, x2);
            }

            @Test
            public void secondZero() {
                final MutableVectorN x1 = MutableVectorN.create(1);
                final MutableVectorN x2 = MutableVectorN.create(0);

                plus(x1, x2);
            }

            @Test
            public void bothPositive() {
                final MutableVectorN x1 = MutableVectorN.create(2);
                final MutableVectorN x2 = MutableVectorN.create(1);

                plus(x1, x2);
            }

            @Test
            public void oneNegative() {
                final MutableVectorN x1 = MutableVectorN.create(2);
                final MutableVectorN x2 = MutableVectorN.create(-1);

                plus(x1, x2);
            }

        }

        @Nested
        public class TwoDimensional {

            @Test
            public void oneZero() {
                final MutableVectorN x1 = MutableVectorN.create(1, 2);
                final MutableVectorN x2 = MutableVectorN.create(0, 0);

                plus(x1, x2);
            }

            @Test
            public void bothNonZero() {
                final MutableVectorN x1 = MutableVectorN.create(1, 2);
                final MutableVectorN x2 = MutableVectorN.create(3, 4);

                plus(x1, x2);
            }

        }

    }

    @Nested
    public class Scale {

        @Test
        public void twoDimensional() {
            final MutableVectorN scaled = scale(MutableVectorN.create(1.0, 2.0), 4.0);

            assertEquals(4.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
            assertEquals(8.0, scaled.get(1), Double.MIN_NORMAL, "scaled[1]");
        }

        @Nested
        public class OneDimensional {

            @Test
            public void oneByZero() {
                final MutableVectorN scaled = scale(MutableVectorN.create(1.0), 0.0);

                assertEquals(0.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
            }

            @Test
            public void zeroByOne() {
                final MutableVectorN scaled = scale(MutableVectorN.create(0.0), 1.0);

                assertEquals(0.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
            }

            @Test
            public void byUnity() {
                final MutableVectorN scaled = scale(MutableVectorN.create(1.0), 1.0);

                assertEquals(1.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
            }

            @Test
            public void byNegative() {
                final MutableVectorN scaled = scale(MutableVectorN.create(1.0), -2.0);

                assertEquals(-2.0, scaled.get(0), Double.MIN_NORMAL, "scaled[0]");
            }

        }

    }

    @Nested
    public class SumMultiple {

        @Nested
        public class OneArgument {

            @Test
            public void twoDimensional() {
                final MutableVectorN sum = sum(MutableVectorN.create(1.0, 3.0));

                assertEquals(1.0, sum.get(0), Double.MIN_NORMAL, "sum[0]");
                assertEquals(3.0, sum.get(1), Double.MIN_NORMAL, "sum[1]");
            }

            @Nested
            public class OneDimensional {

                private static void test(final double x) {
                    final MutableVectorN sum = sum(MutableVectorN.create(x));

                    assertEquals(x, sum.get(0), Double.MIN_NORMAL, "sum[0]");
                }

                @Test
                public void sum_multiple1A() {
                    test(1.0);
                }

                @Test
                public void sum_multiple1B() {
                    test(0.0);
                }

                @Test
                public void sum_multiple1C() {
                    test(-1.0);
                }

            }

        }


        @Nested
        public class TwoArguments {
            private static void test(final double x1, final double x2) {
                final MutableVectorN sum = sum(
                        MutableVectorN.create(x1), MutableVectorN.create(x2));

                assertEquals(x1 + x2, sum.get(0), Double.MIN_NORMAL, "sum[0]");
            }

            @Test
            public void onePlusZero() {
                test(1.0, 0.0);
            }

            @Test
            public void zeroPlusOne() {
                test(0.0, 1.0);
            }

            @Test
            public void onePlusOne() {
                test(1.0, 1.0);
            }

            @Test
            public void onePlusTwo() {
                test(1.0, 2.0);
            }

        }
    }

    @Nested
    public class WeightedSum {

        @Test
        public void twoArguments() {
            final MutableVectorN sum = weightedSum(new double[]{1.0, 2.0},
                    new MutableVectorN[]{MutableVectorN.create(3.0), MutableVectorN.create(5.0)});

            assertEquals(13.0, sum.get(0), Double.MIN_NORMAL, "sum[0]");
        }

        @Nested
        public class SingleArgument {

            @Test
            public void twoDimensional() {
                final MutableVectorN sum = weightedSum(new double[]{2.0},
                        new MutableVectorN[]{MutableVectorN.create(1.0, 3.0)});

                assertEquals(2.0, sum.get(0), Double.MIN_NORMAL, "sum[0]");
                assertEquals(6.0, sum.get(1), Double.MIN_NORMAL, "sum[1]");
            }

            @Nested
            public class OneDimensional {

                private static void test(final double weight, final double x) {
                    final MutableVectorN sum = weightedSum(new double[]{weight},
                            new MutableVectorN[]{MutableVectorN.create(x)});

                    assertEquals(weight * x, sum.get(0), Double.MIN_NORMAL, "sum[0]");
                }

                @Test
                public void zeroWeight() {
                    test(0.0, 1.0);
                }

                @Test
                public void zeroComponent() {
                    test(1.0, 0.0);
                }

                @Test
                public void unityWeightAndComponent() {
                    test(1.0, 1.0);
                }

                @Test
                public void nonUnityWeight() {
                    test(2.0, 1.0);
                }

                @Test
                public void nonUnityComponent() {
                    test(1.0, 2.0);
                }

            }

        }

    }

    @Nested
    public class CopyOf {

        @Test
        public void ofImmutableVector3() {
            copyOf(ImmutableVector3.create(2.0, 3.0, 4.0));
        }

        @Nested
        public class OfMutableVectorN {

            @Test
            public void oneDimensional() {
                copyOf(MutableVectorN.create(1.0));
            }

            @Test
            public void twoDimensional() {
                copyOf(MutableVectorN.create(1.0, 2.0));
            }
        }

        @Nested
        public class OfImmutableVector1 {

            @Test
            public void a() {
                copyOf(ImmutableVector1.create(1.0));
            }

            @Test
            public void b() {
                copyOf(ImmutableVector1.create(2.0));
            }

        }
    }

    @Nested
    public class ComponentsAsArray {

        @Test
        public void zero() {
            test(0.0);
        }

        @Test
        public void nonZero() {
            test(1.0, 2.0, 3.0);
        }

        private void test(double... x) {
            final var v = MutableVectorN.create(x);
            getComponentsAsArray(v);
        }
    }

    @Nested
    public class Set {
        @Test
        public void oneDimension() {
            final var v = new MutableVectorN(1);
            set(v, 0, 2);
        }

        @Test
        public void twoDimension_0() {
            final var v = new MutableVectorN(1, 2);
            set(v, 0, 3);
            assertThat(v.get(1), is(2.0));
        }

        @Test
        public void twoDimension_1() {
            final var v = new MutableVectorN(1, 2);
            set(v, 1, 3);
            assertThat(v.get(0), is(1.0));
        }
    }
}
