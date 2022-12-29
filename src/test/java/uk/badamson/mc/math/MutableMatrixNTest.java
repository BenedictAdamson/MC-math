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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.badamson.dbc.assertions.ObjectVerifier;

import javax.annotation.Nonnegative;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("UnusedReturnValue")
public class MutableMatrixNTest {

    public static void assertInvariants(final MutableMatrixN matrix) {
        ObjectVerifier.assertInvariants(matrix);// inherited
        MatrixTest.assertInvariants(matrix);// inherited
    }

    public static void assertInvariants(final MutableMatrixN matrix1, final MutableMatrixN matrix2) {
        ObjectVerifier.assertInvariants(matrix1, matrix2);// inherited
        MatrixTest.assertInvariants(matrix1, matrix2);// inherited
    }

    private static MutableMatrixN create(final int rows, final int columns, final double[] elements) {
        final MutableMatrixN matrix = MutableMatrixN.create(rows, columns, elements);

        assertThat(matrix, notNullValue());
        assertInvariants(matrix);
        assertThat("rows", matrix.getRows(), is(rows));
        assertThat("columns", matrix.getColumns(), is(columns));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertThat("element [" + i + "," + j + "] bits",
                        Double.doubleToLongBits(matrix.get(i, j)),
                        is(Double.doubleToLongBits(elements[i * columns + j])));
            }
        }
        return matrix;
    }

    private static Vector multiply(final MutableMatrixN a, final Vector x) {
        final var ax = MatrixTest.multiply(a, x);// inherited

        assertThat(ax, notNullValue());
        assertInvariants(a);
        VectorTest.assertInvariants(ax);

        return ax;
    }

    private static void set(final MutableMatrixN m, @Nonnegative final int i, @Nonnegative final int j, final double x) {
        m.set(i, j, x);

        assertInvariants(m);
        assertThat(m.get(i, j), is(x));
    }

    @Nested
    public class Create {
        @Test
        public void rectangular() {
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
        public class Vector {

            private static MutableMatrixN test(final double... elements) {
                return create(elements.length, 1, elements);
            }

            @Test
            public void twoA() {
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

        @Nested
        public class TwoEquivalent {

            @SuppressWarnings("SameParameterValue")
            private static void test(final int rows, final int columns, final double[] elements) {
                final MutableMatrixN matrix1 = MutableMatrixN.create(rows, columns, elements);
                final MutableMatrixN matrix2 = MutableMatrixN.create(rows, columns, elements);

                assertInvariants(matrix1, matrix2);
                assertThat(matrix1, is(matrix2));
            }

            @Nested
            public class Create1x1 {
                private static void test(final double x) {
                    TwoEquivalent.test(1, 1, new double[]{x});
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
            public class Create2x1 {
                private static void test(final double x11, final double x21) {
                    TwoEquivalent.test(2, 1, new double[]{x11, x21});
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
        public class TwoDifferent {

            @Test
            public void sameGivenElementsArray() {
                final var elements = new double[]{1.0, 2.1};
                final var matrix1 = MutableMatrixN.create(2, 1, elements);
                final var matrix2 = MutableMatrixN.create(1, 2, elements);

                assertInvariants(matrix1, matrix2);
                assertThat(matrix1, not(matrix2));
            }

            @Nested
            public class OneByOne {

                private static void test(final double x1, final double x2) {
                    final var matrix1 = MutableMatrixN.create(1, 1, new double[]{x1});
                    final var matrix2 = MutableMatrixN.create(1, 1, new double[]{x2});

                    assertInvariants(matrix1, matrix2);
                    assertThat(matrix1, not(matrix2));
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
    }

    @Nested
    public class Multiply {
        @Nested
        public class OneByOne {

            private static void test(final double a11, final double x11) {
                final var a = MutableMatrixN.create(1, 1, new double[]{a11});
                final var x = ImmutableVectorN.create(x11);

                final var ax = multiply(a, x);

                assertThat(ax.get(0), closeTo(a11 * x11, Double.MIN_NORMAL));
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
        public class OneByTwo {

            private static void test(final double a11, final double a12, final double x11, final double x21) {
                final var a = MutableMatrixN.create(1, 2, new double[]{a11, a12});
                final var x = ImmutableVectorN.create(x11, x21);

                final var ax = multiply(a, x);

                assertThat(ax.get(0), closeTo(a11 * x11 + a12 * x21, Double.MIN_NORMAL));
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
        public class TwoByOne {

            private static void test(final double a11, final double a21, final double x11) {
                final var a = MutableMatrixN.create(2, 1, new double[]{a11, a21});
                final var x = ImmutableVectorN.create(x11);

                final var ax = multiply(a, x);

                assertAll("ax",
                        () -> assertThat("ax(0)", ax.get(0), closeTo(a11 * x11, Double.MIN_NORMAL)),
                        () -> assertThat("ax(1)", ax.get(1), closeTo(a21 * x11, Double.MIN_NORMAL)));
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
    public class Set {

        @Test
        public void noOp() {
            final var value = 2.0;
            final var m = MutableMatrixN.create(1, 1, new double[]{value});
            set(m, 0, 0, value);
        }

        @Test
        public void topLeft() {
            final var m = MutableMatrixN.create(2, 3, new double[]{2, 3, 4, 5, 6, 7});
            set(m, 0, 0, -1);
        }

        @Test
        public void topRight() {
            final var m = MutableMatrixN.create(2, 3, new double[]{2, 3, 4, 5, 6, 7});
            set(m, 0, 2, -1);
        }

        @Test
        public void bottomLeft() {
            final var m = MutableMatrixN.create(2, 3, new double[]{2, 3, 4, 5, 6, 7});
            set(m, 1, 0, -1);
        }

        @Test
        public void bottomRight() {
            final var m = MutableMatrixN.create(2, 3, new double[]{2, 3, 4, 5, 6, 7});
            set(m, 1, 2, 6.0);
        }
    }

}
