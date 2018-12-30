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
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import uk.badamson.mc.ObjectTest;

/**
 * <p>
 * Unit tests for the class {@link Function1WithGradientValue}.
 * </p>
 */
public class Function1WithGradientValueTest {

    public static void assertInvariants(final Function1WithGradientValue v) {
        ObjectTest.assertInvariants(v);// inherited
    }

    public static void assertInvariants(final Function1WithGradientValue v1, final Function1WithGradientValue v2) {
        ObjectTest.assertInvariants(v1, v2);// inherited

        final boolean equals = v1.equals(v2);
        assertFalse(equals && Double.doubleToLongBits(v1.getX()) != Double.doubleToLongBits(v2.getX()),
                "Value semantics, x");
        assertFalse(equals && Double.doubleToLongBits(v1.getF()) != Double.doubleToLongBits(v2.getF()),
                "Value semantics, f");
        assertFalse(equals && Double.doubleToLongBits(v1.getDfDx()) != Double.doubleToLongBits(v2.getDfDx()),
                "Value semantics, dfdx");
    }

    private static Function1WithGradientValue constructor(final double x, final double f, final double dfdx) {
        final Function1WithGradientValue point = new Function1WithGradientValue(x, f, dfdx);

        assertInvariants(point);
        assertEquals(Double.doubleToLongBits(x), Double.doubleToLongBits(point.getX()), "x bits");
        assertEquals(Double.doubleToLongBits(f), Double.doubleToLongBits(point.getF()), "f bits");
        assertEquals(Double.doubleToLongBits(dfdx), Double.doubleToLongBits(point.getDfDx()), "dfdx bits");
        return point;
    }

    private static void equals_equivalent(final double x, final double f, final double dfdx) {
        final Function1WithGradientValue point1 = new Function1WithGradientValue(x, f, dfdx);
        final Function1WithGradientValue point2 = new Function1WithGradientValue(x, f, dfdx);

        assertInvariants(point1, point2);
        assertEquals(point1, point2, "Equivalent");
    }

    @Test
    public void constructor_a() {
        constructor(0, 1, 2);
    }

    @Test
    public void constructor_b() {
        constructor(-1, 2, 3);
    }

    @Test
    public void constructor_nan() {
        constructor(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    @Test
    public void equals_equivalentA() {
        equals_equivalent(1.0, 2.0, 3.0);
    }

    @Test
    public void equals_equivalentNan() {
        equals_equivalent(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }
}
