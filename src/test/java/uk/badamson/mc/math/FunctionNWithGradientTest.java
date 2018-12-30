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
 * Unit tests for classes that implement the {@link FunctionNWithGradient}
 * interface.
 * </p>
 */
public class FunctionNWithGradientTest {

    public static void assertInvariants(final FunctionNWithGradient f) {
        // Do nothing
    }

    public static void assertValueConsistentWithGradientAlongLine(final FunctionNWithGradient f, final double w1,
            final double w2, final int n, final ImmutableVectorN x0, final ImmutableVectorN dx) {
        final Function1WithGradient fLine = MinN.createLineFunction(f, x0, dx);
        Function1WithGradientTest.assertValueConsistentWithGradient(fLine, w1, w2, n);
    }

    public static FunctionNWithGradientValue value(final FunctionNWithGradient f, final ImmutableVectorN x) {
        final FunctionNWithGradientValue v = f.value(x);

        assertNotNull(v, "Not null, result");// guard
        FunctionNWithGradientValueTest.assertInvariants(v);
        assertTrue(x.minus(v.getX()).magnitude2() <= Double.MIN_NORMAL,
                "x <expected " + x + ", actual " + v.getX() + ">");

        return v;
    }
}
