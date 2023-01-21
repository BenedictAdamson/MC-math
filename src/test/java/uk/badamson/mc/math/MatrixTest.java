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

import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class MatrixTest {

    public static void assertInvariants(final Matrix matrix) {
        assertTrue(0 < matrix.getRows(), "rows is positive");
        assertTrue(0 < matrix.getColumns(), "columns is positive");
    }

    public static void assertInvariants(final Matrix matrix1, final Matrix matrix2) {
        if (matrix1.equals(matrix2)) {
            final int rows1 = matrix1.getRows();
            final int columns1 = matrix1.getColumns();
            assertEquals(rows1, matrix2.getRows(), "Equality requires equal rows");// guard
            assertEquals(columns1, matrix2.getColumns(), "Equality requires equal columns");// guard
            for (int i = 0; i < rows1; i++) {
                for (int j = 0; j < columns1; j++) {
                    assertEquals(matrix1.get(i, j), matrix2.get(i, j), Double.MIN_NORMAL,
                            "Equality requires equal components [" + i + "," + j + "]");
                }
            }
        }
    }

    @Nonnull
    public static Matrix minus(@Nonnull final Matrix m) {
        final Matrix minus = m.minus();

        assertThat(minus, notNullValue());
        assertInvariants(minus);
        assertInvariants(m, minus);

        final var rows = m.getRows();
        final var columns = m.getColumns();
        assertAll(
                () -> assertThat("rows", minus.getRows(), is(rows)),
                () -> assertThat("columns", minus.getColumns(), is(columns)));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertThat("[" + i + ',' + j + ']', minus.get(i, j), is(-m.get(i, j)));
            }
        }

        return minus;
    }

    @Nonnull
    public static Matrix scale(@Nonnull final Matrix m, double f) {
        final Matrix scaled = m.scale(f);

        assertThat(scaled, notNullValue());
        assertInvariants(scaled);
        assertInvariants(m, scaled);

        final var rows = m.getRows();
        final var columns = m.getColumns();
        assertAll(
                () -> assertThat("rows", scaled.getRows(), is(rows)),
                () -> assertThat("columns", scaled.getColumns(), is(columns)));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertThat("[" + i + ',' + j + ']', scaled.get(i, j), is(f * m.get(i, j)));
            }
        }

        return scaled;
    }

    @Nonnull
    public static Vector multiply(@Nonnull final Matrix a, @Nonnull final Vector x) {
        final Vector ax = a.multiply(x);

        assertNotNull(ax, "Not null, result");// guard
        VectorTest.assertInvariants(ax);
        assertInvariants(a, ax);
        VectorTest.assertInvariants(x, ax);

        assertEquals(a.getRows(), ax.getRows(),
                "The number of rows of the product is equal to the number of rows of this matrix.");

        return ax;
    }

    public static Matrix mean(final Matrix x, final Matrix that) {
        final var mean = x.mean(that);

        assertThat(mean, notNullValue());
        assertInvariants(mean);
        assertInvariants(x, mean);
        assertInvariants(that, mean);
        assertAll(
                () -> assertThat("rows", mean.getRows(), is(x.getRows())),
                () -> assertThat("columns", mean.getColumns(), is(x.getColumns())));

        return mean;
    }

    @Nonnull
    public static Matrix minus(@Nonnull Matrix m, @Nonnull Matrix that) {
        final Matrix result = m.minus(that);

        assertThat(result, notNullValue());
        assertInvariants(m);
        assertInvariants(m, that);
        assertInvariants(m, result);
        assertInvariants(that, result);

        final var rows = m.getRows();
        final var columns = m.getColumns();
        assertAll(
                () -> assertThat("rows", result.getRows(), is(rows)),
                () -> assertThat("columns", result.getColumns(), is(columns)));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertThat("[" + i + ',' + j + ']', result.get(i, j), is(m.get(i, j) - that.get(i, j)));
            }
        }

        return result;
    }

    @Nonnull
    public static Matrix plus(@Nonnull Matrix m, @Nonnull Matrix that) {
        final Matrix result = m.plus(that);

        assertThat(result, notNullValue());
        assertInvariants(m);
        assertInvariants(m, that);
        assertInvariants(m, result);
        assertInvariants(that, result);

        final var rows = m.getRows();
        final var columns = m.getColumns();
        assertAll(
                () -> assertThat("rows", result.getRows(), is(rows)),
                () -> assertThat("columns", result.getColumns(), is(columns)));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertThat("[" + i + ',' + j + ']', result.get(i, j), is(m.get(i, j) + that.get(i, j)));
            }
        }

        return result;
    }
}
