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


import javax.annotation.concurrent.Immutable;

/**
 * <p>
 * One value from the domain of a {@linkplain Function1To1WithGradient single
 * dimensional function of a continuous variable that also has a computable
 * gradient} with the corresponding value in the codomain of the function and the
 * gradient of the function.
 * </p>
 * <dl>
 * <dt>x</dt><dd>The domain value.</dd>
 * <dt>f</dt><dd>The codomain value.</dd>
 * <dt>dfdx</dt><dd>The gradient value</dd>
 * </dl>
 */
@Immutable
public record Function1To1WithGradientValue(
        double x,
        double f,
        double dfdx
) {
}
