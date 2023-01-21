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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import uk.badamson.dbc.assertions.ObjectVerifier;

import javax.annotation.Nonnull;

@SuppressWarnings("UnusedReturnValue")
public class ImmutableMatrixNTest {

    public static void assertInvariants(final ImmutableMatrixN matrix) {
        ObjectVerifier.assertInvariants(matrix);// inherited
        MatrixTest.assertInvariants(matrix);// inherited
    }

    public static void assertInvariants(final ImmutableMatrixN matrix1, final ImmutableMatrixN matrix2) {
        ObjectVerifier.assertInvariants(matrix1, matrix2);// inherited
        MatrixTest.assertInvariants(matrix1, matrix2);// inherited
    }

    private static ImmutableMatrixN create(final int rows, final int columns, final double[] elements) {
        final ImmutableMatrixN matrix = ImmutableMatrixN.create(rows, columns, elements);

        assertNotNull(matrix, "Always creates a matrix");// guard
        assertInvariants(matrix);
        assertEquals(rows, matrix.getRows(), "rows");
        assertEquals(columns, matrix.getColumns(), "columns");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertEquals(Double.doubleToLongBits(elements[i * columns + j]),
                        Double.doubleToLongBits(matrix.get(i, j)), "element [" + i + "," + j + "] bits");
            }
        }
        return matrix;
    }


    @Nonnull
    private static ImmutableMatrixN minus(@Nonnull final ImmutableMatrixN m) {
        final ImmutableMatrixN result = (ImmutableMatrixN) MatrixTest.minus(m);
        assertInvariants(result);
        assertInvariants(m, result);
        return result;
    }

    @Nonnull
    private static ImmutableMatrixN minus(@Nonnull ImmutableMatrixN m, @Nonnull Matrix that) {
        final ImmutableMatrixN result = (ImmutableMatrixN) MatrixTest.minus(m, that);
        assertInvariants(result);
        assertInvariants(m, result);
        return result;
    }

    @Nonnull
    private static ImmutableMatrixN plus(@Nonnull ImmutableMatrixN m, @Nonnull Matrix that) {
        final ImmutableMatrixN result = (ImmutableMatrixN) MatrixTest.plus(m, that);
        assertInvariants(result);
        assertInvariants(m, result);
        return result;
    }

    @Nonnull
    private static ImmutableMatrixN scale(@Nonnull final ImmutableMatrixN m, double f) {
        final ImmutableMatrixN result = (ImmutableMatrixN) MatrixTest.scale(m, f);
        assertInvariants(result);
        assertInvariants(m, result);
        return result;
    }

    @Nonnull
    private static ImmutableVectorN multiply(@Nonnull final ImmutableMatrixN a, @Nonnull final Vector x) {
        final ImmutableVectorN ax = (ImmutableVectorN) MatrixTest.multiply(a, x);// inherited

        assertInvariants(a);
        ImmutableVectorNTest.assertInvariants(ax);

        return ax;
    }

    @Nested
    public class Create {

        @Test
        public void create_2x3() {
            create(2, 3, new double[]{1, 2, 3, 4, 5, 6});
        }

        @Nested
        public class Create1x1 {

            private static void test(final double x) {
                create(1, 1, new double[]{x});
            }

            @Test
            public void zero() {
                test(0.0);
            }

            @Test
            public void one() {
                test(1.0);
            }

            @Test
            public void nan() {
                test(Double.NaN);
            }
        }

        @Nested
        public class Create2Equals {

            @SuppressWarnings("SameParameterValue")
            private static void test(final int rows, final int columns, final double[] elements) {
                final ImmutableMatrixN matrix1 = ImmutableMatrixN.create(rows, columns, elements);
                final ImmutableMatrixN matrix2 = ImmutableMatrixN.create(rows, columns, elements);

                assertInvariants(matrix1, matrix2);
                assertEquals(matrix1, matrix2, "Equivalent");
            }

            @Nested
            public class Create2Equals1x1 {
                private static void test(final double x) {
                    Create2Equals.test(1, 1, new double[]{x});
                }

                @Test
                public void zero() {
                    test(0.0);
                }

                @Test
                public void one() {
                    test(1.0);
                }

                @Test
                public void nan() {
                    test(Double.NaN);
                }

            }

            @Nested
            public class Create2Equals2x1 {

                private static void test(final double x11, final double x21) {
                    Create2Equals.test(2, 1, new double[]{x11, x21});
                }

                @Test
                public void a() {
                    test(0.0, 1.0);
                }

                @Test
                public void b() {
                    test(1.0, 4.0);
                }

            }

        }

        @Nested
        public class Create2NotEquals {

            @Test
            public void sameElementArrayUsedForBoth() {
                final double[] elements = new double[]{1.0, 2.1};
                final ImmutableMatrixN matrix1 = ImmutableMatrixN.create(2, 1, elements);
                final ImmutableMatrixN matrix2 = ImmutableMatrixN.create(1, 2, elements);

                assertInvariants(matrix1, matrix2);
                assertNotEquals(matrix1, matrix2, "Not equivalent");
            }

            @Nested
            public class Create2NotEquals1x1 {

                private static void test(final double x1, final double x2) {
                    final ImmutableMatrixN matrix1 = ImmutableMatrixN.create(1, 1, new double[]{x1});
                    final ImmutableMatrixN matrix2 = ImmutableMatrixN.create(1, 1, new double[]{x2});

                    assertInvariants(matrix1, matrix2);
                    assertNotEquals(matrix1, matrix2, "Not equivalent");
                }

                @Test
                public void a() {
                    test(1.0, 2.0);
                }

                @Test
                public void b() {
                    test(3.0, 5.0);
                }

            }

        }

        @Nested
        public class CreateVector {


            private static ImmutableMatrixN test(final double... elements) {
                return create(elements.length, 1, elements);
            }

            @Test
            public void t0wA() {
                test(1.0, 2.0);
            }

            @Test
            public void twoB() {
                test(3.0, 5.0);
            }

            @Test
            public void three() {
                test(1.0, 2.0, 3.0);
            }

        }

    }

    @Nested
    public class Multiply {

        @Nested
        public class Multiply1x1 {

            private static void test(final double a11, final double x11) {
                final var a = ImmutableMatrixN.create(1, 1, new double[]{a11});
                final var x = ImmutableVectorN.create(x11);

                final ImmutableVectorN ax = multiply(a, x);

                assertEquals(a11 * x11, ax.get(0), Double.MIN_NORMAL, "product element");
            }

            @Test
            public void a() {
                test(0.0, 0.0);
            }

            @Test
            public void b() {
                test(1.0, 2.0);
            }

            @Test
            public void c() {
                test(-2.0, 3.0);
            }

        }

        @Nested
        public class Multiply1x2 {
            private static void test(final double a11, final double a12, final double x11, final double x21) {
                final var a = ImmutableMatrixN.create(1, 2, new double[]{a11, a12});
                final var x = ImmutableVectorN.create(x11, x21);

                final ImmutableVectorN ax = multiply(a, x);

                assertEquals(a11 * x11 + a12 * x21, ax.get(0), Double.MIN_NORMAL, "product element");
            }

            @Test
            public void a() {
                test(1.0, 2.0, 3.0, 4.0);
            }

            @Test
            public void b() {
                test(2.0, 3.0, 5.0, 7.0);
            }

        }

        @Nested
        public class Multiply2x1 {
            private static void test(final double a11, final double a21, final double x11) {
                final var a = ImmutableMatrixN.create(2, 1, new double[]{a11, a21});
                final var x = ImmutableVectorN.create(x11);

                final ImmutableVectorN ax = multiply(a, x);

                assertEquals(a11 * x11, ax.get(0), Double.MIN_NORMAL, "ax[0]");
                assertEquals(a21 * x11, ax.get(1), Double.MIN_NORMAL, "ax[1]");
            }

            @Test
            public void a() {
                test(1.0, 2.0, 3.0);
            }

            @Test
            public void b() {
                test(2.0, 3.0, 5.0);
            }

        }
    }

    @Nested
    public class UnaryMinus {
        @Test
        public void unaryMinus2x3() {
            final var m = ImmutableMatrixN.create(2, 3, new double[]{1, 2, 3, 4, 5, 6});
            minus(m);
        }

        @Nested
        public class UnaryMinus1x1 {

            private static void test(double m11) {
                final var m = ImmutableMatrixN.create(1, 1, new double[]{m11});
                minus(m);
            }

            @Test
            public void zero() {
                test(0.0);
            }

            @Test
            public void positive() {
                test(1.0);
            }

            @Test
            public void negative() {
                test(-2.0);
            }
        }
    }

    @Nested
    public class Scale {
        @Test
        public void scale2x3() {
            final var m = ImmutableMatrixN.create(2, 3, new double[]{1, 2, 3, 4, 5, 6});
            scale(m, 2.0);
        }

        @Nested
        public class Scale1x1 {

            private static void test(double m11, double f) {
                final var m = ImmutableMatrixN.create(1, 1, new double[]{m11});
                scale(m, f);
            }

            @Test
            public void zero() {
                test(0.0, 0.0);
            }

            @Test
            public void positive() {
                test(1.0, 1.0);
            }

            @Test
            public void negative() {
                test(-2.0, 3.0);
            }
        }
    }

    @Nested
    public class Plus {

        @Test
        public void plus2x3() {
            final var a = ImmutableMatrixN.create(2, 3, new double[]{1, 2, 3, 4, 5, 6});
            final var b = ImmutableMatrixN.create(2, 3, new double[]{6, 5, 4, 3, 2, 1});
            plus(a, b);
        }

        @Nested
        public class Plus1x1 {
            private static void test(double a11, double b11) {
                final var a = ImmutableMatrixN.create(1, 1, new double[]{a11});
                final var b = ImmutableMatrixN.create(1, 1, new double[]{b11});
                plus(a, b);
            }

            @Test
            public void zero() {
                test(0, 0);
            }

            @Test
            public void positive() {
                test(1, 2);
            }

            @Test
            public void negative() {
                test(-2, 3);
            }
        }
    }


    @Nested
    public class BinaryMinus {
        @Test
        public void binaryMinus2x3() {
            final var a = ImmutableMatrixN.create(2, 3, new double[]{1, 2, 3, 4, 5, 6});
            final var b = ImmutableMatrixN.create(2, 3, new double[]{6, 5, 4, 3, 2, 1});
            minus(a, b);
        }

        @Nested
        public class BinaryMinus1x1 {

            private static void test(double a11, double b11) {
                final var a = ImmutableMatrixN.create(1, 1, new double[]{a11});
                final var b = ImmutableMatrixN.create(1, 1, new double[]{b11});
                minus(a, b);
            }

            @Test
            public void zero() {
                test(0.0, 0.0);
            }

            @Test
            public void positive() {
                test(2.0, 1.0);
            }

            @Test
            public void negative() {
                test(1.0, -2.0);
            }
        }
    }
}
