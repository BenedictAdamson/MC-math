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
import java.io.Serial;
import java.util.Objects;

/**
 * <p>
 * An exception class for indicating that minimization of a function is not possible because the
 * function is poorly conditioned.
 * </p>
 * <ul>
 * <li>The exception might indicate that the function does not have a
 * minimum</li>
 * <li>The exception might indicate that, although function has a minimum, it is
 * impossible to {@linkplain Min1To1#findBracket(Function1To1, double, double) find a
 * bracket} for a function with the starting points because the function has an
 * odd-powered high order term that causes the iterative procedure to
 * diverge.</li>
 * </ul>
 */
public class PoorlyConditionedFunctionException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 1L;

    PoorlyConditionedFunctionException(@Nonnull final Function1To1 f) {
        super("Poorly conditioned function " + f);
        Objects.requireNonNull(f);
    }

}// class