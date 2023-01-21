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


import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.function.DoubleFunction;

/**
 * <p>
 * A functor for a one-dimensional function of a continuous variable that also
 * has a computable gradient.
 * </p>
 */
@FunctionalInterface
@Immutable
public interface Function1To1WithGradient extends DoubleFunction<Function1WithGradientValue> {

    /**
     * <p>
     * The value of the function and its gradient for a given value of the
     * continuous variable.
     * </p>
     * <ul>
     * <li>The {@linkplain Function1WithGradientValue#x() domain value} of the
     * returned object is the given domain value.</li>
     * </ul>
     *
     * @param x The domain value
     * @return The value of the function.
     */
    @Nonnull
    Function1WithGradientValue value(double x);

    @Override
    default  Function1WithGradientValue apply(double x) {
        return value(x);
    }
}
