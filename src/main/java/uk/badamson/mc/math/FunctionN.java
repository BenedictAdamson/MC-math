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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.function.ToDoubleFunction;

/**
 * <p>
 * A functor for a multi-dimensional function of continuous variables.
 * </p>
 */
@Immutable
@NotThreadSafe
public interface FunctionN extends ToDoubleFunction<double[]> {

    /**
     * <p>
     * The number of independent variables of the function.
     * </p>
     * <p>
     * This attribute must be positive and <dfn>constant</dfn>: the value for a given object must
     * always be the same value.
     * </p>
     */
    @Nonnegative
    int getDimension();

    /**
     * <p>
     * The value of the function for given values of the continuous variables.
     * </p>
     * <p>
     * Because this interface is {@linkplain NotThreadSafe not thread safe}, callers
     * should not alter the content of the {@code x} array during the computation of
     * teh value.
     * </p>
     *
     * @param x The values of the continuous variables; x[i] is the value of
     *          variable <var>i</var>.
     * @return The value of the function.
     * @throws NullPointerException      If {@code x} is null.
     * @throws IndexOutOfBoundsException (Optional) If the length of {@code x} is not equal to the
     *                                   {@linkplain #getDimension() number of dimensions} of this
     *                                   function. In practice, many implementations will not complain if
     *                                   the length of {@code x} exceeds the number of dimensions of this
     *                                   function.
     */
    double value(@Nonnull double[] x);

    @Override
    default double applyAsDouble(@Nonnull double[] x) {
        return value(x);
    }
}
