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
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * Unit tests for classes that implement the {@link Function1WithGradient}
 * interface.
 * </p>
 */
public class Function1WithGradientTest {

    public static void assertInvariants(final Function1WithGradient f) {
        // Do nothing
    }

    public static void assertValueConsistentWithGradient(final Function1WithGradient f, final double x1,
            final double x2, final int n) {
        assert 3 <= n;
        final Function1WithGradientValue[] fx = new Function1WithGradientValue[n];
        for (int i = 0; i < n; ++i) {
            final double x = x1 + (x2 - x1) * i / n;
            fx[i] = f.value(x);
        }
        for (int i = 1; i < n - 1; i++) {
            final Function1WithGradientValue fl = fx[i - 1];
            final Function1WithGradientValue fi = fx[i];
            final Function1WithGradientValue fr = fx[i + 1];
            final double dfl = fi.getF() - fl.getF();
            final double dfr = fr.getF() - fi.getF();
            assertTrue(sign(dfl) != sign(dfr) || sign(fi.getDfDx()) == sign(dfl),
                    "Consistent gradient <" + fl + "," + fi + "," + fr + ">");
        }
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

    public static Function1WithGradientValue value(final Function1WithGradient f, final double x) {
        final Function1WithGradientValue v = f.value(x);

        assertNotNull(v, "Not null, result");// guard
        Function1WithGradientValueTest.assertInvariants(v);
        assertEquals(x, v.getX(), Double.MIN_NORMAL, "x");

        return v;
    }
}
