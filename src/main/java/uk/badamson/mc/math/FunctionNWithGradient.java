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

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.concurrent.Immutable;

/**
 * <p>
 * A functor for a scalar function of a vector that also has a computable
 * gradient.
 * </p>
 */
@Immutable
public interface FunctionNWithGradient {

    /**
     * <p>
     * The number of independent variables of the function.
     * </p>
     * <p>
     * This attribute must be <dfn>constant</dfn>: the value for a given object must
     * always be the same value.
     * </p>
     *
     * @return the number of dimensions; positive.
     */
    public int getDimension();

    /**
     * <p>
     * The value of the function and its gradient for a given value of the
     * continuous variable.
     * </p>
     * <ul>
     * <li>Always returns a (non null) value.</li>
     * <li>The {@linkplain Function1WithGradientValue#getX() domain value} of the
     * returned object is the given domain value.</li>
     * </ul>
     *
     * @param x
     *            The domain value
     * @return The value of the function.
     *
     * @throws NullPointerException
     *             If {@code x} is null.
     * @throws IllegalArgumentException
     *             If the {@linkplain ImmutableVectorN#getDimension() dimension} of
     *             {@code x} does not equal the {@linkplain #getDimension()
     *             dimension} of this functor.
     */
    public @NonNull FunctionNWithGradientValue value(@NonNull ImmutableVectorN x);
}
