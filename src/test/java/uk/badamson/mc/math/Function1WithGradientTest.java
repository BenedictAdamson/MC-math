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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * Unit tests for classes that implement the {@link Function1To1WithGradient}
 * interface.
 * </p>
 */
@SuppressWarnings("unused")
public class Function1WithGradientTest {

    @SuppressWarnings({"EmptyMethod", "unused"})
    public static void assertInvariants(final Function1To1WithGradient f) {
        // Do nothing
    }

    private static int sign(final double x) {
        if (x < -Double.MIN_NORMAL) {
            return -1;
        } else if (Double.MIN_NORMAL < x) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Function1WithGradientValue value(final Function1To1WithGradient f, final double x) {
        final Function1WithGradientValue v = f.value(x);

        assertNotNull(v, "Not null, result");// guard
        Function1To1WithGradientValueTest.assertInvariants(v);
        assertEquals(x, v.x(), Double.MIN_NORMAL, "x");

        return v;
    }
}
