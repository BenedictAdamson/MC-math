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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Unit tests of classes that implement the {@link Matrix} interface.
 * </p>
 */
public class MatrixTest {

    public static void assertInvariants(Matrix matrix) {
        assertTrue("rows is positive", 0 < matrix.getRows());
        assertTrue("columns is positive", 0 < matrix.getColumns());
    }

    public static void assertInvariants(Matrix matrix1, Matrix matrix2) {
        if (matrix1.equals(matrix2)) {
            final int rows1 = matrix1.getRows();
            final int columns1 = matrix1.getColumns();
            assertEquals("Equality requires equal rows", rows1, matrix2.getRows());// guard
            assertEquals("Equality requires equal columns", columns1, matrix2.getColumns());// guard
            for (int i = 0; i < rows1; i++) {
                for (int j = 0; j < columns1; j++) {
                    assertEquals("Equality requires equal components [" + i + "," + j + "]", matrix1.get(i, j),
                            matrix2.get(i, j), Double.MIN_NORMAL);
                }
            }
        }
    }

    public static final Vector multiply(Matrix a, Vector x) {
        final Vector ax = a.multiply(x);

        assertNotNull("Not null, result", ax);// guard
        VectorTest.assertInvariants(ax);
        assertInvariants(a, ax);
        VectorTest.assertInvariants(x, ax);

        assertEquals("The number of rows of the product is equal to the number of rows of this matrix.", a.getRows(),
                ax.getRows());

        return ax;
    }
}
