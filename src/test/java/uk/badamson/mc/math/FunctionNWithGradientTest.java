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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * Unit tests for classes that implement the {@link FunctionNTo1WithGradient}
 * interface.
 * </p>
 */
public class FunctionNWithGradientTest {

    @SuppressWarnings({"EmptyMethod", "unused"})
    public static void assertInvariants(final FunctionNTo1WithGradient f) {
        // Do nothing
    }

    @SuppressWarnings("unused")
    public static FunctionNTo1WithGradientValue value(final FunctionNTo1WithGradient f, final ImmutableVectorN x) {
        final FunctionNTo1WithGradientValue v = f.value(x);

        assertNotNull(v, "Not null, result");// guard
        FunctionNTo1WithGradientValueTest.assertInvariants(v);
        assertTrue(x.minus(v.getX()).magnitude2() <= Double.MIN_NORMAL,
                "x <expected " + x + ", actual " + v.getX() + ">");

        return v;
    }
}
