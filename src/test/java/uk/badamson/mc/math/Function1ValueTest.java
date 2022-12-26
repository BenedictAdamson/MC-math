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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import uk.badamson.dbc.assertions.ObjectVerifier;

import javax.annotation.Nonnull;

/**
 * <p>
 * Unit tests for the class {@link Function1Value}.
 * </p>
 */
@SuppressWarnings("UnusedReturnValue")
public class Function1ValueTest {

    public static void assertInvariants(final Function1Value point) {
        ObjectVerifier.assertInvariants(point);// inherited
    }

    public static void assertInvariants(final Function1Value point1, final Function1Value point2) {
        ObjectVerifier.assertInvariants(point1, point2);// inherited

        final boolean equals = point1.equals(point2);
        assertFalse(equals && Double.doubleToLongBits(point1.x()) != Double.doubleToLongBits(point2.x()),
                "Value semantics, x");
        assertFalse(equals && Double.doubleToLongBits(point1.f()) != Double.doubleToLongBits(point2.f()),
                "Value semantics, f");
    }

    @Nonnull
    private static Function1Value constructor(final double x, final double f) {
        final Function1Value point = new Function1Value(x, f);

        assertInvariants(point);
        assertEquals(Double.doubleToLongBits(x), Double.doubleToLongBits(point.x()), "x bits");
        assertEquals(Double.doubleToLongBits(f), Double.doubleToLongBits(point.f()), "f bits");
        return point;
    }

    private static void equals_equivalent(final double x, final double f) {
        final Function1Value point1 = new Function1Value(x, f);
        final Function1Value point2 = new Function1Value(x, f);

        assertInvariants(point1, point2);
        assertEquals(point1, point2, "Equivalent");
    }

    @Test
    public void constructor_a() {
        constructor(0, 1);
    }

    @Test
    public void constructor_b() {
        constructor(-1, 2);
    }

    @Test
    public void constructor_nan() {
        constructor(Double.NaN, Double.POSITIVE_INFINITY);
    }

    @Test
    public void equals_equivalentA() {
        equals_equivalent(1.0, 2.0);
    }

    @Test
    public void equals_equivalentNan() {
        equals_equivalent(Double.NaN, Double.POSITIVE_INFINITY);
    }
}
