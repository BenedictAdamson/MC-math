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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import uk.badamson.dbc.assertions.ObjectVerifier;

/**
 * <p>
 * Unit tests of the class {@link ImmutableMatrixN}
 * </p>
 */
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

    private static void create_1x1(final double x) {
        create(1, 1, new double[] { x });
    }

    @SuppressWarnings("SameParameterValue")
    private static void create_equals(final int rows, final int columns, final double[] elements) {
        final ImmutableMatrixN matrix1 = ImmutableMatrixN.create(rows, columns, elements);
        final ImmutableMatrixN matrix2 = ImmutableMatrixN.create(rows, columns, elements);

        assertInvariants(matrix1, matrix2);
        assertEquals(matrix1, matrix2, "Equivalent");
    }

    private static void create_equals_1x1(final double x) {
        create_equals(1, 1, new double[] { x });
    }

    private static void create_equals_2x1(final double x11, final double x21) {
        create_equals(2, 1, new double[] { x11, x21 });
    }

    private static void create_notEquals_1x1(final double x1, final double x2) {
        final ImmutableMatrixN matrix1 = ImmutableMatrixN.create(1, 1, new double[] { x1 });
        final ImmutableMatrixN matrix2 = ImmutableMatrixN.create(1, 1, new double[] { x2 });

        assertInvariants(matrix1, matrix2);
        assertNotEquals(matrix1, matrix2, "Not equivalent");
    }

    private static ImmutableMatrixN create_vector(final double... elements) {
        return create(elements.length, 1, elements);
    }

    private static ImmutableVectorN multiply(final ImmutableMatrixN a, final Vector x) {
        final ImmutableVectorN ax = (ImmutableVectorN) MatrixTest.multiply(a, x);// inherited

        assertNotNull(ax, "Not null, result");// guard
        assertInvariants(a);// check for side effects
        ImmutableVectorNTest.assertInvariants(ax);

        return ax;
    }

    private static void multiply_1x1(final double a11, final double x11) {
        final ImmutableMatrixN a = ImmutableMatrixN.create(1, 1, new double[] { a11 });
        final ImmutableVectorN x = ImmutableVectorN.create(x11);

        final ImmutableVectorN ax = multiply(a, x);

        assertEquals(a11 * x11, ax.get(0), Double.MIN_NORMAL, "product element");
    }

    private static void multiply_1x2(final double a11, final double a12, final double x11, final double x21) {
        final ImmutableMatrixN a = ImmutableMatrixN.create(1, 2, new double[] { a11, a12 });
        final ImmutableVectorN x = ImmutableVectorN.create(x11, x21);

        final ImmutableVectorN ax = multiply(a, x);

        assertEquals(a11 * x11 + a12 * x21, ax.get(0), Double.MIN_NORMAL, "product element");
    }

    private static void multiply_2x1(final double a11, final double a21, final double x11) {
        final ImmutableMatrixN a = ImmutableMatrixN.create(2, 1, new double[] { a11, a21 });
        final ImmutableVectorN x = ImmutableVectorN.create(x11);

        final ImmutableVectorN ax = multiply(a, x);

        assertEquals(a11 * x11, ax.get(0), Double.MIN_NORMAL, "ax[0]");
        assertEquals(a21 * x11, ax.get(1), Double.MIN_NORMAL, "ax[1]");
    }

    @Test
    public void create_1x1_0() {
        create_1x1(0.0);
    }

    @Test
    public void create_1x1_1() {
        create_1x1(1.0);
    }

    @Test
    public void create_1x1_nan() {
        create_1x1(Double.NaN);
    }

    @Test
    public void create_2x3() {
        create(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
    }

    @Test
    public void create_equals_1x1_0() {
        create_equals_1x1(0.0);
    }

    @Test
    public void create_equals_1x1_1() {
        create_equals_1x1(1.0);
    }

    @Test
    public void create_equals_1x1_nan() {
        create_equals_1x1(Double.NaN);
    }

    @Test
    public void create_equals_2x1_A() {
        create_equals_2x1(0.0, 1.0);
    }

    @Test
    public void create_equals_2x1_B() {
        create_equals_2x1(1.0, 4.0);
    }

    @Test
    public void create_notEquals_1x1_A() {
        create_notEquals_1x1(1.0, 2.0);
    }

    @Test
    public void create_notEquals_1x1_B() {
        create_notEquals_1x1(3.0, 5.0);
    }

    /**
     * Tough test: same elements arrays used for construction.
     */
    @Test
    public void create_notEquals_C() {
        final double[] elements = new double[] { 1.0, 2.1 };
        final ImmutableMatrixN matrix1 = ImmutableMatrixN.create(2, 1, elements);
        final ImmutableMatrixN matrix2 = ImmutableMatrixN.create(1, 2, elements);

        assertInvariants(matrix1, matrix2);
        assertNotEquals(matrix1, matrix2, "Not equivalent");
    }

    @Test
    public void create_vector_2A() {
        create_vector(1.0, 2.0);
    }

    @Test
    public void create_vector_2B() {
        create_vector(3.0, 5.0);
    }

    @Test
    public void create_vector_3() {
        create_vector(1.0, 2.0, 3.0);
    }

    @Test
    public void multiply_1x1A() {
        multiply_1x1(0.0, 0.0);
    }

    @Test
    public void multiply_1x1B() {
        multiply_1x1(1.0, 2.0);
    }

    @Test
    public void multiply_1x1C() {
        multiply_1x1(-2.0, 3.0);
    }

    @Test
    public void multiply_1x2A() {
        multiply_1x2(1.0, 2.0, 3.0, 4.0);
    }

    @Test
    public void multiply_1x2B() {
        multiply_1x2(2.0, 3.0, 5.0, 7.0);
    }

    @Test
    public void multiply_2x1A() {
        multiply_2x1(1.0, 2.0, 3.0);
    }

    @Test
    public void multiply_2x1B() {
        multiply_2x1(2.0, 3.0, 5.0);
    }
}
