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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Arrays;

/**
 * <p>
 * A constant (immutable) 2D array of real numbers of any constant size.
 * </p>
 */
@Immutable
public class ImmutableMatrixN extends ArrayMatrix {

    ImmutableMatrixN(final int rows, final int columns, @Nonnull final double[] elements) {
        super(rows, columns, elements);
    }

    /**
     * <p>
     * Create a matrix with given element values.
     * </p>
     *
     * @param rows     The number of rows of the matrix.
     * @param columns  The number of columns of this matrix.
     * @param elements The values of the elements of the matrix; the elements are in
     *                 <i>row-major</i> order, so {@code element[i*columns + j]} is the
     *                 value of cardinal row <var>i</var>, cardinal column <var>j</var>.
     * @throws NullPointerException     If {@code elements} is null.
     * @throws IllegalArgumentException If {@code rows} is not positive.
     *                                  If {@code columns} is not positive.
     *                                  If the length of {@code elements} is not equal to
     *                                  {@code rows} multiplied by {@code columns}.
     */
    @Nonnull
    public static ImmutableMatrixN create(@Nonnegative final int rows,
                                          @Nonnegative final int columns,
                                          @Nonnull final double[] elements) {
        requireValidCreationArguments(rows, columns, elements);
        if (columns == 1) {
            return ImmutableVectorN.create(elements);
        } else {
            return new ImmutableMatrixN(rows, columns, Arrays.copyOf(elements, elements.length));
        }
    }

    @Nonnull
    @Override
    public ImmutableMatrixN minus() {
        return new ImmutableMatrixN(rows, columns, minusElements());
    }

    @Override
    @Nonnull
    public ImmutableMatrixN minus(@Nonnull final Matrix that) {
        return new ImmutableMatrixN(rows, columns, minusElements(that));
    }

    @Override
    @Nonnull
    public ImmutableMatrixN plus(@Nonnull final Matrix that) {
        return new ImmutableMatrixN(rows, columns, plusElements(that));
    }

    @Nonnull
    @Override
    public ImmutableMatrixN scale(double f) {
        return new ImmutableMatrixN(rows, columns, scaleElements(f));
    }

    @Nonnull
    @Override
    public ImmutableMatrixN mean(@Nonnull final Matrix that) {
        return new ImmutableMatrixN(rows, columns, meanElements(that));
    }

}
