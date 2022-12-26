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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * Unit tests of classes that implement the {@link Matrix} interface.
 * </p>
 */
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

    public static Vector multiply(final Matrix a, final Vector x) {
        final Vector ax = a.multiply(x);

        assertNotNull(ax, "Not null, result");// guard
        VectorTest.assertInvariants(ax);
        assertInvariants(a, ax);
        VectorTest.assertInvariants(x, ax);

        assertEquals(a.getRows(), ax.getRows(),
                "The number of rows of the product is equal to the number of rows of this matrix.");

        return ax;
    }
}
