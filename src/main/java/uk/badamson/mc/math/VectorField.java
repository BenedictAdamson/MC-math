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
import java.util.function.Function;

/**
 * <p>
 * A functor for a vector function of a vector.
 * </p>
 */
@Immutable
@NotThreadSafe
public interface VectorField extends Function<Vector, Vector> {

    /**
     * <p>
     * The number of dimensions of the space.
     * </p>
     * <p>
     * This attribute must be positive and <dfn>constant</dfn>: the value for a given object must
     * always be the same value.
     * </p>
     */
    @Nonnegative
    int getSpaceDimension();

    /**
     * <p>
     * The number of dimensions of the vector value.
     * </p>
     * <p>
     * This attribute must be positive and <dfn>constant</dfn>: the value for a given object must
     * always be the same value.
     * </p>
     */
    @Nonnegative
    int getValueDimension();

    /**
     * <p>
     * The value of the function for a given point in space.
     * </p>
     * <p>
     * The {@link Vector#getDimension() dimension} of the returned result is equal to the
     * {@link #getValueDimension()} value dimension} of this function.
     * </p>
     * <p>
     * Because this interface is {@linkplain NotThreadSafe not thread safe}, callers
     * should not alter {@code x} array during the computation of
     * the value.
     * </p>
     *
     * @param x the position vector of the point.
     * @return The value of the function.
     * @throws NullPointerException      If {@code x} is null.
     * @throws IndexOutOfBoundsException (Optional) If the {@link Vector#getDimension() dimension} of {@code x} is not equal to the
     *                                   {@linkplain #getSpaceDimension() space dimension} of this
     *                                   function. In practice, an implementations might not complain if
     *                                   the dimension of {@code x} exceeds the space dimension of this
     *                                   function.
     */
    @Nonnull
    Vector value(@Nonnull Vector x);

    @Override
    default Vector apply(@Nonnull Vector x) {
        return value(x);
    }
}
